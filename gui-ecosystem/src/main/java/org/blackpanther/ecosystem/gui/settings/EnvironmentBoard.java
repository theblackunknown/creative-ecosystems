package org.blackpanther.ecosystem.gui.settings;

import org.blackpanther.ecosystem.ApplicationConstants;
import org.blackpanther.ecosystem.Configuration;
import org.blackpanther.ecosystem.agent.AgentConstants;
import org.blackpanther.ecosystem.agent.Creature;
import org.blackpanther.ecosystem.agent.CreatureConstants;
import org.blackpanther.ecosystem.agent.Resource;
import org.blackpanther.ecosystem.behaviour.BehaviorManager;
import org.blackpanther.ecosystem.behaviour.DraughtsmanBehaviour;
import org.blackpanther.ecosystem.factory.PopulationFactory;
import org.blackpanther.ecosystem.factory.fields.FieldMould;
import org.blackpanther.ecosystem.factory.fields.FieldsConfiguration;
import org.blackpanther.ecosystem.gui.GUIMonitor;
import org.blackpanther.ecosystem.gui.lightweight.EnvironmentInformation;
import org.blackpanther.ecosystem.gui.settings.fields.DoubleSpinnerField;
import org.blackpanther.ecosystem.gui.settings.fields.IntegerSpinnerField;
import org.blackpanther.ecosystem.gui.settings.fields.SettingField;
import org.blackpanther.ecosystem.gui.settings.fields.mutable.BehaviorField;
import org.blackpanther.ecosystem.math.Geometry;

import javax.swing.*;
import javax.swing.border.BevelBorder;
import java.awt.*;
import java.awt.event.ActionListener;
import java.beans.EventHandler;
import java.io.IOException;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

import static org.blackpanther.ecosystem.Configuration.*;
import static org.blackpanther.ecosystem.agent.Agent.*;
import static org.blackpanther.ecosystem.gui.formatter.RangeModels.*;

/**
 * @author MACHIZAUD Andréa
 * @version 1.1-alpha - Thu May 19 01:22:54 CEST 2011
 */
public class EnvironmentBoard extends JPanel {

    private static final Logger logger =
            Logger.getLogger(
                    EnvironmentBoard.class.getCanonicalName()
            );

    private static final String LABEL_ENVIRONMENT_ID = "Environment #%s %s";
    private static final String LABEL_ENVIRONMENT_AGENT = "Agent number : %d";
    private static final String LABEL_ENVIRONMENT_GENERATION = "#Generation %d";
    private static final String NO_ENVIRONMENT = "No Environment Set";
    private static final Dimension PREFERRED_SIZE = new Dimension(280, 800);

    private EnvironmentInformationInstance informationBoard =
            new EnvironmentInformationInstance();

    public EnvironmentBoard() {
        super();
        setMinimumSize(PREFERRED_SIZE);
        add(informationBoard);
        setBorder(
                BorderFactory.createBevelBorder(BevelBorder.RAISED)
        );

        //load agent configuration file
        try {
            Properties agentConfiguration = new Properties();
            agentConfiguration.load(
                    getClass().getClassLoader().getResourceAsStream("agent-configuration.properties"));
            updateInformation(new FieldsConfiguration(agentConfiguration));
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Initial Fields Configuration not found", e);
        }
    }

    private void updateInformation(FieldsConfiguration information) {
        informationBoard.updateInformation(information);
    }

    /*=========================================================================
                         MESSAGES
      =========================================================================*/

    public void clearBoard() {
        informationBoard.deactivate();
    }

    public void updateInformation(EnvironmentInformation information) {
        informationBoard.updateInformation(information);
    }

    public void updateInformation(Configuration information) {
        informationBoard.updateInformation(information);
    }

    public void notifyPause() {
        informationBoard.notifyPause();
    }

    public void notifyRun() {
        informationBoard.notifyRun();
    }

    public FieldsConfiguration createConfiguration() {
        return informationBoard.createConfiguration();
    }

    public void updateConfiguration() {
        informationBoard.update();
    }

    public class EnvironmentInformationInstance extends JPanel {

