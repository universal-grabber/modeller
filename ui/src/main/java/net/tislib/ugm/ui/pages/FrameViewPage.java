package net.tislib.ugm.ui.pages;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.dependency.JavaScript;
import com.vaadin.flow.component.html.IFrame;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.provider.ListDataProvider;
import com.vaadin.flow.spring.annotation.UIScope;
import net.tislib.ugm.lib.markers.model.Example;
import net.tislib.ugm.lib.markers.model.Model;
import org.springframework.stereotype.Component;

@Component
@UIScope
@JavaScript("/inspector.js")
@CssImport("/inspector.css")
@CssImport("/frame.css")
public class FrameViewPage extends VerticalLayout {

    public void render(Model model) {
        removeAll();

        FrameViewPageRender frameViewPageRender = new FrameViewPageRender(model);

        frameViewPageRender.render(this);

        setSizeFull();
    }

    public static class FrameViewPageRender {

        private final ComboBox<Example> exampleSelector;
        private final IFrame iFrame;
        private final Model model;
        private Example selectedExample;

        public FrameViewPageRender(Model model) {
            this.model = model;

            this.iFrame = new IFrame();
            iFrame.setSizeFull();

            this.exampleSelector = new ComboBox<>();
            exampleSelector.setDataProvider(new ListDataProvider<>(model.getExamples()));
            exampleSelector.setItemLabelGenerator(item -> item.getUrl().toString());
            exampleSelector.setWidth("800px");
            exampleSelector.setAllowCustomValue(false);

            exampleSelector.addValueChangeListener(event -> {
                selectedExample = event.getValue();
                loadFrame();
            });
        }

        private void loadFrame() {
            iFrame.setSrc("/model/frame?modelName=" + model.getId() + "&exampleId=" + selectedExample.getId());

            iFrame.setSandbox(IFrame.SandboxType.ALLOW_SAME_ORIGIN);
            iFrame.setId("frame-" + Math.random());

            UI.getCurrent().getPage().executeJs("window.fixFrameView('" + iFrame.getId().get() + "');");
        }

        public void render(FrameViewPage frameViewPage) {
            frameViewPage.add(exampleSelector);
            frameViewPage.add(iFrame);
        }
    }

}
