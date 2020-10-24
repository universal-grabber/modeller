package net.tislib.ugm.data;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import net.tislib.ugm.data.property.ArrayProperty;
import net.tislib.ugm.data.property.StringProperty;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        property = "type")
@JsonSubTypes({
        @JsonSubTypes.Type(value = StringProperty.class, name = "string"),
        @JsonSubTypes.Type(value = ArrayProperty.class, name = "array"),
})
public interface SchemaProperty {
}
