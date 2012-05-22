/* ========================================================
 * DynamicView.java
 *
 * Author:      Vinodhini Anand
 * Created:     Jan 28, 2011, 11:20:54 AM
 *
 * Description
 * --------------------------------------------------------
 * General Class Description.
 *
 * Change Log
 * --------------------------------------------------------
 * Init.Date        Ref.            Description
 * --------------------------------------------------------
 *
 * ===================================================== */

package Goliath.UI.Views.DynamicView;

import Goliath.Applications.Application;
import Goliath.Collections.HashTable;
import Goliath.Collections.List;
import Goliath.Collections.PropertySet;
import Goliath.Environment;
import Goliath.Interfaces.UI.Controls.IContainer;
import Goliath.Interfaces.UI.Controls.IControl;
import Goliath.UI.Controls.Label;
import Goliath.UI.Controls.Layouts.GridLayoutManager;
import Goliath.UI.Views.View;
import java.io.File;
import java.util.Stack;
import javax.xml.parsers.SAXParserFactory;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.helpers.DefaultHandler;


        
/**
 * This class renders a .gsp file to a Goliath View for rendering by the
 * visual adapters
 *
 * For example:
 * <pre>
 *      Example usage
 * </pre>
 *
 * @see         Related Class
 * @version     1.0 Jan 28, 2011
 * @author      kenmchugh
**/
public class DynamicView extends View
{
    private static HashTable<String, DynamicViewHelper> g_oHelpers;

    /**
     * Helper class for handling the gsp parsing and the SAX events
     */
    private class GSPSaxHandler extends DefaultHandler
    {

        private List<String> m_oErrors;

        /**
         * Adds an error to the list of errors
         * @param toException the error to add
         */
        private void addError(Throwable toException)
        {
            if (m_oErrors == null)
            {
                m_oErrors = new List<String>();
            }
            m_oErrors.add(toException.getLocalizedMessage());
        }

        /**
         * Checks if this handler has any errors
         * @return true if there are any errors in the collection
         */
        public boolean hasErrors()
        {
            return m_oErrors != null && m_oErrors.size() > 0;
        }

        /**
         * Gets the list of errors, if there are no errors, an empty list is returned
         * @return the list of errors
         */
        public List<String> getErrors()
        {
            return m_oErrors == null ? new List<String>(0) : m_oErrors;
        }

        /**
         * Clears the list of errors
         */
        public void clearList()
        {
            m_oErrors = null;
        }

        @Override
        public void error(SAXParseException toSpe)
        {
            addError(toSpe);
        }

        @Override
        public void fatalError(SAXParseException toSpe)
        {
            addError(toSpe);
        }

        @Override
        public void startElement(String tcUri, String tcLocalName, String tcTagName,
                        Attributes toAttr) throws SAXException
        {
            // Get the dynamic view helper for the tag
            DynamicViewHelper loHelper = getDynamicViewHelper(tcTagName);
            IControl loCreatedControl = (loHelper == null) ?
                new Label("No helper for tag " + tcTagName) :
                loHelper.createControl(tcTagName, createPropertySet(toAttr));

            IContainer loCurrentLevel = m_oContainerStack.peek();

            loCurrentLevel.addControl(loCreatedControl);

            if (loHelper != null && loHelper.isContainer())
            {
                m_oContainerStack.push((IContainer)loCreatedControl);
            }
        }

        @Override
        public void endElement(String tcUri, String tcLocalName, String tcTagName)
                        throws SAXException
        {
            // Get the dynamic view helper for the tag
            DynamicViewHelper loHelper = getDynamicViewHelper(tcTagName);

            if (loHelper != null && loHelper.isContainer())
            {
                if (m_oContainerStack.size() >0)
                {
                    m_oContainerStack.pop();
                }
            }
        }

        /**
         * Converts a set of sax attributes to a propertyset
         * @param toAttributes
         */
        private PropertySet createPropertySet(Attributes toAttributes)
        {
            PropertySet loReturn = new PropertySet();
            if (toAttributes != null)
            {
                for (int i=0, lnLength = toAttributes.getLength(); i<lnLength; i++)
                {
                    loReturn.setProperty(toAttributes.getLocalName(i), toAttributes.getValue(i));
                }
            }
            return loReturn;
        }

