package net.tislib.ugm.api.data.marker;

import lombok.Data;

import java.util.List;

@Data
public class Marker {

    private String name;

    private List<MarkerParameter> parameters;
}
