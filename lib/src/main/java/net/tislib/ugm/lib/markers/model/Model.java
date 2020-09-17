package net.tislib.ugm.lib.markers.model;

import lombok.Data;

import java.util.List;

@Data
public class Model {

    private String id;

    private List<Example> examples;

    private List<MarkerData> markers;
}
