package net.tislib.ugm.lib.markers.base;

import lombok.RequiredArgsConstructor;
import net.tislib.ugm.lib.markers.base.model.Model;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class ModelDataExtractor {

    private final ModelProcessor modelProcessor = new ModelProcessor();

    public Serializable processDocument(Model model, String html) {
        Document processedDocument = modelProcessor.processDocument(model, html);

        processedDocument.select("ug-field");

        Map<String, Serializable> data = extract(processedDocument);

        data = fixExtracted(data);

        return (Serializable) data;
    }

    private Serializable fixExtracted(Serializable data) {
        if (data instanceof Map) {
            return (Serializable) fixExtracted((Map) data);
        } else if (data instanceof List) {
            return (Serializable) fixExtracted((List) data);
        } else {
            return data;
        }
    }

    private List<Serializable> fixExtracted(List<Serializable> data) {
        return data.stream().map(this::fixExtracted).collect(Collectors.toList());
    }

    private Map<String, Serializable> fixExtracted(Map<String, Serializable> data) {
        Map<String, Serializable> newData = new HashMap<>();

        data.forEach((key, value) -> {
            if (key.contains(".")) {
                Map<String, Serializable> a = newData;
                Map<String, Serializable> val = null;
                while (key.contains(".")) {
                    String leftKey = key.substring(0, key.indexOf("."));
                    key = key.substring(key.indexOf(".") + 1);
                    val = new HashMap<>();

                    if (a.containsKey(leftKey)) {
                        val = (Map<String, Serializable>) a.get(leftKey);
                    } else {
                        a.put(leftKey, (Serializable) val);
                    }
                }

                val.put(key, value);
            } else {
                newData.put(key, fixExtracted(data.get(key)));
            }
        });

        return newData;
    }


    private Map<String, Serializable> extract(Element parent) {
        Map<String, Serializable> childrenData = new HashMap<>();
        for (Element element : parent.children()) {
            childrenData = merge(childrenData, extract(element));
        }

        Map<String, Serializable> data = new HashMap<>();
        if (parent.hasAttr("ug-field")) {
            String key = parent.attr("ug-field");
            Serializable value;
            if (childrenData.size() != 0) {
                value = (Serializable) childrenData;
            } else {
                value = getValue(parent);
            }

            data.put(key, value);
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
