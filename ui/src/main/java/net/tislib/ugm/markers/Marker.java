package net.tislib.ugm.markers;

import java.util.List;

public interface Marker {
    String getName();

    List<MarkerParameter> getParameters();
}
