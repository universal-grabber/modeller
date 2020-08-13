package net.tislib.ugm.ui.pages;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vaadin.flow.component.Html;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.provider.ListDataProvider;
import com.vaadin.flow.spring.annotation.UIScope;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import net.tislib.ugm.model.Example;
import net.tislib.ugm.model.Model;
import net.tislib.ugm.service.ModelDataExtractor;
import org.springframework.stereotype.Component;

import java.io.Serializable;

@Component
@UIScope
@RequiredArgsConstructor
public class ExtractedDataPage extends VerticalLayout {

    private final ModelDataExtractor modelDataExtractor;

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
            Serializable data = modelDataExtractor.processDocument(model, selectedExample.getId());
            ObjectMapper objectMapper = new ObjectMapper();
            String jsonData = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(data);

            content.removeAll();
            content.add(new Html("<pre>" + jsonData + "</pre>"));
        }

        public void render(ExtractedDataPage frameViewPage) {
            frameViewPage.add(exampleSelector);
            frameViewPage.add(content);
        }
    }

}
