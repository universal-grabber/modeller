package net.tislib.ugm.service;

import lombok.RequiredArgsConstructor;
import net.tislib.ugm.markers.Marker;
import net.tislib.ugm.model.MarkerData;
import net.tislib.ugm.model.Model;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ModelProcessor {

    private final MarkerService markerService;

    public String process(Model model, String html) {
        Document document = processDocument(model, html);

        return document.html();
    }

    public Document processDocument(Model model, String html) {
        Document document = Jsoup.parse(html);

        for (MarkerData markerData : model.getMarkers()) {
            document = applyMarker(document, markerData);
        }

        return document;
    }

    private Document applyMarker(Document document, MarkerData markerData) {
        Marker marker = markerService.locate(markerData.getType());

        return marker.process(document, markerData.getParameters());
    }
}
