package Goliath.UI;

import Goliath.Applications.Application;
import Goliath.Collections.List;
import Goliath.Collections.PropertySet;
import Goliath.Environment;
import Goliath.Exceptions.FileNotFoundException;
import Goliath.IO.File;
import Goliath.IO.FileChannel;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.regex.Matcher;
import org.w3c.dom.Document;

// TODO: Implement this as a flyweight that monitors it's dependencies

/**
 * The GSPFile class simplifies the processing and transfer of information
 * around the processing of a .gsp file
 * @author admin
 */
public class GSPFile
{
    private final File m_oGSP;
    private List<String> m_oServlets;
    private List<File> m_oDependencies;
    private List<File> m_oXSLTFiles;
    private Goliath.IO.File m_oCachedXSLT;
    private Goliath.IO.File m_oCachedGSP;
    private boolean m_lPrepared;
    private List<String> m_oGSPParameters;
    private List<String> m_oXSLTParameters;
    private List<String> m_oParameters;
    
    /**
     * Creates a new instance of GSP File
     * @param toGSPFile the reference to the original .gsp, not a cached version
     */
    public GSPFile(File toGSPFile)
    {
        Goliath.Utilities.checkParameterNotNull("toGSPFile", toGSPFile);
        m_oGSP = toGSPFile;
    }
    
    /**
     * Creates a new instance of a GSP file based on the
     * XML passed in
     * @param toGSPXML the gsp data
     * @throws FileNotFoundException if the temporary file could not be written for any reason
     */
    public GSPFile(Document toGSPXML)
            throws FileNotFoundException
    {
        // TODO: Keep this in memory so it can be discarded later
        String lcGSP = Goliath.XML.Utilities.toString(toGSPXML);
        String lcHash = Goliath.Utilities.encryptMD5(lcGSP);
        
        java.io.File loFile = Goliath.IO.Utilities.File.getTemporary("./GSP/" + lcHash, ".gsp");
        if (!loFile.exists())
        {
            try
            {
                Goliath.IO.Utilities.File.create(loFile);
                FileWriter loWriter = new FileWriter(loFile);
                loWriter.write(lcGSP);
                loWriter.close();
            }
            catch (Throwable ex)
            {
                Application.getInstance().log(ex);
            }
        }
        m_oGSP = new File(loFile);
    }
    
    /**
     * Gets the list of servlets that need to be processed by this GSP in the order
     * they need to be processed.  This list will always contain each servlet only once.
     * This will pick up the order based on the first mention of each servlet
     * @return the list of servlets
     */
    public final List<String> getServlets(List<Throwable> toErrors)
    {
        if (m_oServlets == null)
        {
            prepareGSP(toErrors);
            if (m_oCachedGSP != null)
            {
                synchronized (m_oGSP)
                {
                    m_oServlets = new List<String>();

                    Matcher loMatcher = getServletMatcher(Goliath.IO.Utilities.File.toString(m_oCachedGSP), true);
                    while (loMatcher.find())
                    {
                        String lcClass = loMatcher.group(1).toLowerCase();
                        if (!m_oServlets.contains(lcClass))
                        {
                            m_oServlets.add(lcClass);
                        }
                    }        
                }
            }
            else
            {
                m_oServlets = new List<String>(0);
            }
        }
        return m_oServlets;
    }
    
    /**
     * Gets the RegEx Matcher for matching include files in the specified string
     * @param tcMatchString the string to extract include files from
     * @return the Matcher
     */
    private Matcher getIncludeMatcher(String tcMatchString, boolean tlGrouped)
    {
        return Goliath.Utilities.getRegexMatcher("<include>" + (tlGrouped ? "(.+?)" : ".+?") + "</include>|<xsl:import.+href=\"" + (tlGrouped ? "(.+?)" : ".+?") + "\"/>|<xsl:include.+href=\"" + (tlGrouped ? "(.+?)" : ".+?") + "\"/>", tcMatchString);
    }
    
