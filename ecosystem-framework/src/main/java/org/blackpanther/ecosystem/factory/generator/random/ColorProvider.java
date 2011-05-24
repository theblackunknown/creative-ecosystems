package org.blackpanther.ecosystem.factory.generator.random;

import org.blackpanther.ecosystem.Configuration;
import org.blackpanther.ecosystem.factory.generator.RandomProvider;

import java.awt.*;

/**
 * @author MACHIZAUD Andréa
 * @version 1.0-alpha - Tue May 24 23:49:58 CEST 2011
 */
public class ColorProvider
        extends RandomProvider<Color> {

    private ColorProvider(){}

    private static class ColorProviderHolder {
        private static final ColorProvider instance =
            new ColorProvider();
    }

    public static ColorProvider getInstance(){
        return ColorProviderHolder.instance;
    }

    @Override
    public Color getValue() {
        return new Color(
                Configuration.Configuration.getRandom().nextFloat(),
                Configuration.Configuration.getRandom().nextFloat(),
                Configuration.Configuration.getRandom().nextFloat()
        );
    }
}
