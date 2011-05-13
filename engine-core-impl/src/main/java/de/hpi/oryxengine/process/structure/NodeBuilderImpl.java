package de.hpi.oryxengine.process.structure;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.hpi.oryxengine.exception.JodaEngineRuntimeException;
import de.hpi.oryxengine.node.activity.Activity;
import de.hpi.oryxengine.node.incomingbehaviour.IncomingBehaviour;
import de.hpi.oryxengine.node.outgoingbehaviour.OutgoingBehaviour;

/**
 * The implementation of the {@link NodeBuilder}.
 */
public class NodeBuilderImpl implements NodeBuilder {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    protected Activity activityBehavior;
    protected OutgoingBehaviour outgoingBehaviour;
    protected IncomingBehaviour incomingBehaviour;

    @Override
    public NodeBuilder setActivityBehavior(Activity activityBehavior) {

        this.activityBehavior = activityBehavior;
        return this;
    }

    @Override
    public NodeBuilder setIncomingBehaviour(IncomingBehaviour incomingBehaviour) {

        this.incomingBehaviour = incomingBehaviour;
        return this;
    }

    @Override
    public NodeBuilder setOutgoingBehaviour(OutgoingBehaviour outgoingBehaviour) {

        this.outgoingBehaviour = outgoingBehaviour;
        return this;
    }

    @Override
    public Node buildNode() {

        checkingNodeConstraints();

        return buildResultNode();
    }

    /**
     * Builds the node to be retrieved. This method encapsulates the creation of the {@link Node}.
     * 
     * @return the {@link Node} to be retrieved
     */
    protected Node buildResultNode() {

        return new NodeImpl(activityBehavior, incomingBehaviour, outgoingBehaviour);
    }

    /**
     * This method checks the constraints for creating a node.
     */
    protected void checkingNodeConstraints() {

        if (activityBehavior == null) {

            String errorMessage = "The ActivityBehavior for the Node needs to be set."
                + "Perform setActivityBehavior(...) before.";

            logger.error(errorMessage);
            throw new JodaEngineRuntimeException(errorMessage);
        }
    }
}
