package net.tislib.ugm.ui.inspector;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.html.IFrame;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.provider.ListDataProvider;
import elemental.json.impl.JreJsonArray;
import lombok.RequiredArgsConstructor;
import net.tislib.ugm.markers.MarkerParameter;
import net.tislib.ugm.model.Example;
import net.tislib.ugm.model.Model;
import net.tislib.ugm.ui.helper.*;
import org.jsoup.nodes.Document;

import java.util.*;
import java.util.function.Function;

@RequiredArgsConstructor
public class InspectorView {

    private final Model model;
    private final MarkerParameter.InspectorOptions inspectorOptions;

    private final ComboBox<Example> exampleSelectorField = new ComboBox<>();
    private final ComboBox<SelectorAlgorithm> algorithmField = new ComboBox<>();
    private final Function<Example, Document> exampleDocumentResolver;

    private final Button applyButton = new Button("Apply");
    private final Button applyAllButton = new Button("Apply All");
    private final Button loadButton = new Button("Load");
    private final Button compileButton = new Button("Compile");

    private final Button fetchButton = new Button("Fetch");
    private final Checkbox noNthChildField = new Checkbox("no-nth-child");
    private final Checkbox noAttributeField = new Checkbox("no-attribute");
    private final Checkbox fullSelectors = new Checkbox("no-attribute");

    private final TextField cssSelector = new TextField();

    private final IFrame iFrame = new IFrame();
    private Example selectedExample;

    private final Map<Example, Document> documentMap = new HashMap<>();
    private final Map<Example, List<String>> exampleSelector = new HashMap<>();
    private final Map<Example, String> selectors = new HashMap<>();

    private void loadFrame() {
        iFrame.setSrc("/model/frame?modelName=" + model.getId() + "&exampleId=" + selectedExample.getId());
        iFrame.setId("frame-" + Math.random());
        iFrame.setSandbox(IFrame.SandboxType.ALLOW_SAME_ORIGIN);

        UI.getCurrent().getPage().executeJs("window.inspector = new Inspector('" + iFrame.getId().get() + "');");
    }

    public VerticalLayout render() {
        VerticalLayout verticalLayout = new VerticalLayout();
        iFrame.setSizeFull();

        cssSelector.setWidth("500px");

        exampleSelectorField.setDataProvider(new ListDataProvider<>(model.getExamples()));
        exampleSelectorField.setItemLabelGenerator(item -> item.getUrl().toString());
        exampleSelectorField.setWidth("500px");
        exampleSelectorField.setAllowCustomValue(false);

        algorithmField.setDataProvider(new ListDataProvider<>(Arrays.asList(SelectorAlgorithm.values())));
        algorithmField.setItemLabelGenerator(Enum::name);
        algorithmField.setAllowCustomValue(false);

        exampleSelectorField.addValueChangeListener(event -> {
            selectedExample = event.getValue();
            loadFrame();

            exampleSelector.putIfAbsent(selectedExample, new ArrayList<>());
            if (!documentMap.containsKey(selectedExample)) {
                documentMap.put(selectedExample, exampleDocumentResolver.apply(selectedExample));
            }
        });

        algorithmField.setValue(SelectorAlgorithm.SINGLE);
        algorithmField.addValueChangeListener(event -> {
            UI.getCurrent().getPage().executeJs("window.inspector.setAlgorithm($0)", algorithmField.getValue().name());
        });

        HorizontalLayout selectorBar = new HorizontalLayout();
        HorizontalLayout algorithmBar = new HorizontalLayout();
        selectorBar.add(cssSelector);
        selectorBar.add(applyButton);
        selectorBar.add(applyAllButton);
        selectorBar.add(loadButton);
        selectorBar.add(compileButton);

        algorithmBar.add(exampleSelectorField);
        algorithmBar.add(algorithmField);
        algorithmBar.add(fetchButton);
        algorithmBar.add(noNthChildField);
        algorithmBar.add(noAttributeField);
        algorithmBar.add(fullSelectors);

        verticalLayout.add(selectorBar);
        verticalLayout.add(algorithmBar);
        verticalLayout.add(iFrame);

        fetchButton.addClickListener((event -> {
            fetchSelections();
        }));

        compileButton.addClickListener(event -> {
            compile();
        });

        return verticalLayout;
    }

    private void compile() {
        SelectionAlgorithm selectionAlgorithm = getSelectionAlgorithm();

        selectors.clear();

        exampleSelector.forEach((example, selecterElements) -> {
            String selector = selectionAlgorithm.select(documentMap.get(example), selecterElements);
            selectors.put(example, selector);
        });

        // not implemented

        cssSelector.setValue(selectors.get(selectedExample));
    }

    private SelectionAlgorithm getSelectionAlgorithm() {
        SelectionAlgorithm selectionAlgorithm = null;
        switch (algorithmField.getValue()) {
            case SINGLE:
                selectionAlgorithm = new SingleSelectionAlgorithm();
                break;
            case ITERATIVE:
                selectionAlgorithm = new IterativeSelectionAlgorithm();
                break;
            case COMMON_PARENT:
                selectionAlgorithm = new CommonParentSelectionAlgorithm();
                break;
            case COMMON_PARENT_ITERATIVE:
                selectionAlgorithm = new CommonParentIterativeSelectionAlgorithm();
                break;
            default:
                throw new IllegalArgumentException();
        }
        return selectionAlgorithm;
    }

    private void fetchSelections() {
        UI.getCurrent().getPage().executeJs("return window.inspector.getElements();").toCompletableFuture().thenAccept(resp -> {
            System.out.println(resp);
            JreJsonArray array = (JreJsonArray) resp;
            exampleSelector.get(selectedExample).clear();

            for (int i = 0; i < array.length(); i++) {
                exampleSelector.get(selectedExample).add(array.get(i).asString());
            }

            Notification.show(exampleSelector.get(selectedExample).toString());
        });
    }

    public void temp() {

    }

    public String getSelector() {
        return cssSelector.getValue();
    }

    public void setSelector(String value) {
        cssSelector.setValue(value);
    }
}
