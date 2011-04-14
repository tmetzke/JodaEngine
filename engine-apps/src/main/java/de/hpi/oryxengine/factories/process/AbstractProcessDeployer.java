package de.hpi.oryxengine.factories.process;

import java.util.UUID;

import de.hpi.oryxengine.ServiceFactory;
import de.hpi.oryxengine.exception.IllegalStarteventException;
import de.hpi.oryxengine.process.definition.ProcessBuilder;
import de.hpi.oryxengine.process.definition.ProcessDefinition;
import de.hpi.oryxengine.repository.DeploymentBuilder;
import de.hpi.oryxengine.repository.ProcessDefinitionImporter;
import de.hpi.oryxengine.repository.importer.RawProcessDefintionImporter;

/**
 * The Class AbstractProcessDeployer.
 */
public abstract class AbstractProcessDeployer implements ProcessDeployer {
    
    /** The builder. */
    protected ProcessBuilder builder;
    
    @Override
    public UUID deploy() throws IllegalStarteventException {
        this.createPseudoHuman();
        this.initializeNodes();
        ProcessDefinition definition = this.builder.buildDefinition();
        DeploymentBuilder deploymentBuilder = ServiceFactory.getRepositoryService().getDeploymentBuilder();
        deploymentBuilder.deployProcessDefinition(new RawProcessDefintionImporter(definition));
        return definition.getID();
    }
    
    /**
     * Initialize nodes.
     */
    abstract public void initializeNodes();
    
    /**
     * Creates a thread to complete human tasks. So human task Process deployers shall overwrite this method.
     * Nothing happens here for automated task nodes, so they must not overwrite this method.
     */
    public void createPseudoHuman() {

    }
    
    @Override
    public void stop() {
        // do nothing
    }
}