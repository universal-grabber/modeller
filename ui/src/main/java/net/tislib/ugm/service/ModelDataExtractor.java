package net.tislib.ugm.service;

import kong.unirest.Unirest;
import lombok.RequiredArgsConstructor;
import net.tislib.ugm.model.Example;
import net.tislib.ugm.model.Model;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ModelDataExtractor {

    private final ModelProcessor modelProcessor;

    public Serializable processDocument(Model model, Integer exampleId) {
        Example example = model.getExamples().stream().filter(item -> item.getId().equals(exampleId)).findAny().get();

        String html = Unirest.get(example.getUrl().toString()).asString().getBody();

        return processDocument(model, html);
    }

    public Serializable processDocument(Model model, String html) {
        Document processedDocument = modelProcessor.processDocument(model, html);

        processedDocument.select("ug-field");

        Serializable data = (Serializable) extract(processedDocument);

        return data;
    }

    private Map<String, Serializable> extract(Element parent) {
        Map<String, Serializable> childrenData = new HashMap<>();
        for (Element element : parent.children()) {
            childrenData = merge(childrenData, extract(element));
        }

        Map<String, Serializable> data = new HashMap<>();
        if (parent.hasAttr("ug-field")) {
            String key = parent.attr("ug-field");
            if (childrenData.size() != 0) {
                data.put(key, (Serializable) childrenData);
            } else {
                data.put(key, getValue(parent));
            }
        }

        if (data.size() == 0) {
            return childrenData;
        }

        return data;
    }

    private Serializable getValue(Element parent) {
        if (parent.hasAttr("ug-value")) {
            return parent.attr("ug-value");
        } else {
            return parent.text();
        }
    }

    private Map<String, Serializable> merge(Map<String, Serializable> data, Map<String, Serializable> extracted) {
        Map<String, Serializable> newData = new HashMap<>(data);

        extracted.forEach((key, value) -> {
            if (newData.containsKey(key)) {
                if (newData.get(key) instanceof List) {
                    List list = (List) newData.get(key);
                    list.add(value);
                } else {
                    List list = new ArrayList();
                    list.add(newData.get(key));
                    list.add(value);
                    newData.put(key, (Serializable) list);
                }
            } else {
                newData.put(key, value);
            }
        });

        return newData;
    }

}
