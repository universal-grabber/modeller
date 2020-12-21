package net.tislib.ugm.data.property;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import net.tislib.ugm.data.Example;
import net.tislib.ugm.data.HasProperties;
import net.tislib.ugm.data.SchemaProperty;

import javax.validation.constraints.NotNull;
import java.util.Map;
import java.util.Set;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

@Data
@JsonInclude(NON_NULL)
public class ObjectProperty implements SchemaProperty, HasProperties {

    @NotNull
    Map<String, SchemaProperty> properties;

    private Set<String> tags;

    private String description;

    private boolean allowQuery = true;

    private final Example example = null;
}
