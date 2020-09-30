package net.tislib.ugm.ui.helper;


import org.jsoup.nodes.Document;

import java.util.List;

public interface SelectionAlgorithm {
    String select(Document document, List<String> elements);
}
