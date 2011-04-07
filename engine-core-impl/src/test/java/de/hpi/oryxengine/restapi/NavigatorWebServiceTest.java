package de.hpi.oryxengine.restapi;

import static org.testng.Assert.assertEquals;

import java.net.URISyntaxException;

import org.jboss.resteasy.core.Dispatcher;
import org.jboss.resteasy.mock.MockDispatcherFactory;
import org.jboss.resteasy.mock.MockHttpRequest;
import org.jboss.resteasy.mock.MockHttpResponse;
import org.jboss.resteasy.plugins.server.resourcefactory.POJOResourceFactory;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import de.hpi.oryxengine.Service;
import de.hpi.oryxengine.ServiceFactory;
import de.hpi.oryxengine.activity.impl.AddNumbersAndStoreActivity;
import de.hpi.oryxengine.activity.impl.EndActivity;
import de.hpi.oryxengine.exception.IllegalStarteventException;
import de.hpi.oryxengine.process.definition.NodeParameter;
import de.hpi.oryxengine.process.definition.NodeParameterImpl;
import de.hpi.oryxengine.process.definition.ProcessBuilder;
import de.hpi.oryxengine.process.definition.ProcessBuilderImpl;
import de.hpi.oryxengine.process.definition.ProcessDefinition;
import de.hpi.oryxengine.process.structure.Node;
import de.hpi.oryxengine.routing.behaviour.incoming.impl.SimpleJoinBehaviour;
import de.hpi.oryxengine.routing.behaviour.outgoing.impl.TakeAllSplitBehaviour;

/**
 * The Class NavigatorWebServiceTest.
 */
@ContextConfiguration(locations = "/test.oryxengine.cfg.xml")
@DirtiesContext(classMode = ClassMode.AFTER_CLASS)
public class NavigatorWebServiceTest extends AbstractTestNGSpringContextTests {

    // TODO extend these tests as soon as it is possible to deploy a real process. We need some more integration tests.
    private Dispatcher dispatcher;

    @Test
    public void testRunningInstances()
    throws URISyntaxException {

        MockHttpRequest request = MockHttpRequest.get("/navigator/runninginstances");
        MockHttpResponse response = new MockHttpResponse();

        dispatcher.invoke(request, response);

        assertEquals(Integer.valueOf(response.getContentAsString()).intValue(), 0);
    }

    @Test
    public void testFinishedInstances()
    throws URISyntaxException {

        MockHttpRequest request = MockHttpRequest.get("/navigator/endedinstances");
        MockHttpResponse response = new MockHttpResponse();

        dispatcher.invoke(request, response);

        assertEquals(Integer.valueOf(response.getContentAsString()).intValue(), 0);
    }

    @Test
    public void testStartInstance()
    throws IllegalStarteventException, URISyntaxException, InterruptedException {

        // create simple process
        ProcessBuilder builder = new ProcessBuilderImpl();
        NodeParameter param = new NodeParameterImpl();
        param.setActivity(new AddNumbersAndStoreActivity("result", 1, 1));
        param.makeStartNode(true);
        param.setOutgoingBehaviour(new TakeAllSplitBehaviour());
        param.setIncomingBehaviour(new SimpleJoinBehaviour());
        Node startNode = builder.createNode(param);

        param.setActivity(new EndActivity());
        param.makeStartNode(false);
        Node endNode = builder.createNode(param);

        builder.createTransition(startNode, endNode);
        ProcessDefinition definition = builder.buildDefinition();

        // deploy it

        ServiceFactory.getDeplyomentService().deploy(definition);
        String id = definition.getID().toString();

        // run it via REST request
        MockHttpRequest request = MockHttpRequest.get("/navigator/start/" + id);
        MockHttpResponse response = new MockHttpResponse();

        dispatcher.invoke(request, response);

        // check, if it has finished after two seconds.
        Thread.sleep(1500);

        request = MockHttpRequest.get("/navigator/endedinstances");
        response = new MockHttpResponse();

        dispatcher.invoke(request, response);

        assertEquals(Integer.valueOf(response.getContentAsString()).intValue(), 1);
    }

    @BeforeClass
    public void setUpAndStartServer() {

        dispatcher = MockDispatcherFactory.createDispatcher();
        POJOResourceFactory factory = new POJOResourceFactory(NavigatorWebService.class);

        dispatcher.getRegistry().addResourceFactory(factory);
        
        Service nav = (Service) ServiceFactory.getNavigatorService();
        nav.start();
    }
}