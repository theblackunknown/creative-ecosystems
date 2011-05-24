package org.blackpanther.ecosystem.gui.settings;

import org.blackpanther.ecosystem.gui.settings.fields.SettingField;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;

/**
 * @author MACHIZAUD Andréa
 * @version 22/05/11
 */
abstract class ParameterPanel
        extends JPanel {

    protected final Map<String, SettingField> parameters;

    protected ParameterPanel(String name) {
        super(new BorderLayout());
        setName(name);

        JLabel presentation = new JLabel("<html><i><b>" + name + "</b></i></html>");
        presentation.setAlignmentX(LEFT_ALIGNMENT);
        parameters = generateParameters();

        Box layout = Box.createVerticalBox();

        layout.add(presentation);

        generateContent(layout);

        //"bite me, fucking UI"
        layout.add(Box.createVerticalStrut(40));

        add(new JScrollPane(layout));
    }

    abstract protected Map<String, SettingField> generateParameters();

    abstract protected void generateContent(Box layout);

    public Map<Object, Object> getParameters() {
        Map<Object, Object> externalisedParameters = new HashMap<Object, Object>();
        for (Map.Entry<String, SettingField> parameter : parameters.entrySet())
            externalisedParameters.put(
                    parameter.getKey(),
                    String.valueOf(parameter.getValue().getValue()));
        return externalisedParameters;
    }

}
