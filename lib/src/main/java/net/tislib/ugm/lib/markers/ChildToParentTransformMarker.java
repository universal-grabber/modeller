package net.tislib.ugm.lib.markers;

import com.github.slugify.Slugify;
import net.tislib.ugm.lib.markers.base.Marker;
import net.tislib.ugm.lib.markers.base.MarkerParameter;
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

public class ChildToParentTransformMarker implements Marker {


    private static final String PARAM_PARENT_SELECTOR = "parent_selector";
    private static final String PARAM_PARENT_ATTR = "parent_attr";
    private static final String PARAM_CHILD_SELECTOR = "child_selector";

    private static final Slugify slg = new Slugify();

    @Override
    public String getName() {
        return "child-to-parent-transform";
    }

    @Override
    public List<MarkerParameter> getParameters() {
        List<MarkerParameter> parameters = new ArrayList<>();

        parameters.add(MarkerParameter.builder()
                .caption("Child selector(from parent)")
                .name(PARAM_CHILD_SELECTOR)
                .parameterType(MarkerParameter.ParameterType.INSPECTOR)
                .build());

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

        return parameters;
    }

    @Override
    public Optional<Page> process(Page page, MarkerData markerData) {
        Map<String, Serializable> parameters = markerData.getParameters();
        Document document = page.getDocument();

        String parentSelector = (String) parameters.get(PARAM_PARENT_SELECTOR);
        String parentAttr = (String) parameters.get(PARAM_PARENT_ATTR);
        String childSelector = (String) parameters.get(PARAM_CHILD_SELECTOR);

        if (!StringUtils.isBlank(parentSelector) &&
                !StringUtils.isBlank(childSelector) &&
                !StringUtils.isBlank(parentAttr)
        ) {
            Elements parentElements = document.select(parentSelector);

            parentElements.forEach(parent -> {
                Optional<Element> child = parent.select(childSelector).stream().findFirst();

                child.ifPresent(element -> parent.attr(parentAttr, slg.slugify(element.text())));

            });
        }

        return Optional.of(page);
    }
}
