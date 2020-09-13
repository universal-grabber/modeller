package net.tislib.ugm.ui.helper;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.util.ArrayList;
import java.util.List;

public class CommonParentSelectionAlgorithm implements SelectionAlgorithm {
    @Override
    public String select(Document document, List<String> elements) {
        List<Element> selectedElements = new ArrayList<>();

        elements.forEach(element -> selectedElements.addAll(document.select(element)));

        Element commonParent = SelectionUtil.commonParent(selectedElements);

        return commonParent.cssSelector();
    }
}
