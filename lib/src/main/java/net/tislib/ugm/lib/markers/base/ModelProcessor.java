package net.tislib.ugm.lib.markers.base;

import net.tislib.ugm.lib.markers.Page;
import net.tislib.ugm.lib.markers.base.model.MarkerData;
import net.tislib.ugm.lib.markers.base.model.Model;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class ModelProcessor {

    public String process(Model model, String url, String html) {
        Document document = processDocument(model, url, html);

        return document.html();
    }

    public Document processDocument(Model model, String url, String html) {
        Document document = Jsoup.parse(html);

        Page page = applyMarkers(model.getMarkers(), null, Page.of(url, document));

        return page.getDocument();
    }

    private Page applyMarkers(List<MarkerData> markers, String parentMarker, Page page) {
        for (MarkerData markerData : markers) {
            if (Objects.equals(markerData.getParentName(), parentMarker)) {
                Optional<Page> appliedResult = applyMarker(page, markerData);
                if (appliedResult.isPresent()) {
                    page = appliedResult.get();
                    page = applyMarkers(markers, markerData.getName(), page);
                }
            }
        }
        return page;
    }

    private Optional<Page> applyMarker(Page page, MarkerData markerData) {
        Marker marker = Marker.locate(markerData.getType());

        return marker.process(page, markerData);
    }

    public Model materialize(Model model) {
        for (MarkerData markerData : model.getMarkers()) {
            Marker marker = Marker.locate(markerData.getType());

            marker.materializeParameters(markerData.getParameters());
        }
        return model;
    }
}
