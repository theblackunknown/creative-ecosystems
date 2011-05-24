package org.blackpanther.ecosystem.line;

import org.blackpanther.ecosystem.agent.Creature;
import org.blackpanther.ecosystem.behaviour.PredatorBehaviour;

import java.awt.*;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;

import static org.blackpanther.ecosystem.agent.AgentConstants.AGENT_NATURAL_COLOR;

/**
 * @author MACHIZAUD Andréa
 * @version 1.0-alpha - Tue May 24 23:49:57 CEST 2011
 */
public final class TraceHandler {
    private TraceHandler() {
    }

    public static AgentLine trace(Point2D from, Point2D to, Creature monster) {
        Color genotypeColor = monster.getGene(AGENT_NATURAL_COLOR, Color.class);
        Color phenotypeColor = monster.getColor();
        Color expressedColor = new Color(
                (genotypeColor.getRed() + phenotypeColor.getRed()) / 2,
                (genotypeColor.getGreen() + phenotypeColor.getGreen()) / 2,
                (genotypeColor.getBlue() + phenotypeColor.getBlue()) / 2
        );
        return new AgentLine(from, to, expressedColor, monster.getBehaviour().getClass());
    }

    public static boolean canCross(Line2D a, Line2D b) {
        return a instanceof AgentLine && b instanceof AgentLine
                && ((AgentLine) a).getSpecies().equals(PredatorBehaviour.class)
                && !((AgentLine) b).getSpecies().equals(PredatorBehaviour.class);
    }
}
