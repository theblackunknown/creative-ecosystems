package org.blackpanther.ecosystem.line;

import org.blackpanther.ecosystem.agent.Creature;
import org.blackpanther.ecosystem.behaviour.PredatorBehaviour;

import java.awt.geom.Line2D;
import java.awt.geom.Point2D;

/**
 * @author MACHIZAUD Andréa
 * @version 1.0-alpha - Tue May 24 23:49:57 CEST 2011
 */
public final class TraceHandler {
    private TraceHandler() {
    }

    public static AgentLine trace(Point2D from, Point2D to, Creature monster) {
        return new AgentLine(from, to, monster);
    }

    public static boolean canCross(Line2D a, Line2D b) {
        if (a instanceof AgentLine && b instanceof AgentLine) {
            AgentLine first = (AgentLine) a;
            AgentLine second = (AgentLine) b;
            return first.getSpecies().equals(PredatorBehaviour.class)
                    && !second.getSpecies().equals(PredatorBehaviour.class);
        } else
            return false;
    }
}
