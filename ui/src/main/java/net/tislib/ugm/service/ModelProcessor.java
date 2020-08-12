package net.tislib.ugm.service;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.stereotype.Service;

@Service
public class ModelProcessor {
    public String process(String html) {
        Document document = Jsoup.parse(html);

        return document.html();
    }
}
