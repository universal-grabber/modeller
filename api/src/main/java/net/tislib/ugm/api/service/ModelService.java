package net.tislib.ugm.api.service;

import kong.unirest.Unirest;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import net.tislib.ugm.api.component.CacheHelper;
import net.tislib.ugm.api.data.repository.ModelRepository;
import net.tislib.ugm.data.Schema;
import net.tislib.ugm.lib.markers.base.ModelDataExtractor;
import net.tislib.ugm.lib.markers.base.ModelDataSchemaExtractor;
import net.tislib.ugm.lib.markers.base.ModelProcessor;
import net.tislib.ugm.lib.markers.base.model.MarkerData;
import net.tislib.ugm.lib.markers.base.model.Model;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ModelService {

    private final ModelRepository repository;
    private final ModelProcessor modelProcessor = new ModelProcessor();
    private final CacheHelper cacheHelper;
    private final SchemaService schemaService;

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
        existingModel.setSchema(model.getSchema());
        existingModel.setUrlCheck(model.getUrlCheck());
        existingModel.setRef(model.getRef());
        existingModel.setObjectType(model.getObjectType());

        repository.save(existingModel);

        return get(name);
    }

    public Serializable extractSingleData(String name, List<String> url, boolean cache, boolean merge) {
        List<Serializable> list = url.stream().map(item -> extractDataSingle(name, item, cache)).collect(Collectors.toList());

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

    public Serializable extractDataSingle(String name, String url, boolean cache) {


        Model model = get(name);

        String html = download(url, cache);

        if (model.getSchema() != null) {
            ModelDataSchemaExtractor modelDataSchemaExtractor = new ModelDataSchemaExtractor();

            Schema schema = schemaService.get(model.getSchema());

            return modelDataSchemaExtractor.processDocument(model, schema, url, html);
        } else {
            ModelDataExtractor modelDataExtractor = new ModelDataExtractor();
            return modelDataExtractor.processDocument(model, url, html);
        }
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

    public Model split(String name) {
        Model existingModel = get(name);
        existingModel.setId(null);
        existingModel.setName(name + "_old_" + Math.random());
        repository.save(existingModel);

        List<MarkerData> commonMarkers = new ArrayList<>();
        List<MarkerData> pageMarkers = new ArrayList<>();

        existingModel.getMarkers().forEach(item -> {
            if (!item.getType().equals("page-marker") && StringUtils.isBlank(item.getParentName())) {
                commonMarkers.add(item);
            } else if (item.getType().equals("page-marker")) {
                pageMarkers.add(item);
            }
        });

        pageMarkers.forEach(item -> {
            Model model = new Model();
            model.setName(name + "/" + item.getName());
            model.setExamples(existingModel.getExamples());

            model.setObjectType((String) item.getParameters().get("objectType"));
            model.setUrlCheck((String) item.getParameters().get("url-check"));
            model.setRef((String) item.getParameters().get("ref"));

            List<MarkerData> markers = new ArrayList<>(commonMarkers);

            existingModel.getMarkers().forEach(markerData -> {
                if (StringUtils.equals(markerData.getParentName(), item.getName())) {
                    markerData.setParentName(null);
                    markers.add(markerData);
                }
            });

            model.setMarkers(markers);
            repository.save(model);
        });

        return existingModel;
    }
}
