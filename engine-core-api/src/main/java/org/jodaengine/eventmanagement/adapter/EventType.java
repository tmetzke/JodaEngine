package org.jodaengine.eventmanagement.adapter;

import java.util.UUID;

import javax.annotation.Nonnull;

import org.jodaengine.util.Identifiable;

/**
 * The Event Type which are supported by our adapters, such as E-Mail or Telnet.
 */
public interface EventType extends Identifiable<UUID> {

    /**
     * Gets a human readable name of the Event (such as "E-Mail").
     * 
     * @return the event type's name
     */
    @Nonnull
    String getName();
}
