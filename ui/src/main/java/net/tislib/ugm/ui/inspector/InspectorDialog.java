package net.tislib.ugm.ui.inspector;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import net.tislib.ugm.markers.MarkerParameter;
import net.tislib.ugm.model.Example;
import net.tislib.ugm.model.Model;
import org.jsoup.nodes.Document;

import java.util.function.Consumer;
import java.util.function.Function;

public class InspectorDialog {

    private final Model model;
    private final MarkerParameter.InspectorOptions inspectorOptions;
    private final Consumer<String> onSave;
    private final Consumer<String> onCancel;

    private final InspectorView inspectorView;
    private final Function<Example, Document> documentResolver;

    public InspectorDialog(Model model, MarkerParameter.InspectorOptions inspectorOptions, Consumer<String> onSave, Consumer<String> onCancel, Function<Example, Document> documentResolver) {
        this.model = model;
        this.inspectorOptions = inspectorOptions;
        this.onSave = onSave;
        this.onCancel = onCancel;
        this.documentResolver = documentResolver;

        inspectorView = new InspectorView(model, inspectorOptions, this.documentResolver);
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

        Button saveButton = new Button("Save");
        Button tempButton = new Button("Temp");
        Button closeButton = new Button("Close");

        saveButton.addClickListener((event) -> {
            String result = inspectorView.getSelector();
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

        inspectorView.getSelectorBar().add(saveButton);
        inspectorView.getSelectorBar().add(tempButton);
        inspectorView.getSelectorBar().add(closeButton);

        dialog.setWidth("1500px");
        dialog.setHeight("1000px");
        dialog.add(content);
        dialog.open();
    }

    private VerticalLayout render() {
        return inspectorView.render();
    }

    public void setValue(String value) {
        inspectorView.setSelector(value);
    }
}
