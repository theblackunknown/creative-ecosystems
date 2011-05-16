package org.blackpanther.ecosystem.gui;

import org.blackpanther.ecosystem.gui.actions.EnvironmentCreationAction;
import org.blackpanther.ecosystem.gui.lightweight.ConfigurationInformation;
import org.blackpanther.ecosystem.gui.lightweight.EnvironmentInformation;

import javax.swing.*;
import javax.swing.border.BevelBorder;
import java.awt.*;

/**
 * @author MACHIZAUD Andréa
 * @version 0.2 - Wed May 11 02:54:46 CEST 2011
 */
public class EnvironmentInformationPanel extends JPanel {

    private static final String LABEL_ENVIRONMENT_ID = "Environment #%s %s";
    private static final String LABEL_ENVIRONMENT_AGENT = "Agent number : %d";
    private static final String LABEL_ENVIRONMENT_GENERATION = "#Generation %d";

    private final JPanel DEFAULT_PANEL = new NoEnvironmentInformationPanel();
    private EnvironmentInformationInstance informationBoard =
            new EnvironmentInformationInstance();
    private boolean defaultPanelIsSet = true;

    public EnvironmentInformationPanel() {
        super();
        add(DEFAULT_PANEL);
        setBorder(
                BorderFactory.createBevelBorder(BevelBorder.RAISED)
        );
    }

    void clearBoard() {
        remove(new JScrollPane(
                informationBoard));
        add(DEFAULT_PANEL);
        defaultPanelIsSet = true;
    }

    private void checkDefaultPanel() {
        if (defaultPanelIsSet) {
            remove(DEFAULT_PANEL);
            add(new JScrollPane(informationBoard));
            defaultPanelIsSet = false;
        }
    }

    public void updateInformation(EnvironmentInformation information) {
        checkDefaultPanel();
        informationBoard.updateInformation(information);
    }

    public void updateInformation(ConfigurationInformation information) {
        checkDefaultPanel();
        informationBoard.updateInformation(information);
    }

    public void notifyPause() {
        checkDefaultPanel();
        informationBoard.notifyPause();
    }

    public void notifyRun() {
        checkDefaultPanel();
        informationBoard.notifyRun();
    }

    class NoEnvironmentInformationPanel extends JPanel {
        NoEnvironmentInformationPanel() {
            setName("No environment is set");
            JButton createEnvironment =
                    new JButton(EnvironmentCreationAction.getInstance());
            add(createEnvironment);
        }
    }

    class EnvironmentInformationInstance extends JPanel {

        private JLabel environmentLabel;
        private JLabel environmentAgentCounter;
        private JLabel environmentGenerationLabel;
        private JLabel initialParametersLabel;

        public EnvironmentInformationInstance() {
            setName("Environment's information board");
            setLayout(new GridBagLayout());

            environmentLabel =
                    new JLabel("No environment set");

            environmentAgentCounter =
                    new JLabel("No environment set");

            environmentGenerationLabel =
                    new JLabel("No environment set");
            initialParametersLabel =
                    new JLabel("No environment set");
            JButton resetEnvironment = new JButton(
                    EnvironmentCreationAction.getInstance());

            GridBagConstraints constraints = new GridBagConstraints();
            constraints.fill = GridBagConstraints.BOTH;
            constraints.gridwidth = GridBagConstraints.REMAINDER;

            //Environment Label
            add(environmentLabel, constraints);
            add(environmentAgentCounter, constraints);
            add(environmentGenerationLabel, constraints);

            constraints.insets = new Insets(10, 0, 0, 0);
            add(initialParametersLabel, constraints);


            constraints.fill = GridBagConstraints.HORIZONTAL;
            constraints.weighty = 1.0;   //request any extra vertical space
            constraints.anchor = GridBagConstraints.PAGE_END; //bottom of space
            constraints.insets = new Insets(45, 0, 0, 0);  //top padding
            add(resetEnvironment, constraints);
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

        public void updateInformation(ConfigurationInformation information) {
            this.initialParametersLabel.setText(information.toString());
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
    }
}
