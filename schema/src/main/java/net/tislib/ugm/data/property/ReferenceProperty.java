package net.tislib.ugm.data.property;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import net.tislib.ugm.data.Example;
import net.tislib.ugm.data.SchemaProperty;

import javax.validation.constraints.NotBlank;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

@Data
@JsonInclude(NON_NULL)
public class ReferenceProperty implements SchemaProperty {

    @NotBlank
    private String schema;

    private String description;

    private boolean allowQuery = true;

    private Example example;
}
