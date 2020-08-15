package net.tislib.ugm.ui.helper;

import lombok.experimental.UtilityClass;
import org.jsoup.nodes.Element;

import java.util.*;
import java.util.stream.Collectors;

@UtilityClass
public class SelectionUtil {

    public Element commonParent(List<Element> elements) {
        List<List<Element>> paths = elements.stream().map(SelectionUtil::getPath).collect(Collectors.toList());

        int r = 0;
        Element lca = null;
        while (true) {
            Element parent = null;
            for (List<Element> path : paths) {
                Element element = path.get(r);
                if (parent == null) {
                    parent = element;
                }

                if (parent != element) {
                    return lca;
                }
            }

            lca = parent;
            r++;
        }
    }

    private static List<Element> reverse(List<Element> list) {
        List<Element> res = new ArrayList<>(list);
        Collections.reverse(res);

        return res;
    }

    public static List<Element> getPath(Element item) {
        List<Element> res = new ArrayList<>();

        Element parent = item.parent();
        if (parent != null) {
            res.addAll(getPath(parent));
        }
        res.add(item);

        return res;
    }

    public static Element findChild(Element item, Element commonParent) {
        Element parent = item.parent();
        if (parent == commonParent || parent == null) {
            return item;
        }

        return findChild(item.parent(), commonParent);
    }

    public static StringBuilder commonSelector(List<Element> childElements) {
        StringBuilder childSelector = new StringBuilder();

        Set<String> classes = new HashSet<>(childElements.get(0).classNames());
        Set<String> attributes = new HashSet<>(childElements.get(0).attributes().dataset().keySet());
        Set<String> attributesWithValue = new HashSet<>(childElements.get(0).attributes().dataset().entrySet().stream().map(item -> item.getKey() + "=" + item.getValue()).collect(Collectors.toList()));

        for (Element element : childElements) {
            classes.retainAll(element.classNames());
            attributes.retainAll(element.classNames());
            attributesWithValue.retainAll(element.attributes().dataset().entrySet().stream().map(item -> item.getKey() + "=" + item.getValue()).collect(Collectors.toList()));
        }

        if (classes.size() > 0) {
            childSelector.append(".").append(String.join(".", classes));
        }
        childSelector.append(attributes.stream().map(item -> "[" + item + "]").collect(Collectors.joining("")));
        childSelector.append(attributesWithValue.stream().map(item -> "[" + item + "]").collect(Collectors.joining("")));
        return childSelector;
    }
}
