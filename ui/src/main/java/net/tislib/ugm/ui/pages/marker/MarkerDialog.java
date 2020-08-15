package net.tislib.ugm.ui.pages.marker;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.provider.ListDataProvider;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import net.tislib.ugm.markers.Marker;
import net.tislib.ugm.markers.MarkerParameter;
import net.tislib.ugm.model.Example;
import net.tislib.ugm.model.MarkerData;
import net.tislib.ugm.model.Model;
import net.tislib.ugm.ui.ReloadHandler;
import net.tislib.ugm.ui.inspector.InspectorDialog;
import org.jsoup.nodes.Document;

import java.io.Serializable;
import java.util.*;
import java.util.function.Consumer;
import java.util.function.Function;

@RequiredArgsConstructor
public class MarkerDialog {
    private final Model model;
    private final Marker marker;
    private final MarkerData markerData;
    private final Function<Example, Document> documentResolver;

    private Map<String, Serializable> markerParameters;

    @Setter
    private ReloadHandler reloadHandler = () -> {

    };

    public void open() {
        if (markerData.getParameters() == null) {
            markerData.setParameters(new HashMap<>());
        }

        markerParameters = new HashMap<>(markerData.getParameters());

        Dialog dialog = new Dialog();
        VerticalLayout content = new VerticalLayout();
        Label headerTitle = new Label("Add " + marker.getName());
        content.add(headerTitle);

        VerticalLayout inner = render();

        content.add(inner);
        inner.setSizeFull();

        HorizontalLayout actionButtons = new HorizontalLayout();
        content.add(actionButtons);

        Button saveButton = new Button("Save");
        Button closeButton = new Button("Close");

        saveButton.addClickListener((event) -> {
            save();
            dialog.close();
            reloadHandler.reload();
        });

        closeButton.addClickListener((event) -> {
            dialog.close();
            reloadHandler.reload();
        });

        actionButtons.add(saveButton);
        actionButtons.add(closeButton);

        dialog.setWidth("600px");
        dialog.setHeight("auto");
        dialog.add(content);
        dialog.open();
    }

    private void save() {
        marker.materializeParameters(markerParameters);

        markerData.setParameters(new HashMap<>(markerParameters));

        Optional<MarkerData> existingMarkerData = model.getMarkers().stream().filter(item -> item.getName().equals(markerData.getName())).findAny();

        if (existingMarkerData.isPresent()) {
            existingMarkerData.get().setParameters(markerData.getParameters());
        } else {
            model.getMarkers().add(markerData);
        }
    }

    private VerticalLayout render() {
        VerticalLayout verticalLayout = new VerticalLayout();

        verticalLayout.setSizeFull();

        List<MarkerParameter> parameters = marker.getParameters();

        for (MarkerParameter markerParameter : parameters) {
            renderMarkerParameter(verticalLayout, markerParameter);
        }

        return verticalLayout;
    }

    private void renderMarkerParameter(VerticalLayout verticalLayout, MarkerParameter markerParameter) {
        switch (markerParameter.getParameterType()) {
            case TEXT:
                renderTextParameter(verticalLayout, markerParameter);
                break;
            case INSPECTOR:
                renderInspectorParameter(verticalLayout, markerParameter);
                break;
            case COMBOBOX:
                renderComboboxParameter(verticalLayout, markerParameter);
                break;
            case CHECKBOX:
                renderCheckboxParameter(verticalLayout, markerParameter);
                break;
        }
    }

    private void renderInspectorParameter(VerticalLayout verticalLayout, MarkerParameter markerParameter) {
        VerticalLayout container = new VerticalLayout();

        TextField textField = new TextField();
        textField.setLabel(markerParameter.getCaption());
        container.add(textField);
        verticalLayout.add(container);

        container.setWidth("500px");
        textField.setWidth("500px");

        if (markerParameters.containsKey(markerParameter.getName())) {
            textField.setValue(String.valueOf(markerParameters.get(markerParameter.getName())));
        } else if (markerParameter.getDefaultValue() != null) {
            textField.setValue(String.valueOf(markerParameter.getDefaultValue()));
        }

        Button inspect = new Button("inspect");

        container.add(inspect);

        inspect.addClickListener((event) -> {
            Consumer<String> onSave = (res) -> {
                textField.setValue(res);
                markerParameters.put(markerParameter.getName(), res);
            };
            Consumer<String> onCancel = (res) -> {

            };

            MarkerParameter.InspectorOptions inspectorOptions = (MarkerParameter.InspectorOptions) markerParameter.getOptions();

            InspectorDialog inspectorDialog = new InspectorDialog(model, inspectorOptions, onSave, onCancel, documentResolver);
            inspectorDialog.open();
            inspectorDialog.setValue(textField.getValue());
        });
    }

    private void renderTextParameter(VerticalLayout verticalLayout, MarkerParameter markerParameter) {
        TextField textField = new TextField();
        textField.setLabel(markerParameter.getCaption());
        verticalLayout.add(textField);
        textField.setWidth("500px");

        if (markerParameters.containsKey(markerParameter.getName())) {
            textField.setValue(String.valueOf(markerParameters.get(markerParameter.getName())));
        } else if (markerParameter.getDefaultValue() != null) {
            textField.setValue(String.valueOf(markerParameter.getDefaultValue()));
        }

        textField.addValueChangeListener(event -> {
            markerParameters.put(markerParameter.getName(), event.getValue());
        });
    }

    private void renderComboboxParameter(VerticalLayout verticalLayout, MarkerParameter markerParameter) {
        ComboBox<Serializable> comboBox = new ComboBox<>();
        comboBox.setLabel(markerParameter.getCaption());
        verticalLayout.add(comboBox);
        comboBox.setWidth("500px");

        comboBox.setAllowCustomValue(false);
        comboBox.setDataProvider(new ListDataProvider<>(Arrays.asList(markerParameter.getValues())));

        if (markerParameters.containsKey(markerParameter.getName())) {
            comboBox.setValue(markerParameters.get(markerParameter.getName()));
        } else if (markerParameter.getDefaultValue() != null) {
            comboBox.setValue(markerParameter.getDefaultValue());
        }

        comboBox.addValueChangeListener(event -> {
            markerParameters.put(markerParameter.getName(), event.getValue());
        });
    }

    private void renderCheckboxParameter(VerticalLayout verticalLayout, MarkerParameter markerParameter) {
        Checkbox checkbox = new Checkbox();
        checkbox.setLabel(markerParameter.getCaption());
        verticalLayout.add(checkbox);
        checkbox.setWidth("500px");

        if (markerParameters.containsKey(markerParameter.getName())) {
            checkbox.setValue(Boolean.valueOf(String.valueOf(markerParameters.get(markerParameter.getName()))));
        } else if (markerParameter.getDefaultValue() != null) {
            checkbox.setValue(Boolean.valueOf(String.valueOf(markerParameter.getDefaultValue())));
        }

        checkbox.addValueChangeListener(event -> {
            markerParameters.put(markerParameter.getName(), event.getValue());
        });
    }
}
