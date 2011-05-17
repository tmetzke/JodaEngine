package org.jodaengine.correlation.registration;

import java.util.List;
import java.util.UUID;

import org.jodaengine.correlation.adapter.AdapterConfiguration;
import org.jodaengine.correlation.adapter.EventType;


/**
 * The Class StartEventImpl. Have a look at {@link StartEvent}.
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
    public StartEventImpl(EventType type,
                          AdapterConfiguration config,
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
