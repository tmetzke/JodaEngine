package de.hpi.oryxengine.correlation.timing;

import javax.annotation.Nonnull;

import de.hpi.oryxengine.correlation.CorrelationManager;
import de.hpi.oryxengine.correlation.adapter.InboundPullAdapter;
import de.hpi.oryxengine.correlation.adapter.TimedAdapterConfiguration;
import de.hpi.oryxengine.exception.AdapterSchedulingException;

/**
 * This class is responsible for providing timing support.
 */
public interface TimingManager {

    /**
     * Registers a new pull adapter.
     * 
     * @param adapter the adapter
     * @throws AdapterSchedulingException thrown if scheduling fails
     */
    void registerPullAdapter(@Nonnull InboundPullAdapter adapter)
    throws AdapterSchedulingException;
    void registerNonRecurringJob(@Nonnull TimedAdapterConfiguration configuration)
    throws AdapterSchedulingException;
}
