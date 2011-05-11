package org.blackpanther.ecosystem.gui.lightweight;

import org.blackpanther.ecosystem.Configuration;

import java.util.Map;

import static org.blackpanther.ecosystem.Agent.AGENT_BEHAVIOUR;
import static org.blackpanther.ecosystem.Configuration.RANDOM;

/**
 * TODO Agent placement strategy
 *
 * @author MACHIZAUD Andréa
 * @version 5/10/11
 */
public class ConfigurationInformation {

    public static ConfigurationInformation dump(Configuration config) {
        return new ConfigurationInformation(config);
    }

    private String representation;

    public ConfigurationInformation(Configuration config) {
        StringBuilder sb = new StringBuilder();
        sb.append("<html><ul>");
        sb.append("<li>" + RANDOM + "=").append(config.getRandomSeed()).append("</li>");
        for (Map.Entry<Object, Object> parameter : config.parameters().entrySet()) {
            if (!parameter.getKey().equals(RANDOM)) {
                if (parameter.getKey().equals(AGENT_BEHAVIOUR)) {
                    sb.append("<li>").append(parameter.getKey()).append("=<br/>")
                            .append(parameter.getValue().getClass().getSimpleName()).append("</li>");
                } else {
                    sb.append("<li>").append(parameter.getKey()).append("=")
                            .append(parameter.getValue()).append("</li>");
                }
            }
        }
        sb.append("</ul></html>");
        representation = sb.toString();
    }

    @Override
    public String toString() {
        return representation;
    }
}
