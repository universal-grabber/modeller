package net.tislib.ugm.api.controller;

import lombok.RequiredArgsConstructor;
import net.tislib.ugm.api.service.ModelHtmlProcessorService;
import net.tislib.ugm.api.service.ModelService;
import net.tislib.ugm.lib.markers.base.model.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.io.Serializable;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/1.0/models")
@CrossOrigin
public class ModelsController {

    private final ModelService service;
    private final ModelHtmlProcessorService modelHtmlProcessorService;

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
    public Model update(@RequestParam String name, @RequestBody @Validated Model model) {
        return service.update(name, model);
    }

    @GetMapping(value = "/process", produces = "text/html")
    public String processedFrame(@RequestParam String name, @RequestParam String url) {
        return modelHtmlProcessorService.processedFrame(name, url);
    }

    @GetMapping(value = "/extract")
    public Serializable extractData(@RequestParam String name, @RequestParam String url) {
        return service.extractData(name, url);
    }

}
