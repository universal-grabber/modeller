package net.tislib.ugm.ui.pages;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.dnd.GridDropLocation;
import com.vaadin.flow.component.grid.dnd.GridDropMode;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.treegrid.TreeGrid;
import com.vaadin.flow.data.provider.ListDataProvider;
import com.vaadin.flow.data.provider.hierarchy.TreeData;
import com.vaadin.flow.data.provider.hierarchy.TreeDataProvider;
import com.vaadin.flow.spring.annotation.UIScope;
import lombok.RequiredArgsConstructor;
import net.tislib.ugm.lib.markers.base.Marker;
import net.tislib.ugm.lib.markers.base.MarkerParameter;
import net.tislib.ugm.lib.markers.base.model.MarkerData;
import net.tislib.ugm.lib.markers.base.model.Model;
import net.tislib.ugm.service.MarkerService;
import net.tislib.ugm.service.ModelHtmlProcessorService;
import net.tislib.ugm.ui.inspector.InspectorDialog;
import net.tislib.ugm.ui.pages.marker.MarkerDialog;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

@Component
@UIScope
@RequiredArgsConstructor
public class MarkersPage extends VerticalLayout {

    private final MarkerService markerService;
    private final ModelHtmlProcessorService modelHtmlProcessorService;

    public void render(Model model) {
        removeAll();

        MarkersPageRender frameViewPageRender = new MarkersPageRender(model);

        frameViewPageRender.render2(this);

        setSizeFull();
    }

    public class MarkersPageRender {

        private final Model model;
        private final TreeGrid<MarkerData> grid;
        private final HorizontalLayout addMarkersLayout = new HorizontalLayout();
        private MarkerData draggedItem;

        public MarkersPageRender(Model model) {
            this.model = model;

            grid = new TreeGrid<>(MarkerData.class);

            TreeData<MarkerData> treeData = new TreeData<>();

            setData(model, treeData);

            grid.setDataProvider(new TreeDataProvider<>(treeData));

            grid.setSelectionMode(Grid.SelectionMode.NONE);
            grid.setRowsDraggable(true);

            grid.addDragStartListener(event -> {
                draggedItem = event.getDraggedItems().get(0);
                grid.setDropMode(GridDropMode.ON_TOP_OR_BETWEEN);
            });

            grid.addDragEndListener(event -> {
                draggedItem = null;
                grid.setDropMode(null);
            });

            grid.addDropListener(event -> {
                MarkerData dropOverItem = event.getDropTargetItem().get();
                if (!dropOverItem.equals(draggedItem)) {
                    model.getMarkers().remove(draggedItem);
                    int dropIndex = model.getMarkers().indexOf(dropOverItem)
                            + (event.getDropLocation() == GridDropLocation.BELOW ? 1
                            : 0);

                    if (event.getDropLocation() == GridDropLocation.ON_TOP) {
                        draggedItem.setParentName(dropOverItem.getName());
                    } else {
                        draggedItem.setParentName(dropOverItem.getParentName());
                    }

                    model.getMarkers().add(dropIndex, draggedItem);

                    setData(model, treeData);

                    grid.getDataProvider().refreshAll();
                } else {
                    System.out.println("Else occoured");
                }
            });

            grid.addComponentColumn(markerData -> {
                        HorizontalLayout horizontalLayout = new HorizontalLayout();

                        Button editButton = new Button("edit");
                        Button deleteButton = new Button("delete");

                        editButton.addClickListener(event -> openEditMarkerPopup(markerData));

                        deleteButton.addClickListener(event -> {
                            deleteMarker(markerData);
                        });

                        horizontalLayout.add(editButton);
                        horizontalLayout.add(deleteButton);

                        return horizontalLayout;
                    }
            ).setHeader("Actions");

            grid.setColumnReorderingAllowed(true);

            List<Marker> markers = markerService.getAllMarkers();

            for (Marker marker : markers) {
                Button button = new Button("Add: " + marker.getName());
                button.addClickListener(event -> {
                    openAddMarkerPopup(marker);
                });
                addMarkersLayout.add(button);
            }

            Button button = new Button("temp");
            button.addClickListener(event -> {
                Consumer<String> onSave = Notification::show;
                Consumer<String> onCancel = (res) -> {

                };

                MarkerParameter.InspectorOptions inspectorOptions = new MarkerParameter.InspectorOptions();

                InspectorDialog inspectorDialog = new InspectorDialog(model, inspectorOptions, onSave, onCancel,
                        example -> modelHtmlProcessorService.getDocument(model, example.getId()));
                inspectorDialog.open();
            });

            addMarkersLayout.add(button);
        }

        private void setData(Model model, TreeData<MarkerData> treeData) {
            treeData.clear();
            model.getMarkers().forEach(item -> {
                if (item.getParentName() == null) {
                    treeData.addItem(null, item);
                } else {
                    treeData.addItem(model.getMarkers().stream().filter(a -> a.getName().equals(item.getParentName())).findAny().get(), item);
                }
            });
        }

        private void deleteMarker(MarkerData markerData) {
            model.getMarkers().remove(markerData);
            render(model);
        }

        private void openEditMarkerPopup(MarkerData markerData) {
            MarkerDialog markerDialog = new MarkerDialog(model, markerService.locate(markerData.getType()), markerData,
                    example -> modelHtmlProcessorService.getDocument(model, example.getId()));

            markerDialog.open();

            markerDialog.setReloadHandler(() -> render(model));
        }

        private void openAddMarkerPopup(Marker marker) {
            MarkerData markerData = new MarkerData();
            markerData.setName(getUniqueName());
            markerData.setType(marker.getName());

            MarkerDialog markerDialog = new MarkerDialog(model, marker, markerData,
                    example -> modelHtmlProcessorService.getDocument(model, example.getId()));

            markerDialog.open();

            markerDialog.setReloadHandler(() -> render(model));
        }

        private String getUniqueName() {
            int l = model.getMarkers().size();
            final String[] name = {"marker-" + l};

            while (model.getMarkers().stream().anyMatch(item -> item.getName().equals(name[0]))) {
                l++;
                name[0] = "marker-" + l;
            }

            return name[0];
        }

        public void render2(MarkersPage markersPage) {
            markersPage.add(addMarkersLayout);
            markersPage.add(grid);
        }
    }

}
