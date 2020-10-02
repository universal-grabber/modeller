package net.tislib.ugm.lib.markers.base;

import net.tislib.ugm.lib.markers.*;
import net.tislib.ugm.lib.markers.base.model.MarkerData;
import org.jsoup.nodes.Document;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface Marker {
    static List<Marker> getAllMarkers() {
        List<Marker> markers = new ArrayList<>();

        markers.add(new PageMarker());
        markers.add(new FieldSelectorMarker());
        markers.add(new TextTransformMarker());
        markers.add(new MetaDataMarker());
        markers.add(new ChildToParentTransformMarker());
        markers.add(new TextWrapMarker());
        markers.add(new ChunkWrapMarker());

        return markers;
    }

    static Marker locate(String type) {
        return getAllMarkers().stream().filter(item -> item.getName().equals(type)).findAny().get();
    }

    String getName();

    List<MarkerParameter> getParameters();

    Optional<Page> process(Page page, MarkerData parameters);

    default void materializeParameters(Map<String, Serializable> parameters) {
        getParameters().forEach(item -> {
            if (!parameters.containsKey(item.getName()) && item.getDefaultValue() != null) {
                parameters.put(item.getName(), item.getDefaultValue());
            }
        });
    }
}
