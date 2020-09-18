package net.tislib.ugm.api.controller;

import lombok.RequiredArgsConstructor;
import net.tislib.ugm.api.service.MarkerService;
import net.tislib.ugm.lib.markers.base.Marker;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/1.0/markers")
public class MarkersController {

    private final MarkerService markerService;

    @GetMapping
    public List<Marker> list() {
        return markerService.getAllMarkers();
    }

}
