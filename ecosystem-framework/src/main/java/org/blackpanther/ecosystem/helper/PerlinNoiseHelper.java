package org.blackpanther.ecosystem.helper;

import java.awt.geom.Point2D;
import java.util.Random;

import static java.lang.Math.*;

/**
 * @author MACHIZAUD Andréa
 * @version 27/05/11
 */
public final class PerlinNoiseHelper {
    private static final int STEP = 128;
    private static final double PERSISTANCE = 0.8;
    private static final int OCTAVES = 7;

    private static final int HEIGHT = 1000;
    private static final int WIDTH = 1000;

    private static double[][] builtin_noise = initializeNoise();

    private static double[][] initializeNoise() {
        int longueur_max = (int)
                ceil(WIDTH * pow(2.0, (double) OCTAVES - 1) / STEP);
        int hauteur_max = (int)
                ceil(HEIGHT * pow(2.0, (double) OCTAVES - 1) / STEP);

        double[][] internal = new double[longueur_max][hauteur_max];
        Random fixedRandom = new Random(42L);

        for (int i = 0; i < longueur_max; i++)
            for(int j = 0; j < hauteur_max;j++)
            internal[i][j] = fixedRandom.nextDouble();

        return internal;
    }

    private static double builtinNoise(int i, int j) {
        return builtin_noise[i][j];
    }

    private static double linearInterpolation(double a, double b, double x) {
        return a * (1 - x) + b * x;
    }

    private static double cosinusInterpolation(double a, double b, double x) {
        double k = (1 - cos(x * PI)) / 2;
        return linearInterpolation(a, b, k);
    }

    private static double cosinusInterpolation(double a, double b, double c, double d, double x, double y) {
        double x1 = cosinusInterpolation(a, b, x);
        double x2 = cosinusInterpolation(c, d, x);
        return cosinusInterpolation(x1, x2, y);
    }

    private static double noise(double x, double y) {
        int i = (int) (x / STEP);
        int j = (int) (y / STEP);
        return cosinusInterpolation(
                builtinNoise(i, j), builtinNoise(i + 1, j), builtinNoise(i, j + 1), builtinNoise(i + 1, j + 1),
                (x / STEP) % 1, (y / STEP) % 1);
    }

    public static double coherentNoise(Point2D location) {
        return coherentNoise(location.getX(),location.getY());
    }

    public static double coherentNoise(double x, double y) {
        double somme = 0.0;
        double p = 1.0;
        int f = 1;

        for (int i = 0; i < OCTAVES; i++) {
            somme += p * noise(x * f, y * f);
            p *= PERSISTANCE;
            f *= 2;
        }

        return somme * (1.0 - PERSISTANCE) / (1.0 - p);
    }

}
