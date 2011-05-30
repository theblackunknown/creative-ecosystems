package org.blackpanther.ecosystem.gui.actions;

import javax.swing.*;
import java.awt.event.ActionEvent;

import static org.blackpanther.ecosystem.Configuration.Configuration;
import static org.blackpanther.ecosystem.Configuration.PERLIN_NOISE_OPTION;

/**
 * @author MACHIZAUD Andréa
 * @version 26/05/11
 */
public class TogglePerlinNoise
        extends AbstractAction {

    private TogglePerlinNoise(){
        super("Toggle perlin noise");}

    private static class TogglePerlinNoiseHolder {
        private static final TogglePerlinNoise instance =
            new TogglePerlinNoise();
    }

    public static TogglePerlinNoise getInstance(){
        return TogglePerlinNoiseHolder.instance;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Configuration.setParameter(
                PERLIN_NOISE_OPTION,
                !Configuration.getParameter(PERLIN_NOISE_OPTION, Boolean.class),
                Boolean.class
        );
    }
}
