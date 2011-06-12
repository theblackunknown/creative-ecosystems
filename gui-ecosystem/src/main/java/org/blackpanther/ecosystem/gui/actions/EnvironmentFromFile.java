package org.blackpanther.ecosystem.gui.actions;

import org.blackpanther.ecosystem.ApplicationConstants;
import org.blackpanther.ecosystem.Configuration;
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
import static org.blackpanther.ecosystem.gui.formatter.RangeModels.generateIntegerModel;
import static org.blackpanther.ecosystem.gui.formatter.RangeModels.generatePositiveDoubleModel;
import static org.blackpanther.ecosystem.gui.formatter.RangeModels.generatePositiveIntegerModel;
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
        private JSpinner dropperStep = new JSpinner(generateIntegerModel(1, Integer.MAX_VALUE));
        private boolean excludeWhite = false;

        JButton accept = new JButton("Accept");
        private File selectedFile = null;

        private EnvironmentWizard() {
            super(WorldFrame.getInstance());
            defaultEnergy.setValue(100.0);
            defaultEnergy.setPreferredSize(new Dimension(100, 30));
            dropperStep.setValue(1);
            dropperStep.setPreferredSize(new Dimension(100, 30));

            accept.setEnabled(false);

            path.setEnabled(false);
            path.setFont(new Font("Default", Font.ITALIC, 10));

            JButton chooseFile = new JButton("Select your image");
            JButton cancel = new JButton("Cancel");

            JLabel energyAmountLabel = new JLabel("Initial amount for resources : ");
            energyAmountLabel.setLabelFor(defaultEnergy);

            JLabel dropperStepLabel = new JLabel("Drop step (in pixels) : ");
            dropperStepLabel.setLabelFor(dropperStep);

            JCheckBox excludeWhite = new JCheckBox("Exclude white color ? ", false);
            excludeWhite.addActionListener(EventHandler.create(
                    ActionListener.class,
                    this,
                    "updateExcludeWhiteProperty",
                    "source.selected"
            ));

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

            Box labeledEnergyField = new Box(X_AXIS);
            labeledEnergyField.add(energyAmountLabel);
            labeledEnergyField.add(defaultEnergy);

            Box labeledDropperField = new Box(X_AXIS);
            labeledDropperField.add(dropperStepLabel);
            labeledDropperField.add(dropperStep);

            Box top = new Box(Y_AXIS);
            top.add(chooseFile);
            top.add(path);
            top.add(labeledEnergyField);
            top.add(labeledDropperField);
            top.add(excludeWhite);

            Box commands = new Box(X_AXIS);
            commands.add(accept);
            commands.add(cancel);

            JPanel content = new JPanel(new BorderLayout());
            content.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));

            content.add(top, BorderLayout.CENTER);
            content.add(commands, BorderLayout.SOUTH);

            setContentPane(content);

            pack();
        }

        public void updateExcludeWhiteProperty(boolean shouldBeExcluded){
            excludeWhite = shouldBeExcluded;
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
                Configuration.Configuration.setParameter(
                        ApplicationConstants.SPACE_WIDTH,
                        imageDimension.getWidth(),
                        Double.class
                );
                Configuration.Configuration.setParameter(
                        ApplicationConstants.SPACE_HEIGHT,
                        imageDimension.getHeight(),
                        Double.class
                );
                Environment imageEnvironment = new Environment(imageDimension);
                Double resourceEnergyAmount = (Double) defaultEnergy.getValue();
                Integer dropStep = (Integer) dropperStep.getValue();

                FieldsConfiguration resourceConfiguration = new FieldsConfiguration(
                        new StateFieldMould<Double>(
                                ResourceConstants.RESOURCE_ENERGY,
                                StandardProvider(resourceEnergyAmount))
                );

                for (int i = 0; i < imageDimension.getWidth(); i+= dropStep)
                    for (int j = 0; j < imageDimension.getHeight(); j+= dropStep) {
                        Color pixelColor = new Color(inputImage.getRGB(i, j));

                        if( pixelColor.equals(Color.WHITE) && excludeWhite )
                            continue;

                        resourceConfiguration.updateMould(new StateFieldMould(
                                AGENT_LOCATION,
                                StandardProvider(
                                        imageToEnvironment(i, j, imageDimension))
                        ));
                        resourceConfiguration.updateMould(new StateFieldMould(
                                ResourceConstants.RESOURCE_NATURAL_COLOR,
                                StandardProvider(
                                        pixelColor
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
