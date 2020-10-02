package net.tislib.ugm.api.service;

import kong.unirest.Unirest;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import net.tislib.ugm.api.component.CacheHelper;
import net.tislib.ugm.api.data.repository.ModelRepository;
import net.tislib.ugm.lib.markers.base.ModelDataExtractor;
import net.tislib.ugm.lib.markers.base.ModelProcessor;
import net.tislib.ugm.lib.markers.base.model.Model;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ModelService {

    private final ModelRepository repository;
    private final ModelProcessor modelProcessor = new ModelProcessor();
    private final CacheHelper cacheHelper;

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

    public Serializable extractData(String name, List<String> url, boolean cache, boolean merge) {
        List<Serializable> list = url.stream().map(item -> extractData(name, item, cache)).collect(Collectors.toList());

        if (list.size() == 1) {
            return list.get(0);
        }

        if (list.size() == 0) {
            return null;
        }

        if (merge) {
            return merge(list);
        } else {
            return (Serializable) list;
        }
    }

    private Serializable merge(List<Serializable> list) {
        Map<String, Serializable> result = new HashMap<>();

        for (Serializable dataObj : list) {
            Map<String, Serializable> data = (Map<String, Serializable>) dataObj;
            String ref = String.valueOf(data.get("_ref"));
            String page = String.valueOf(data.get("_page"));
            result.putIfAbsent(ref, new HashMap<String, Serializable>());

            Map<String, Serializable> mergedData = (Map<String, Serializable>) result.get(ref);
            mergedData.put(page, dataObj);

        }

        return (Serializable) result;
    }

    public Serializable extractData(String name, String url, boolean cache) {
        ModelDataExtractor modelDataExtractor = new ModelDataExtractor();

        Model model = get(name);

        String html = download(url, cache);

        return modelDataExtractor.processDocument(model, url, html);
    }

    private String download(String url, boolean cache) {
        if (cache && cacheHelper.getPageCache().containsKey(url)) {
            return cacheHelper.getPageCache().get(url);
        }

        String html = Unirest.get(url).asString().getBody();

        if (cache) {
            cacheHelper.getPageCache().put(url, html);
        }

        return html;
    }
}
