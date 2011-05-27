package org.blackpanther.ecosystem.helper;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * @author MACHIZAUD Andréa
 * @version 27/05/11
 */
public class PerlinNoiseHelperTest {
    @Test
    public void testCoherentNoise() throws Exception {
        double[] expected = new double[]{
                0.7275636800328681,
                0.7252998748971317,
                0.7217944331835608,
                0.7069397450405795,
                0.7261300679577548,
                0.7238588178428513,
                0.7203447987031422,
                0.7098434697216442,
                0.7239101579973745,
                0.7216303304563326,
                0.7181049459540886,
                0.7119540115024322,
                0.7181998108869471,
                0.7128168224712721,
                0.7061853124627532,
                0.6982972404152299
        };

        int index = 0;
        for (double i = 0.0; i < 4.0; i++)
            for (double j = 0.0; j < 4.0; j++)
                assertEquals(PerlinNoiseHelper.coherentNoise(i, j),expected[index++], 10e-16);
    }
}
