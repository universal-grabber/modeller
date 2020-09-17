package net.tislib.ugm.lib.markers;

import net.tislib.ugm.lib.markers.base.Marker;
import net.tislib.ugm.lib.markers.base.MarkerParameter;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class TextWrapMarker implements Marker {


    private static final String PARAM_ELEMENT = "element";

    @Override
    public String getName() {
        return "text-wrap";
    }

    @Override
    public List<MarkerParameter> getParameters() {
        List<MarkerParameter> parameters = new ArrayList<>();

        parameters.add(MarkerParameter.builder()
                .caption("Element")
                .name(PARAM_ELEMENT)
                .parameterType(MarkerParameter.ParameterType.INSPECTOR)
                .build());

        return parameters;
    }

    @Override
    public Document process(Document document, Map<String, Serializable> parameters) {
        String elementSelector = (String) parameters.get(PARAM_ELEMENT);

        if (!StringUtils.isBlank(elementSelector)) {
            for (Element element : document.select(elementSelector)) {
                element.html(wrapElement(element).html());
            }
        }

        return document;
    }

    private Element wrapElement(Element element) {
        Element newElement = element.clone();
        newElement.html("");

        for (Node node : new ArrayList<>(element.childNodes())) {
            if (node instanceof TextNode) {
                if (StringUtils.isBlank(((TextNode) node).text())) {
                    continue;
                }
                Element textElement = new Element("text");
                textElement.text(((TextNode) node).text());
                newElement.appendChild(textElement);
            } else {
                newElement.appendChild(node);
            }
        }

        return newElement;
    }
}
