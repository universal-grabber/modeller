package net.tislib.ugm.data;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.util.Map;

@Data
public class Schema implements HasProperties {

    @NotBlank
    private String version;

    @NotBlank
    private String namespace;

    @NotBlank
    private String name;

    Map<String, SchemaProperty> properties;
}
