package net.tislib.ugm.lib.markers.base.model;

import lombok.Data;

import java.util.List;

@Data
public class Model {

    private String id;

    private String name;

    private List<Example> examples;

    private List<MarkerData> markers;

    private String schema;

    private String objectType;

    private String urlCheck;

    private String ref;
}
