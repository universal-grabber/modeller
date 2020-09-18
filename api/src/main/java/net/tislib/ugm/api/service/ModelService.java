package net.tislib.ugm.api.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import kong.unirest.Unirest;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import net.tislib.ugm.api.data.ModelRepository;
import net.tislib.ugm.lib.markers.base.Marker;
import net.tislib.ugm.lib.markers.base.ModelDataExtractor;
import net.tislib.ugm.lib.markers.base.model.Example;
import net.tislib.ugm.lib.markers.base.model.Model;
import org.bson.Document;
import org.springframework.beans.BeanUtils;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.Serializable;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ModelService {

    String wdPath = "/home/taleh/Projects/UniersalDataPlatform/ugm-backend/wd";

    private final ObjectMapper objectMapper = new ObjectMapper();

    private final ModelRepository repository;

    private final MongoTemplate mongoTemplate;

    @SneakyThrows
    public Model get(String name) {
        return repository.findByName(name).orElseThrow(() -> new RuntimeException("model not found"));
    }

    public List<Model> getAll() {
        return repository.findAll();
    }

    public void delete(String name) {
        repository.delete(get(name));
    }

    public Model update(String name, Model model) {
        Model existingModel = get(name);

        existingModel.setExamples(model.getExamples());
        existingModel.setMarkers(model.getMarkers());

        repository.save(existingModel);

        return get(name);
    }

    public Serializable extractData(String name, Integer exampleId) {
        ModelDataExtractor modelDataExtractor = new ModelDataExtractor();

        Model model = get(name);

        Example example = model.getExamples().stream().filter(item -> item.getId().equals(exampleId)).findAny().get();

        String html = Unirest.get(example.getUrl().toString()).asString().getBody();

        return modelDataExtractor.processDocument(model, html);
    }
}
