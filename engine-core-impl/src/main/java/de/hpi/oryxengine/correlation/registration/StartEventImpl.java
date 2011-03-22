package de.hpi.oryxengine.correlation.registration;

import java.util.List;
import java.util.UUID;

import de.hpi.oryxengine.correlation.adapter.AdapterType;
import de.hpi.oryxengine.correlation.adapter.PullAdapterConfiguration;

/**
 * The Class StartEventImpl.
 */
public class StartEventImpl extends ProcessEventImpl implements StartEvent {

    private UUID definitionID;

    /**
     * Instantiates a new start event impl.
     * 
     * @param type
     *            the type
     * @param config
     *            the config
     * @param conditions
     *            the conditions
     * @param definitionID
     *            the def
     */
    public StartEventImpl(AdapterType type,
                             PullAdapterConfiguration config,
                             List<EventCondition> conditions,
                             UUID definitionID) {

        super(type, config, conditions);
        this.definitionID = definitionID;
    }

    @Override
    public UUID getDefinitionID() {

        return definitionID;
    }

}
