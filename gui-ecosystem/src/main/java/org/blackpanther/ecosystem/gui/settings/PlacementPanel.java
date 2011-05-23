package org.blackpanther.ecosystem.gui.settings;

import org.blackpanther.ecosystem.agent.Agent;
import org.blackpanther.ecosystem.factory.fields.FieldsConfiguration;
import org.blackpanther.ecosystem.math.Geometry;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.geom.Dimension2D;
import java.beans.EventHandler;
import java.util.Collection;

import static org.blackpanther.ecosystem.factory.PopulationFactory.*;
import static org.blackpanther.ecosystem.gui.formatter.RangeModels.generatePositiveDoubleModel;
import static org.blackpanther.ecosystem.gui.formatter.RangeModels.generatePositiveIntegerModel;
import static org.blackpanther.ecosystem.helper.Helper.createLabeledField;

/**
 * @author MACHIZAUD Andréa
 * @version 22/05/11
 */
abstract class PlacementPanel<T extends Agent> extends JPanel {

    private static final String GRID = "Grid";
    private static final String CIRCLE = "Circle";
    private static final String RANDOM = "Random";

    public static final String[] BUILTIN_PLACEMENT = new String[]{
            GRID,
            CIRCLE,
            RANDOM,
    };

    /**
     * Panel management
     */
    private JComboBox strategyList;
    private JPanel cardPanel;

    /**
     * Standard provided
     */
    private JSpinner gridWidth;
    private JSpinner gridHeight;
    private JSpinner step;

    /**
     * Circle placement
     */
    private JSpinner circleRadius;

    /**
     * common
     */
    private JSpinner agentNumberCircle;
    private JSpinner agentNumberRandom;

    public PlacementPanel(String name, String itemName) {
        super();
        setName(name);

        JLabel presentation = new JLabel("<html><i><b>" + name + "</b></i></html>");
        presentation.setAlignmentX(LEFT_ALIGNMENT);

        Box layout = Box.createVerticalBox();

        layout.add(presentation);

        generateContent(layout, itemName);

        add(layout);
    }

    private void generateContent(Box layout, String itemName) {
        cardPanel = new JPanel(new CardLayout());

        strategyList = new JComboBox(BUILTIN_PLACEMENT);
        strategyList.addActionListener(EventHandler.create(
                ActionListener.class,
                this,
                "switchStrategyPanel",
                "source.selectedItem"
        ));

        gridWidth = new JSpinner(generatePositiveDoubleModel());
        gridWidth.setValue(400.0);
        gridHeight = new JSpinner(generatePositiveDoubleModel());
        gridHeight.setValue(400.0);
        step = new JSpinner(generatePositiveDoubleModel());
        step.setValue(60.0);

        agentNumberCircle = new JSpinner(generatePositiveIntegerModel());
        agentNumberCircle.setValue(500);
        agentNumberRandom = new JSpinner(generatePositiveIntegerModel());
        agentNumberRandom.setValue(500);

        circleRadius = new JSpinner(generatePositiveDoubleModel());
        circleRadius.setValue(120.0);

        JPanel gridPlacement = new JPanel(new GridLayout(0, 1));
        JPanel circlePlacement = new JPanel(new GridLayout(0, 1));
        JPanel randomPlacement = new JPanel(new GridLayout(0, 1));
        JButton addToEnvironmentGrid = new JButton("Add to environment");
        JButton addToEnvironmentCircle = new JButton("Add to environment");
        JButton addToEnvironmentRandom = new JButton("Add to environment");

        addToEnvironmentGrid.addActionListener(setButtonActionListener());
        addToEnvironmentCircle.addActionListener(setButtonActionListener());
        addToEnvironmentRandom.addActionListener(setButtonActionListener());

        gridPlacement.add(createLabeledField(
                "Grid width",
                gridWidth
        ));
        gridPlacement.add(createLabeledField(
                "Grid height",
                gridHeight
        ));
        gridPlacement.add(createLabeledField(
                "Step",
                step
        ));
        gridPlacement.add(addToEnvironmentGrid);

        circlePlacement.add(createLabeledField(
                "Number of " + itemName,
                agentNumberCircle
        ));
        circlePlacement.add(createLabeledField(
                "Circle's radius",
                circleRadius
        ));
        circlePlacement.add(addToEnvironmentCircle);

        randomPlacement.add(createLabeledField(
                "Number of " + itemName,
                agentNumberRandom
        ));
        randomPlacement.add(addToEnvironmentRandom);

        cardPanel.add(gridPlacement, GRID);
        cardPanel.add(circlePlacement, CIRCLE);
        cardPanel.add(randomPlacement, RANDOM);

        layout.add(strategyList);
        layout.add(cardPanel);
    }

    public void switchStrategyPanel(String panelName) {
        CardLayout layout = (CardLayout) cardPanel.getLayout();
        layout.show(cardPanel, panelName);
    }

    abstract protected ActionListener setButtonActionListener();

    abstract protected Class<T> getSpecies();

    abstract protected FieldsConfiguration getConfiguration();

    public Collection<T> generatePopulation() {
        if (strategyList.getSelectedItem().equals(GRID)) {
            return generateGridPopulation();
        } else if (strategyList.getSelectedItem().equals(CIRCLE)) {
            return generateCirclePopulation();
        } else if (strategyList.getSelectedItem().equals(RANDOM)) {
            return generateRandomPopulation();
        } else
            throw new IllegalStateException("Unknown placement strategy");
    }

    protected Collection<T> generateRandomPopulation() {
        return generatePool(
                getSpecies(),
                getConfiguration(),
                getRandomAgentNumber()
        );
    }

    protected Collection<T> generateCirclePopulation() {
        return generateCirclePool(
                getSpecies(),
                getConfiguration(),
                getCircleAgentNumber(),
                getCircleRadius()
        );
    }

    protected Collection<T> generateGridPopulation() {
        return generateGridPool(
                getSpecies(),
                getConfiguration(),
                getGridDimension(),
                getGridStep()
        );
    }

    protected  Dimension2D getGridDimension() {
        return new Geometry.Dimension(
                (Double) gridWidth.getValue(),
                (Double) gridHeight.getValue()
        );
    }

    protected Double getGridStep() {
        return (Double) step.getValue();
    }

    protected Integer getCircleAgentNumber() {
        return (Integer) agentNumberCircle.getValue();
    }

    protected Integer getRandomAgentNumber() {
        return (Integer) agentNumberRandom.getValue();
    }

    protected Double getCircleRadius() {
        return (Double) circleRadius.getValue();
    }
}
