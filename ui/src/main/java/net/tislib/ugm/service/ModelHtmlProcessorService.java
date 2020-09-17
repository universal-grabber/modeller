package net.tislib.ugm.service;

import kong.unirest.Unirest;
import lombok.RequiredArgsConstructor;
import net.tislib.ugm.lib.markers.ModelProcessor;
import net.tislib.ugm.lib.markers.model.Example;
import net.tislib.ugm.lib.markers.model.Model;
import org.jsoup.nodes.Document;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ModelHtmlProcessorService {

    private final ModelService modelService;
    private final ModelProcessor modelProcessor = new ModelProcessor();

    public String processedFrame(String modelName, Integer exampleId) {
        Model model = modelService.get(modelName);

        Example example = model.getExamples().stream().filter(item -> item.getId().equals(exampleId)).findAny().get();

        String html = Unirest.get(example.getUrl().toString()).asString().getBody();

        return modelProcessor.process(model, html);
    }

    public Document getDocument(Model model, Integer exampleId) {
        Example example = model.getExamples().stream().filter(item -> item.getId().equals(exampleId)).findAny().get();

        String html = Unirest.get(example.getUrl().toString()).asString().getBody();

        return modelProcessor.processDocument(model, html);
    }
}