    /**
     * Gets the RegEx Matcher for matching servlet files in the specified string
     * @param tcMatchString the string to extract servlet files from
     * @return the Matcher
     */
    private Matcher getServletMatcher(String tcMatchString, boolean tlGrouped)
    {
        return Goliath.Utilities.getRegexMatcher("(?i)<servlet.+class=\"" + (tlGrouped ? "(" : "" ) + "[^\"]+" + (tlGrouped ? ")" : "") + "\"(?:/>|.+</servlet>)", tcMatchString);
    }
    
    
    /**
     * Gets the list of XSLT files that are included in this GSP
     * @return the List of XSLT Files included
     */
    public final List<File> getXSLTIncluded(List<Throwable> toErrors)
    {
        if (m_oXSLTFiles == null)
        {
            synchronized (m_oGSP)
            {
                List<File> loDependencies = new List<File>(getDependencies(toErrors));
                m_oXSLTFiles = new List<File>();
                for (File loFile : loDependencies)
                {
                    if (Goliath.Utilities.getRegexMatcher("(?i)\\.xslt", loFile.getName()).find())
                    {
                        try
                        {
                            if (!m_oXSLTFiles.contains(loFile))
                            {
                                m_oXSLTFiles.add(loFile);
                            }
                        }
                        catch(Throwable ex)
                        {
                            toErrors.add(ex);
                        }
                    }
                }
            }
        }
        return m_oXSLTFiles;
    }
    
    /**
     * Checks the list of dependencies and ensures they are still valid, if they
     * are not valid, this will reset the GSPFile to reprocess
     */
    private void checkDependencies(List<Throwable> toErrors)
    {
        // If there is no cached file and no xslt file then there is no need to check this, we know we are invalid
        if (m_oCachedXSLT == null && m_oCachedGSP == null)
        {
            // We are not resetting here, because this just means we are in the process of processing
            return;
        }
        
        long lnXSLTDate = m_oCachedXSLT == null ? 0 : m_oCachedXSLT.lastModified();
        long lnGSPDate = m_oCachedGSP == null ? 0 : m_oCachedGSP.lastModified();
        
        // Check the main file
        if (m_oGSP.lastModified() > lnXSLTDate || m_oGSP.lastModified() > lnGSPDate)
        {
            // The file is out of date, reset and stop checking
            reset();
            return;
        }
        
        
        // Check each dependency file, and the main file against the cached versions of the files
        List<File> loDependencies = new List<File>(getDependencies(toErrors));
        for (File loFile : loDependencies)
        {
            // If any dependency is out of date, clear the whole lot as everything is invalid
            if (!loFile.exists() || loFile.lastModified() > lnXSLTDate || loFile.lastModified() > lnGSPDate)
            {
                reset();
                return;
            }
        }
    }
    
    /**
     * Resets the GSP so it will be reprocessed
     */
    public final void reset()
    {
        m_oCachedXSLT = null;
        m_oCachedGSP = null;
        m_oDependencies = null;
        m_oServlets = null;
        m_oXSLTFiles = null;
        m_lPrepared = false;
        m_oGSPParameters = null;
        m_oXSLTParameters = null;
        m_oParameters = null;
    }
    
