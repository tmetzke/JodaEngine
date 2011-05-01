package de.hpi.oryxengine.util.xml;

import java.io.InputStream;

import javax.xml.parsers.SAXParser;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.hpi.oryxengine.deployment.importer.bpmn.BpmnXmlParse;
import de.hpi.oryxengine.exception.DalmatinaRuntimeException;
import de.hpi.oryxengine.process.definition.ProcessDefinition;
import de.hpi.oryxengine.util.io.StreamSource;

/**
 * The {@link XmlParse} triggers the saxParser in order to parse through the xml. This class is designed to be inherited
 * from in order to implement custom parse behaviour and convert the XML into objects, e.g. see {@link BpmnXmlParse}.
 */
public class XmlParse implements XmlParseable {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private static final String JAXP_SCHEMA_SOURCE = "http://java.sun.com/xml/jaxp/properties/schemaSource";
    private static final String JAXP_SCHEMA_LANGUAGE = "http://java.sun.com/xml/jaxp/properties/schemaLanguage";
    private static final String W3C_XML_SCHEMA = "http://www.w3.org/2001/XMLSchema";

    protected XmlParser parser;
    
    protected StreamSource streamSource;
    
    protected XmlElement rootElement;
    
    private XmlProblemLogger problemLogger;

    protected String schemaResource;
    
    public XmlParse(XmlParser parser, StreamSource streamSource) {

        this(parser, streamSource, null);
    }

    /**
     * Default Constructor.
     * 
     * @param xmlParser
     *            - the {@link XmlParser} containing information about the concrete parse process.
     * @param streamSource
     *            - the {@link StreamSource} that contains the XML that should be parsed
     * @param schemaResource
     *            - this represents the XML schema that should be used by the {@link SAXParser}
     */
    public XmlParse(XmlParser xmlParser, StreamSource streamSource, String schemaResource) {

        this.parser = xmlParser;
        this.streamSource = streamSource;
        this.schemaResource = schemaResource;
        
        // Prepare fields for execution
        this.rootElement = null;
        this.problemLogger = new XmlProblemLogger(streamSource.getName());
    }

    @Override
    public ProcessDefinition getFinishedProcessDefinition() {

        String errorMessage = "This class does not support the generation of a process definintion."
            + "This class is supposed to be inherited from.";
        throw new UnsupportedOperationException(errorMessage);
    }

    @Override
    public XmlParseable execute() {

        try {
            InputStream inputStream = streamSource.getInputStream();

            if (schemaResource == null) {
                // must be done before parser is created
                parser.getSaxParserFactory().setNamespaceAware(false);
                parser.getSaxParserFactory().setValidating(false);
            }

            SAXParser saxParser = parser.getSaxParser();
            if (schemaResource != null) {
                saxParser.setProperty(JAXP_SCHEMA_LANGUAGE, W3C_XML_SCHEMA);
                saxParser.setProperty(JAXP_SCHEMA_SOURCE, schemaResource);
            }
            saxParser.parse(inputStream, new XmlParseHandler(this));

        } catch (Exception e) {
            String errorMessage = "The Stream '" + streamSource.getName() + "' could not be parsed. Following error ocurred: "
                + e.getMessage();
            logger.error(errorMessage, e);
            throw new DalmatinaRuntimeException(errorMessage, e);
        }

        return this;
    }

    /**
     * This class only parses through the XML and creates an object model of the XML. That's why this method retrieves
     * the {@link XmlElement rootElement} of the XML.
     * 
     * @return the {@link XmlElement rootElement}
     */
    public XmlElement getRootElement() {

        return rootElement;
    }

    public XmlProblemLogger getProblemLogger() {
    
        return problemLogger;
    }
}
