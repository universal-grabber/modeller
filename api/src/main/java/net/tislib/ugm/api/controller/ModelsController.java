package net.tislib.ugm.api.controller;

import lombok.RequiredArgsConstructor;
import net.tislib.ugm.api.data.ModelRepository;
import net.tislib.ugm.api.service.ModelService;
import net.tislib.ugm.lib.markers.base.Marker;
import net.tislib.ugm.lib.markers.base.model.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/1.0/models")
public class ModelsController {

    private final ModelService service;

    @GetMapping
    public List<Model> list() {
        return service.getAll();
    }

    @GetMapping(params = {"name"})
    public Model get(@RequestParam String name) {
        return service.get(name);
    }

    @DeleteMapping(params = {"name"})
    public void delete(@RequestParam String name) {
        service.delete(name);
    }

    @PutMapping(params = {"name"})
    public Model update(@RequestParam String name, @Validated Model model) {
        return service.update(name, model);
    }

}
