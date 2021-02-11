package net.tislib.ugm.api.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import net.tislib.ugm.api.data.SchemaEntity;
import net.tislib.ugm.api.data.repository.SchemaRepository;
import net.tislib.ugm.data.SchemaProperty;
import org.bson.Document;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;

import net.tislib.ugm.data.Schema;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SchemaService {

    private final SchemaRepository repository;
    private final MongoTemplate mongoTemplate;
    private final ObjectMapper mapper;
    private final TypeReference<Map<String, SchemaProperty>> schemaPropertiesTypeReference = new TypeReference<Map<String, SchemaProperty>>() {
    };

    public List<Schema> list() {
        List<Document> resp = mongoTemplate.findAll(Document.class, "schema");

        return resp.stream()
                .map(this::mapSchema)
                .collect(Collectors.toList());
    }

    @SneakyThrows
    private Schema mapSchema(Document item) {
        Schema schema = new Schema();

        schema.setName(item.getString("_id").replaceAll("/", "."));
        schema.setVersion(item.getString("version"));
        schema.setProperties(mapper.readValue(item.get("properties", Document.class).toJson(), schemaPropertiesTypeReference));
        schema.setDescription(item.getString("description"));
        // tags

        return schema;
    }

    public Schema get(String name) {
        return list().stream()
                .filter(item -> item.getName().equals(name))
                .findAny()
                .orElseThrow(RuntimeException::new);
    }
}
