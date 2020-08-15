package net.tislib.ugm.markers;

import org.jsoup.nodes.Document;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

public interface Marker {
    String getName();

    List<MarkerParameter> getParameters();

    Document process(Document document, Map<String, Serializable> parameters);

    default void materializeParameters(Map<String, Serializable> parameters) {
        getParameters().forEach(item -> {
            if (!parameters.containsKey(item.getName()) && item.getDefaultValue() != null) {
                parameters.put(item.getName(), item.getDefaultValue());
            }
        });
    }
}
