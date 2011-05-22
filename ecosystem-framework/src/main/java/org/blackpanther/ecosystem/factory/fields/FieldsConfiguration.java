package org.blackpanther.ecosystem.factory.fields;

import org.blackpanther.ecosystem.AgentConstants;
import org.blackpanther.ecosystem.agent.CreatureConstants;

import java.util.HashMap;
import java.util.Map;

/**
 * @author MACHIZAUD Andréa
 * @version 5/21/11
 */
public class FieldsConfiguration {

    private Map<String, FieldMould> wrappedFieldProvider = new HashMap<String, FieldMould>();

    public FieldsConfiguration(FieldMould... moulds) {
        for (FieldMould mould : moulds)
            wrappedFieldProvider.put(mould.getName(), mould);
    }

    public void updateMould(FieldMould mould) {
        if (mould != null)
            wrappedFieldProvider.put(mould.getName(), mould);
    }

    public Object getValue(String traitName) {
        FieldMould mould = wrappedFieldProvider.get(traitName);
        if (mould == null)
            throw new IllegalStateException(traitName + " is not bundled in this configuration");
        return mould.getValue();
    }

    public boolean isMutable(Class species, String trait) {
        try {
            GeneFieldMould mould = (GeneFieldMould) wrappedFieldProvider.get(trait);
            return mould.isMutable();
        } catch (Throwable e) {
            return false;
        }
    }

    public static void checkAgentConfiguration(FieldsConfiguration configuration) {
        for (String stateTrait : AgentConstants.BUILD_PROVIDED_AGENT_STATE)
            if (!configuration.wrappedFieldProvider.containsKey(stateTrait))
                throw new IllegalStateException("Given configuration is incomplete to build an agent : " +
                        "missing trait '" + stateTrait + "'");

        for (String genotypeTrait : AgentConstants.AGENT_GENOTYPE)
            if (!configuration.wrappedFieldProvider.containsKey(genotypeTrait))
                throw new IllegalStateException("Given configuration is incomplete to build an agent : " +
                        "missing trait '" + genotypeTrait + "'");
    }

    public static void checkCreatureConfiguration(FieldsConfiguration configuration) {
        for (String stateTrait : CreatureConstants.BUILD_PROVIDED_CREATURE_STATE)
            if (!configuration.wrappedFieldProvider.containsKey(stateTrait))
                throw new IllegalStateException("Given configuration is incomplete to build a creature : " +
                        "missing trait '" + stateTrait + "'");

        for (String genotypeTrait : CreatureConstants.CREATURE_GENOTYPE)
            if (!configuration.wrappedFieldProvider.containsKey(genotypeTrait))
                throw new IllegalStateException("Given configuration is incomplete to build a creature : " +
                        "missing trait '" + genotypeTrait + "'");
    }
}
