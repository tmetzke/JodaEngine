package org.jodaengine.process.structure;

import java.util.List;
import java.util.UUID;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.annotate.JsonTypeInfo;
import org.codehaus.jackson.annotate.JsonTypeInfo.As;
import org.codehaus.jackson.annotate.JsonTypeInfo.Id;
import org.jodaengine.node.activity.Activity;
import org.jodaengine.node.incomingbehaviour.IncomingBehaviour;
import org.jodaengine.node.outgoingbehaviour.OutgoingBehaviour;
import org.jodaengine.util.Attributable;
import org.jodaengine.util.Identifiable;

/**
 * The Interface for Nodes. Nodes are hubs in the graph representation of a process.
 */
@JsonTypeInfo(use = Id.CLASS, include = As.PROPERTY, property = "@classifier")
public interface Node extends Identifiable<UUID>, Attributable {

    /**
     * Gets the activity. The activity is the behavior of a node.
     * 
     * @return the {@link Activity} of this node
     */
    @JsonProperty
    Activity getActivityBehaviour();

    /**
     * Gets the incoming behaviour.
     * 
     * @return the incoming behaviour
     */
    @JsonIgnore
    IncomingBehaviour getIncomingBehaviour();

    /**
     * Gets the outgoing behaviour.
     * 
     * @return the outgoing behaviour
     */
    @JsonIgnore
    OutgoingBehaviour getOutgoingBehaviour();

    /**
     * Next.
     * 
     * @return the next Node(s) depending on the node (normal nodes vs. Splits which have multiple next nodes).
     */
    @JsonProperty
    List<ControlFlow> getOutgoingControlFlows();

    /**
     * Gets the incoming ControlFlows.
     * 
     * @return the incoming {@link ControlFlow}s
     */
    @JsonIgnore
    List<ControlFlow> getIncomingControlFlows();

    /**
     * Describes a new outgoing edge to the given node.
     *
     * @param id the id of the control flow
     * @param node the node to which a new {@link ControlFlow} shall be established
     * @return the created {@link ControlFlow}
     */
    ControlFlow controlFlowTo(String id, Node node);
    
    /**
     * Describes a new outgoing edge to the given node with an auto-generated id.
     *
     * @param node the node
     * @return the control flow
     */
    ControlFlow controlFlowTo(Node node);

    /**
     * Creates a {@link ControlFlow} with a condition.
     * 
     * @param id the id of the control flow
     * @param node
     *            the destination
     * @param c
     *            the condition
     * @return the created {@link ControlFlow}
     */
    ControlFlow controlFlowToWithCondition(String id, Node node, Condition c);
    
    /**
     * Creates a {@link ControlFlow} with a condition and an auto-generated id.
     *
     * @param node the node
     * @param c the c
     * @return the control flow
     */
    ControlFlow controlFlowToWithCondition(Node node, Condition c);

}
