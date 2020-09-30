package net.tislib.ugm.api.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import kong.unirest.Unirest;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import net.tislib.ugm.api.data.repository.ModelRepository;
import net.tislib.ugm.lib.markers.base.ModelDataExtractor;
import net.tislib.ugm.lib.markers.base.ModelProcessor;
import net.tislib.ugm.lib.markers.base.model.Model;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ModelService {

    String wdPath = "/home/taleh/Projects/UniersalDataPlatform/ugm-backend/wd";

    private final ObjectMapper objectMapper = new ObjectMapper();

    private final ModelRepository repository;
    private final ModelProcessor modelProcessor = new ModelProcessor();

    private final MongoTemplate mongoTemplate;

    @SneakyThrows
    public Model get(String name) {
        return repository.findByName(name)
                .orElseThrow(() -> new RuntimeException("model not found"));
    }

    public List<Model> getAll() {
        return repository.findAll();
    }

    public void delete(String name) {
        repository.delete(get(name));
    }

    public Model update(String name, Model model) {
        Model existingModel = get(name);

        modelProcessor.materialize(model);

        existingModel.setExamples(model.getExamples());
        existingModel.setMarkers(model.getMarkers());

        repository.save(existingModel);

        return get(name);
    }

    public Serializable extractData(String name, String url) {
        ModelDataExtractor modelDataExtractor = new ModelDataExtractor();

        Model model = get(name);

        String html = Unirest.get(url).asString().getBody();

        return modelDataExtractor.processDocument(model, html);
    }
}
