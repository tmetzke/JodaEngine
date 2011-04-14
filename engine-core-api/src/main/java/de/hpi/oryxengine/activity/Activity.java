package de.hpi.oryxengine.activity;

import javax.annotation.Nonnull;

import de.hpi.oryxengine.process.token.Token;

/**
 * An activity is the behaviour of a Node, e.g. execution
 * behaviour for sending a mail.
 */
public interface Activity {
    
    /**
     * Execute. Starts the execution of the Activity.
     * 
     * @param token the instance the activity operates on
     */
    void execute(@Nonnull Token token);
    
    /**
     * Do some cleanup, if necessary. Cancellation of the activity's execution is not handled here.
     */
    void cancel();
   
}