        public static final String APPLICATION_PARAMETERS = "Applications parameters";
        public static final String CREATURE_PARAMETERS = "Creature parameters";
        public static final String RESOURCE_PARAMETERS = "Resource parameters";
        public static final String AGENT_PLACEMENT = "Agent Placement";
        public static final String RESOURCE_PLACEMENT = "Resource Placement";

        private ApplicationParameterPanel internalApplicationParametersPanel =
                new ApplicationParameterPanel(APPLICATION_PARAMETERS);
        private CreatureParameterPanel internalCreatureParametersPanel =
                new CreatureParameterPanel(CREATURE_PARAMETERS);
        private ResourceParameterPanel internalResourceParametersPanel =
                new ResourceParameterPanel(RESOURCE_PARAMETERS);
        private CreaturePlacementPanel internalCreaturePlacementPanel =
                new CreaturePlacementPanel(AGENT_PLACEMENT);
        private ResourcePlacementPanel internalResourcePlacementPanel =
                new ResourcePlacementPanel(RESOURCE_PLACEMENT);

        private JLabel environmentLabel;
        private JLabel environmentAgentCounter;
        private JLabel environmentGenerationLabel;
        private JPanel parametersPanel;
        private JComboBox panelSelector;

        private final String[] PANELS_NAME = new String[]{
                APPLICATION_PARAMETERS,
                CREATURE_PARAMETERS,
                RESOURCE_PARAMETERS,
                AGENT_PLACEMENT,
                RESOURCE_PLACEMENT
        };

        public EnvironmentInformationInstance() {
            super(new BorderLayout());
            setName("Environment's information board");

            //initialize components
            environmentLabel =
                    new JLabel(NO_ENVIRONMENT);
            environmentLabel.setAlignmentX(LEFT_ALIGNMENT);

            environmentAgentCounter =
                    new JLabel(NO_ENVIRONMENT);
            environmentAgentCounter.setAlignmentX(LEFT_ALIGNMENT);

            environmentGenerationLabel =
                    new JLabel(NO_ENVIRONMENT);
            environmentGenerationLabel.setAlignmentX(LEFT_ALIGNMENT);

            //parameters panels
            parametersPanel = new JPanel(new CardLayout());
            parametersPanel.setPreferredSize(PREFERRED_SIZE);
            panelSelector = new JComboBox(PANELS_NAME);
            panelSelector.addActionListener(EventHandler.create(
                    ActionListener.class,
                    this,
                    "switchPanel",
                    "source.selectedItem"
            ));

            parametersPanel.add(
                    internalApplicationParametersPanel,
                    internalApplicationParametersPanel.getName());
            parametersPanel.add(
                    internalCreatureParametersPanel,
                    internalCreatureParametersPanel.getName()
            );
            parametersPanel.add(
                    internalResourceParametersPanel,
                    internalResourceParametersPanel.getName()
            );
            parametersPanel.add(
                    internalCreaturePlacementPanel,
                    internalCreaturePlacementPanel.getName()
            );
            parametersPanel.add(
                    internalResourcePlacementPanel,
                    internalResourcePlacementPanel.getName()
            );

            Box information = Box.createVerticalBox();

            information.add(environmentLabel);
            information.add(environmentGenerationLabel);
            information.add(environmentAgentCounter);
            information.add(Box.createVerticalStrut(20));

            //initialize layout objects
            Box box = Box.createVerticalBox();
            box.add(panelSelector);
            box.add(parametersPanel);

            add(information, BorderLayout.NORTH);
            add(box, BorderLayout.CENTER);

            //initialize fields
            updateInformation(Configuration);
        }

        /**
         * quick switch between internal panel thanks to combo box + card layout
         */
        public void switchPanel(String panelName) {
            CardLayout layout = (CardLayout) parametersPanel.getLayout();
            layout.show(parametersPanel, panelName);
        }

        public void updateInformation(EnvironmentInformation information) {
            this.environmentLabel.setText(
                    String.format(LABEL_ENVIRONMENT_ID,
                            Long.toHexString(information.getId()),
                            information.getState()));
            this.environmentAgentCounter.setText(
                    String.format(LABEL_ENVIRONMENT_AGENT,
                            information.getPoolSize()));
            this.environmentGenerationLabel.setText(
                    String.format(LABEL_ENVIRONMENT_GENERATION,
                            information.getGenerationCounter()));
        }

