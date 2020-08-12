package net.tislib.ugm.ui.helper;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.List;

public class SingleSelectionHelper {

    public String select(Document document, List<String> elements) {
        String selector = elements.get(0);
        Element selected = document.selectFirst(selector);

        return selected.cssSelector();
    }

}
