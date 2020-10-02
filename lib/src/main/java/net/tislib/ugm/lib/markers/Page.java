package net.tislib.ugm.lib.markers;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.jsoup.nodes.Document;

@Data
@AllArgsConstructor
public class Page {
    private final String url;
    private final Document document;

    public static Page of(String url, Document document) {
        return new Page(url, document);
    }
}
