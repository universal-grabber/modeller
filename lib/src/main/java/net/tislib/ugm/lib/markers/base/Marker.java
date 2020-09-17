package net.tislib.ugm.lib.markers.base;

import net.tislib.ugm.lib.markers.*;
import org.jsoup.nodes.Document;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public interface Marker {
    static List<Marker> getAllMarkers() {
        List<Marker> markers = new ArrayList<>();

        markers.add(new PageMarker());
        markers.add(new FieldSelectorMarker());
        markers.add(new TextTransformMarker());
        markers.add(new MetaDataMarker());
        markers.add(new ElementToElementTransformMarker());
        markers.add(new TextWrapMarker());

        return markers;
    }

    static Marker locate(String type) {
        return getAllMarkers().stream().filter(item -> item.getName().equals(type)).findAny().get();
    }

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
