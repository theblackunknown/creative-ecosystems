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
 * @version 0.2 - Wed May 11 02:54:46 CEST 2011
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
                SPACE_WIDTH,
                SPACE_HEIGHT,
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
        private JSpinner orientationField;
        private JSpinner curvatureField;
        private JSpinner speedField;
        private JSpinner irrationalityField;
        private JSpinner mortalityField;
        private JSpinner fecundityField;
        private JSpinner mutationField;
        private JComboBox behavioursField;
        private JSpinner orientationLauncherField;
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

            orientationField = new JSpinner(generateAngleModel());
            orientationLauncherField = new JSpinner(generateAngleModel());
            curvatureField = new JSpinner(generateDecimalModel());
            speedField = new JSpinner(generatePositiveDecimalModel());
            speedLauncherField = new JSpinner(generatePositiveDecimalModel());
            irrationalityField = new JSpinner(generateProbabilityModel());
            mortalityField = new JSpinner(generateProbabilityModel());
            fecundityField = new JSpinner(generateProbabilityModel());
            mutationField = new JSpinner(generateProbabilityModel());
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
            randomSeedField.setValue(configuration.get(RANDOM));
            orientationField.setValue(configuration.get(AGENT_ORIENTATION));
            speedField.setValue(configuration.get(AGENT_SPEED));
            curvatureField.setValue(configuration.get(AGENT_CURVATURE));
            orientationLauncherField.setValue(configuration.get(AGENT_ORIENTATION_LAUNCHER));
            speedLauncherField.setValue(configuration.get(AGENT_SPEED_LAUNCHER));
            irrationalityField.setValue(configuration.get(AGENT_IRRATIONALITY));
            mortalityField.setValue(configuration.get(AGENT_MORTALITY));
            fecundityField.setValue(configuration.get(AGENT_FECUNDITY));
            mutationField.setValue(configuration.get(AGENT_MUTATION));

            String behaviourClass = (String) configuration.get(AGENT_BEHAVIOUR);
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
            Properties parameters = new Properties();
            parameters.put(RANDOM, randomSeedField.getValue().toString());
            parameters.put(AGENT_ORIENTATION, orientationField.getValue().toString());
            parameters.put(AGENT_SPEED, speedField.getValue().toString());
            parameters.put(AGENT_CURVATURE, curvatureField.getValue().toString());
            parameters.put(AGENT_ORIENTATION_LAUNCHER, orientationLauncherField.getValue().toString());
            parameters.put(AGENT_SPEED_LAUNCHER, speedLauncherField.getValue().toString());
            parameters.put(AGENT_IRRATIONALITY, irrationalityField.getValue().toString());
            parameters.put(AGENT_MORTALITY, mortalityField.getValue().toString());
            parameters.put(AGENT_FECUNDITY, fecundityField.getValue().toString());
            parameters.put(AGENT_MUTATION, mutationField.getValue().toString());
            parameters.put(AGENT_BEHAVIOUR, behavioursField.getSelectedItem().toString());
            return parameters;
        }
    }

}