        /**
         * Helper function to get and cache any dynamic view helpers
         * @param tcTagName the name of the tag to get the helper for
         * @return the dynamic view helper
         */
        private DynamicViewHelper getDynamicViewHelper(String tcTagName)
        {
            if (g_oHelpers == null)
            {
                g_oHelpers = new HashTable<String, DynamicViewHelper>();

                List<Class<DynamicViewHelper>> loHelpers = Application.getInstance().getObjectCache().getClasses(DynamicViewHelper.class);
                try
                {
                    // Loop through all of the classes that inherit from dynamic view
                    for (Class<DynamicViewHelper> loClass : loHelpers)
                    {
                        // Create a new instance of each class
                        DynamicViewHelper loHelper = loClass.newInstance();

                        // Store the class in the cache
                        for (String lcSupported : loHelper.getSupportedTags())
                        {
                            g_oHelpers.put(lcSupported, loHelper);
                        }
                    }
                }
                catch (Throwable ex)
                {
                    // Could not create the view helper class
                    Application.getInstance().log(ex);
                }
            }
            return g_oHelpers.get(tcTagName.toLowerCase());
        }
        
    }

    private GSPSaxHandler m_oHandler;
    private Stack<IContainer> m_oContainerStack;


    /**
     * Creates a new instance of a Dynamic view
     * @param tcGspFile The full path and file name of the .gsp file
     */
    public DynamicView(String tcGspFile)
    {
        this(new File(tcGspFile));
    }

    /**
     * Creates a new instance of a Dynamic view from the file passed in
     * @param toGspFile the File to use to create the view
     */
    public DynamicView(File toGSPFile)
    {
        Goliath.Utilities.checkParameterNotNull("toGSPFile", toGSPFile);

        initialiseComponent();

        if (!toGSPFile.exists())
        {
            // The file does not exist, so create a label notifying the user
            this.addControl(new Label("The file specified does not exist :" + toGSPFile.getAbsolutePath()));
        }
        else
        {
            try
            {
                m_oHandler = new GSPSaxHandler();
                
                // Set up the stack for parsing
                if (m_oContainerStack == null)
                {
                    m_oContainerStack = new Stack<IContainer>();
                    m_oContainerStack.push(this);
                }
                
                // Parse the .gsp file
                SAXParserFactory.newInstance().newSAXParser().parse(toGSPFile, m_oHandler);
            }
            catch (Throwable ex)
            {
                // Clear any controls that are in place.
                this.clearChildren();
                // There was an error parsing the .gsp file, so inform the user of the errors
                this.addControl(new Label("The following errors occurred while parsing the file " + toGSPFile.getAbsolutePath() + Environment.NEWLINE() + ex.getLocalizedMessage()));
            }
        }
    }

    /**
     * Initialises the component
     */
    private void initialiseComponent()
    {
        //setLayoutManager(new GridLayoutManager(1, 1));
        setLayoutManager(GridLayoutManager.class);
    }
}



/*

public class DynamicViewSax extends View
{

	class GSPSaxHandler extends DefaultHandler
	{

		@Override
		public void characters(char[] tcChars, int tnStart, int tnLen)
				throws SAXException
		{
		   String lcContentStr = new String(tcChars, tnStart, tnLen);
		   // skip blank lines
		   lcContentStr = lcContentStr.trim();
		   if (lcContentStr.equals(""))
			   return;

		   if (m_oContent != null)
		   {
			   m_oContent = new StringBuilder();
		   }
		   // append the content to the buffer
		   m_oContent.append(lcContentStr);
		   //System.out.format("[CONTENT] %s\n", m_oContent);
		}
	}


	GSPSaxHandler m_oHandler;

	Stack<Container> m_oContainerStack;

	String m_cLastTag;

	Attributes m_oLastAttr;


	StringBuilder m_oContent;

	
        private void initialiseComponent(){
            GridLayoutManager loGridLM = new GridLayoutManager(1,1);
            setLayoutManager(loGridLM);
            //setLayoutManager(new BorderLayoutManager());
            //PropertySet loProperties = new PropertySet();

            //loProperties.setProperty("layoutConstraint", BorderLayoutConstants.CENTER());

            //Button bn = new Button();
            //bn.setText("center");
            //addControl(bn);
            //addControl(new Label("next"));


        }
	public DynamicViewSax(File toGspFile) throws IOException, SAXException, ParserConfigurationException
	{
            System.out.println("in constructor");
            initialiseComponent();

		m_oContainerStack = new Stack<Container>();
		if (!toGspFile.exists()) {
			throw new IOException("GSP file not found.");
		}

		SAXParserFactory loFactory = SAXParserFactory.newInstance();
		SAXParser loSaxParser = loFactory.newSAXParser();

		// instantiate the handler inner class
		m_oHandler = this.new GSPSaxHandler();

		// parse the gsp file
		loSaxParser.parse(toGspFile, m_oHandler);
	}

        public DynamicViewSax(){
            initialiseComponent();
        }

	
}

 * 
 */