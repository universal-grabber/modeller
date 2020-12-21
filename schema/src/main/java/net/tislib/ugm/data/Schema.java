package net.tislib.ugm.data;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.util.Map;
import java.util.Set;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

@Data
@JsonInclude(NON_NULL)
public class Schema implements HasProperties {

    @NotBlank
    private String version;

    @NotBlank
    private String namespace;

    @NotBlank
    private String name;

    private String description;

    private Set<String> tags;

    Map<String, SchemaProperty> properties;
}