    /**
     * Writes the contents of the gsp to the writer, this will also resolve dependencies
     * @param toWriter the writer
     * @param toContent the content to write
     * @param toDependencies the list of dependencies that have been written
     * @param tlXSLT true if we are writing into an XSLT
     * @param toErrors the list of errors that have occured during this processing
     */
    private void writeIncludeContents(FileChannel toWriter, File toContent, List<File> toDependencies, boolean tlXSLT, List<Throwable> toErrors)
            throws IOException
    {
        // Get the contents of the include file
        String lcFileContents = Goliath.IO.Utilities.File.toString(toContent);
        
        // If the content file is a .gsp or .xslt then we need to also process it, otherwise we just write the file as given
        // TODO: We may want to format files of certain types differently, if so, then we will want to use a factory here
        if (Goliath.Utilities.getRegexMatcher("(?i)\\.gsp$|\\.xslt$", toContent.getName()).find())
        {
            // Process the .gsp
            Matcher loMatcher = getIncludeMatcher(lcFileContents, false);

            int lnIndex = 0;
            int lnCurrent = 0;
            while (loMatcher.find())
            {
                lnCurrent = loMatcher.start();

                // Write from the index to the match
                // Replace the ampersands if needed

                // TODO: Revisit replacement of ampersands
                toWriter.write(
                        tlXSLT ?
                            Goliath.XML.Utilities.stripXSLT(lcFileContents.substring(lnIndex, lnCurrent)).replaceAll("&", "[-amp-]") :
                            Goliath.XML.Utilities.stripXMLHeader(lcFileContents.substring(lnIndex, lnCurrent))
                        );
                
                lnIndex = loMatcher.end();
                
                // If we are importing or including into an XSLT then we need to adjust the path to make it relative to the xslt
                String lcFileName = lcFileContents.substring(loMatcher.start(), loMatcher.end()).replace("<include>", "").replace("</include>", "");
                
                // If the filename is .xslt and we are not processing an xslt, then skip
                if (!tlXSLT && lcFileName.matches("(?:).+\\.xslt$"))
                {
                    continue;
                }
                
                boolean llIsXSLImport = lcFileName.matches("(?i)^<xsl:import.+");
                if (llIsXSLImport)
                {
                    // Modify the import to pick up the correct path
                    lcFileName = lcFileName.replaceAll("^[^\"]+\"|\"[^\"]+$", "");
                }
                
                try
                {
                    File loIncluded = new File(Goliath.IO.Utilities.File.getRelativeLocation(toContent, lcFileName));
                    if (!toDependencies.contains(loIncluded))
                    {
                        toDependencies.add(loIncluded);

                        if (!llIsXSLImport)
                        {
                            // Write the contents of the file
                            writeIncludeContents(toWriter, loIncluded, toDependencies, tlXSLT, toErrors);
                        }
                        else
                        {
                            toWriter.write("<xsl:import href=\"");
                            toWriter.write(loIncluded.getPath());
                            toWriter.write("\"/>");
                        }
                    }
                }
                catch (FileNotFoundException ex)
                {
                    toErrors.add(ex);
                }
            }

            // Write the rest of the file
            if (tlXSLT)
            {
                toWriter.write(Goliath.XML.Utilities.stripXSLT(lcFileContents.substring(lnIndex)).replaceAll("&", "[-amp-]"));
            }
            else
            {
                toWriter.write(lcFileContents.substring(lnIndex));
            }
        }
        else
        {
            // Include the file with no processing
            if (tlXSLT)
            {
                toWriter.write(Goliath.XML.Utilities.stripXSLT(lcFileContents).replaceAll("&", "[-amp-]"));
            }
            else
            {
                toWriter.write(lcFileContents);
            }
        }
    }
    
