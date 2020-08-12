package net.tislib.ugm.ui.helper;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class SingleSelectionHelperTest {

    private static final SingleSelectionHelper singleSelectionHelper = new SingleSelectionHelper();


    public static void main(String[] args) throws IOException {
        Document document = Jsoup.parse(Thread.currentThread().getContextClassLoader().getResourceAsStream("html/page1.html"), "UTF-8", "http://localhost");

        List<String> selections = new ArrayList<>();

        selections.add("#name-overview-widget-layout > tbody > tr:nth-child(1) > td > h1 > span");

        String result = singleSelectionHelper.select(document, selections);
    }

}
