package net.tislib.ugm.lib.markers;

import net.tislib.ugm.lib.markers.base.Marker;
import net.tislib.ugm.lib.markers.base.MarkerParameter;
import net.tislib.ugm.lib.markers.base.OutputForm;
import net.tislib.ugm.lib.markers.base.model.MarkerData;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static net.tislib.ugm.lib.markers.base.MarkerParameter.ParameterType.*;

public class FieldSelectorMarker implements Marker {

    public static final String PARAM_NAME = "name";
    public static final String PARAM_SELECTOR = "selector";
    public static final String PARAM_OUTPUT_FORM = "outputForm";
    public static final String PARAM_OUTPUT_TYPE = "outputType";

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

        parameters.add(MarkerParameter.builder()
                .name(PARAM_OUTPUT_FORM)
                .caption("Output form")
                .defaultValue("SINGULAR")
                .parameterType(COMBOBOX)
                .required(true)
                .values(new Serializable[]{
                        OutputForm.SINGLE.name(),
                        OutputForm.OBJECT.name()
                })
                .build());

        parameters.add(MarkerParameter.builder()
                .name(PARAM_OUTPUT_TYPE)
                .caption("Output type")
                .defaultValue("text")
                .parameterType(TEXT)
                .required(true)
                .build());

        return parameters;
    }

    @Override
    public Optional<Page> process(Page page, MarkerData markerData) {
        Map<String, Serializable> parameters = markerData.getParameters();
        Document document = page.getDocument();

        String selector = (String) parameters.get("selector");

        if (StringUtils.isBlank(selector)) {
            return Optional.empty();
        }

        Elements selectedElements = document.select(selector);

        selectedElements.forEach(element -> this.applyParameter(element, markerData));

        return Optional.of(page);
    }

    private void applyParameter(Element element, MarkerData markerData) {
        Map<String, Serializable> parameters = markerData.getParameters();

        String fieldName = (String) parameters.get(PARAM_NAME);
        String paramOutputForm = (String) parameters.get(PARAM_OUTPUT_FORM);
        String outputType = (String) parameters.get(PARAM_OUTPUT_TYPE);
        element.attr("ug-field", fieldName);
        element.attr("ug-marker", markerData.getName());
        element.attr("ug-form", paramOutputForm);

        applyValueIf(element, outputType);
    }

    private void applyValueIf(Element element, String outputType) {
        String value = null;
        if (outputType.equals("img")) {
            value = element.attr("src");
        }
        if (outputType.equals("text")) {
            value = element.text();
        }
        if (outputType.equals("html")) {
            value = element.html();
        }
        if (outputType.equals("outerHtml")) {
            value = element.outerHtml();
        }
        if (outputType.startsWith("attr:")) {
            value = element.attr(outputType.substring(5));
        }

        if (value != null) {
            element.attr("ug-value", value);
        }
    }
}
