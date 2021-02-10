package net.tislib.ugm.api.controller;

import lombok.RequiredArgsConstructor;
import net.tislib.ugm.api.data.marker.Marker;
import net.tislib.ugm.api.service.MarkerService;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/1.0/markers")
@CrossOrigin
public class MarkersController {

    private final MarkerService service;

    @GetMapping
    public List<Marker> list() {
        return service.getAll();
    }

}
