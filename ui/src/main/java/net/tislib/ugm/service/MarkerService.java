package net.tislib.ugm.service;

import net.tislib.ugm.markers.FieldSelectorMarker;
import net.tislib.ugm.markers.Marker;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
public class MarkerService {
    public Set<Marker> getAllMarkers() {
        Set<Marker> markers = new HashSet<>();

        markers.add(new FieldSelectorMarker());

        return markers;
    }

    public Marker locate(String type) {
        return getAllMarkers().stream().filter(item -> item.getName().equals(type)).findAny().get();
    }
}
