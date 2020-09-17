package net.tislib.ugm.lib.markers;

import net.tislib.ugm.lib.markers.model.MarkerData;
import net.tislib.ugm.lib.markers.model.Model;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

public class ModelProcessor {

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
        Marker marker = Marker.locate(markerData.getType());

        return marker.process(document, markerData.getParameters());
    }

}
