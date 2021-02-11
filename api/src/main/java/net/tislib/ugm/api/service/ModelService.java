package net.tislib.ugm.api.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import kong.unirest.GenericType;
import kong.unirest.Unirest;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import net.tislib.ugm.api.component.CacheHelper;
import net.tislib.ugm.api.data.ProcessData;
import net.tislib.ugm.api.data.marker.Marker;
import net.tislib.ugm.api.data.model.MarkerData;
import net.tislib.ugm.api.data.model.Model;
import net.tislib.ugm.api.data.repository.ModelRepository;
import net.tislib.ugm.data.Schema;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ModelService {

    private final ModelRepository repository;
    private final CacheHelper cacheHelper;
    private final SchemaService schemaService;
    private final MarkerService markerService;
    private final ObjectMapper mapper;

    @Value("${service.modeller-processor}")
    private String modelProcessorService;

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

        materialize(model);

        existingModel.setExamples(model.getExamples());
        existingModel.setMarkers(model.getMarkers());
        existingModel.setSchema(model.getSchema());
        existingModel.setUrlCheck(model.getUrlCheck());
        existingModel.setRef(model.getRef());
        existingModel.setObjectType(model.getObjectType());
        existingModel.setSource(model.getSource());

        repository.save(existingModel);

        return get(name);
    }

    public Model materialize(Model model) {
        for (MarkerData markerData : model.getMarkers()) {
            Marker marker = markerService.get(markerData.getType());
            materializeParameters(marker, markerData.getParameters());
        }
        return model;
    }

    public void materializeParameters(Marker marker, Map<String, Serializable> parameters) {
        marker.getParameters().forEach(item -> {
            if (!parameters.containsKey(item.getName()) && item.getDefaultValue() != null) {
                parameters.put(item.getName(), item.getDefaultValue());
            }
        });
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

    @SneakyThrows
    public Serializable extractDataSingle(String name, String url, boolean cache) {
        Model model = get(name);

        String html = download(url, cache);

        if (model.getSchema() == null) {
            throw new RuntimeException("schema must be not null");
        }

        Schema schema = schemaService.get(model.getSchema());

        ProcessData processData = new ProcessData();
        processData.setModel(model);
        processData.setSchema(schema);
        processData.setAdditionalModels(new HashSet<>(repository.findAll()));
        processData.setUrl(url);
        processData.setHtml(html);

        String requestBody = mapper.writeValueAsString(processData);

        String jsonBody = Unirest.post(modelProcessorService + "/api/1.0/parse")
                .header("Content-type", "application/json")
                .body(requestBody).asString().getBody();

        return (Serializable) mapper.readValue(jsonBody, Object.class);

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

    public Model create(Model model) {
        model = repository.save(model);
        return model;
    }

    @SneakyThrows
    public String processedFrame(String name, String url) {
        Model model = get(name);

        String html = download(url, false);

        if (model.getSchema() == null) {
            throw new RuntimeException("schema must be not null");
        }

        Schema schema = schemaService.get(model.getSchema());

        ProcessData processData = new ProcessData();
        processData.setModel(model);
        processData.setSchema(schema);
        processData.setAdditionalModels(new HashSet<>(repository.findAll()));
        processData.setUrl(url);
        processData.setHtml(html);

        String requestBody = mapper.writeValueAsString(processData);

        return Unirest.post(modelProcessorService + "/api/1.0/process")
                .header("Content-type", "application/json")
                .body(requestBody).asString().getBody();
    }
}
