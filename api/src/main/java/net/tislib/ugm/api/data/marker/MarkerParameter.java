package net.tislib.ugm.api.data.marker;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MarkerParameter {

    private String name;

    private String caption;

    private ParameterType parameterType;

    private MarkerParameterOptions options;

    private Serializable[] values;

    private Serializable defaultValue;

    private boolean required;

    public enum ParameterType {
        TEXT, REGEXP, REGEX_SUB, INSPECTOR, NUMBER, COMBOBOX, CHECKBOX
    }

    public interface MarkerParameterOptions {

    }

    @Data
    public static class InspectorOptions implements MarkerParameterOptions {

    }
}
