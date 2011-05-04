package org.blackpanther.ecosystem.gui.helper;

import org.blackpanther.ecosystem.Environment;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Point2D;
import java.util.logging.Logger;

/**
 * @author MACHIZAUD Andréa
 * @version 5/5/11
 */
public final class Scaler {

    private static final Logger logger =
            Logger.getLogger(
                    Scaler.class.getCanonicalName()
            );


    private Scaler() {
    }

    public static Point2D environmentToPanel(
            JPanel panel,
            Environment environment,
            Point2D environmentPosition
    ) {
        //get current panel's dimension
        Dimension panelDimension = panel.getBounds().getSize();

        double panelAbscissa =
                ((panelDimension.getWidth() / 2.0) + environmentPosition.getX());
        //minus because environment is real mathematics cartesian coordinate
        double panelOrdinate =
                ((panelDimension.getHeight() / 2.0) - environmentPosition.getY());

        logger.fine(String.format(
                "(%.2f,%.2f) translated to (%.2f,%.2f) [panelDimension=%s]",
                environmentPosition.getX(),
                environmentPosition.getY(),
                panelAbscissa,
                panelOrdinate,
                panelDimension
        ));

        return new Point2D.Double(panelAbscissa, panelOrdinate);
    }
}
