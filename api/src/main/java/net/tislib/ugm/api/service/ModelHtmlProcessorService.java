package net.tislib.ugm.api.service;

import kong.unirest.Unirest;
import lombok.RequiredArgsConstructor;
import net.tislib.ugm.lib.markers.base.ModelProcessor;
import net.tislib.ugm.lib.markers.base.model.Example;
import net.tislib.ugm.lib.markers.base.model.Model;
import org.jsoup.nodes.Document;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ModelHtmlProcessorService {

    private final ModelService modelService;
    private final ModelProcessor modelProcessor = new ModelProcessor();

    public String processedFrame(String modelName, String url) {
        Model model = modelService.get(modelName);

        String html = Unirest.get(url).asString().getBody();

        return modelProcessor.process(model, url, html);
    }
}
