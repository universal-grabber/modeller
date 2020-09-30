package net.tislib.ugm.lib.markers.base;

import net.tislib.ugm.lib.markers.base.model.MarkerData;
import net.tislib.ugm.lib.markers.base.model.Model;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class ModelProcessor {

    public String process(Model model, String html) {
        Document document = processDocument(model, html);

        return document.html();
    }

    public Document processDocument(Model model, String html) {
        Document document = Jsoup.parse(html);

        document = applyMarkers(model.getMarkers(), null, document);

        return document;
    }

    private Document applyMarkers(List<MarkerData> markers, String parentMarker, Document document) {
        for (MarkerData markerData : markers) {
            if (Objects.equals(markerData.getParentName(), parentMarker)) {
                Optional<Document> appliedResult = applyMarker(document, markerData);
                if (appliedResult.isPresent()) {
                    document = appliedResult.get();
                    document = applyMarkers(markers, markerData.getName(), document);
                }
            }
        }
        return document;
    }

    private Optional<Document> applyMarker(Document document, MarkerData markerData) {
        Marker marker = Marker.locate(markerData.getType());

        return marker.process(document, markerData);
    }

    public Model materialize(Model model) {
        for (MarkerData markerData : model.getMarkers()) {
            Marker marker = Marker.locate(markerData.getType());

            marker.materializeParameters(markerData.getParameters());
        }
        return model;
    }
}
