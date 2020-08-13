package net.tislib.ugm.markers;

import org.apache.commons.lang3.StringUtils;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static net.tislib.ugm.markers.MarkerParameter.ParameterType.INSPECTOR;
import static net.tislib.ugm.markers.MarkerParameter.ParameterType.TEXT;

public class FieldSelectorMarker implements Marker {

    public static final String PARAM_NAME = "name";
    public static final String PARAM_SELECTOR = "selector";

    @Override
    public String getName() {
        return "field-selector";
    }

    @Override
    public List<MarkerParameter> getParameters() {
        List<MarkerParameter> parameters = new ArrayList<>();

        MarkerParameter nameParameter = new MarkerParameter();
        nameParameter.setName(PARAM_NAME);
        nameParameter.setCaption("Name");
        nameParameter.setParameterType(TEXT);

        MarkerParameter inspectorParameter = new MarkerParameter();
        inspectorParameter.setName(PARAM_SELECTOR);
        inspectorParameter.setCaption("Selector");
        inspectorParameter.setParameterType(INSPECTOR);

        parameters.add(nameParameter);
        parameters.add(inspectorParameter);

        return parameters;
    }

    @Override
    public Document process(Document document, Map<String, Serializable> parameters) {
        String fieldName = (String) parameters.get(PARAM_NAME);
        String selector = (String) parameters.get("selector");

        if (StringUtils.isBlank(selector)) {
            return document;
        }

        Elements selectedElements = document.select(selector);

        selectedElements.forEach(element -> this.applyParameter(element, fieldName));

        return document;
    }

    private void applyParameter(Element element, String fieldName) {
        element.attr("ug-field", fieldName);
    }
}