    /**
     * Creates the XSLT and GSP files required for all of the dependencies within this document.
     * This function will NOT replace any parameters
     */
    private void prepareGSP(List<Throwable> toErrors)
    {
        checkDependencies(toErrors);
        if (!m_lPrepared)
        {
            // Prepare the Cached XSLT file if needed
            if (m_oCachedXSLT == null)
            {
                // Create the cached XSLT
                synchronized(m_oGSP)
                {
                    try
                    {
                        // Extract the full list of XSLT Files
                        List<File> loXSLTs = new List<File>(getXSLTIncluded(toErrors));
                        java.io.File loXSLTFile = Goliath.IO.Utilities.File.getTemporary(m_oGSP.getPath(), "xslt");
                        if (loXSLTs.size() > 0)
                        {

                            Goliath.IO.Utilities.File.create(loXSLTFile);
                            m_oCachedXSLT = new File(loXSLTFile);

                            FileChannel loXSLTChannel = new FileChannel(new FileOutputStream(m_oCachedXSLT).getChannel());

                            // Write the XSLT Header
                            loXSLTChannel.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>" + Environment.NEWLINE());
                            loXSLTChannel.write("<xsl:stylesheet version=\"1.0\" xmlns:xsl=\"http://www.w3.org/1999/XSL/Transform\"  xmlns:exslt=\"http://exslt.org/common\">" + Environment.NEWLINE());
                            loXSLTChannel.write("<xsl:output method=\"xml\" version=\"1.0\" encoding=\"UTF-8\" doctype-public=\"-//W3C//DTD XHTML 1.0 Strict//EN\"");
                            loXSLTChannel.write(" doctype-system=\"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd\"/>" + Environment.NEWLINE());

                            // Create a list of dependencies to ensure files are only added one time
                            List<File> loDependencies = new List<File>();
                            for (File loXSLT : loXSLTs)
                            {
                                if (!loDependencies.contains(loXSLT))
                                {
                                    loDependencies.add(loXSLT);

                                    // Write the contents of the gsp
                                    writeIncludeContents(loXSLTChannel, loXSLT, loDependencies, true, toErrors);
                                }
                            }

                            // Write the XSLT Footer
                            loXSLTChannel.write("</xsl:stylesheet>");
                            loXSLTChannel.close();
                        }
                        else
                        {
                            if (m_oCachedXSLT.exists())
                            {
                                m_oCachedXSLT.delete();
                            }
                        }
                    }
                    catch (Throwable ex)
                    {
                        // If there was an error we need to reset
                        reset();
                    }

                }
            }

            // Prepare the cached gsp file if needed, this is pre parameter replacement
            if (m_oCachedGSP == null)
            {
                synchronized(m_oGSP)
                {
                    try
                    {
                        java.io.File loGSPFile = Goliath.IO.Utilities.File.getTemporary(m_oGSP.getPath(), "gsp");
                        Goliath.IO.Utilities.File.create(loGSPFile);
                        m_oCachedGSP = new File(loGSPFile);
                        
                        FileChannel loGSPChannel = new FileChannel(new FileOutputStream(m_oCachedGSP).getChannel());
                        List<File> loDependencies = new List<File>();

                        if (!loDependencies.contains(m_oGSP))
                        {
                            loDependencies.add(m_oGSP);

                            // Process the GSP
                            writeIncludeContents(loGSPChannel, m_oGSP, loDependencies, false, toErrors);
                        }
                        loGSPChannel.close();
                    }
                    catch (Throwable ex)
                    {
                        // If there was an error we need to reset
                        reset();
                    }
                }
            }
            
            m_lPrepared = true;
        }
    }
    
    /**
     * Gets the processed version of the file, this could return a temporary file
     * if there are parameters in a file.  This will not cause the file to be prepared 
     * or processed multiple times.
     * @param toParameters The list of replacement parameters
     * @param toErrors the error list that is added to if any errors occur during processing
     * @return the final processed version for the parameters passed
     */
    public final File getProcessedFile(PropertySet toParameters, List<Throwable> toErrors)
    {
        // Make sure we are already prepared
        prepareGSP(toErrors);
        
        File loOutput = null;
        
        // No need to process if there were errors
        if (toErrors.size() == 0)
        {
            // Get the name of the unprocessed cached files
            if (m_oCachedXSLT != null && m_oCachedXSLT.exists())
            {
                // An XSLT file exists, so we must process using the XSLT
                loOutput = processWithXSLT(toParameters, toErrors);
            }
            else
            {
                // No XSLT file exists, so just process the cached GSP
                loOutput = m_oCachedGSP;
                if (gspHasParameters())
                {
                    try
                    {
                        File loFile = Goliath.IO.Utilities.File.getTemporary(".gsp");
                        String lcContent = Goliath.IO.Utilities.File.toString(m_oCachedGSP);

                        // Final parameter replacement
                        lcContent = Goliath.IO.Utilities.replaceParameters(lcContent, toParameters, true);

                        // Write out the final file
                        FileWriter loFileWriter = new FileWriter(loFile);
                        // XSLT Transformations can't handle the DOCTYPE, so it is hardcoded here for the moment
                        loFileWriter.write("<!DOCTYPE html>\r\n");
                        loFileWriter.write(lcContent);
                        loFileWriter.close();
                        
                        loOutput = loFile;
                    }
                    catch (Throwable ex)
                    {
                        toErrors.add(ex);
                    }
                }
            }
            
        }
        return loOutput;
    }
    
    /**
     * Gets the full list of parameters that are used in the xslt and gsp
     * @return the list of parameters
     */
    public List<String> getParameters()
    {
        if (m_oParameters == null)
        {
            m_oParameters = new List(getGSPParameters());

            for (String lcParam : getXSLTParameters())
            {
                if (!m_oParameters.contains(lcParam))
                {
                    m_oParameters.add(lcParam);
                }
            }
        }
        return m_oParameters;
    }
    
