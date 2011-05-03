package org.blackpanther.ecosystem.gui;

import org.blackpanther.ecosystem.Environment;

import javax.swing.*;
import java.awt.*;

/**
 * @author MACHIZAUD Andréa
 * @version 5/2/11
 */
public class WorldFrame
    extends JFrame {

    public WorldFrame(Environment env) {
        super();

        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(
                new GraphicEnvironment(env),
                BorderLayout.CENTER
        );

        setDefaultCloseOperation(EXIT_ON_CLOSE);
        //setLocationRelativeTo(null);
        pack();
        setExtendedState(MAXIMIZED_BOTH);
    }
}
