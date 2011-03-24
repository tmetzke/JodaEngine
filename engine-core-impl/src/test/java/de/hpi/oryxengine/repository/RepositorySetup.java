package de.hpi.oryxengine.repository;

import java.util.UUID;

import de.hpi.oryxengine.ServiceFactory;
import de.hpi.oryxengine.activity.impl.AddNumbersAndStoreActivity;
import de.hpi.oryxengine.process.definition.ProcessBuilder;
import de.hpi.oryxengine.process.definition.ProcessBuilderImpl;
import de.hpi.oryxengine.process.definition.ProcessDefinition;
import de.hpi.oryxengine.process.definition.StartNodeParameter;
import de.hpi.oryxengine.process.definition.StartNodeParameterImpl;
import de.hpi.oryxengine.process.structure.Node;
import de.hpi.oryxengine.routing.behaviour.incoming.impl.SimpleJoinBehaviour;
import de.hpi.oryxengine.routing.behaviour.outgoing.impl.TakeAllSplitBehaviour;

/**
 * The Class RepositorySetup.
 */
public final class RepositorySetup {

    /**
     * Hidden constructor.
     */
    private RepositorySetup() {

    }

    /** The Constant FIRST_EXAMPLE_PROCESS_ID. */
    public static final UUID FIRST_EXAMPLE_PROCESS_ID = UUID.randomUUID();

    /**
     * Fill repository.
     */
    public static void fillRepository() {

        ProcessRepository repo = ServiceFactory.getRepositoryService();
        repo.addDefinition(exampleProcess1());
    }

    /**
     * Example process1.
     * 
     * @return the process definition
     */
    private static ProcessDefinition exampleProcess1() {

        ProcessBuilder builder = new ProcessBuilderImpl();
        StartNodeParameter param = new StartNodeParameterImpl(new AddNumbersAndStoreActivity("result", 1, 1),
            new SimpleJoinBehaviour(), new TakeAllSplitBehaviour(), null); 
        // add a MailEvent here.

        Node node1 = builder.createStartNode(param);
        Node node2 = builder.createNode(param);
        builder.createTransition(node1, node2).setDescription("").setID(FIRST_EXAMPLE_PROCESS_ID);
        return builder.buildDefinition();
    }
}
