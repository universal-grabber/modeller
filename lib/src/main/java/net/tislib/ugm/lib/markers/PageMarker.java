package net.tislib.ugm.lib.markers;

import net.tislib.ugm.lib.markers.base.Marker;
import net.tislib.ugm.lib.markers.base.MarkerParameter;
import org.jsoup.nodes.Document;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class PageMarker implements Marker {

    public static final String PARAM_OBJECT_TYPE = "objectType";
    public static final String PARAM_NAME = "name";
    public static final String PARAM_URL_CHECK = "url-check";
    public static final String PARAM_REF = "ref";

    @Override
    public String getName() {
        return "page-marker";
    }

    @Override
    public List<MarkerParameter> getParameters() {
        return Arrays.asList(
                MarkerParameter.builder()
                        .name(PARAM_OBJECT_TYPE)
                        .caption("Object type")
                        .parameterType(MarkerParameter.ParameterType.TEXT)
                        .required(true)
                        .build(),
                MarkerParameter.builder()
                        .name(PARAM_NAME)
                        .caption("Name")
                        .parameterType(MarkerParameter.ParameterType.TEXT)
                        .required(true)
                        .build(),
                MarkerParameter.builder()
                        .name(PARAM_URL_CHECK)
                        .caption("Url Check")
                        .parameterType(MarkerParameter.ParameterType.REGEXP)
                        .required(false)
                        .build(),
                MarkerParameter.builder()
                        .name(PARAM_REF)
                        .caption("Reference")
                        .parameterType(MarkerParameter.ParameterType.REGEX_SUB)
                        .required(false)
                        .build()
        );
    }

    @Override
    public Document process(Document document, Map<String, Serializable> parameters) {
        return document;
    }
}
