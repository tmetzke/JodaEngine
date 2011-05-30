package org.jodaengine.eventmanagement.subscription.processevent.start;

import org.jodaengine.eventmanagement.adapter.EventType;
import org.jodaengine.eventmanagement.adapter.configuration.AdapterConfiguration;
import org.jodaengine.eventmanagement.subscription.ProcessStartEvent;
import org.jodaengine.eventmanagement.subscription.condition.EventCondition;
import org.jodaengine.eventmanagement.subscription.processevent.AbstractProcessEvent;
import org.jodaengine.exception.DefinitionNotFoundException;
import org.jodaengine.exception.JodaEngineRuntimeException;
import org.jodaengine.navigator.Navigator;
import org.jodaengine.process.definition.ProcessDefinitionID;

/**
 * The Class StartEventImpl. Have a look at {@link ProcessStartEvent}.
 */
public class DefaultProcessStartEvent extends AbstractProcessEvent implements ProcessStartEvent {

    private ProcessDefinitionID definitionID;
    private Navigator navigator;

    /**
     * Instantiates a new start event impl.
     * 
     * @param type
     *            the type
     * @param config
     *            the config
     * @param condition
     *            the conditions
     * @param definitionID
     *            the def
     */
    // TODO @EVENTTEAM: EventType??? WTF? Configuration? hmm? Delete it we must? 
    public DefaultProcessStartEvent(EventType type,
                          AdapterConfiguration config,
                          EventCondition condition,
                          ProcessDefinitionID definitionID) {

        super(type, config, condition);
        this.definitionID = definitionID;
        // we do not yet have one
        this.navigator = null;
    }

    @Override
    public ProcessDefinitionID getDefinitionID() {

        return definitionID;
    }

    @Override
    public void trigger() {

        logger.info("Starting a new processInstance for event {}", this);
        try {

            navigator.startProcessInstance(getDefinitionID(), this);

        } catch (DefinitionNotFoundException e) {
            String errorMessage = "The processDefinition belonging to the id '" + getDefinitionID()
                + "' could not be found.";
            logger.error(errorMessage, e);
            throw new JodaEngineRuntimeException(errorMessage, e);
        } catch (NullPointerException e) {
            String errorMessage = "trying to start a process instance by calling the navigator " 
                + "without having injected a navigator";
            logger.error(errorMessage, e);
            throw new JodaEngineRuntimeException(errorMessage, e);
        }
        logger.info("ProcessInstance created for event {}", this);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {

        return "Start event of the processdefinitionId: " + definitionID.toString();
    }

    @Override
    public void injectNavigatorService(Navigator navigator) {

        this.navigator = navigator;

    }

}