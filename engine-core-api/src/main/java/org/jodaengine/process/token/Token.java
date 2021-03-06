package org.jodaengine.process.token;

import java.util.Collection;
import java.util.Map;
import java.util.UUID;

import javax.annotation.Nullable;

import org.codehaus.jackson.annotate.JsonBackReference;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.annotate.JsonTypeInfo;
import org.codehaus.jackson.annotate.JsonTypeInfo.As;
import org.codehaus.jackson.annotate.JsonTypeInfo.Id;
import org.jodaengine.exception.JodaEngineException;
import org.jodaengine.navigator.Navigator;
import org.jodaengine.node.activity.ActivityState;
import org.jodaengine.process.instance.AbstractProcessInstance;
import org.jodaengine.process.structure.ControlFlow;
import org.jodaengine.process.structure.Node;
import org.jodaengine.util.Attributable;
import org.jodaengine.util.Identifiable;

/**
 * The Interface Token. A Token is able to navigate through the process, but does not make up the whole process
 * instance. Moreover it is a single strand of execution.
 */
@JsonTypeInfo(use = Id.CLASS, include = As.PROPERTY, property = "@classifier")
public interface Token extends Identifiable<UUID>, Attributable {

    /**
     * Gets the current node.
     * 
     * @return the current node
     */
    @JsonProperty
    Node getCurrentNode();

    /**
     * Sets the current node.
     * 
     * @param node
     *            the new current node
     */
    void setCurrentNode(Node node);

    /**
     * Gets the state of the activity, that belongs to the node that token currently points to. The token holds this
     * state, as want to have stateless Activity-obejcts.
     * 
     * @return the current activity state
     */
    @JsonProperty
    ActivityState getCurrentActivityState();

    /**
     * Executes a step for the given instance, which is usually a single step beginning with the current node.
     * 
     * @throws JodaEngineException
     *             the exception
     */
    void executeStep()
    throws JodaEngineException;

    /**
     * Create a new to navigate instance for every node. Therefore it is possible to use this generic for e.g. and,
     * xor...
     * 
     * @param controlFlowList
     *            a list with redirections
     * @return newly created subprocesses
     */
    Collection<Token> navigateTo(Collection<ControlFlow> controlFlowList);

    /**
     * Gets the process instance this token belongs to.
     * 
     * @return the context
     */
    @JsonBackReference
    AbstractProcessInstance getInstance();

    /**
     * Gets the last taken {@link ControlFlow} of the token.
     * 
     * @return the last taken {@link ControlFlow}
     */
    @JsonIgnore
    ControlFlow getLastTakenControlFlow();

    /**
     * Sets the last taken {@link ControlFlow}.
     * 
     * @param t
     *            the new last taken {@link ControlFlow}s
     */
    void setLastTakenControlFlow(ControlFlow t);

    /**
     * Stopping the token navigation.
     */
    void suspend();

    /**
     * Continuing the token navigation.
     * 
     * @param resumeObject
     *            - an object that is passed from class that resumes the Token
     */
    void resume(Object resumeObject);

    /**
     * Gets the navigator that this token is assigned to.
     * 
     * @return the navigator
     */
    @JsonIgnore
    Navigator getNavigator();

    /**
     * Cancels the currently ongoing activity.
     */
    void cancelExecution();
    
    /**
     * Checks if this token is suspandable.
     *
     * @return true, if is suspandable
     */
    @JsonIgnore
    boolean isSuspandable();

    //
    // TODO replace getInternal() etc with methods from Attributable
    //
    /**
     * Gets an internal variable. Internal variables can for example be used by activities that need state. These are
     * not process instance variables.
     * 
     * @param id
     *            the id
     * @return the internal variable
     */
    Object getInternalVariable(String id);

    /**
     * Get all internal variable. Internal variables can for example be used by activities that need state. These are
     * not process instance variables.
     * 
     * @return the internal variable
     */
    Map<String, Object> getAllInternalVariables();

    /**
     * Sets an internal variable. Internal variables can for example be used by activities that need state. These are
     * not process instance variables.
     * 
     * @param variableId
     *            the variable id
     * @param variableValue
     *            the variable value
     */
    void setInternalVariable(String variableId, Object variableValue);

    /**
     * Delete an internal variable.
     * 
     * @param id
     *            the id
     */
    void deleteInternalVariable(String id);
    
    /**
     * Returns the parent {@link Token}, if any.
     * 
     * @return a parent token, if this one was created via a parallel split
     */
    @Nullable Token getParentToken();
}
