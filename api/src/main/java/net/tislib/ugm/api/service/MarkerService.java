package net.tislib.ugm.api.service;

import net.tislib.ugm.lib.markers.base.Marker;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MarkerService {
    public List<Marker> getAll() {
        return Marker.getAllMarkers();
    }

    public Marker locate(String type) {
        return Marker.locate(type);
    }
}
