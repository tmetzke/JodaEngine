package org.jodaengine.eventmanagement.processevent.incoming.condition.simple;

import org.jodaengine.eventmanagement.adapter.AbstractAdapterEvent;

//TODO @Gerardo: please, really please learn how to mock.
/**
 * Only a dummy {@link AdapterEvent} for the {@link MethodInvokingEventConditionTest}.
 */
public class DummyAdapterEvent extends AbstractAdapterEvent {

    public final static String METHOD_RETURNS_STRING = "dummyMethodReturnsString";
    public final static String METHOD_RETURNS_INT = "dummyMethodReturnsInt";

    private String stringToReturn;
    private int intToReturn;

    /**
     * Constructor that has to exist.
     * 
     * @param stringToReturn
     *            - the {@link String} that should be returned
     * @param intToReturn
     *            - the {@link Integer} that should be returned
     */
    public DummyAdapterEvent(String stringToReturn, int intToReturn) {

        super(null);

        this.stringToReturn = stringToReturn;
        this.intToReturn = intToReturn;
    }

    /**
     * Dummy method that should be called by the {@link MethodInvokingEventCondition}.
     */
    public String dummyMethodReturnsString() {

        return stringToReturn;
    }

    /**
     * Dummy method that should be called by the {@link MethodInvokingEventCondition}.
     */
    public int dummyMethodReturnsInt() {

        return intToReturn;
    }
}
