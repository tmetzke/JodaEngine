package de.hpi.oryxengine.process.definition;

import javax.annotation.Nonnull;

import de.hpi.oryxengine.correlation.registration.StartEvent;
import de.hpi.oryxengine.exception.DalmatinaException;
import de.hpi.oryxengine.exception.IllegalStarteventException;
import de.hpi.oryxengine.process.structure.Condition;
import de.hpi.oryxengine.process.structure.Node;

/**
 * The Interface ProcessBuilder. The process builder is a comfortable way to construct a process definition.
 * 
 * @author Thorben
 */
public interface ProcessDefinitionBuilder {

    /**
     * Gets the definition as the result of the building process.
     * 
     * @return the process definition
     * @throws IllegalStarteventException
     *             the exception for an illegal start event
     */
    @Nonnull
    ProcessDefinition buildDefinition()
    throws IllegalStarteventException;

    /**
     * Creates a new node with the given parameters.
     * 
     * @param param
     *            the param
     * @return the node
     */
    @Nonnull
    Node createNode(NodeParameter param);

    /**
     * Creates a new start node for the process.
     * 
     * @param param
     *            the param
     * @return the node
     */
    @Nonnull
    Node createStartNode(NodeParameter param);

    // /**
    // * Creates a new start node with the given parameters.
    // *
    // * @param param the param
    // * @return the node
    // */
    // @Nonnull StartNode createStartNode(StartNodeParameter param);

    /**
     * Creates the transition.
     * 
     * @param source
     *            the source
     * @param destination
     *            the destination
     * @return the {@link ProcessDefinitionBuilder} in order to keep on building the {@link ProcessDefinition}
     */
    @Nonnull
    ProcessDefinitionBuilder createTransition(@Nonnull Node source, @Nonnull Node destination);

    /**
     * Creates the transition.
     * 
     * @param source
     *            the source
     * @param destination
     *            the destination
     * @param condition
     *            the condition
     * @return the {@link ProcessDefinitionBuilder} in order to keep on building the {@link ProcessDefinition}
     */
    @Nonnull
    ProcessDefinitionBuilder createTransition(@Nonnull Node source,
                                              @Nonnull Node destination,
                                              @Nonnull Condition condition);

    // /**
    // * Sets the iD.
    // *
    // * @param id
    // * the new ID
    // * @return the {@link ProcessDefinitionBuilder} in order to keep on building the {@link ProcessDefinition}
    // */
    // @Nonnull
    // ProcessBuilder setID(@Nonnull UUID id);

    /**
     * Sets the description.
     * 
     * @param processDescription
     *            the new description
     * @return the {@link ProcessDefinitionBuilder} in order to keep on building the {@link ProcessDefinition}
     */
    @Nonnull
    ProcessDefinitionBuilder setDescription(String processDescription);

    /**
     * sets the name of the process definition to build.
     * 
     * @param processName
     *            the process name
     * @return the {@link ProcessDefinitionBuilder} in order to keep on building the {@link ProcessDefinition}
     */
    @Nonnull
    ProcessDefinitionBuilder setName(String processName);

    /**
     * This will create a start trigger for the process definition.
     * 
     * @param event
     *            the event
     * @param startNode
     *            the start node
     * @throws DalmatinaException
     *             thrown if the provided node isn't a startNode.
     */
    void createStartTrigger(@Nonnull StartEvent event, @Nonnull Node startNode)
    throws DalmatinaException;

    /**
     * In order to store dynamic attributes to the {@link ProcessDefinition}.
     * 
     * @param attributeId
     *            - the id of the attribute
     * @param attibuteValue
     *            - value of the attribute
     * @return the {@link ProcessDefinitionBuilder} in order to keep on building the {@link ProcessDefinition}
     */
    @Nonnull
    ProcessDefinitionBuilder setAttribute(String attributeId, Object attibuteValue);
}