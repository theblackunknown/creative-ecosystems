package org.blackpanther.ecosystem.line;

import org.blackpanther.ecosystem.agent.Creature;
import org.blackpanther.ecosystem.behaviour.BehaviorManager;

import java.awt.*;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;

import static org.blackpanther.ecosystem.agent.CreatureConstants.CREATURE_NATURAL_COLOR;

/**
 * @author MACHIZAUD Andréa
 * @version 1.0-alpha - Tue May 24 23:49:57 CEST 2011
 */
public class AgentLine
    extends Line2D.Double{

    private Color genotypeColor;
    private Color phenotypeColor;
    private Class<? extends BehaviorManager> species;
    private java.lang.Double power;

    public AgentLine(Point2D x, Point2D y,
                     Creature agent) {
        super(x, y);
        this.genotypeColor = agent.getGene(CREATURE_NATURAL_COLOR,Color.class);
        this.phenotypeColor = agent.getColor();
        this.species = agent.getBehaviour().getClass();
        this.power = agent.getEnergy();
    }

    public Color getGenotypeColor() {
        return genotypeColor;
    }

    public Color getPhenotypeColor() {
        return phenotypeColor;
    }

    public Class<? extends BehaviorManager> getSpecies() {
        return species;
    }

    public java.lang.Double getPower() {
        return power;
    }
}
