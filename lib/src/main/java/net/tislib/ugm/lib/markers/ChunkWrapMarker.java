package net.tislib.ugm.lib.markers;

import net.tislib.ugm.lib.markers.base.Marker;
import net.tislib.ugm.lib.markers.base.MarkerParameter;
import net.tislib.ugm.lib.markers.base.model.MarkerData;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.select.Elements;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class ChunkWrapMarker implements Marker {


    private static final String PARAM_ELEMENT = "element";
    private static final String PARAM_CHUNK_SIZE = "chunkSize";

    @Override
    public String getName() {
        return "chunk-wrap";
    }

    @Override
    public List<MarkerParameter> getParameters() {
        List<MarkerParameter> parameters = new ArrayList<>();

        parameters.add(MarkerParameter.builder()
                .caption("Element")
                .name(PARAM_ELEMENT)
                .parameterType(MarkerParameter.ParameterType.INSPECTOR)
                .build());

        parameters.add(MarkerParameter.builder()
                .caption("Chunk size")
                .defaultValue(2)
                .required(true)
                .name(PARAM_CHUNK_SIZE)
                .parameterType(MarkerParameter.ParameterType.NUMBER)
                .build());

        return parameters;
    }

    @Override
    public Optional<Page> process(Page page, MarkerData markerData) {
        Map<String, Serializable> parameters = markerData.getParameters();
        Document document = page.getDocument();

        String elementSelector = (String) parameters.get(PARAM_ELEMENT);
        int chunkSize = Integer.parseInt(String.valueOf(parameters.get(PARAM_CHUNK_SIZE)));

        Element parent = document.selectFirst(elementSelector);

        List<Element> children = new ArrayList<>(parent.children());
        children.forEach(Node::remove);

        Element container = null;

        for (int i = 0; i < children.size(); i++) {
            if (i % chunkSize == 0) {
                if (container != null) {
                    parent.appendChild(container);
                }
                container = document.createElement("div");
            }

            Element element = children.get(i);
            container.appendChild(element);
        }
        parent.appendChild(container);


        return Optional.of(page);
    }
}
