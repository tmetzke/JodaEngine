package de.hpi.oryxengine.deployment.importer;

import java.io.InputStream;

import de.hpi.oryxengine.deployment.ProcessDefinitionImporter;
import de.hpi.oryxengine.deployment.importer.bpmn.BpmnXmlParser;
import de.hpi.oryxengine.process.definition.ProcessDefinition;
import de.hpi.oryxengine.util.io.FileStreamSource;
import de.hpi.oryxengine.util.xml.XmlParseable;

/**
 * It is capable of importing an XML file that represents a BPMN process.
 * 
 * In order to parse through the XML we use some classes of the Activiti Project (activiti.org).
 */
public class BpmnXmlImporter implements ProcessDefinitionImporter {


    private XmlParseable bpmnXmlParse;
    
    /**
     * Instantiates a new BPMN XML importer with the String representation of the XML-File.
     *
     * @param xmlString the xml string
     */
    public BpmnXmlImporter(String xmlString) {
        BpmnXmlParser bpmnXmlParser = new BpmnXmlParser();
        bpmnXmlParse = bpmnXmlParser.getXmlParseBuilder()
                                                 .defineSourceAsString(xmlString)
                                                 .buildXmlParse();
    }
    
    /**
     * Instantiates a new bpmn xml importer, using a fileStream as the source.
     *
     * @param fileStreamSource the file stream source
     */
    public BpmnXmlImporter(FileStreamSource fileStreamSource) {
        BpmnXmlParser bpmnXmlParser = new BpmnXmlParser();
        bpmnXmlParse = bpmnXmlParser.getXmlParseBuilder()
                                                 .defineSourceAsStreamSource(fileStreamSource)
                                                 .buildXmlParse();
    }
    
    /**
     * Instantiates a new bpmn xml importer using an inputStream as the source.
     *
     * @param inputStream the input stream
     */
    public BpmnXmlImporter(InputStream inputStream) {
        BpmnXmlParser bpmnXmlParser = new BpmnXmlParser();
        bpmnXmlParse = bpmnXmlParser.getXmlParseBuilder()
                                                 .defineSourceAsInputStream(inputStream)
                                                 .buildXmlParse();
    }
    

    @Override
    public ProcessDefinition createProcessDefinition() {
        bpmnXmlParse.execute();
        ProcessDefinition processDefinition = bpmnXmlParse.getFinishedProcessDefinition();
        return processDefinition;
    }
}
