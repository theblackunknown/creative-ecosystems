package org.blackpanther.ecosystem.gui.actions;

import org.blackpanther.ecosystem.Environment;
import org.blackpanther.ecosystem.agent.Resource;
import org.blackpanther.ecosystem.agent.ResourceConstants;
import org.blackpanther.ecosystem.factory.fields.FieldsConfiguration;
import org.blackpanther.ecosystem.factory.fields.StateFieldMould;
import org.blackpanther.ecosystem.gui.GUIMonitor;
import org.blackpanther.ecosystem.gui.WorldFrame;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.beans.EventHandler;
import java.io.File;
import java.io.IOException;

import static javax.swing.BoxLayout.X_AXIS;
import static javax.swing.BoxLayout.Y_AXIS;
import static org.blackpanther.ecosystem.agent.AgentConstants.AGENT_LOCATION;
import static org.blackpanther.ecosystem.factory.generator.StandardProvider.StandardProvider;
import static org.blackpanther.ecosystem.gui.formatter.RangeModels.generatePositiveDoubleModel;
import static org.blackpanther.ecosystem.helper.Helper.require;

/**
 * @author MACHIZAUD Andréa
 * @version 6/11/11
 */
public class EnvironmentFromFile
        extends FileBrowserAction {

    private EnvironmentWizard creationWizard = new EnvironmentWizard();

    private EnvironmentFromFile() {
        super("Create an environment from an image file",
                "Image files",
                "png",
                "jpg",
                "jpeg",
                "gif"
        );
    }

    private static class EnvironmentFromFileHolder {
        private static final EnvironmentFromFile instance =
                new EnvironmentFromFile();
    }

    public static EnvironmentFromFile getInstance() {
        return EnvironmentFromFileHolder.instance;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        creationWizard.setVisible(true);
    }

    public class EnvironmentWizard extends JDialog {

        private static final String NO_FILE_SELECTED = "No file selected.";

        private JLabel path = new JLabel(NO_FILE_SELECTED);
        private JSpinner defaultEnergy = new JSpinner(generatePositiveDoubleModel());

        JButton accept = new JButton("Accept");
        private File selectedFile = null;

        private EnvironmentWizard() {
            super(WorldFrame.getInstance());
            defaultEnergy.setValue(100.0);
            defaultEnergy.setPreferredSize(new Dimension(100, 30));

            accept.setEnabled(false);

            path.setEnabled(false);
            path.setFont(new Font("Default", Font.ITALIC, 10));

            JButton chooseFile = new JButton("Select your image");
            JButton cancel = new JButton("Cancel");

            JLabel energyAmountLabel = new JLabel("Initial amount for resources : ");
            energyAmountLabel.setLabelFor(defaultEnergy);

            chooseFile.addActionListener(EventHandler.create(
                    ActionListener.class,
                    this,
                    "updateFilePath"
            ));

            accept.addActionListener(EventHandler.create(
                    ActionListener.class,
                    this,
                    "generateEnvironment"
            ));

            cancel.addActionListener(EventHandler.create(
                    ActionListener.class,
                    this,
                    "cancelOperation"
            ));

            Box labeledSpinner = new Box(X_AXIS);
            labeledSpinner.add(energyAmountLabel);
            labeledSpinner.add(defaultEnergy);

            Box top = new Box(Y_AXIS);
            top.add(chooseFile);
            top.add(path);
            top.add(labeledSpinner);

            Box commands = new Box(X_AXIS);
            commands.add(accept);
            commands.add(cancel);

            setLayout(new BorderLayout());
            add(top, BorderLayout.CENTER);
            add(commands, BorderLayout.SOUTH);

            pack();
        }

        public void updateFilePath() throws IOException {
            switch (fc.showOpenDialog(null)) {
                case JFileChooser.APPROVE_OPTION:
                    selectedFile = fc.getSelectedFile();
                    if (selectedFile != null) {
                        path.setText(selectedFile.getCanonicalPath());
                        accept.setEnabled(true);
                    } else {
                        path.setText(NO_FILE_SELECTED);
                        accept.setEnabled(false);
                    }
            }
        }

        @SuppressWarnings("unchecked")
        public void generateEnvironment() {
            require(selectedFile != null);
            try {
                BufferedImage inputImage = ImageIO.read(selectedFile);
                Dimension imageDimension = new Dimension(inputImage.getWidth(), inputImage.getHeight());
                Environment imageEnvironment = new Environment(imageDimension);
                Double resourceEnergyAmount = (Double) defaultEnergy.getValue();

                FieldsConfiguration resourceConfiguration = new FieldsConfiguration(
                        new StateFieldMould<Double>(
                                ResourceConstants.RESOURCE_ENERGY,
                                StandardProvider(resourceEnergyAmount))
                );

                for (int i = 0; i < imageDimension.getWidth(); i++)
                    for (int j = 0; j < imageDimension.getHeight(); j++) {
                        resourceConfiguration.updateMould(new StateFieldMould(
                                AGENT_LOCATION,
                                StandardProvider(
                                        imageToEnvironment(i, j, imageDimension))
                        ));
                        resourceConfiguration.updateMould(new StateFieldMould(
                                ResourceConstants.RESOURCE_NATURAL_COLOR,
                                StandardProvider(
                                        new Color(inputImage.getRGB(i, j))
                                )
                        ));
                        imageEnvironment.add(new Resource(resourceConfiguration));
                    }

                GUIMonitor.Monitor.setCurrentEnvironment(imageEnvironment);
                setVisible(false);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        public void cancelOperation() {
            setVisible(false);
        }

        private Point2D imageToEnvironment(int imageAbscissa, int imageOrdinate, Dimension imageDimension) {
            double environmentAbscissa = imageAbscissa - (imageDimension.getWidth() / 2.0);
            double environmentOrdinate = (imageDimension.getHeight() / 2.0) - imageOrdinate;

            return new Point2D.Double(
                    environmentAbscissa,
                    environmentOrdinate
            );
        }
    }
}
