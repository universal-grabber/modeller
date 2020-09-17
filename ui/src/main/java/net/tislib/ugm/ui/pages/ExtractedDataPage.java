package net.tislib.ugm.ui.pages;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vaadin.flow.component.Html;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.provider.ListDataProvider;
import com.vaadin.flow.spring.annotation.UIScope;
import kong.unirest.Unirest;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import net.tislib.ugm.lib.markers.model.Example;
import net.tislib.ugm.lib.markers.model.Model;
import net.tislib.ugm.lib.markers.ModelDataExtractor;
import org.springframework.stereotype.Component;

import java.io.Serializable;

@Component
@UIScope
@RequiredArgsConstructor
public class ExtractedDataPage extends VerticalLayout {

    private final ModelDataExtractor modelDataExtractor = new ModelDataExtractor();

    public void render(Model model) {
        removeAll();

        ExtractedDataPageRender extractedDataPageRender = new ExtractedDataPageRender(model);

        extractedDataPageRender.render(this);

        setSizeFull();
    }

    public class ExtractedDataPageRender {

        private final ComboBox<Example> exampleSelector;
        private final Div content = new Div();
        private final Model model;
        private Example selectedExample;

        public ExtractedDataPageRender(Model model) {
            this.model = model;

            content.setSizeFull();

            this.exampleSelector = new ComboBox<>();
            exampleSelector.setDataProvider(new ListDataProvider<>(model.getExamples()));
            exampleSelector.setItemLabelGenerator(item -> item.getUrl().toString());
            exampleSelector.setWidth("800px");
            exampleSelector.setAllowCustomValue(false);

            exampleSelector.addValueChangeListener(event -> {
                selectedExample = event.getValue();
                loadContent();
            });
        }

        @SneakyThrows
        private void loadContent() {
            Serializable data = processDocument(model, selectedExample.getId());
            ObjectMapper objectMapper = new ObjectMapper();
            String jsonData = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(data);

            content.removeAll();
            content.add(new Html("<pre>" + jsonData + "</pre>"));
        }

        public Serializable processDocument(Model model, Integer exampleId) {
            Example example = model.getExamples().stream().filter(item -> item.getId().equals(exampleId)).findAny().get();

            String html = Unirest.get(example.getUrl().toString()).asString().getBody();

            return modelDataExtractor.processDocument(model, html);
        }

        public void render(ExtractedDataPage frameViewPage) {
            frameViewPage.add(exampleSelector);
            frameViewPage.add(content);
        }
    }

}
