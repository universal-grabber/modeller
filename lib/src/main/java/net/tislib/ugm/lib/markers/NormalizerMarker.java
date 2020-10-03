package net.tislib.ugm.lib.markers;

import net.tislib.ugm.lib.markers.base.Marker;
import net.tislib.ugm.lib.markers.base.MarkerParameter;
import net.tislib.ugm.lib.markers.base.model.MarkerData;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.jsoup.select.Elements;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class NormalizerMarker implements Marker {

    private static final String PARAM_ELEMENT = "element";

    @Override
    public String getName() {
        return "normalize";
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
    public Optional<Page> process(Page page, MarkerData markerData) {
        Map<String, Serializable> parameters = markerData.getParameters();
        Document document = page.getDocument();

        String elementSelector = (String) parameters.get(PARAM_ELEMENT);

        if (!StringUtils.isBlank(elementSelector)) {
            for (Element element : document.select(elementSelector)) {
                wrapText(element);

                normalizeTableSpan(element);
            }
        }

        return Optional.of(page);
    }

    private void normalizeTableSpan(Element element) {
        // fix row spans
        Elements elementsWithRowspan = element.select("td[rowspan]");

        if (elementsWithRowspan.size() > 0) {
            elementsWithRowspan.forEach(this::fixRowSpan);
        }
    }

    private void fixRowSpan(Element element) {
        int rowSpan = Integer.parseInt(element.attr("rowspan"));
        element.removeAttr("rowspan");
        Element tr = element.parent();
        Element nextSibling = tr.nextElementSibling();

        for (int i = 0; i < rowSpan - 1; i++) {
            if (nextSibling != null) {
                nextSibling.prependChild(element.clone());
                nextSibling = nextSibling.nextElementSibling();
            }
        }
    }

    private void wrapText(Element element) {
        for (Node node : new ArrayList<>(element.childNodes())) {
            if (node instanceof TextNode) {
                if (StringUtils.isBlank(((TextNode) node).text())) {
                    continue;
                }

                // check if has not sibling elements
                Element parent = (Element) node.parent();
                if (parent.children().size() == 0) {
                    continue;
                }

                int index = node.siblingIndex();

                Element textElement = new Element("text");
                textElement.appendChild(node);
                element.insertChildren(index, textElement);
            }
        }
    }
}