        public void updateInformation(Configuration information) {
            activate();
            internalApplicationParametersPanel.updateInformation(information);
        }

        public void updateInformation(FieldsConfiguration information) {
            activate();
            internalCreatureParametersPanel.updateInformation(information);
            internalResourceParametersPanel.updateInformation(information);
        }

        //forgive me for this hack...
        void notifyPause() {
            int index = environmentLabel.getText().lastIndexOf(" ");
            if (index > 0) {
                environmentLabel.setText(
                        environmentLabel.getText().substring(0, index + 1)
                                + EnvironmentInformation.State.PAUSED
                );
            }
        }

        //my apologies, again.
        public void notifyRun() {
            int index = environmentLabel.getText().lastIndexOf(" ");
            if (index > 0) {
                environmentLabel.setText(
                        environmentLabel.getText().substring(0, index + 1)
                                + EnvironmentInformation.State.RUNNING
                );
            }
        }

        private void activate() {
            panelSelector.setEnabled(true);
        }

        public void deactivate() {
            environmentLabel.setText(NO_ENVIRONMENT);
            environmentAgentCounter.setText(NO_ENVIRONMENT);
            environmentGenerationLabel.setText(NO_ENVIRONMENT);
            panelSelector.setSelectedItem(APPLICATION_PARAMETERS);
            panelSelector.setEnabled(false);
        }

        public void update() {
            Properties props = new Properties();
            props.putAll(internalApplicationParametersPanel.getParameters());
            Configuration.loadConfiguration(props);
        }

        public FieldsConfiguration createConfiguration() {
            Collection<FieldMould> agentFieldMould = new ArrayList<FieldMould>();
            agentFieldMould.addAll(internalCreatureParametersPanel.getMoulds());
            agentFieldMould.addAll(internalResourceParametersPanel.getMoulds());
            FieldMould[] mouldArray = new FieldMould[agentFieldMould.size()];
            agentFieldMould.toArray(mouldArray);
            return new FieldsConfiguration(mouldArray);
        }
    }

    /**
     * Handle panel switching by a card layout
     */

    class ApplicationParameterPanel extends ParameterPanel {

        public ApplicationParameterPanel(String name) {
            super(name);
        }

        @Override
        protected Map<String, SettingField> generateParameters() {
            return new TreeMap<String, SettingField>() {{
                for (String parameter : ApplicationConstants.DOUBLE_UNBOUNDED)
                    put(parameter,
                            new DoubleSpinnerField(parameter, generateDoubleModel()));

                for (String parameter : ApplicationConstants.POSITIVE_DOUBLE)
                    put(parameter,
                            new DoubleSpinnerField(parameter, generatePositiveDoubleModel()));

                for (String parameter : ApplicationConstants.POSITIVE_INTEGER)
                    put(parameter,
                            new IntegerSpinnerField(parameter, generatePositiveIntegerModel()));
            }};
        }

        @Override
        protected void generateContent(Box layout) {
            Box global = Box.createVerticalBox();
            Box threshold = Box.createVerticalBox();
            Box variation = Box.createVerticalBox();

            global.setBorder(BorderFactory.createTitledBorder("Miscellaneous"));
            threshold.setBorder(BorderFactory.createTitledBorder("Thresholds"));
            variation.setBorder(BorderFactory.createTitledBorder("Variations"));

            for (String parameter : ApplicationConstants.MISCELLANEOUS)
                global.add(parameters.get(parameter));

            for (String parameter : ApplicationConstants.THRESHOLD)
                threshold.add(parameters.get(parameter));

            for (String parameter : ApplicationConstants.VARIATION)
                variation.add(parameters.get(parameter));


            layout.add(global);
            layout.add(threshold);
            layout.add(variation);
        }

        @SuppressWarnings("unchecked")
        void updateInformation(Configuration information) {
            for (Map.Entry<String, SettingField> entry : parameters.entrySet()) {
                if (entry.getKey().equals(MAX_AGENT_NUMBER)
                        || entry.getKey().equals(COLOR_VARIATION))
                    parameters.get(entry.getKey()).setValue(
                            information.getParameter(entry.getKey(), Integer.class));
                else
                    parameters.get(entry.getKey()).setValue(
                            information.getParameter(entry.getKey(), Double.class));
            }
        }
    }

