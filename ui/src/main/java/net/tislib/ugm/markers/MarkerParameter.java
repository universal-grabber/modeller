package net.tislib.ugm.markers;

import lombok.Data;

@Data
public class MarkerParameter {

    private String name;

    private String caption;

    private ParameterType parameterType;

    private MarkerParameterOptions options;

    public enum ParameterType {
        TEXT, REGEXP, INSPECTOR, NUMBER, COMBOBOX
    }

    public interface MarkerParameterOptions {

    }

    @Data
    public static class InspectorOptions implements MarkerParameterOptions {

    }
}
