package org.blackpanther.ecosystem.gui.wizard;

import com.nexes.wizard.WizardPanelDescriptor;
import org.blackpanther.ecosystem.DraughtsmanBehaviour;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.HashSet;
import java.util.Properties;
import java.util.Set;

import static javax.swing.BoxLayout.Y_AXIS;
import static org.blackpanther.ecosystem.Agent.*;
import static org.blackpanther.ecosystem.Configuration.*;
import static org.blackpanther.ecosystem.gui.formatter.RangeModels.*;
import static org.blackpanther.ecosystem.helper.Helper.createLabeledField;

/**
 * @author MACHIZAUD Andréa
 * @version 5/10/11
 */
public class InitialParameterPanelDescriptor
        extends WizardPanelDescriptor {

    public static final String IDENTIFIER = "InitializeParameter";
    private InitializationPanel panel;

    public InitialParameterPanelDescriptor() {
        super(null);
        panel = new InitializationPanel();
        setPanelComponent(panel);
    }

    @Override
    protected Object generateID() {
        return IDENTIFIER;
    }

    @Override
    public Object getNextPanelDescriptor() {
        return PlacementStrategyPanelDescriptor.IDENTIFIER;
    }

    @Override
    public Object getBackPanelDescriptor() {
        return null;
    }

    @Override
    public void aboutToDisplayPanel() {
        panel.setProperties(Configuration.parameters());
    }

    @Override
    public void displayingPanel() {
        panel.paintImmediately(
                panel.getBounds());
    }

    @Override
    public void aboutToHidePanel() {
        getWizardModel().setParameters(
                panel.getProperties());
    }

    @Override
    public CreationModel getWizardModel() {
        return (CreationModel) super.getWizardModel();
    }

    static class InitializationPanel extends EnvironmentCreationWizardInternalPanel {

        private static final String[] initialParametersName = new String[]{
                RANDOM,
                SPAWN_ABSCISSA_THRESHOLD,
                SPAWN_ORDINATE_THRESHOLD,
                AGENT_ORIENTATION,
                AGENT_CURVATURE,
                AGENT_SPEED,
                AGENT_IRRATIONALITY,
                AGENT_MORTALITY,
                AGENT_FECUNDITY,
                AGENT_MUTATION,
                AGENT_ORIENTATION_LAUNCHER,
                AGENT_SPEED_LAUNCHER,
                AGENT_BEHAVIOUR
        };

        private static final String[] behavioursParameters = new String[]{
                DraughtsmanBehaviour.class.getCanonicalName()
        };


        private JSpinner randomSeedField;
        private JSlider orientationField;
        private JSpinner curvatureField;
        private JSpinner speedField;
        private JSlider irrationalityField;
        private JSlider mortalityField;
        private JSlider fecundityField;
        private JSlider mutationField;
        private JComboBox behavioursField;
        private JSlider orientationLauncherField;
        private JSpinner speedLauncherField;

        @Override
        protected Component generateContent() {
            JPanel container = new JPanel(new BorderLayout());

            JLabel titleLabel = new JLabel("Set your default parameter");
            JLabel applicationParameters = new JLabel("<html><i><u>Application parameters</u></i></html>");
            JLabel genotypeParameters = new JLabel("<html><i><u>Genotype parameters</u></i></html>");
            JLabel stateParameters = new JLabel("<html><i><u>Agent initial state parameters</u></i></html>");

            titleLabel.setFont(new Font("MS Sans Serif", Font.BOLD, 14));

            randomSeedField = new JSpinner(generatePositiveLongModel());

            orientationField = new JSlider(generateAngleModel());
            orientationLauncherField = new JSlider(generateAngleModel());
            curvatureField = new JSpinner(generateDecimalModel());
            speedField = new JSpinner(generatePositiveDecimalModel());
            speedLauncherField = new JSpinner(generatePositiveDecimalModel());
            irrationalityField = new JSlider(generateProbabilityModel());
            mortalityField = new JSlider(generateProbabilityModel());
            fecundityField = new JSlider(generateProbabilityModel());
            mutationField = new JSlider(generateProbabilityModel());
            behavioursField = new JComboBox(behavioursParameters);

            Box fields = new Box(Y_AXIS);
            fields.setBorder(new EmptyBorder(new Insets(5, 10, 5, 10)));
            fields.add(applicationParameters);
            fields.add(Box.createVerticalStrut(5));
            fields.add(createLabeledField(
                    "Random generator's seed",
                    randomSeedField
            ));
            fields.add(Box.createVerticalStrut(10));
            fields.add(genotypeParameters);
            fields.add(Box.createVerticalStrut(5));
            fields.add(createLabeledField(
                    "Orientation's angle at child's born",
                    orientationLauncherField
            ));
            fields.add(createLabeledField(
                    "Speed at child's born",
                    speedLauncherField
            ));
            fields.add(createLabeledField(
                    "Irrationality",
                    irrationalityField
            ));
            fields.add(createLabeledField(
                    "Fecundity",
                    fecundityField
            ));
            fields.add(createLabeledField(
                    "Mortality",
                    mortalityField
            ));
            fields.add(createLabeledField(
                    "Mutation",
                    mutationField
            ));
            fields.add(createLabeledField(
                    "Behaviour",
                    behavioursField
            ));
            fields.add(Box.createVerticalStrut(10));
            fields.add(stateParameters);
            fields.add(Box.createVerticalStrut(5));
            fields.add(createLabeledField(
                    "1st generation's orientation",
                    orientationField
            ));
            fields.add(createLabeledField(
                    "1st generation's curvature",
                    curvatureField
            ));
            fields.add(createLabeledField(
                    "1st generation's speed",
                    speedField
            ));

            container.add(titleLabel, BorderLayout.NORTH);
            container.add(new JSeparator(SwingConstants.VERTICAL));
            container.add(fields, BorderLayout.CENTER);
            return container;
        }

        public void setProperties(Properties configuration) {
            Double value;
            randomSeedField.setValue(configuration.get(RANDOM));
            value = (Double) configuration.get(AGENT_ORIENTATION);
            orientationField.setValue(
                    (int) ((value / (2.0 * Math.PI))
                            * ANGLE_APPROXIMATION));
            speedField.setValue(configuration.get(AGENT_SPEED));
            curvatureField.setValue(configuration.get(AGENT_CURVATURE));
            value = (Double) configuration.get(AGENT_ORIENTATION_LAUNCHER);
            orientationLauncherField.setValue(
                    (int) ((value / (2.0 * Math.PI))
                            * ANGLE_APPROXIMATION));
            speedLauncherField.setValue(configuration.get(AGENT_SPEED_LAUNCHER));
            value = (Double) configuration.get(AGENT_IRRATIONALITY);
            irrationalityField.setValue(
                    (int) (value * PROBABILITY_APPROXIMATION));
            value = (Double) configuration.get(AGENT_MORTALITY);
            mortalityField.setValue(
                    (int) (value * PROBABILITY_APPROXIMATION));
            value = (Double) configuration.get(AGENT_FECUNDITY);
            fecundityField.setValue(
                    (int) (value * PROBABILITY_APPROXIMATION));
            value = (Double) configuration.get(AGENT_MUTATION);
            mutationField.setValue(
                    (int) (value * PROBABILITY_APPROXIMATION));

            String behaviourClass = configuration.get(AGENT_BEHAVIOUR)
                    .getClass().getCanonicalName();
            Set<String> behaviours =
                    new HashSet<String>(behavioursField.getModel().getSize());
            behavioursField.removeAllItems();
            for (int i = 0; i < behavioursField.getModel().getSize(); i++)
                behaviours.add(
                        (String) behavioursField.getModel().getElementAt(i));
            behaviours.add(behaviourClass);
            for (String behaviourManager : behaviours)
                behavioursField.addItem(behaviourManager);
            behavioursField.setSelectedIndex(0);
        }

        public Properties getProperties() {
            Double value;
            Properties parameters = new Properties();
            parameters.put(RANDOM, randomSeedField.getValue().toString());
            value = (orientationField.getValue() / (double) ANGLE_APPROXIMATION)
                    * 2.0 * Math.PI;
            parameters.put(AGENT_ORIENTATION, value.toString());
            parameters.put(AGENT_SPEED, speedField.getValue().toString());
            parameters.put(AGENT_CURVATURE, curvatureField.getValue().toString());
            value = (orientationLauncherField.getValue() / (double) ANGLE_APPROXIMATION)
                    * 2.0 * Math.PI;
            parameters.put(AGENT_ORIENTATION_LAUNCHER, value.toString());
            parameters.put(AGENT_SPEED_LAUNCHER, speedLauncherField.getValue().toString());
            value = irrationalityField.getValue() / (double) PROBABILITY_APPROXIMATION;
            parameters.put(AGENT_IRRATIONALITY, value.toString());
            value = mortalityField.getValue() / (double) PROBABILITY_APPROXIMATION;
            parameters.put(AGENT_MORTALITY, value.toString());
            value = fecundityField.getValue() / (double) PROBABILITY_APPROXIMATION;
            parameters.put(AGENT_FECUNDITY, value.toString());
            value = mutationField.getValue() / (double) PROBABILITY_APPROXIMATION;
            parameters.put(AGENT_MUTATION, value.toString());
            parameters.put(AGENT_BEHAVIOUR, behavioursField.getSelectedItem().toString());
            return parameters;
        }
    }

}
