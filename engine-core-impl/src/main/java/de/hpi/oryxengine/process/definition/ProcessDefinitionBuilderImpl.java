package de.hpi.oryxengine.process.definition;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.hpi.oryxengine.correlation.adapter.AdapterConfiguration;
import de.hpi.oryxengine.correlation.adapter.EventType;
import de.hpi.oryxengine.correlation.registration.EventCondition;
import de.hpi.oryxengine.correlation.registration.StartEvent;
import de.hpi.oryxengine.correlation.registration.StartEventImpl;
import de.hpi.oryxengine.exception.IllegalStarteventException;
import de.hpi.oryxengine.exception.JodaEngineRuntimeException;
import de.hpi.oryxengine.process.structure.Node;
import de.hpi.oryxengine.process.structure.NodeBuilder;
import de.hpi.oryxengine.process.structure.NodeBuilderImpl;
import de.hpi.oryxengine.process.structure.StartNodeBuilderImpl;
import de.hpi.oryxengine.process.structure.TransitionBuilder;
import de.hpi.oryxengine.process.structure.TransitionBuilderImpl;

/**
 * The Class ProcessBuilderImpl. As you would think, only nodes that were created using createStartNode() become
 * actually start nodes.
 */
public class ProcessDefinitionBuilderImpl implements ProcessDefinitionBuilder {

    private final Logger logger = LoggerFactory.getLogger(getClass());
    private List<Node> startNodes;
    private UUID id;
    private String name;
    private String description;
    private Map<StartEvent, Node> temporaryStartTriggers;
    private Map<String, Object> temporaryAttributeTable;
    private List<ProcessInstantiationPattern> temporaryInstantiationPatterns;

    /**
     * Instantiates some temporary datastructures.
     */
    public ProcessDefinitionBuilderImpl() {

        resetingThisBuilder();
    }

    private void resetingThisBuilder() {

        this.startNodes = new ArrayList<Node>();
        this.id = UUID.randomUUID();
        this.name = null;
        this.description = null;
        this.temporaryStartTriggers = new HashMap<StartEvent, Node>();
        this.temporaryAttributeTable = null;
        this.temporaryInstantiationPatterns = new ArrayList<ProcessInstantiationPattern>();
    }

    @Override
    public ProcessDefinitionBuilder setName(String processName) {

        this.name = processName;
        return this;
    }

    @Override
    public ProcessDefinitionBuilder setDescription(String description) {

        this.description = description;
        return this;

    }

    @Override
    public ProcessDefinitionBuilder createStartTrigger(EventType eventType,
                                                       AdapterConfiguration adapterConfig,
                                                       List<EventCondition> eventConditions,
                                                       Node startNode) {

        StartEvent event = new StartEventImpl(eventType, adapterConfig, eventConditions, id);
        this.temporaryStartTriggers.put(event, startNode);

        return this;
    }

    @Override
    public ProcessDefinitionBuilder setAttribute(String attributeId, Object attibuteValue) {

        if (this.temporaryAttributeTable == null) {
            this.temporaryAttributeTable = new HashMap<String, Object>();
        }

        this.temporaryAttributeTable.put(attributeId, attibuteValue);

        return this;
    }

    @Override
    public NodeBuilder getNodeBuilder() {

        return new NodeBuilderImpl();
    }

    @Override
    public TransitionBuilder getTransitionBuilder() {

        return new TransitionBuilderImpl();
    }

    @Override
    public NodeBuilder getStartNodeBuilder() {

        return new StartNodeBuilderImpl(this);
    }

    /**
     * Getter for the StartNodes-List.
     * 
     * @return a {@link List} of {@link Node}
     */
    public List<Node> getStartNodes() {

        return startNodes;
    }

    @Override
    public void addInstanciationPattern(ProcessInstantiationPattern instantiationPattern) {

        temporaryInstantiationPatterns.add(instantiationPattern);
    }

    @Override
    public ProcessDefinition buildDefinition()
    throws IllegalStarteventException {

        checkingDefinitionConstraints();

        ProcessDefinitionImpl definition = buildResultDefinition();

        // cleanup
        resetingThisBuilder();

        return definition;
    }

    private ProcessDefinitionImpl buildResultDefinition()
    throws IllegalStarteventException {

        ProcessInstantiationPattern startInstantionPattern = appendingInstantiationPatterns();

        ProcessDefinitionImpl definition = new ProcessDefinitionImpl(id, name, description, startNodes,
            startInstantionPattern);

        for (Map.Entry<StartEvent, Node> entry : temporaryStartTriggers.entrySet()) {
            definition.addStartTrigger(entry.getKey(), entry.getValue());
        }

        return definition;
    }

    private ProcessInstantiationPattern appendingInstantiationPatterns() {

        ProcessInstantiationPattern lastInstantiationPattern = null;
        for (ProcessInstantiationPattern instantiationPattern : temporaryInstantiationPatterns) {

            if (lastInstantiationPattern != null) {
                lastInstantiationPattern.setNextPattern(instantiationPattern);
            }
            lastInstantiationPattern = instantiationPattern;
        }

        // Returning the first Pattern
        return temporaryInstantiationPatterns.get(0);
    }

    private void checkingDefinitionConstraints() {

        if (temporaryInstantiationPatterns.isEmpty()) {

            String errorMessage = "No Pattern for the process instanciation was defined.";
            logger.error(errorMessage);
            throw new JodaEngineRuntimeException(errorMessage);
        }
    }
}
