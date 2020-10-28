package net.tislib.ugm.data.property;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import net.tislib.ugm.data.SchemaProperty;

import java.util.Set;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

@Data
@JsonInclude(NON_NULL)
public class ReferenceProperty implements SchemaProperty {
    private String schema;
}
