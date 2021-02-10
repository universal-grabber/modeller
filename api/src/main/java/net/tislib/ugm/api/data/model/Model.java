package net.tislib.ugm.api.data.model;

import lombok.Data;

import java.util.List;

@Data
public class Model {

    private String id;

    private String name;

    private String source;

    private List<Example> examples;

    private List<MarkerData> markers;

    private String schema;

    private String objectType;

    private String urlCheck;

    private String ref;

    // todo implement logic private boolean allowQueryParams;
}
