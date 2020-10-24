package net.tislib.ugm.data.property;

import lombok.Data;
import net.tislib.ugm.data.HasProperties;
import net.tislib.ugm.data.SchemaProperty;

import java.util.Map;

@Data
public class ObjectProperty implements SchemaProperty, HasProperties {
    private String pattern;

    Map<String, SchemaProperty> properties;
}