    public class CreatureParameterPanel
            extends AgentParameterPanel {

        public CreatureParameterPanel(String name) {
            super(name);
        }

        @Override
        protected Map<String, SettingField> generateParameters() {
            return new TreeMap<String, SettingField>() {{
                put(AGENT_ENERGY,
                        new org.blackpanther.ecosystem.gui.settings.fields.randomable.SpinnerField(
                                AGENT_ENERGY,
                                generatePositiveDoubleModel(),
                                0.0, Configuration.getParameter(ENERGY_AMOUNT_THRESHOLD, Double.class)));
                put(CREATURE_COLOR,
                        new org.blackpanther.ecosystem.gui.settings.fields.randomable.ColorField(CREATURE_COLOR));
                put(CREATURE_ORIENTATION,
                        new org.blackpanther.ecosystem.gui.settings.fields.randomable.SpinnerField(
                                CREATURE_ORIENTATION,
                                generatePositiveDoubleModel(),
                                0.0, Geometry.PI_2));
                put(CREATURE_CURVATURE,
                        new org.blackpanther.ecosystem.gui.settings.fields.randomable.SpinnerField(
                                CREATURE_CURVATURE,
                                generateDoubleModel(),
                                -Configuration.getParameter(CURVATURE_THRESHOLD, Double.class),
                                Configuration.getParameter(CURVATURE_THRESHOLD, Double.class)));
                put(CREATURE_SPEED,
                        new org.blackpanther.ecosystem.gui.settings.fields.randomable.SpinnerField(
                                CREATURE_SPEED,
                                generatePositiveDoubleModel(),
                                0.0, Configuration.getParameter(SPEED_THRESHOLD, Double.class)));

                put(AGENT_NATURAL_COLOR,
                        new org.blackpanther.ecosystem.gui.settings.fields.mutable.ColorField(AGENT_NATURAL_COLOR));
                put(CREATURE_MOVEMENT_COST,
                        new org.blackpanther.ecosystem.gui.settings.fields.mutable.SpinnerField(
                                CREATURE_MOVEMENT_COST,
                                generatePercentageModel(),
                                0.0, 1.0));
                put(CREATURE_FECUNDATION_COST,
                        new org.blackpanther.ecosystem.gui.settings.fields.mutable.SpinnerField(
                                CREATURE_FECUNDATION_COST,
                                generatePositiveDoubleModel(),
                                0.0, Configuration.getParameter(FECUNDATION_CONSUMMATION_THRESHOLD, Double.class)));
                put(CREATURE_FECUNDATION_LOSS,
                        new org.blackpanther.ecosystem.gui.settings.fields.mutable.SpinnerField(
                                CREATURE_FECUNDATION_LOSS,
                                generatePercentageModel(),
                                0.0, 1.0));
                put(CREATURE_GREED,
                        new org.blackpanther.ecosystem.gui.settings.fields.mutable.SpinnerField(
                                CREATURE_GREED,
                                generatePercentageModel(),
                                0.0, 1.0));
                put(CREATURE_FLEE,
                        new org.blackpanther.ecosystem.gui.settings.fields.mutable.SpinnerField(
                                CREATURE_FLEE,
                                generatePercentageModel(),
                                0.0, 1.0));
                put(CREATURE_SENSOR_RADIUS,
                        new org.blackpanther.ecosystem.gui.settings.fields.mutable.SpinnerField(
                                CREATURE_SENSOR_RADIUS,
                                generatePositiveDoubleModel(),
                                0.0, Configuration.getParameter(SENSOR_THRESHOLD, Double.class)));
                put(CREATURE_IRRATIONALITY,
                        new org.blackpanther.ecosystem.gui.settings.fields.mutable.SpinnerField(
                                CREATURE_IRRATIONALITY,
                                generatePercentageModel(),
                                0.0, 1.0));
                put(CREATURE_MORTALITY,
                        new org.blackpanther.ecosystem.gui.settings.fields.mutable.SpinnerField(
                                CREATURE_MORTALITY,
                                generatePercentageModel(),
                                0.0, 1.0));
                put(CREATURE_FECUNDITY,
                        new org.blackpanther.ecosystem.gui.settings.fields.mutable.SpinnerField(
                                CREATURE_FECUNDITY,
                                generatePercentageModel(),
                                0.0, 1.0));
                put(CREATURE_MUTATION,
                        new org.blackpanther.ecosystem.gui.settings.fields.mutable.SpinnerField(
                                CREATURE_MUTATION,
                                generatePercentageModel(),
                                0.0, 1.0));
                put(CREATURE_ORIENTATION_LAUNCHER,
                        new org.blackpanther.ecosystem.gui.settings.fields.mutable.SpinnerField(
                                CREATURE_ORIENTATION_LAUNCHER,
                                generatePositiveDoubleModel(),
                                0.0, Geometry.PI_2));
                put(CREATURE_SPEED_LAUNCHER,
                        new org.blackpanther.ecosystem.gui.settings.fields.mutable.SpinnerField(
                                CREATURE_SPEED_LAUNCHER,
                                generatePositiveDoubleModel(),
                                0.0, Configuration.getParameter(SPEED_THRESHOLD, Double.class)));
                put(CREATURE_BEHAVIOR,
                        new BehaviorField(CREATURE_BEHAVIOR, new BehaviorManager[]{
                                DraughtsmanBehaviour.getInstance()
                        }));
            }};
        }

        @Override
        void fillUpState(Box layout) {
            for (String stateParameter : CreatureConstants.CUSTOMIZABLE_CREATURE_STATE)
                layout.add(parameters.get(stateParameter));
        }

        @Override
        void fillUpGenotype(Box layout) {
            for (String genotypeParameter : CreatureConstants.CREATURE_GENOTYPE)
                layout.add(parameters.get(genotypeParameter));
        }
    }

