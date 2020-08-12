package net.tislib.ugm.ui.pages;

import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.provider.ListDataProvider;
import com.vaadin.flow.spring.annotation.UIScope;
import net.tislib.ugm.model.Example;
import net.tislib.ugm.model.Model;
import org.springframework.stereotype.Component;

@Component
@UIScope
public class ExtractedDataPage extends VerticalLayout {

    public void render(Model model) {
        removeAll();

        ExtractedDataPageRender extractedDataPageRender = new ExtractedDataPageRender(model);

        extractedDataPageRender.render(this);

        setSizeFull();
    }

    public static class ExtractedDataPageRender {

        private final ComboBox<Example> exampleSelector;
        private final Label content = new Label();
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

        private void loadContent() {
//            content.setText(model.getExamples().toString());
            content.setText(model.getMarkers().toString());
        }

        public void render(ExtractedDataPage frameViewPage) {
            frameViewPage.add(exampleSelector);
            frameViewPage.add(content);
        }
    }

}
