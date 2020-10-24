package net.tislib.ugm.data;

import java.util.Map;

public interface HasProperties {
    void setProperties(Map<String, SchemaProperty> properties);

    Map<String, SchemaProperty> getProperties();
}
