package org.blackpanther.ecosystem.gui;

import org.blackpanther.ecosystem.*;
import org.blackpanther.ecosystem.gui.lightweight.EnvironmentInformation;
import org.blackpanther.ecosystem.placement.GenerationStrategy;

import javax.swing.*;
import javax.swing.border.BevelBorder;
import java.awt.*;
import java.awt.event.ActionListener;
import java.beans.EventHandler;
import java.util.*;

import static org.blackpanther.ecosystem.Agent.*;
import static org.blackpanther.ecosystem.Configuration.*;
import static org.blackpanther.ecosystem.gui.formatter.RangeModels.*;
import static org.blackpanther.ecosystem.helper.Helper.createLabeledField;

/**
 * @author MACHIZAUD Andréa
 * @version 0.2 - Wed May 11 02:54:46 CEST 2011
 */
public class EnvironmentInformationPanel extends JPanel {

    private static final String LABEL_ENVIRONMENT_ID = "Environment #%s %s";
    private static final String LABEL_ENVIRONMENT_AGENT = "Agent number : %d";
    private static final String LABEL_ENVIRONMENT_GENERATION = "#Generation %d";
    private static final String NO_ENVIRONMENT = "No Environment Set";
    private static final Dimension PREFERRED_SIZE = new Dimension(280, 600);

    private EnvironmentConfigurationModel model =
            new EnvironmentConfigurationModel();
    private EnvironmentInformationInstance informationBoard =
            new EnvironmentInformationInstance();

    public EnvironmentInformationPanel() {
        super();
        add(informationBoard);
        setBorder(
                BorderFactory.createBevelBorder(BevelBorder.RAISED)
        );
        clearBoard();
    }

