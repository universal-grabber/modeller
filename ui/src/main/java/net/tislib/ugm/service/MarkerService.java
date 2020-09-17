package net.tislib.ugm.service;

import net.tislib.ugm.lib.markers.Marker;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MarkerService {
    public List<Marker> getAllMarkers() {
        return Marker.getAllMarkers();
    }

    public Marker locate(String type) {
        return Marker.locate(type);
    }
}
