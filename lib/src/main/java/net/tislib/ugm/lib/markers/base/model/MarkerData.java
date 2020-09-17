package net.tislib.ugm.lib.markers.base.model;

import lombok.Data;

import java.io.Serializable;
import java.util.Map;
import java.util.Set;

@Data
public class MarkerData {
    private String name;
    private String type;
    private String parentName;

    private Map<String, Serializable> parameters;
}
