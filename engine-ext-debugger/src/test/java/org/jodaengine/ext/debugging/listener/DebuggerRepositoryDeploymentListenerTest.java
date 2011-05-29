package org.jodaengine.ext.debugging.listener;

import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.util.List;

import org.jodaengine.RepositoryService;
import org.jodaengine.exception.IllegalStarteventException;
import org.jodaengine.ext.debugging.DebuggerServiceImpl;
import org.jodaengine.ext.debugging.shared.DebuggerAttribute;
import org.jodaengine.ext.listener.RepositoryDeploymentListener;
import org.jodaengine.ext.service.ExtensionNotAvailableException;
import org.jodaengine.node.factory.bpmn.BpmnCustomNodeFactory;
import org.jodaengine.node.factory.bpmn.BpmnProcessDefinitionModifier;
import org.jodaengine.process.definition.ProcessDefinition;
import org.jodaengine.process.definition.ProcessDefinitionBuilder;
import org.jodaengine.process.definition.ProcessDefinitionBuilderImpl;
import org.jodaengine.util.testing.AbstractJodaEngineTest;
import org.mockito.ArgumentCaptor;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

/**
 * This class tests the {@link DebuggerRepositoryDeploymentListener} implementation.
 * 
 * @author Jan Rehwaldt
 * @since 2011-05-29
 */
public class DebuggerRepositoryDeploymentListenerTest extends AbstractJodaEngineTest {
    
    private RepositoryDeploymentListener deployListener;
    private DebuggerServiceImpl debugger;
    
    /**
     * Setup.
     * 
     * @throws ExtensionNotAvailableException test fails
     */
    @BeforeClass
    public void setUp() throws ExtensionNotAvailableException {
        this.debugger = mock(DebuggerServiceImpl.class);
        this.deployListener = new DebuggerRepositoryDeploymentListener(this.debugger);
    }
    
    /**
     * Tests that the listener correctly extracts the debugger enabled state.
     * 
     * @throws IllegalStarteventException test fails
     */
    @SuppressWarnings("unchecked")
    @Test
    public void testBreakpointsAreCorrectlyRegistered() throws IllegalStarteventException {
        
        //
        // build a definition
        //
        ProcessDefinitionBuilder builder = new ProcessDefinitionBuilderImpl();
        BpmnCustomNodeFactory.createBpmnNullStartNode(builder);
        BpmnProcessDefinitionModifier.decorateWithDefaultBpmnInstantiationPattern(builder);
        ProcessDefinition definition = builder.buildDefinition();
        
        DebuggerAttribute attribute = DebuggerAttribute.getAttributeIfExists(definition);
        Assert.assertNotNull(attribute);
        
        Assert.assertNotNull(attribute.getBreakpoints());
        Assert.assertEquals(attribute.getBreakpoints().size(), 1);
        
        //
        // simulate the definition deployment
        //
        RepositoryService repository = this.jodaEngineServices.getRepositoryService();
        this.deployListener.definitionDeployed(repository, definition);
        
        //
        // the breakpoints should be registered within our DebuggerService
        //
        @SuppressWarnings("rawtypes")
        ArgumentCaptor<List> breakpointsCaptor = ArgumentCaptor.forClass(List.class);
        verify(this.debugger, times(1)).registerBreakpoints(breakpointsCaptor.capture(), eq(definition));
        
        Assert.assertEquals(attribute.getBreakpoints().size(), breakpointsCaptor.getValue().size());
        Assert.assertEquals(attribute.getBreakpoints(), breakpointsCaptor.getValue());
    }
}
