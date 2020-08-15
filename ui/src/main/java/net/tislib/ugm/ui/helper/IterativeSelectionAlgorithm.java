package net.tislib.ugm.ui.helper;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class IterativeSelectionAlgorithm implements SelectionAlgorithm {

    @Override
    public String select(Document document, List<String> elements) {
        List<Element> selectedElements = new ArrayList<>();

        elements.forEach(element -> {
            selectedElements.addAll(document.select(element));
        });

        List<List<Element>> paths = selectedElements.stream().map(SelectionUtil::getPath).collect(Collectors.toList());

        int r = 0;
        Element lca = null;

        lcaLoop:
        while (true) {
            Element parent = null;
            for (List<Element> path : paths) {
                Element element = path.get(r);
                if (parent == null) {
                    parent = element;
                }

                if (parent != element) {
                    break lcaLoop;
                }
            }

            lca = parent;
            r++;
        }

        StringBuilder selector = new StringBuilder();

        selector.append(lca.cssSelector());

        while (true) {
            if (paths.get(0).size() == r + 1) {
                break;
            }
            int finalR = r;
            List<Element> levelElements = paths.stream().map(item -> item.get(finalR)).collect(Collectors.toList());

            selector.append(" > ").append(SelectionUtil.commonSelector(levelElements));
            r++;
        }

        return selector.toString();
    }

}
