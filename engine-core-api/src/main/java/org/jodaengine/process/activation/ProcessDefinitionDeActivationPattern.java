package org.jodaengine.process.activation;

import org.jodaengine.process.definition.ProcessDefinition;
import org.jodaengine.util.PatternAppendable;

/**
 * The {@link ProcessDefinitionDeActivationPattern} is responsible for activation and deactivation of a
 * {@link ProcessDefinition}.
 * 
 * <p>
 * This Pattern is designed to part of a linked list of {@link ProcessDefinitionDeActivationPattern activationPattern}.
 * </p>
 */
public interface ProcessDefinitionDeActivationPattern extends PatternAppendable<ProcessDefinitionDeActivationPattern> {

    /**
     * Activates the {@link ProcessDefinition}.
     * 
     * @param patternContext
     *            - in order to provide further information for this {@link ProcessDefinitionActivationPattern
     *            activationPattern}.
     */
    void activateProcessDefinition(ProcessDefinitionActivationPatternContext patternContext);

    /**
     * Deactivates the {@link ProcessDefinition}.
     * 
     * @param patternContext
     *            - in order to provide further information for this {@link ProcessDefinitionDeactivationPattern
     *            activationPattern}.
     */
    void deactivateProcessDefinition(ProcessDefinitionActivationPatternContext patternContext);
}
