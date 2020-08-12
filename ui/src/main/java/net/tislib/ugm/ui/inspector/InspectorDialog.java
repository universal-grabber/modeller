package net.tislib.ugm.ui.inspector;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import net.tislib.ugm.markers.MarkerParameter;
import net.tislib.ugm.model.Model;

import java.util.function.Consumer;

public class InspectorDialog {

    private final Model model;
    private final MarkerParameter.InspectorOptions inspectorOptions;
    private final Consumer<String> onSave;
    private final Consumer<String> onCancel;

    private final InspectorView inspectorView;

    public InspectorDialog(Model model, MarkerParameter.InspectorOptions inspectorOptions, Consumer<String> onSave, Consumer<String> onCancel) {
        this.model = model;
        this.inspectorOptions = inspectorOptions;
        this.onSave = onSave;
        this.onCancel = onCancel;

        inspectorView = new InspectorView(model, inspectorOptions);
    }

    public void open() {
        Dialog dialog = new Dialog();
        VerticalLayout content = new VerticalLayout();
        Label headerTitle = new Label("Inspector");
        content.add(headerTitle);
        content.setSizeFull();

        VerticalLayout inner = render();
        inner.setSizeFull();

        content.add(inner);
        inner.setSizeFull();

        HorizontalLayout actionButtons = new HorizontalLayout();
        content.add(actionButtons);

        Button saveButton = new Button("Save");
        Button tempButton = new Button("Temp");
        Button closeButton = new Button("Close");

        saveButton.addClickListener((event) -> {
            String result = inspectorView.compileSelector();
            onSave.accept(result);
            dialog.close();
        });

        closeButton.addClickListener((event) -> {
            dialog.close();
            onCancel.accept(null);
        });

        tempButton.addClickListener((event) -> {
            inspectorView.temp();
        });

        actionButtons.add(saveButton);
        actionButtons.add(tempButton);
        actionButtons.add(closeButton);

        dialog.setWidth("1500px");
        dialog.setHeight("1000px");
        dialog.add(content);
        dialog.open();
    }

    private VerticalLayout render() {
        return inspectorView.render();
    }
}
