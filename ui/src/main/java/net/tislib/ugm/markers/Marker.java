package net.tislib.ugm.markers;

import org.jsoup.nodes.Document;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

public interface Marker {
    String getName();

    List<MarkerParameter> getParameters();

    Document process(Document document, Map<String, Serializable> parameters);
}
