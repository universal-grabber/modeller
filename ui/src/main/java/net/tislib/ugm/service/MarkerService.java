package net.tislib.ugm.service;

import net.tislib.ugm.markers.FieldSelectorMarker;
import net.tislib.ugm.markers.Marker;
import net.tislib.ugm.markers.MetaDataMarker;
import net.tislib.ugm.markers.TextTransformMarker;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class MarkerService {
    public List<Marker> getAllMarkers() {
        List<Marker> markers = new ArrayList<>();

        markers.add(new FieldSelectorMarker());
        markers.add(new TextTransformMarker());
        markers.add(new MetaDataMarker());

        return markers;
    }

    public Marker locate(String type) {
        return getAllMarkers().stream().filter(item -> item.getName().equals(type)).findAny().get();
    }
}