    public void clearBoard() {
        informationBoard.deadactivate();
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


    private static final String PASSIVE = "Normal";
    private static final String[] BEHAVIOURS_NAME = new String[]{
            PASSIVE
    };

    public class EnvironmentInformationInstance extends JPanel {

        private JLabel environmentLabel;
        private JLabel environmentAgentCounter;
        private JLabel environmentGenerationLabel;
        private JPanel parametersPanel;
        private JComboBox panelSelector;

        private ApplicationParameterPanel internalApplicationParametersPanel =
                new ApplicationParameterPanel(APPLICATION_PARAMETERS);
        private AgentParameterPanel internalAgentParametersPanel =
                new AgentParameterPanel(AGENT_PARAMETERS);
        private PlacementAgentPanel internalAgentPlacementPanel =
                new PlacementAgentPanel(AGENT_PLACEMENT);
        private PlacementResourcePanel internalResourcePlacementPanel =
                new PlacementResourcePanel(RESOURCE_PLACEMENT);

        public static final String APPLICATION_PARAMETERS = "Applications parameters";
        public static final String AGENT_PARAMETERS = "Agent parameters";
        public static final String AGENT_PLACEMENT = "Agent Placement";
        public static final String RESOURCE_PLACEMENT = "Resource Placement";

        private final String[] PANELS_NAME = new String[]{
                APPLICATION_PARAMETERS,
                AGENT_PARAMETERS,
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
            environmentGenerationLabel.setAlignmentX(CENTER_ALIGNMENT);

            JButton resetEnvironment = new JButton("Reset Environment");
            resetEnvironment.setAlignmentX(LEFT_ALIGNMENT);
            resetEnvironment.addActionListener(EventHandler.create(
                    ActionListener.class,
                    model,
                    "update"
            ));

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
                    internalAgentParametersPanel,
                    internalAgentParametersPanel.getName()
            );
            parametersPanel.add(
                    internalAgentPlacementPanel,
                    internalAgentPlacementPanel.getName()
            );
            parametersPanel.add(
                    internalResourcePlacementPanel,
                    internalResourcePlacementPanel.getName()
            );

            Box information = Box.createVerticalBox();

            information.add(environmentLabel);
            information.add(environmentAgentCounter);
            information.add(environmentAgentCounter);
            information.add(Box.createVerticalStrut(10));
            information.add(resetEnvironment);
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
            internalAgentParametersPanel.updateInformation(information);
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

        public Collection<Agent> getAgentPool() {
            return internalAgentPlacementPanel.generatePopulation();
        }

        public Collection<Resource> getResourcePool() {
            return internalResourcePlacementPanel.generatePopulation();
        }

        public Collection<Map.Entry<Object, Object>> getParameters() {
            Set<Map.Entry<Object, Object>> allParameters = new HashSet<Map.Entry<Object, Object>>();
            allParameters.addAll(internalApplicationParametersPanel.getParameters().entrySet());
            allParameters.addAll(internalAgentParametersPanel.getParameters().entrySet());
            return allParameters;
        }

        private void activate(){
            panelSelector.setEnabled(true);
            internalApplicationParametersPanel.activate();
            internalAgentParametersPanel.activate();
        }

        public void deadactivate() {
            environmentLabel.setText(NO_ENVIRONMENT);
            environmentAgentCounter.setText(NO_ENVIRONMENT);
            environmentGenerationLabel.setText(NO_ENVIRONMENT);
            panelSelector.setEnabled(false);
            internalApplicationParametersPanel.deactivate();
            internalAgentParametersPanel.deactivate();
        }
    }

    private abstract class ParametersPanel extends JPanel {

        protected final Map<String, JSpinner> parameters =
                generateParameters();

        public ParametersPanel(String name) {
            super(new BorderLayout());
            setName(name);

            JLabel presentation = new JLabel("<html><i><b>" + name + "</b></i></html>");
            presentation.setAlignmentX(LEFT_ALIGNMENT);

            setUpComponents();

            Box layout = Box.createVerticalBox();

            layout.add(presentation);

            generateContent(layout);

            //"bite me, fucking UI"
            layout.add(Box.createVerticalStrut(25));

            add(new JScrollPane(layout));
        }

        abstract void setUpComponents();

        abstract Map<String, JSpinner> generateParameters();

        void generateContent(Box layout) {
            for (Map.Entry<String, JSpinner> entry : parameters.entrySet()) {
                layout.add(createLabeledField(
                        entry.getKey(),
                        entry.getValue()
                ));
            }
        }

        /**
         * Update information with given configuration
         */
        void updateInformation(Configuration information) {
            for (Map.Entry<String, JSpinner> entry : parameters.entrySet()) {
                if (entry.getKey().equals(RANDOM))
                    parameters.get(entry.getKey()).setValue(
                            information.getRandomSeed());
                else if (entry.getKey().equals(MAX_AGENT_NUMBER))
                    parameters.get(entry.getKey()).setValue(
                            information.getParameter(entry.getKey(), Integer.class));
                else
                    parameters.get(entry.getKey()).setValue(
                            information.getParameter(entry.getKey(), Double.class));
            }
        }

        public Map<Object, Object> getParameters() {
            Map<Object, Object> externalisedParameters = new HashMap<Object, Object>();
            for (Map.Entry<String, JSpinner> parameter : parameters.entrySet())
                externalisedParameters.put(
                        parameter.getKey(),
                        String.valueOf(parameter.getValue().getValue()));
            return externalisedParameters;
        }

        void activate(){
            for (JSpinner components : parameters.values())
                components.setEnabled(true);
        }

        void deactivate(){
            for (JSpinner components : parameters.values())
                components.setEnabled(false);
        }
    }

    /**
     * Handle panel switching by a card layout
     */

    class ApplicationParameterPanel extends ParametersPanel {

        public ApplicationParameterPanel(String name) {
            super(name);
        }

        @Override
        void setUpComponents() {
        }

        @Override
        Map<String, JSpinner> generateParameters() {
            return new HashMap<String, JSpinner>() {{
                put(RANDOM, new JSpinner(generatePositiveLongModel()));
                put(SPACE_WIDTH, new JSpinner(generatePositiveDoubleModel()));
                put(SPACE_HEIGHT, new JSpinner(generatePositiveDoubleModel()));
                put(RESOURCE_AMOUNT, new JSpinner(generatePositiveDoubleModel()));
                put(MAX_AGENT_NUMBER, new JSpinner(generatePositiveLongModel()));
                put(CONSUMMATION_RADIUS, new JSpinner(generatePositiveDoubleModel()));
                //randomized threshold
                put(RESOURCE_AMOUNT_THRESHOLD, new JSpinner(generatePositiveDoubleModel()));
                put(SPEED_THRESHOLD, new JSpinner(generatePositiveDoubleModel()));
                put(CURVATURE_THRESHOLD, new JSpinner(generatePositiveDoubleModel()));
                put(SENSOR_THRESHOLD, new JSpinner(generatePositiveDoubleModel()));
                put(ENERGY_AMOUNT_THRESHOLD, new JSpinner(generatePositiveDoubleModel()));
                put(FECUNDATION_CONSUMMATION_THRESHOLD, new JSpinner(generatePositiveDoubleModel()));
                //variation
                put(PROBABILITY_VARIATION, new JSpinner(generateDoubleModel()));
                put(CURVATURE_VARIATION, new JSpinner(generateDoubleModel()));
                put(ANGLE_VARIATION, new JSpinner(generateDoubleModel()));
                put(SPEED_VARIATION, new JSpinner(generateDoubleModel()));
            }};
        }
    }

    class AgentParameterPanel extends ParametersPanel {

        private JComboBox behaviours;

        public AgentParameterPanel(String name) {
            super(name);
        }

        @Override
        void setUpComponents() {
            behaviours = new JComboBox(BEHAVIOURS_NAME);
        }

        @Override
        Map<String, JSpinner> generateParameters() {
            return new HashMap<String, JSpinner>() {{
                put(AGENT_ENERGY, new JSpinner(generatePositiveDoubleModel()));
                put(AGENT_ORIENTATION, new JSpinner(generateAngleModel()));
                put(AGENT_CURVATURE, new JSpinner(generateDoubleModel()));
                put(AGENT_SPEED, new JSpinner(generatePositiveDoubleModel()));
                put(AGENT_MOVEMENT_COST, new JSpinner(generatePercentageModel()));
                put(AGENT_FECUNDATION_COST, new JSpinner(generatePositiveDoubleModel()));
                put(AGENT_FECUNDATION_LOSS, new JSpinner(generatePercentageModel()));
                put(AGENT_ORIENTATION_LAUNCHER, new JSpinner(generateAngleModel()));
                put(AGENT_SPEED_LAUNCHER, new JSpinner(generatePositiveDoubleModel()));
                put(AGENT_GREED, new JSpinner(generatePercentageModel()));
                put(AGENT_SENSOR_RADIUS, new JSpinner(generatePositiveDoubleModel()));
                put(AGENT_IRRATIONALITY, new JSpinner(generatePercentageModel()));
                put(AGENT_MORTALITY, new JSpinner(generatePercentageModel()));
                put(AGENT_FECUNDITY, new JSpinner(generatePercentageModel()));
                put(AGENT_MUTATION, new JSpinner(generatePercentageModel()));
            }};
        }

        @Override
        void generateContent(Box layout) {
            super.generateContent(layout);
            layout.add(createLabeledField(
                    AGENT_BEHAVIOUR,
                    behaviours
            ));
        }

        @Override
        void updateInformation(Configuration information) {
            super.updateInformation(information);
            String behaviourClass =
                    information.getParameter(AGENT_BEHAVIOUR, BehaviorManager.class)
                            .getClass().getCanonicalName();
            //trick to avoid doubles
            //FIXME event fired
            behaviours.removeItem(behaviourClass);
            behaviours.addItem(behaviourClass);
        }

        @Override
        public Map<Object, Object> getParameters() {
            Map<Object, Object> externalisedParameters = super.getParameters();
            String selectedBehaviour = (String) behaviours.getSelectedItem();
            if (selectedBehaviour.equals(PASSIVE))
                externalisedParameters.put(
                        AGENT_BEHAVIOUR, DraughtsmanBehaviour.class.getCanonicalName());
            return externalisedParameters;
        }
    }

    public abstract class PlacementPanel<T> extends JPanel {

        /**
         * Panel management
         */
        private JComboBox strategyList;
        private JPanel cardPanel;

        /**
         * Standard provided
         */
        private JSpinner abscissaLimit;
        private JSpinner ordinateLimit;
        private JSpinner step;

        /**
         * Standard random position
         */
        private JSpinner numberOfItemRandomized;

        /**
         * Full random
         */
        private JSpinner numberOfItemRandom;

        /**
         * Circle placement
         */
        private JSpinner circleRadius;
        private JSpinner numberOfItemCircle;

        public PlacementPanel(String name) {
            super();
            setName(name);

            JLabel presentation = new JLabel("<html><i><b>" + name + "</b></i></html>");
            presentation.setAlignmentX(LEFT_ALIGNMENT);

            Box layout = Box.createVerticalBox();

            layout.add(presentation);

            generateContent(layout);

            add(layout);
        }

        private void generateContent(Box layout) {
            cardPanel = new JPanel(new CardLayout());

            strategyList = new JComboBox(GenerationStrategy.GenerationType.values());
            strategyList.addActionListener(EventHandler.create(
                    ActionListener.class,
                    this,
                    "switchStrategyPanel",
                    "source.selectedItem"
            ));

            abscissaLimit = new JSpinner(generateDoubleModel());
            abscissaLimit.setValue(1.0);
            ordinateLimit = new JSpinner(generateDoubleModel());
            ordinateLimit.setValue(1.0);
            step = new JSpinner(generateDoubleModel());
            step.setValue(1.0);

            numberOfItemRandomized = new JSpinner(generatePositiveLongModel());
            numberOfItemRandomized.setValue(20L);
            numberOfItemRandom = new JSpinner(generatePositiveLongModel());
            numberOfItemRandom.setValue(20L);
            numberOfItemCircle = new JSpinner(generatePositiveLongModel());
            numberOfItemCircle.setValue(45L);

            circleRadius = new JSpinner(generatePositiveDoubleModel());
            circleRadius.setValue(5.0);

            JPanel standardPositionProvided = new JPanel(new GridLayout(0, 1));
            JPanel standardPositionRandomized = new JPanel(new GridLayout(0, 1));
            JPanel standardCircle = new JPanel(new GridLayout(0, 1));
            JPanel random = new JPanel(new GridLayout(0, 1));

            standardPositionProvided.add(createLabeledField(
                    "Maximal Abscissa",
                    abscissaLimit
            ));
            standardPositionProvided.add(createLabeledField(
                    "Maximal Ordinate",
                    ordinateLimit
            ));
            standardPositionProvided.add(createLabeledField(
                    "Step",
                    step
            ));

            standardPositionRandomized.add(createLabeledField(
                    "Number of agent",
                    numberOfItemRandomized
            ));

            standardCircle.add(createLabeledField(
                    "Number of agent",
                    numberOfItemCircle
            ));
            standardCircle.add(createLabeledField(
                    "Circle's radius",
                    circleRadius
            ));

            random.add(createLabeledField(
                    "Number of agent",
                    numberOfItemRandom
            ));

            cardPanel.add(standardPositionProvided,
                    GenerationStrategy.GenerationType.STANDARD_POSITION_PROVIDED.toString());
            cardPanel.add(standardPositionRandomized,
                    GenerationStrategy.GenerationType.STANDARD_POSITION_RANDOMIZED.toString());
            cardPanel.add(standardCircle,
                    GenerationStrategy.GenerationType.STANDARD_CIRCLE.toString());
            cardPanel.add(random,
                    GenerationStrategy.GenerationType.RANDOM.toString());

            layout.add(strategyList);
            layout.add(cardPanel);
        }

        public void switchStrategyPanel(GenerationStrategy.GenerationType strategyType) {
            CardLayout layout = (CardLayout) cardPanel.getLayout();
            layout.show(cardPanel, strategyType.toString());
        }

        protected GenerationStrategy.GenerationType getGenerationType() {
            return (GenerationStrategy.GenerationType) strategyList.getSelectedItem();
        }

        protected Object[] aggregateAdditionalParameters() {
            switch ((GenerationStrategy.GenerationType) strategyList.getSelectedItem()) {
                case STANDARD_POSITION_PROVIDED:
                    return new Object[]{
                            abscissaLimit.getValue(),
                            ordinateLimit.getValue(),
                            step.getValue()
                    };
                case STANDARD_POSITION_RANDOMIZED:
                    return new Object[]{
                            numberOfItemRandomized.getValue()
                    };
                case STANDARD_CIRCLE:
                    return new Object[]{
                            numberOfItemCircle.getValue(),
                            circleRadius.getValue()
                    };
                case RANDOM:
                    return new Object[]{
                            numberOfItemRandom.getValue()
                    };
                default:
                    throw new IllegalStateException("strategy unknown : " + strategyList.getSelectedItem());
            }
        }

        abstract public Collection<T> generatePopulation();
    }

    public class PlacementAgentPanel extends PlacementPanel<Agent> {

        public PlacementAgentPanel(String name) {
            super(name);
        }

        @Override
        public Collection<Agent> generatePopulation() {
            return GenerationStrategy.generatePopulation(
                    Agent.class,
                    getGenerationType(),
                    aggregateAdditionalParameters()
            );
        }
    }

    public class PlacementResourcePanel extends PlacementPanel<Resource> {
        public PlacementResourcePanel(String name) {
            super(name);
        }

        @Override
        public Collection<Resource> generatePopulation() {
            return GenerationStrategy.generatePopulation(
                    Resource.class,
                    getGenerationType(),
                    aggregateAdditionalParameters()
            );
        }
    }

    /**
     * A model in a backend keep records of every parameters data
     */
    public class EnvironmentConfigurationModel {

        private Properties currentProperties = new Properties();
        private Collection<Agent> agentPool;
        private Collection<Resource> resourcePool;

        private void notifyChanges() {
            GUIMonitor.Monitor.resetEnvironment(
                    currentProperties,
                    agentPool,
                    resourcePool
            );
        }

        public void update() {
            currentProperties.clear();
            for (Map.Entry<Object, Object> parameter : informationBoard.getParameters())
                currentProperties.put(parameter.getKey(), parameter.getValue());
            agentPool = informationBoard.getAgentPool();
            resourcePool = informationBoard.getResourcePool();
            notifyChanges();
        }
    }
}
