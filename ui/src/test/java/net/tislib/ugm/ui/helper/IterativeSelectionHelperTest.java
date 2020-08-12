package net.tislib.ugm.ui.helper;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

class IterativeSelectionHelperTest {

    private final static IterativeSelectionAlgorithm ITERATIVE_SELECTION_ALGORITHM = new IterativeSelectionAlgorithm();


    public static void main(String[] args) throws IOException {
        Document document = Jsoup.parse(Thread.currentThread().getContextClassLoader().getResourceAsStream("html/page1.html"), "UTF-8", "http://localhost");

        List<String> selections = new ArrayList<>();

        selections.add("#jumpto > a:nth-child(1)");
        selections.add("#jumpto > a:nth-child(3)");
        selections.add("#jumpto > a:nth-child(5)");

        String result = ITERATIVE_SELECTION_ALGORITHM.select(document, selections);
    }

}
