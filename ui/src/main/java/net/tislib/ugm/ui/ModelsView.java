package net.tislib.ugm.ui;

import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import net.tislib.ugm.ui.vaadin.SuperCrud;
import net.tislib.ugm.ui.vaadin.SuperCrudConfig;

@Route("/models")
public class ModelsView extends VerticalLayout {

    public void ModelsView() {
        SuperCrud superCrud = new SuperCrud(SuperCrudConfig.builder().build());

        this.add(superCrud);
    }
}
