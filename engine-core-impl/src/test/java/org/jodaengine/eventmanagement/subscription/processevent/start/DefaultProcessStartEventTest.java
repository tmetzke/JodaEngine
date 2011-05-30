package org.jodaengine.eventmanagement.subscription.processevent.start; 

import org.jodaengine.deployment.Deployment;
import org.jodaengine.eventmanagement.subscription.ProcessEvent;
import org.jodaengine.eventmanagement.subscription.processevent.start.DefaultProcessStartEvent;
import org.jodaengine.exception.DefinitionNotFoundException;
import org.jodaengine.exception.IllegalStarteventException;
import org.jodaengine.navigator.NavigatorImpl;
import org.jodaengine.node.factory.TransitionFactory;
import org.jodaengine.node.factory.bpmn.BpmnCustomNodeFactory;
import org.jodaengine.node.factory.bpmn.BpmnNodeFactory;
import org.jodaengine.node.factory.bpmn.BpmnProcessDefinitionModifier;
import org.jodaengine.process.definition.ProcessDefinition;
import org.jodaengine.process.definition.ProcessDefinitionBuilder;
import org.jodaengine.process.definition.ProcessDefinitionID;
import org.jodaengine.util.testing.AbstractJodaEngineTest;
import org.mockito.Mockito;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 * Tests the {@link DefaultProcessStartEvent}.
 */
public class DefaultProcessStartEventTest extends AbstractJodaEngineTest {

    private ProcessDefinitionID processDefinitionID;

    /**
     * Test process event triggering.
     *
     * @throws DefinitionNotFoundException the definition not found exception
     * @throws InterruptedException the interrupted exception
     */
    @Test
    public void testProcessEventTriggering()
    throws DefinitionNotFoundException, InterruptedException {

        // Bind the startEvent to a deployed processDefinition
        DefaultProcessStartEvent startEvent = new DefaultProcessStartEvent(null, null, null, processDefinitionID);
        

        Assert.assertNotNull(startEvent.getDefinitionID());
        
        NavigatorImpl navigatorMock = Mockito.mock(NavigatorImpl.class);
        // we don't test the EventManager here, so we inject our mock manually
        startEvent.injectNavigatorService(navigatorMock);
        
        // Trigger should start a processInstance of the deployed process
        startEvent.trigger();

        // Let the processInstance finish
        Mockito.verify(navigatorMock).startProcessInstance(processDefinitionID, startEvent);
    }

    /**
     * Sets the up.
     * Ok it builds a process definition and deploys it.
     *
     * @throws IllegalStarteventException the illegal startevent exception
     */
    @BeforeMethod
    public void setUp()
    throws IllegalStarteventException {

        ProcessDefinition processDefinition = buildLittleProcessDefinition();
        processDefinitionID = processDefinition.getID();
        Deployment deployment = jodaEngineServices.getRepositoryService().getDeploymentBuilder()
        .addProcessDefinition(processDefinition).buildDeployment();

        jodaEngineServices.getRepositoryService().deployInNewScope(deployment);

    }

    /**
     * Build a little process that should be started by the {@link ProcessEvent}.
     *
     * @return the built {@link ProcessDefinition}
     * @throws IllegalStarteventException the illegal startevent exception
     */
    private ProcessDefinition buildLittleProcessDefinition()
    throws IllegalStarteventException {

        ProcessDefinitionBuilder definitionBuilder = jodaEngineServices.getRepositoryService().getDeploymentBuilder()
        .getProcessDefinitionBuilder();
        TransitionFactory.createTransitionFromTo(definitionBuilder,
            BpmnCustomNodeFactory.createBpmnNullStartNode(definitionBuilder),
            BpmnNodeFactory.createBpmnEndEventNode(definitionBuilder));

        BpmnProcessDefinitionModifier.decorateWithDefaultBpmnInstantiationPattern(definitionBuilder);
        return definitionBuilder.buildDefinition();
    }

}