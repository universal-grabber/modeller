package net.tislib.ugm.api.service;

import lombok.RequiredArgsConstructor;
import net.tislib.ugm.api.data.SchemaEntity;
import net.tislib.ugm.api.data.repository.SchemaRepository;
import org.springframework.stereotype.Service;

import net.tislib.ugm.data.Schema;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SchemaService {

    private final SchemaRepository repository;

    public List<Schema> list() {
        return repository.findAll()
                .stream()
                .map(SchemaEntity::getSchema)
                .collect(Collectors.toList());
    }

    public Schema get(String name) {
        return repository.findBySchema_Name(name)
                .orElseThrow(RuntimeException::new).getSchema();
    }

    public Schema apply(Schema schema) {
        SchemaEntity schemaEntity = repository.findBySchema_Name(schema.getName()).orElse(new SchemaEntity());

        schemaEntity.setSchema(schema);

        repository.save(schemaEntity);

        return schemaEntity.getSchema();
    }

    public Schema delete(String name) {
        SchemaEntity schemaEntity = repository.findBySchema_Name(name)
                .orElse(new SchemaEntity());

        repository.delete(schemaEntity);

        return schemaEntity.getSchema();
    }
}
