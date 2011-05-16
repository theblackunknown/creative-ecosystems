package org.blackpanther.ecosystem.gui.lightweight;

import org.blackpanther.ecosystem.Configuration;

import java.util.Map;

import static org.blackpanther.ecosystem.Configuration.RANDOM;

/**
 * TODO Agent placement strategy
 *
 * @author MACHIZAUD Andréa
 * @version 0.2 - Wed May 11 02:54:46 CEST 2011
 */
public class ConfigurationInformation {

    public static ConfigurationInformation dump(Configuration config) {
        return new ConfigurationInformation(config);
    }

    private String representation;

    public ConfigurationInformation(Configuration config) {
        StringBuilder sb = new StringBuilder();
        sb.append("<html><p>");
        sb.append("<b>" + RANDOM + "</b>:<br/>   <i>").append(config.getRandomSeed()).append("</i><br/>");
        for (Map.Entry<Object, Object> parameter : config.parameters().entrySet()) {
            if (!parameter.getKey().equals(RANDOM)) {
                sb.append("<b>").append(parameter.getKey()).append("</b>:<br/>   <i>");
                sb.append(parameter.getValue());
                sb.append("</i><br/>");
            }
        }
        sb.append("</p></html>");
        representation = sb.toString();
    }

    @Override
    public String toString() {
        return representation;
    }
}
