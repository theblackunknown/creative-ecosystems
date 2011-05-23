package org.blackpanther.ecosystem.factory;

import org.blackpanther.ecosystem.ColorfulTrace;
import org.blackpanther.ecosystem.agent.Creature;

import java.awt.geom.Line2D;
import java.awt.geom.Point2D;

/**
 * @author MACHIZAUD Andréa
 * @version 5/22/11
 */
public class TraceFactory {
    private TraceFactory(){}

    private static class LineFactoryHolder {
        private static final TraceFactory instance =
            new TraceFactory();
    }

    public static TraceFactory getInstance(){
        return LineFactoryHolder.instance;
    }

    public Line2D trace(Point2D from, Point2D to, Creature monster){
        return new ColorfulTrace(from,to,monster.getColor());
    }
}
