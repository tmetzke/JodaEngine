package de.hpi.oryxengine.activity;

import static org.testng.Assert.assertEquals;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import de.hpi.oryxengine.activity.impl.NullActivity;
import de.hpi.oryxengine.exception.DalmatinaException;
import de.hpi.oryxengine.exception.IllegalStarteventException;
import de.hpi.oryxengine.navigator.NavigatorImplMock;
import de.hpi.oryxengine.process.definition.NodeParameter;
import de.hpi.oryxengine.process.definition.NodeParameterImpl;
import de.hpi.oryxengine.process.definition.ProcessBuilder;
import de.hpi.oryxengine.process.definition.ProcessBuilderImpl;
import de.hpi.oryxengine.process.definition.ProcessDefinition;
import de.hpi.oryxengine.process.instance.ProcessInstance;
import de.hpi.oryxengine.process.instance.ProcessInstanceImpl;
import de.hpi.oryxengine.process.structure.Node;
import de.hpi.oryxengine.process.token.Token;
import de.hpi.oryxengine.routing.behaviour.incoming.impl.SimpleJoinBehaviour;
import de.hpi.oryxengine.routing.behaviour.outgoing.impl.TakeAllSplitBehaviour;

/**
 * The Class ConcurrentActivityStateTest. It tackles a problem with our process definition, nodes and the contained
 * activities. If a node is executed several times, its activity is already "dirty" (e.g. its state is COMPLETED).
 * 
 * The Problem exists in the following cases: 1. Two instances work on the same process definition, execute the same
 * activity 2. One token is in a loop and executes the activity twice
 * 
 * Possible solutions: 1. Create the node-Tree for each process instance (which implies activity recreation). This will
 * only solve case 1 2. Create an activity instance, when the token reaches the node and only link some kind of activity
 * blueprint to the node upon definition creation. This should solve both cases. Drawbacks are: Plugins cannot be
 * registered as it is not clear when to do that (maybe keep them with the process instance, when did we do this before
 * (not in a test)?). Deepcopys of activity members might be needed (e.g. the task that is assigned to the
 * HumanTaskActivity has to be copied). I don't have a clue how to implement this cleanly.
 */
public class ConcurrentActivityStateTest {
    private ProcessDefinition definition;
    private Activity startActivity;
    private Node startNode;

    /**
     * One definition, two instances, two tokens that work on the same activities. This test ensures that these
     * activities do not share their state.
     * 
     * @throws DalmatinaException
     *             the dalmatina exception
     */
    @Test
    public void testConcurrentAcitivityUse()
    throws DalmatinaException {

        // Create two process instances
        NavigatorImplMock nav = new NavigatorImplMock();
        ProcessInstance instance1 = new ProcessInstanceImpl(definition);
        Token token1 = instance1.createToken(startNode, nav);

        ProcessInstance instance2 = new ProcessInstanceImpl(definition);
        Token token2 = instance2.createToken(startNode, nav);

        assertEquals(token1.getCurrentActivityState(), ActivityState.INIT);
        assertEquals(token2.getCurrentActivityState(), ActivityState.INIT);

        // Execute a step with token1 on instance1. We expect, that the activity state changes for instance1, but not
        // for instance2/token2.

        token1.executeStep();
        
        // TODO implement a method to view the states of formerly executed activities?
        // otherwise the states are hard to verify
        assertEquals(token1.getCurrentActivityState(), ActivityState.COMPLETED);
        assertEquals(token2.getCurrentActivityState(), ActivityState.INIT);

    }

    /**
     * Sets the up process.
     * 
     * @throws IllegalStarteventException
     *             the illegal startevent exception
     */
    @BeforeClass
    public void setUpProcess()
    throws IllegalStarteventException {

        ProcessBuilder builder = new ProcessBuilderImpl();
        NodeParameter param = new NodeParameterImpl();
//        startActivity = new NullActivity();
        param.setActivityClass(NullActivity.class);
        param.setIncomingBehaviour(new SimpleJoinBehaviour());
        param.setOutgoingBehaviour(new TakeAllSplitBehaviour());

        startNode = builder.createStartNode(param);

//        param.setActivity(new NullActivity());
        Node endNode = builder.createNode(param);

        definition = builder.createTransition(startNode, endNode).buildDefinition();

    }
}
