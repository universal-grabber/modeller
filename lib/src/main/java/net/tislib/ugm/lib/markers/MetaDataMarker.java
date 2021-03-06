package net.tislib.ugm.lib.markers;

import net.tislib.ugm.lib.markers.base.Marker;
import net.tislib.ugm.lib.markers.base.MarkerParameter;
import net.tislib.ugm.lib.markers.base.model.MarkerData;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class MetaDataMarker implements Marker {

    public static final String PARAM_META_TAGS = "meta-tags";

    @Override
    public String getName() {
        return "meta-data";
    }

    @Override
    public List<MarkerParameter> getParameters() {
        List<MarkerParameter> parameters = new ArrayList<>();

        parameters.add(MarkerParameter.builder()
                .caption("Extract Meta Tags")
                .name(PARAM_META_TAGS)
                .parameterType(MarkerParameter.ParameterType.CHECKBOX)
                .defaultValue(true)
                .build());

        return parameters;
    }

    @Override
    public Optional<Page> process(Page page, MarkerData markerData) {
        Map<String, Serializable> parameters = markerData.getParameters();
        Document document = page.getDocument();

        boolean metaTags = Boolean.parseBoolean(String.valueOf(parameters.get(PARAM_META_TAGS)));

        if (metaTags) {
            processMetaTags(document);
        }

        return Optional.of(page);
    }

    private void processMetaTags(Document document) {
        for (Element element : document.select("meta")) {
            String key = element.attr("name");

            if (key == null) {
                key = element.attr("property");
            }

            String value = element.attr("content");

            if (!StringUtils.isBlank(key) && !StringUtils.isBlank(value)) {
                element.attr("ug-field", "meta." + key);
                element.attr("ug-value", value);
            }
        }
    }
}