    /**
     * Gets the parameters that are in the GSP cached files
     * @return the list of parameters in the GSP cache
     */
    private List<String> getGSPParameters()
    {
        if (m_oGSPParameters == null)
        {
            m_oGSPParameters = m_oCachedGSP == null ? new List<String>(0) : Goliath.IO.Utilities.File.extractParameters(m_oCachedGSP).getPropertyKeys();
        }
        return m_oGSPParameters;
        
    }
    
    /**
     * Gets the parameters that are in the GSP cached files
     * @return the list of parameters in the GSP cache
     */
    private List<String> getXSLTParameters()
    {
        if (m_oXSLTParameters == null)
        {
            m_oXSLTParameters = m_oCachedXSLT == null ? new List<String>(0) :  Goliath.IO.Utilities.File.extractParameters(m_oCachedXSLT).getPropertyKeys();
        }
        return m_oXSLTParameters;
        
    }
    
    /**
     * Checks if the Cached GSP Has parameters
     * @return true if there are parameters
     */
    private boolean gspHasParameters()
    {
        return getGSPParameters().size() > 0;
    }
    
    /**
     * Checks if the Cached XSLT Has parameters
     * @return true if there are parameters
     */
    private boolean xsltHasParameters()
    {
        return getXSLTParameters().size() > 0;
    }
    
    /**
     * Processes the GSP with an XSLT
     * @param toReplacements the replacement parameters to push in
     * @param toErrors the list of errors that gets added to if there are any errors during processing
     * @return the file that contains the processed data
     */
    private File processWithXSLT(PropertySet toParameters, List<Throwable> toErrors)
    {
        boolean llGSPHasParameters = gspHasParameters();
        boolean llXSLTHasParameters = xsltHasParameters();
        boolean llHasParameters = llGSPHasParameters || llXSLTHasParameters;
        File loProcessXSLT = m_oCachedXSLT;
        
        try
        {

            // If the file has parameters, then create it in the OS temp folder, otherwise we can reuse by placing in the Goliath temp folder
            java.io.File loProcessedFile = llHasParameters ? Goliath.IO.Utilities.File.getTemporary("gsp") : Goliath.IO.Utilities.File.getTemporary(m_oGSP.getPath(), "gsp");

            // If this file has parameters or does not exist or is no longer valid, then write it out
            if (llHasParameters || 
                    !loProcessedFile.exists() || loProcessedFile.length() == 0 ||
                        loProcessedFile.lastModified() <= m_oCachedGSP.lastModified())
            {
                // Load the GSP document in to memory to transform
                String lcGSPDocument = Goliath.IO.Utilities.File.toString(m_oCachedGSP);

                // Get the XSLT
                String lcGSPTransform = llXSLTHasParameters ? Goliath.IO.Utilities.File.toString(m_oCachedXSLT) : null;

                // Initial replacement of parameters, this ensures that any transformation variables are resolved before a transform
                lcGSPDocument = llGSPHasParameters ? Goliath.IO.Utilities.replaceParameters(lcGSPDocument, toParameters, false) : lcGSPDocument;
                lcGSPTransform = llXSLTHasParameters ? Goliath.IO.Utilities.replaceParameters(lcGSPTransform, toParameters, false) : null;

                // If the GSP has parameters we need to write it out
                if (lcGSPTransform != null && llXSLTHasParameters)
                {
                    loProcessXSLT = new Goliath.IO.File(Goliath.IO.Utilities.File.getTemporary("xslt"));
                    FileWriter loXSLTWriter = new FileWriter(loProcessXSLT);
                    loXSLTWriter.write(lcGSPTransform);
                    loXSLTWriter.close();
                }

                // Transform the file
                Document loDoc = Goliath.XML.Utilities.transform(lcGSPDocument, loProcessXSLT.getAbsolutePath(), false);

                // TODO: Revisit dealing with ampersands in xml
                lcGSPDocument = Goliath.XML.Utilities.stripXMLHeader(Goliath.XML.Utilities.toString(loDoc).replaceAll("\\[-amp-]", "&"));

                // Final parameter replacement
                lcGSPDocument = Goliath.IO.Utilities.replaceParameters(lcGSPDocument, toParameters, true);

                // Make sure the file and directory structure exist
                if (!loProcessedFile.exists())
                {
                    Goliath.IO.Utilities.File.create(loProcessedFile);
                }
                loProcessedFile = new Goliath.IO.File(loProcessedFile);

                if (!((Goliath.IO.File)loProcessedFile).isTemporary())
                {
                    loProcessedFile = Goliath.IO.Utilities.File.getTemporary("gsp");
                }
                // Write out the final file
                FileWriter loFileWriter = new FileWriter(loProcessedFile);
                // XSLT Transformations can't handle the DOCTYPE, so it is hardcoded here for the moment
                loFileWriter.write("<!DOCTYPE html>\r\n");
                loFileWriter.write(lcGSPDocument);
                loFileWriter.close();
                
                return (Goliath.IO.File)loProcessedFile;
            }
        }
        catch (Throwable ex)
        {
            toErrors.add(ex);
        }
        return null;
    }
    
    
    
