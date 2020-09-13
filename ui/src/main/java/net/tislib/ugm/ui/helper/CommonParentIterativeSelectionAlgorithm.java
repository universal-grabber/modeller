package net.tislib.ugm.ui.helper;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class CommonParentIterativeSelectionAlgorithm implements SelectionAlgorithm {
    @Override
    public String select(Document document, List<String> elements) {
        List<Element> selectedElements = new ArrayList<>();

        elements.forEach(element -> selectedElements.addAll(document.select(element)));

        Element commonParent = SelectionUtil.commonParent(selectedElements);

        List<Element> childElements = selectedElements.stream().map(item -> SelectionUtil.findChild(item, commonParent)).collect(Collectors.toList());

        String parentSelector = commonParent.cssSelector();

        StringBuilder childSelector = SelectionUtil.commonSelector(childElements);

        return parentSelector + " > " + childSelector;
    }
}
