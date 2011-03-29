package de.hpi.oryxengine.routing.behaviour.incoming.impl;

import java.util.LinkedList;
import java.util.List;

import de.hpi.oryxengine.process.instance.ProcessInstanceContext;
import de.hpi.oryxengine.process.token.Token;
import de.hpi.oryxengine.routing.behaviour.incoming.AbstractIncomingBehaviour;

/**
 * The Class AndJoinBehaviour. Realizes the joining of more than one incoming path.
 */
public class AndJoinBehaviour extends AbstractIncomingBehaviour {

    /**
     * Join.
     *
     * @param token the token
     * @return the list
     * {@inheritDoc}
     */
    @Override
    public List<Token> join(Token token) {
        ProcessInstanceContext context = token.getInstance().getContext();
        context.setWaitingExecution(token.getLastTakenTransition());
        return super.join(token);
    }
    /*
     * (non-Javadoc)
     * 
     * @see
     * de.hpi.oryxengine.routingBehaviour.joinBehaviour.AbstractJoinBehaviour#joinable(de.hpi.oryxengine.processInstance
     * .ProcessInstance)
     */
    /**
     * Joinable.
     *
     * @param token the token
     * @return true, if successful
     * {@inheritDoc}
     */
    @Override
    protected boolean joinable(Token token) {

        return token.joinable();
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * de.hpi.oryxengine.routingBehaviour.joinBehaviour.AbstractJoinBehaviour
     *      #performJoin(de.hpi.oryxengine.processInstance
     * .ProcessInstance)
     */
    /**
     * Perform join.
     *
     * @param token the token
     * @return the list
     * {@inheritDoc}
     */
    @Override
    protected List<Token> performJoin(Token token) {

        // We can do this, as we currently assume that an and join has a single outgoing transition
        List<Token> newTokens = new LinkedList<Token>();
        newTokens.add(token.performJoin());
        return newTokens;
    }

}