    public class ResourceParameterPanel
            extends AgentParameterPanel {

        public ResourceParameterPanel(String name) {
            super(name);
        }

        @Override
        protected Map<String, SettingField> generateParameters() {
            return new TreeMap<String, SettingField>() {{
                put(AGENT_ENERGY,
                        new org.blackpanther.ecosystem.gui.settings.fields.randomable.SpinnerField(
                                AGENT_ENERGY,
                                generatePositiveDoubleModel(),
                                0.0, Configuration.getParameter(ENERGY_AMOUNT_THRESHOLD, Double.class)));
            }};
        }

        @Override
        void fillUpState(Box layout) {
            for (String stateParameter : AgentConstants.CUSTOMIZABLE_AGENT_STATE)
                layout.add(parameters.get(stateParameter));
        }

        @Override
        void fillUpGenotype(Box layout) {
        }
    }

    public class CreaturePlacementPanel extends PlacementPanel<Creature> {

        public CreaturePlacementPanel(String name) {
            super(name, "creature");
        }

        @Override
        protected ActionListener setButtonActionListener() {
            return EventHandler.create(
                    ActionListener.class,
                    this,
                    "addCreatures"
            );
        }

        @Override
        protected Class<Creature> getSpecies() {
            return Creature.class;
        }

        @Override
        protected FieldsConfiguration getConfiguration() {
            return createConfiguration();
        }

        @Override
        protected Collection<Creature> generateCirclePopulation() {
            return PopulationFactory.generateCreatureCirclePool(
                    getConfiguration(),
                    getCircleAgentNumber(),
                    getCircleRadius()
            );
        }

        public void addCreatures() {
            GUIMonitor.Monitor.addCreatures(generatePopulation());
        }
    }

    public class ResourcePlacementPanel extends PlacementPanel<Resource> {
        public ResourcePlacementPanel(String name) {
            super(name, "resource");
        }

        @Override
        protected ActionListener setButtonActionListener() {
            return EventHandler.create(
                    ActionListener.class,
                    this,
                    "addResources"
            );
        }

        @Override
        protected Class<Resource> getSpecies() {
            return Resource.class;
        }

        @Override
        protected FieldsConfiguration getConfiguration() {
            return createConfiguration();
        }

        public void addResources() {
            GUIMonitor.Monitor.addResources(generatePopulation());
        }
    }
}
