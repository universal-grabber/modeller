package net.tislib.ugm.lib.markers;

import net.tislib.ugm.lib.markers.base.Marker;
import net.tislib.ugm.lib.markers.base.MarkerParameter;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ElementToElementTransformMarker implements Marker {


    private static final String PARAM_PARENT_SELECTOR = "parent_selector";
    private static final String PARAM_PARENT_ATTR = "parent_attr";
    private static final String PARAM_CHILD_SELECTOR = "child_selector";

    @Override
    public String getName() {
        return "element-to-element-transform";
    }

    @Override
    public List<MarkerParameter> getParameters() {
        List<MarkerParameter> parameters = new ArrayList<>();

        parameters.add(MarkerParameter.builder()
                .caption("Parent selector")
                .name(PARAM_PARENT_SELECTOR)
                .parameterType(MarkerParameter.ParameterType.INSPECTOR)
                .build());

        parameters.add(MarkerParameter.builder()
                .caption("Parent attribute")
                .name(PARAM_PARENT_ATTR)
                .parameterType(MarkerParameter.ParameterType.TEXT)
                .build());

        parameters.add(MarkerParameter.builder()
                .caption("Child selector")
                .name(PARAM_CHILD_SELECTOR)
                .parameterType(MarkerParameter.ParameterType.INSPECTOR)
                .build());

        return parameters;
    }

    @Override
    public Document process(Document document, Map<String, Serializable> parameters) {
        String parentSelector = (String) parameters.get(PARAM_PARENT_SELECTOR);
        String parentAttr = (String) parameters.get(PARAM_PARENT_ATTR);
        String childSelector = (String) parameters.get(PARAM_CHILD_SELECTOR);

        if (!StringUtils.isBlank(parentSelector) &&
                !StringUtils.isBlank(childSelector) &&
                !StringUtils.isBlank(parentAttr)
        ) {
            Elements parentElements = document.select(parentSelector);
            Elements childElements = document.select(childSelector);

            for (int i = 0; i < Math.min(parentElements.size(), childElements.size()); i++) {
                childElements.get(i).attr(parentAttr, parentElements.get(i).text());
            }
        }

        return document;
    }
}
