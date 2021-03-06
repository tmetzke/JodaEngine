package org.jodaengine.navigator;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import org.jodaengine.bootstrap.JodaEngine;
import org.jodaengine.factory.node.RoutingBehaviourTestFactory;
import org.jodaengine.process.instance.ProcessInstance;
import org.jodaengine.process.structure.Node;
import org.jodaengine.process.token.BpmnToken;
import org.jodaengine.process.token.Token;
import org.jodaengine.process.token.builder.BpmnTokenBuilder;
import org.mockito.Mockito;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;


/**
 * The test for the navigator.
 */
public class NavigatorTest {

    private static final int SLEEP_TIME = 3000;

    private NavigatorImpl navigator = null;

    private Node node = null, node2 = null;

    private Token processToken = null;

    private final ByteArrayOutputStream out = new ByteArrayOutputStream();

    private PrintStream tmp = null;

    /**
     * Set up.
     * 
     * @throws Exception
     *             the exception
     */
    @BeforeClass
    public void setUp()
    throws Exception {

        tmp = System.out;
        System.setOut(new PrintStream(out));

        navigator = new NavigatorImpl();
        navigator.start(new JodaEngine());

        node = new RoutingBehaviourTestFactory().createWithAndSplit();
        node2 = new RoutingBehaviourTestFactory().createWithAndSplit();
        node.controlFlowTo(node2);
        processToken = new BpmnToken(node, new ProcessInstance(null, Mockito.mock(BpmnTokenBuilder.class)), navigator);

    }

    /**
     * Test signal length.
     * 
     */
    @Test
    public void testSignalLength() {

        navigator.startArbitraryInstance(processToken);

        // this is not so nice, but I am not sure how to test correctly with parrallel behaviour
        try {
            Thread.sleep(SLEEP_TIME);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Assert.assertEquals(processToken.getCurrentNode(), node2);
    }

    /**
     * Tear down.
     */
    @AfterClass
    public void tearDown() {

        System.setOut(tmp);
    }

}
