package net.tislib.ugm.ui.pages;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.provider.ListDataProvider;
import com.vaadin.flow.spring.annotation.UIScope;
import lombok.RequiredArgsConstructor;
import net.tislib.ugm.markers.Marker;
import net.tislib.ugm.markers.MarkerParameter;
import net.tislib.ugm.model.MarkerData;
import net.tislib.ugm.model.Model;
import net.tislib.ugm.service.MarkerService;
import net.tislib.ugm.ui.inspector.InspectorDialog;
import net.tislib.ugm.ui.inspector.InspectorView;
import net.tislib.ugm.ui.pages.marker.MarkerDialog;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.function.Consumer;

@Component
@UIScope
@RequiredArgsConstructor
public class MarkersPage extends VerticalLayout {

    private final MarkerService markerService;

    public void render(Model model) {
        removeAll();

        MarkersPageRender frameViewPageRender = new MarkersPageRender(model);

        frameViewPageRender.render2(this);

        setSizeFull();
    }

    public class MarkersPageRender {

        private final Model model;
        private final Grid<MarkerData> grid;
        private final HorizontalLayout addMarkersLayout = new HorizontalLayout();

        public MarkersPageRender(Model model) {
            this.model = model;

            grid = new Grid<>(MarkerData.class);

            grid.setDataProvider(new ListDataProvider<>(model.getMarkers()));

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

            Set<Marker> markers = markerService.getAllMarkers();

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

                InspectorDialog inspectorDialog = new InspectorDialog(model, inspectorOptions, onSave, onCancel);
                inspectorDialog.open();
            });

            addMarkersLayout.add(button);
        }

        private void deleteMarker(MarkerData markerData) {
            model.getMarkers().remove(markerData);
            render(model);
        }

        private void openEditMarkerPopup(MarkerData markerData) {
            MarkerDialog markerDialog = new MarkerDialog(model, markerService.locate(markerData.getType()), markerData);

            markerDialog.open();

            markerDialog.setReloadHandler(() -> render(model));
        }

        private void openAddMarkerPopup(Marker marker) {
            MarkerData markerData = new MarkerData();
            markerData.setName("marker-" + model.getMarkers().size());
            markerData.setType(marker.getName());

            MarkerDialog markerDialog = new MarkerDialog(model, marker, markerData);

            markerDialog.open();

            markerDialog.setReloadHandler(() -> render(model));
        }

        public void render2(MarkersPage markersPage) {
            markersPage.add(addMarkersLayout);
            markersPage.add(grid);
        }
    }

}
