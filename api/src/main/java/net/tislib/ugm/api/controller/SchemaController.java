package net.tislib.ugm.api.controller;

import lombok.RequiredArgsConstructor;
import net.tislib.ugm.api.service.SchemaService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import net.tislib.ugm.data.Schema;

import java.util.List;

@RestController
@RequestMapping("/api/1.0/schemas")
@CrossOrigin
@RequiredArgsConstructor
public class SchemaController {

    private final SchemaService schemaService;

    @GetMapping(produces = {
            "application/json",
            "application/yaml",
    })
    public List<Schema> list() {
        return schemaService.list();
    }

    @GetMapping("{name}")
    public Schema get(@PathVariable String name) {
        return schemaService.get(name);
    }

    @DeleteMapping("{name}")
    public Schema delete(@PathVariable String name) {
        return schemaService.delete(name);
    }

    @PostMapping(consumes = {
            "application/json",
            "application/yaml",
    })
    public Schema apply(@RequestBody @Validated Schema schema) {
        return schemaService.apply(schema);
    }

}
