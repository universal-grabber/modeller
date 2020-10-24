package net.tislib.ugm.data.property;

import lombok.Data;
import net.tislib.ugm.data.SchemaProperty;

@Data
public class ArrayProperty implements SchemaProperty {
    private String pattern;
    SchemaProperty items;
}
