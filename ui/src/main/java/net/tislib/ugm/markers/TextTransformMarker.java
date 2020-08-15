package net.tislib.ugm.markers;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class TextTransformMarker implements Marker {

    public static final String PARAM_INPUT_TYPE = "input-type";
    public static final String PARAM_INPUT_SELECT = "input-select";
    public static final String PARAM_TRANSFORMER = "transformer";
    public static final String PARAM_ELEMENT = "element";
    public static final String PARAM_SCRIPT = "script";
    public static final String PARAM_SUBSTITUTION = "substitution";
    public static final String PARAM_OUTPUT_TYPE = "output-type";
    public static final String PARAM_OUTPUT_SELECT = "output-select";

    @Override
    public String getName() {
        return "text-transform";
    }

    @Override
    public List<MarkerParameter> getParameters() {
        List<MarkerParameter> parameters = new ArrayList<>();

        parameters.add(MarkerParameter.builder()
                .caption("Input Type")
                .name(PARAM_INPUT_TYPE)
                .parameterType(MarkerParameter.ParameterType.COMBOBOX)
                .defaultValue(ElemBindType.TEXT)
                .values(ElemBindType.values())
                .build());

        parameters.add(MarkerParameter.builder()
                .caption("Input Select")
                .parameterType(MarkerParameter.ParameterType.TEXT)
                .name(PARAM_INPUT_SELECT)
                .build());

        parameters.add(MarkerParameter.builder()
                .caption("Transformer")
                .name(PARAM_TRANSFORMER)
                .parameterType(MarkerParameter.ParameterType.COMBOBOX)
                .defaultValue(Transformer.REGEX_REPLACE)
                .values(Transformer.values())
                .build());

        parameters.add(MarkerParameter.builder()
                .caption("Element")
                .parameterType(MarkerParameter.ParameterType.INSPECTOR)
                .name(PARAM_ELEMENT)
                .build());

        parameters.add(MarkerParameter.builder()
                .caption("Script")
                .parameterType(MarkerParameter.ParameterType.TEXT)
                .name(PARAM_SCRIPT)
                .build());

        parameters.add(MarkerParameter.builder()
                .caption("SUBSTITUTION")
                .parameterType(MarkerParameter.ParameterType.TEXT)
                .defaultValue("")
                .name(PARAM_SUBSTITUTION)
                .build());

        parameters.add(MarkerParameter.builder()
                .caption("Output Type")
                .name(PARAM_OUTPUT_TYPE)
                .parameterType(MarkerParameter.ParameterType.COMBOBOX)
                .defaultValue(ElemBindType.ATTR)
                .values(ElemBindType.values())
                .build());

        parameters.add(MarkerParameter.builder()
                .caption("Output Select")
                .parameterType(MarkerParameter.ParameterType.TEXT)
                .defaultValue("ug-value")
                .name(PARAM_OUTPUT_SELECT)
                .build());

        return parameters;
    }

    @Override
    public Document process(Document document, Map<String, Serializable> parameters) {
        Transformer transformerType = Transformer.valueOf((String) parameters.get(PARAM_TRANSFORMER));
        Elements elements = document.select(String.valueOf(parameters.get(PARAM_ELEMENT)));

        for (Element element : elements) {
            String input = getInput(element, parameters);

            String result = null;
            switch (transformerType) {
                case REGEX_REPLACE:
                    result = regexReplaceProcess(input, parameters);
                    break;
            }

            if (result != null) {
                saveOutput(element, parameters, result);
            }
        }

        return document;
    }

    private String getInput(Element element, Map<String, Serializable> parameters) {
        ElemBindType inputType = ElemBindType.valueOf(parameters.get(PARAM_INPUT_TYPE).toString());
        switch (inputType) {
            case TEXT:
                return element.text();
            case INNER_HTML:
                return element.html();
            case ATTR:
                return element.attr(String.valueOf(parameters.get(PARAM_INPUT_SELECT)));
            default:
                throw new IllegalArgumentException();
        }
    }

    private void saveOutput(Element element, Map<String, Serializable> parameters, String value) {
        ElemBindType outputType = ElemBindType.valueOf(parameters.get(PARAM_OUTPUT_TYPE).toString());
        switch (outputType) {
            case TEXT:
                element.text(value);
                break;
            case INNER_HTML:
                element.html(value);
                break;
            case ATTR:
                element.attr(String.valueOf(parameters.get(PARAM_OUTPUT_SELECT)), value);
                break;
            default:
                throw new IllegalArgumentException();
        }
    }

    private String regexReplaceProcess(String data, Map<String, Serializable> parameters) {
        String script = (String) parameters.get(PARAM_SCRIPT);
        String substitution = (String) parameters.get(PARAM_SUBSTITUTION);

        return data.replaceAll(script, substitution);
    }

    public enum ElemBindType {
        TEXT, INNER_HTML, ATTR
    }

    public enum Transformer {
        REGEX_MATCH, REGEX_REPLACE, REGEX_SPLIT, JSON, JAVASCRIPT
    }
}
