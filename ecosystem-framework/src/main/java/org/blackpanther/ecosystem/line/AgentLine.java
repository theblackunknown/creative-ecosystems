package org.blackpanther.ecosystem.line;

import org.blackpanther.ecosystem.behaviour.BehaviorManager;

import java.awt.*;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;

/**
 * @author MACHIZAUD Andréa
 * @version 1.0-alpha - Tue May 24 23:49:57 CEST 2011
 */
public class AgentLine
    extends Line2D.Double{

    private Color color;
    private Class<? extends BehaviorManager> species;

    public AgentLine(Point2D x, Point2D y,
                         Color c,
                         Class<? extends BehaviorManager> species) {
        super(x, y);
        this.color = c;
        this.species = species;
    }

    public Color getColor() {
        return color;
    }

    public Class<? extends BehaviorManager> getSpecies() {
        return species;
    }
}
