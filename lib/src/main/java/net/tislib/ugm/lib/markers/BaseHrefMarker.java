package net.tislib.ugm.lib.markers;

import net.tislib.ugm.lib.markers.base.Marker;
import net.tislib.ugm.lib.markers.base.MarkerParameter;
import net.tislib.ugm.lib.markers.base.model.MarkerData;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class BaseHrefMarker implements Marker {

    public static final String PARAM_BASE_HREF = "baseHref";

    @Override
    public String getName() {
        return "page-marker";
    }

    @Override
    public List<MarkerParameter> getParameters() {
        return Collections.singletonList(
                MarkerParameter.builder()
                        .name(PARAM_BASE_HREF)
                        .caption("Object type")
                        .parameterType(MarkerParameter.ParameterType.TEXT)
                        .required(true)
                        .build()
        );
    }

    @Override
    public Optional<Page> process(Page page, MarkerData markerData) {
        Map<String, Serializable> parameters = markerData.getParameters();
        Document document = page.getDocument();

        String baseHref = (String) parameters.get(PARAM_BASE_HREF);

        Element baseElem = document.head().appendElement("base");

        baseElem.attr("href", baseHref);

        return Optional.of(page);
    }
}
