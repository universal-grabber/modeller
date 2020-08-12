package net.tislib.ugm.markers;

import java.util.ArrayList;
import java.util.List;

import static net.tislib.ugm.markers.MarkerParameter.ParameterType.INSPECTOR;
import static net.tislib.ugm.markers.MarkerParameter.ParameterType.TEXT;

public class FieldSelectorMarker implements Marker {
    @Override
    public String getName() {
        return "field-selector";
    }

    @Override
    public List<MarkerParameter> getParameters() {
        List<MarkerParameter> parameters = new ArrayList<>();

        MarkerParameter nameParameter = new MarkerParameter();
        nameParameter.setName("name");
        nameParameter.setCaption("Name");
        nameParameter.setParameterType(TEXT);

        MarkerParameter inspectorParameter = new MarkerParameter();
        inspectorParameter.setName("selector");
        inspectorParameter.setCaption("Selector");
        inspectorParameter.setParameterType(INSPECTOR);

        parameters.add(nameParameter);
        parameters.add(inspectorParameter);

        return parameters;
    }
}
