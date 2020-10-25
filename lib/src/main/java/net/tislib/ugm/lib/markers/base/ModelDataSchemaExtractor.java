package net.tislib.ugm.lib.markers.base;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import net.tislib.ugm.data.HasProperties;
import net.tislib.ugm.data.Schema;
import net.tislib.ugm.data.SchemaProperty;
import net.tislib.ugm.data.property.ArrayProperty;
import net.tislib.ugm.data.property.NumberProperty;
import net.tislib.ugm.data.property.ObjectProperty;
import net.tislib.ugm.data.property.StringProperty;
import net.tislib.ugm.data.structure.Record;
import net.tislib.ugm.lib.markers.base.model.Model;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@RequiredArgsConstructor
//@Log4j2
public class ModelDataSchemaExtractor {

    private final ModelProcessor modelProcessor = new ModelProcessor();

    public Record processDocument(Model model, Schema schema, String url, String html) {
        Document processedDocument = modelProcessor.processDocument(model, url, html);

        Map<String, Object> data = extract(schema, processedDocument);

        Record record = new Record();

        record.setData(data);
        record.setSource(model.getSource());
        record.setTags(new HashSet<>());
        record.setRef(extractRef(model, url, html));
        record.setObjectType(model.getObjectType());
        record.setSourceUrl(url);
        record.setSchema(model.getSchema());
        record.setMeta(extractMeta(processedDocument));

        record.setId(UUID.nameUUIDFromBytes(url.getBytes()));

        if (data.get("name") != null) {
            record.setName((String) data.get("name"));
        } else if (record.getMeta().get("title") != null) {
            record.setName(record.getMeta().get("title"));
        }

        if (data.get("description") != null) {
            record.setDescription((String) data.get("description"));
        } else if (record.getMeta().get("description") != null) {
            record.setDescription(record.getMeta().get("description"));
        }

        return record;
    }

    private Map<String, String> extractMeta(Document document) {
        Map<String, String> meta = new HashMap<>();

        Elements metaFields = document.select("meta[ug-field]");

        metaFields.forEach(item -> {
            String key = item.attr("ug-field");
            if (key.startsWith("meta.")) {
                key = key.substring(5);
            }

            meta.put(key, item.attr("ug-value"));
        });

        return meta;
    }

    private String extractRef(Model model, String url, String html) {
        String ref = model.getRef();

        if (!StringUtils.isBlank(ref)) {
            Pattern pattern = Pattern.compile(ref);

            Matcher matcher = pattern.matcher(url);

            if (matcher.find()) {
                return matcher.group(matcher.groupCount() - 1);
            }
        }

        return null;
    }

    private UUID computeRefId(String source, String type, String ref) {
        String token = String.format("%s|%s|%s", source, type, ref);
        return UUID.nameUUIDFromBytes(token.getBytes());
    }

    private Map<String, Object> extract(HasProperties schema, Element parent) {
        Map<String, Object> data = new HashMap<>();

        schema.getProperties().forEach((key, property) -> {
            Object value = locatePropertyValue(parent, key, property);

            if (value != null) {
                data.put(key, (Serializable) value);
            }
        });

        return data;
    }

    private Object locatePropertyValue(Element parent, String key, SchemaProperty property) {
        Elements fields = parent.select("[ug-field=\"" + key + "\"]");

        return locatePropertyValue(property, fields);
    }

    private Object locatePropertyValue(SchemaProperty property, List<Element> fields) {
        if (property instanceof ArrayProperty) {
            ArrayProperty arrayProperty = (ArrayProperty) property;
            SchemaProperty itemsProperty = arrayProperty.getItems();

            List<Object> result = new ArrayList<>();

            fields.forEach(field -> {
                Object val = locatePropertyValue(itemsProperty, field);
                if (val != null) {
                    result.add(val);
                }
            });

            return result;
        } else {
            if (fields.size() > 0) {
                return locatePropertyValue(property, fields.get(0));
            }
        }
        return null;
    }

    private Object locatePropertyValue(SchemaProperty property, Element field) {
        if (property instanceof StringProperty) {
            return getValue(field);
        } else if (property instanceof NumberProperty) {
            Serializable val = getValue(field);
            if (val == null) {
                return null;
            }
            String valStr = String.valueOf(val);

            String valStrNum = valStr.replaceAll("[\\D.]+", "");

            if (valStrNum.length() == 0) {
                return null;
            } else {
                return new BigDecimal(valStrNum);
            }
        } else if (property instanceof ArrayProperty) {
            return locatePropertyValue(property, Collections.singletonList(field));
        } else if (property instanceof HasProperties) {
            ObjectProperty objectProperty = (ObjectProperty) property;
            return extract(objectProperty, field);
        }
        return null;
    }

    private Serializable getValue(Element element) {
        if (element.hasAttr("ug-value")) {
            return element.attr("ug-value");
        } else {
            return element.text();
        }
    }
}
