package net.tislib.ugm.ui.helper;

import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.util.List;

public class IterativeSelectionHelper {

    public String select(Document document, List<String> elements) {
        String selector = elements.get(0);
        Elements selected = document.select(selector);

        return selected.toString();
    }

}
