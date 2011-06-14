package org.blackpanther.ecosystem.factory.fields;

import org.blackpanther.ecosystem.behaviour.BehaviorManager;
import org.blackpanther.ecosystem.math.Geometry;

import java.awt.*;
import java.io.Serializable;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import static org.blackpanther.ecosystem.agent.CreatureConstants.*;
import static org.blackpanther.ecosystem.agent.ResourceConstants.*;
import static org.blackpanther.ecosystem.factory.generator.StandardProvider.StandardProvider;
import static org.blackpanther.ecosystem.helper.Helper.isGene;

/**
 * @author MACHIZAUD Andréa
 * @version 1.0-alpha - Tue May 24 23:49:57 CEST 2011
 */
public class FieldsConfiguration
        implements Serializable, Cloneable {

    private static final long serialVersionUID = 1L;

    public static final String[] UNBOUNDED_FIELDS = new String[]{
            CREATURE_CURVATURE,
    };

    public static final String[] ANGLE_FIELDS = new String[]{
            CREATURE_ORIENTATION,
            CREATURE_ORIENTATION_LAUNCHER,
    };

    public static final String[] POSITIVE_FIELDS = new String[]{
            CREATURE_ENERGY,
            RESOURCE_ENERGY,
            CREATURE_SPEED,
            CREATURE_SPEED_LAUNCHER,
            CREATURE_FECUNDATION_COST,
            CREATURE_SENSOR_RADIUS,
            CREATURE_CONSUMMATION_RADIUS,
    };

    public static final String[] PROBABILITY_FIELDS = new String[]{
            CREATURE_MOVEMENT_COST,
            CREATURE_FECUNDATION_LOSS,
            CREATURE_GREED,
            CREATURE_FLEE,
            CREATURE_IRRATIONALITY,
            CREATURE_MORTALITY,
            CREATURE_FECUNDITY,
            CREATURE_MUTATION
    };

    public static final String[] COLOR_FIELDS = new String[]{
            CREATURE_NATURAL_COLOR,
            RESOURCE_NATURAL_COLOR,
            CREATURE_COLOR
    };

    public static final String[] BEHAVIOR_FIELDS = new String[]{
            CREATURE_BEHAVIOR
    };

    static {
        Arrays.sort(UNBOUNDED_FIELDS);
        Arrays.sort(ANGLE_FIELDS);
        Arrays.sort(POSITIVE_FIELDS);
        Arrays.sort(PROBABILITY_FIELDS);
        Arrays.sort(COLOR_FIELDS);
        Arrays.sort(BEHAVIOR_FIELDS);
    }

    private Map<String, FieldMould> wrappedFieldProvider = new HashMap<String, FieldMould>();

    public FieldsConfiguration(FieldMould... moulds) {
        for (FieldMould mould : moulds)
            wrappedFieldProvider.put(mould.getName(), mould);
    }

    @SuppressWarnings("unchecked")
    public FieldsConfiguration(Properties props) {
        for (String field : UNBOUNDED_FIELDS) {
            Double fieldValue = Double.parseDouble((String) props.get(field));
            FieldMould mould =
                    isGene(field)
                            ? new GeneFieldMould(field, StandardProvider(fieldValue), false)
                            : new StateFieldMould(field, StandardProvider(fieldValue));
            wrappedFieldProvider.put(mould.getName(), mould);
        }
        for (String field : ANGLE_FIELDS) {
            Double fieldValue = Double.parseDouble((String) props.get(field)) % Geometry.PI_2;
            FieldMould mould =
                    isGene(field)
                            ? new GeneFieldMould(field, StandardProvider(fieldValue), false)
                            : new StateFieldMould(field, StandardProvider(fieldValue));
            wrappedFieldProvider.put(mould.getName(), mould);
        }
        for (String field : POSITIVE_FIELDS) {
            Double fieldValue = Math.max(
                    0.0,
                    Double.parseDouble((String) props.get(field)));
            FieldMould mould =
                    isGene(field)
                            ? new GeneFieldMould(field, StandardProvider(fieldValue), false)
                            : new StateFieldMould(field, StandardProvider(fieldValue));
            wrappedFieldProvider.put(mould.getName(), mould);
        }
        for (String field : PROBABILITY_FIELDS) {
            Double fieldValue = Math.max(
                    0.0,
                    Math.min(
                            1.0,
                            Double.parseDouble((String) props.get(field))));
            FieldMould mould =
                    isGene(field)
                            ? new GeneFieldMould(field, StandardProvider(fieldValue), false)
                            : new StateFieldMould(field, StandardProvider(fieldValue));
            wrappedFieldProvider.put(mould.getName(), mould);
        }
        for (String field : COLOR_FIELDS) {
            Color fieldValue = new Color(
                    Integer.parseInt(
                            (String) props.get(field),
                            16));
            FieldMould mould =
                    isGene(field)
                            ? new GeneFieldMould(field, StandardProvider(fieldValue), false)
                            : new StateFieldMould(field, StandardProvider(fieldValue));
            wrappedFieldProvider.put(mould.getName(), mould);
        }
        for (String field : BEHAVIOR_FIELDS) {
            try {
                BehaviorManager fieldValue = (BehaviorManager) Class.forName(
                        (String) props.get(field)
                ).newInstance();
                FieldMould mould =
                        isGene(field)
                                ? new GeneFieldMould(field, StandardProvider(fieldValue), false)
                                : new StateFieldMould(field, StandardProvider(fieldValue));
                wrappedFieldProvider.put(mould.getName(), mould);
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
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

    public FieldMould getField(String traitName) {
        return wrappedFieldProvider.get(traitName);
    }

    public boolean isMutable(Class species, String trait) {
        isGene(species, trait);
        try {
            GeneFieldMould mould = (GeneFieldMould) wrappedFieldProvider.get(trait);
            return mould.isMutable();
        } catch (Throwable e) {
            return false;
        }
    }

    public static void checkResourceConfiguration(FieldsConfiguration configuration) {
        for (String stateTrait : BUILD_PROVIDED_RESOURCE_STATE)
            if (!configuration.wrappedFieldProvider.containsKey(stateTrait))
                throw new IllegalStateException("Given configuration is incomplete to build a resource : " +
                        "missing trait '" + stateTrait + "'");

        for (String genotypeTrait : RESOURCE_GENOTYPE)
            if (!configuration.wrappedFieldProvider.containsKey(genotypeTrait))
                throw new IllegalStateException("Given configuration is incomplete to build a resource : " +
                        "missing trait '" + genotypeTrait + "'");
    }

    public static void checkCreatureConfiguration(FieldsConfiguration configuration) {
        for (String stateTrait : BUILD_PROVIDED_CREATURE_STATE)
            if (!configuration.wrappedFieldProvider.containsKey(stateTrait))
                throw new IllegalStateException("Given configuration is incomplete to build a creature : " +
                        "missing trait '" + stateTrait + "'");

        for (String genotypeTrait : CREATURE_GENOTYPE)
            if (!configuration.wrappedFieldProvider.containsKey(genotypeTrait))
                throw new IllegalStateException("Given configuration is incomplete to build a creature : " +
                        "missing trait '" + genotypeTrait + "'");
    }

    @Override
    public FieldsConfiguration clone() {
        try {
            return (FieldsConfiguration) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new Error("unexpected error");
        }
    }
}
