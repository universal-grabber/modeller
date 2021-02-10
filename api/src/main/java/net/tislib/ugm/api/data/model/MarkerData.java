package net.tislib.ugm.api.data.model;

import lombok.Data;

import java.io.Serializable;
import java.util.Map;

@Data
public class MarkerData {
    private String name;
    private String type;
    private String parentName;

    private Map<String, Serializable> parameters;
}
