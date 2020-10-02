package net.tislib.ugm.lib.markers;

import net.tislib.ugm.lib.markers.base.Marker;
import net.tislib.ugm.lib.markers.base.MarkerParameter;
import net.tislib.ugm.lib.markers.base.model.MarkerData;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
    public Optional<Page> process(Page page, MarkerData markerData) {
        Map<String, Serializable> parameters = markerData.getParameters();
        Document document = page.getDocument();

        String name = markerData.getName();
        String objectType = (String) parameters.get(PARAM_OBJECT_TYPE);
        String urlCheck = (String) parameters.get(PARAM_URL_CHECK);
        String ref = (String) parameters.get(PARAM_REF);

        Element html = document.getElementsByTag("html").get(0);

        html.attr("ug-page-name", name);

        if (!StringUtils.isBlank(objectType)) {
            html.attr("ug-object-type", objectType);
        }

        if (!StringUtils.isBlank(urlCheck)) {
            Pattern pattern = Pattern.compile(urlCheck);

            Matcher matcher = pattern.matcher(page.getUrl());

            if (!matcher.find()) {
                return Optional.empty();
            }
        }

        if (!StringUtils.isBlank(ref)) {
            Pattern pattern = Pattern.compile(ref);

            Matcher matcher = pattern.matcher(page.getUrl());

            if (matcher.find()) {
                String refValue = matcher.group(matcher.groupCount() - 1);
                html.attr("ug-ref", refValue);
            }
        }

        return Optional.of(page);
    }
}