    /**
     * Gets the list of dependencies for the file specified
     * @return the list of dependencies
     */
    public final List<Goliath.IO.File> getDependencies(List<Throwable> toErrors)
    {
        if (m_oDependencies == null)
        {
            synchronized (m_oGSP)
            {
                m_oDependencies = new List<File>();
                // Extract all the required properties
                List<File> loDeps = new List<File>();
                extractDependencies(m_oGSP, loDeps, toErrors);
                m_oDependencies = new List<File>(loDeps);
            }
        }
        return m_oDependencies;
    }
    
    /**
     * Gets the list of dependencies from the .gsp
     * @param toGSP the gsp file to extract the dependencies from
     * @param toList the list to add the dependencies to
     */
    private void extractDependencies(File toGSP, List<File> toList, List<Throwable> toErrors)
    {
        // Implement this by processing with a file channel
        Goliath.Utilities.checkParameterNotNull("toGSP", toGSP);
        Goliath.Utilities.checkParameterNotNull("toList", toList);

        // TODO: Build up some metadata around the dependencies so we can monitor for changes rather than reading the files each time

        if (toGSP.exists() && !toList.contains(toGSP))
        {
            toList.add(toGSP);
            // Load the GSP document in to memory to transform
            String lcGSPDocument = Goliath.IO.Utilities.File.toString(toGSP);

            // Extract all of the includes from the .gsp
            Matcher loMatcher = getIncludeMatcher(lcGSPDocument, true);
            while (loMatcher.find())
            {
                // Loop through each of the groups and get the dependencies
                java.io.File loIncluded = getFileFromMatch(toGSP, loMatcher, toErrors);
                if (loIncluded.exists() && !toList.contains(loIncluded))
                {
                    try
                    {
                        // Also need to process the file just included if it is a .gsp
                        if (loIncluded.getName().toLowerCase().endsWith(".gsp") ||
                                loIncluded.getName().toLowerCase().endsWith(".xslt"))
                        {
                            // If this is an xslt, it could contain an xsl:include or xsl:import, we want to deal with them as well
                            extractDependencies(new File(loIncluded), toList, toErrors);
                        }
                        else
                        {
                            if (!toList.contains(new File(loIncluded)))
                            {
                                toList.add(new File(loIncluded));
                            }
                        }
                    }
                    catch(Throwable ex)
                    {
                        toErrors.add(ex);
                    }
                }
                else if (!loIncluded.exists())
                {
                    toErrors.add(new Goliath.Exceptions.Exception("The file " + loIncluded.getAbsolutePath() + " was included in " + toGSP.getAbsolutePath() + " but it does not exist", false));
                }
            }
        }
    }
    
    /**
     * Extracts the relative file location from the xslt
     * @param toRootLocation the file to base the location on
     * @param toMatcher the Matcher to get the file name from
     * @return the Relative file.
     */
    private java.io.File getFileFromMatch(File toRootLocation, Matcher toMatcher, List<Throwable> toErrors)
    {
        // Group 1 is the <include> group 2 is <xsl:import, group 3 is <xsl:include
        return Goliath.IO.Utilities.File.getRelativeLocation(toRootLocation, Goliath.Utilities.isNull(toMatcher.group(1), Goliath.Utilities.isNull(toMatcher.group(2), toMatcher.group(3))));
    }
    
    
}
