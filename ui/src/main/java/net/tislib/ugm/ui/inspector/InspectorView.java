package net.tislib.ugm.ui.inspector;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.html.IFrame;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.provider.ListDataProvider;
import elemental.json.impl.JreJsonArray;
import lombok.RequiredArgsConstructor;
import net.tislib.ugm.markers.MarkerParameter;
import net.tislib.ugm.model.Example;
import net.tislib.ugm.model.Model;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
public class InspectorView {

    private final Model model;
    private final MarkerParameter.InspectorOptions inspectorOptions;

    private final ComboBox<Example> exampleSelector = new ComboBox<>();
    private final IFrame iFrame = new IFrame();
    private Example selectedExample;

    private List<String> selectedElements = new ArrayList<>();

    private void loadFrame() {
        iFrame.setSrc("/model/frame?modelName=" + model.getId() + "&exampleId=" + selectedExample.getId());
        iFrame.setId("frame-" + Math.random());

        UI.getCurrent().getPage().executeJs("window.inspector = new Inspector('" + iFrame.getId().get() + "');");
    }

    public String compileSelector() {
        return "div.asd";
    }

    public VerticalLayout render() {
        VerticalLayout verticalLayout = new VerticalLayout();
        iFrame.setSizeFull();

        exampleSelector.setDataProvider(new ListDataProvider<>(model.getExamples()));
        exampleSelector.setItemLabelGenerator(item -> item.getUrl().toString());
        exampleSelector.setWidth("800px");
        exampleSelector.setAllowCustomValue(false);

        exampleSelector.addValueChangeListener(event -> {
            selectedExample = event.getValue();
            loadFrame();
        });

        exampleSelector.setValue(model.getExamples().get(0));

        HorizontalLayout horizontalLayout = new HorizontalLayout();
        horizontalLayout.add(exampleSelector);
        verticalLayout.add(horizontalLayout);
        verticalLayout.add(iFrame);

        Button startInspectionButton = new Button("Start Inspection");
        Button stopInspectionButton = new Button("Stop Inspection");

        Button singleSelectButton = new Button("single");
        singleSelectButton.setEnabled(false);

        horizontalLayout.add(startInspectionButton);
        horizontalLayout.add(stopInspectionButton);

        startInspectionButton.addClickListener(event -> {
            UI.getCurrent().getPage().executeJs("window.inspector.startInspection();");
            singleSelectButton.setEnabled(false);
        });

        stopInspectionButton.addClickListener(event -> {
            UI.getCurrent().getPage().executeJs("return window.inspector.getElements();").toCompletableFuture().thenAccept(resp -> {
                System.out.println(resp);
                JreJsonArray array = (JreJsonArray) resp;
                selectedElements.clear();

                for (int i = 0; i < array.length(); i++) {
                    selectedElements.add(array.get(i).asString());
                }

                Notification.show(selectedElements.toString());

                UI.getCurrent().getPage().executeJs("window.inspector.stopInspection();");
                singleSelectButton.setEnabled(true);
            });
        });

        singleSelectButton.addClickListener(event -> {

        });


        return verticalLayout;
    }

    public void temp() {

    }
}
