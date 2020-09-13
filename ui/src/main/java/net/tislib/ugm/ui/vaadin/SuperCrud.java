package net.tislib.ugm.ui.vaadin;

import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.provider.ListDataProvider;

public class SuperCrud<T> extends VerticalLayout {
    private final SuperCrudConfig<T> superCrudConfig;
    private final Grid<T> grid;


    public SuperCrud(SuperCrudConfig<T> superCrudConfig) {
        this.superCrudConfig = superCrudConfig;
        grid = new Grid<>(superCrudConfig.getEntityClass());

        grid.setDataProvider(new ListDataProvider<>(superCrudConfig.getRecordsProvider().get()));

        grid.addComponentColumn(markerData -> {
//                    HorizontalLayout horizontalLayout = new HorizontalLayout();
//
//                    Button editButton = new Button("edit");
//                    Button deleteButton = new Button("delete");
//
//                    Button upButton = new Button("U");
//                    Button downButton = new Button("D");
//
//                    editButton.addClickListener(event -> openEditMarkerPopup(markerData));
//
//                    deleteButton.addClickListener(event -> {
//                        deleteMarker(markerData);
//                    });
//
//                    final Function<MarkerData, Integer> getIndex = md -> model.getMarkers().indexOf(md);
//
//                    upButton.addClickListener(event -> {
//                        Integer index = getIndex.apply(markerData);
//
//                        if (index < model.getMarkers().size()) {
//                            model.getMarkers().remove(markerData);
//                            model.getMarkers().add(index + 1, markerData);
//                            render(model);
//                        }
//                    });
//
//                    downButton.addClickListener(event -> {
//                        Integer index = getIndex.apply(markerData);
//
//                        if (index > 0) {
//                            model.getMarkers().remove(markerData);
//                            model.getMarkers().add(index - 1, markerData);
//                            render(model);
//                        }
//                    });
//
//                    horizontalLayout.add(upButton);
//                    horizontalLayout.add(downButton);
//                    horizontalLayout.add(editButton);
//                    horizontalLayout.add(deleteButton);

//                    return horizontalLayout;
                    return null;
                }
        ).setHeader("Actions");
    }
}
