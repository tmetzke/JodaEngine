package de.hpi.oryxengine.activity.impl;

import de.hpi.oryxengine.activity.AbstractActivityImpl;
import de.hpi.oryxengine.processInstance.ProcessInstance;

// TODO Auto-generated Javadoc
/**
 * The Class AutomatedDummyNode.
 */
public class AutomatedDummyActivity extends AbstractActivityImpl {

    /** This is the message the node prints out during its execution. */
    private String message;

    /**
     * Instantiates a new automated dummy node.
     *  
     * @param s
     *            the String which message gets set to.
     */
    public AutomatedDummyActivity(String s) {

        super();
        this.message = s;
    }

    /**
     * @see de.hpi.oryxengine.activity.AbstractActivityImpl#execute()
     */
    @Override
    // A simple execution
    // all the state setting may be handled by superclass later on
    public void execute(ProcessInstance instance) {

        this.setState(State.RUNNING);
        System.out.println(this.message);
        this.setState(State.TERMINATED);
    }

}
