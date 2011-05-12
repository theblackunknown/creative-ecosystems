package org.blackpanther.ecosystem.gui.wizard;

import com.nexes.wizard.WizardPanelDescriptor;
import org.blackpanther.ecosystem.Agent;
import org.blackpanther.ecosystem.placement.Strategy;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Collection;

import static org.blackpanther.ecosystem.gui.formatter.RangeModels.*;
import static org.blackpanther.ecosystem.helper.Helper.createLabeledField;
import static org.blackpanther.ecosystem.placement.Strategy.generatePopulation;

/**
 * @author MACHIZAUD Andréa
 * @version 0.2 - Wed May 11 02:54:46 CEST 2011
 */
public class PlacementStrategyPanelDescriptor
        extends WizardPanelDescriptor {

    public static final String IDENTIFIER = "PlacementStrategy";
    private PlacementStrategyPanel panel;

    public PlacementStrategyPanelDescriptor() {
        super(null);
        panel = new PlacementStrategyPanel();
        setPanelComponent(panel);
    }

    @Override
    protected Object generateID() {
        return IDENTIFIER;
    }

    @Override
    public Object getNextPanelDescriptor() {
        return WizardPanelDescriptor.FINISH;
    }

    @Override
    public Object getBackPanelDescriptor() {
        return InitialParameterPanelDescriptor.IDENTIFIER;
    }

    @Override
    public void aboutToDisplayPanel() {
        panel.setStrategy(Strategy.GenerationType.STANDARD_CIRCLE);
    }

    @Override
    public void displayingPanel() {
        //Nothing
    }

    @Override
    public void aboutToHidePanel() {
        getWizardModel().setPlacementStrategy(
                panel.getStrategy());
        getWizardModel().setAdditionalParameters(
                panel.getAdditionalParameters());
    }

    @Override
    public CreationModel getWizardModel() {
        return (CreationModel) super.getWizardModel();
    }

    class PlacementStrategyPanel
            extends EnvironmentCreationWizardInternalPanel
            implements ActionListener {

        private JComboBox strategyList;
        private JPanel cardPanel;
        private CardLayout cardLayout;

        private JSpinner abscissaLimit;
        private JSpinner ordinateLimit;
        private JSpinner step;

        private JSpinner numberOfAgentRandomized;

        private JSpinner numberOfAgentRandom;

        private JSpinner circleRadius;
        private JSpinner numberOfAgentCircle;

        @Override
        protected Component generateContent() {
            cardLayout = new CardLayout();
            cardPanel = new JPanel(cardLayout);

            strategyList = new JComboBox(Strategy.GenerationType.values());
            strategyList.addActionListener(this);

            abscissaLimit = new JSpinner(generateDecimalModel());
            abscissaLimit.setValue(400.0);
            ordinateLimit = new JSpinner(generateDecimalModel());
            ordinateLimit.setValue(300.0);
            step = new JSpinner(generateDecimalModel());
            step.setValue(100.0);

            numberOfAgentRandomized = new JSpinner(generatePositiveLongModel());
            numberOfAgentRandomized.setValue(20L);
            numberOfAgentRandom = new JSpinner(generatePositiveLongModel());
            numberOfAgentRandom.setValue(20L);
            numberOfAgentCircle = new JSpinner(generatePositiveLongModel());
            numberOfAgentCircle.setValue(45L);

            circleRadius = new JSpinner(generatePositiveDecimalModel());
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
                    numberOfAgentRandomized
            ));

            standardCircle.add(createLabeledField(
                    "Number of agent",
                    numberOfAgentCircle
            ));
            standardCircle.add(createLabeledField(
                    "Circle's radius",
                    circleRadius
            ));

            random.add(createLabeledField(
                    "Number of agent",
                    numberOfAgentRandom
            ));

            cardPanel.add(standardPositionProvided,
                    Strategy.GenerationType.STANDARD_POSITION_PROVIDED.toString());
            cardPanel.add(standardPositionRandomized,
                    Strategy.GenerationType.STANDARD_POSITION_RANDOMIZED.toString());
            cardPanel.add(standardCircle,
                    Strategy.GenerationType.STANDARD_CIRCLE.toString());
            cardPanel.add(random,
                    Strategy.GenerationType.RANDOM.toString());

            JPanel container = new JPanel(new BorderLayout());
            container.add(strategyList, BorderLayout.NORTH);
            container.add(cardPanel, BorderLayout.CENTER);

            return container;
        }

        public void setStrategy(Strategy.GenerationType strategy) {
            strategyList.setSelectedItem(strategy);
        }

        public Collection<Agent> getPool() {
            return generatePopulation(
                    (Strategy.GenerationType) strategyList.getSelectedItem(),
                    aggregateAdditionalParameters()
            );
        }

        private Object[] aggregateAdditionalParameters() {
            switch ((Strategy.GenerationType) strategyList.getSelectedItem()) {
                case STANDARD_POSITION_PROVIDED:
                    return new Object[]{
                            abscissaLimit.getValue(),
                            ordinateLimit.getValue(),
                            step.getValue()
                    };
                case STANDARD_POSITION_RANDOMIZED:
                    return new Object[]{
                            numberOfAgentRandomized.getValue()
                    };
                case STANDARD_CIRCLE:
                    return new Object[]{
                            numberOfAgentCircle.getValue(),
                            circleRadius.getValue()
                    };
                case RANDOM:
                    return new Object[]{
                            numberOfAgentRandom.getValue()
                    };
                default:
                    throw new IllegalStateException("strategy unknown : " + strategyList.getSelectedItem());
            }
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            cardLayout.show(cardPanel,
                    strategyList.getSelectedItem().toString());
        }

        public Strategy.GenerationType getStrategy() {
            return (Strategy.GenerationType) strategyList.getSelectedItem();
        }

        public Object[] getAdditionalParameters() {
            return aggregateAdditionalParameters();
        }
    }


}
