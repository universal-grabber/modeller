package net.tislib.ugm.ui.pages;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.provider.ListDataProvider;
import com.vaadin.flow.spring.annotation.UIScope;
import lombok.RequiredArgsConstructor;
import net.tislib.ugm.lib.markers.Marker;
import net.tislib.ugm.lib.markers.MarkerParameter;
import net.tislib.ugm.lib.markers.model.MarkerData;
import net.tislib.ugm.lib.markers.model.Model;
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
public class MarkersPage2 extends VerticalLayout {

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

                        Button upButton = new Button("U");
                        Button downButton = new Button("D");

                        editButton.addClickListener(event -> openEditMarkerPopup(markerData));

                        deleteButton.addClickListener(event -> {
                            deleteMarker(markerData);
                        });

                        final Function<MarkerData, Integer> getIndex = md -> model.getMarkers().indexOf(md);

                        upButton.addClickListener(event -> {
                            Integer index = getIndex.apply(markerData);

                            if (index < model.getMarkers().size()) {
                                model.getMarkers().remove(markerData);
                                model.getMarkers().add(index + 1, markerData);
                                render(model);
                            }
                        });

                        downButton.addClickListener(event -> {
                            Integer index = getIndex.apply(markerData);

                            if (index > 0) {
                                model.getMarkers().remove(markerData);
                                model.getMarkers().add(index - 1, markerData);
                                render(model);
                            }
                        });

                        horizontalLayout.add(upButton);
                        horizontalLayout.add(downButton);
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

        public void render2(MarkersPage2 markersPage) {
            markersPage.add(addMarkersLayout);
            markersPage.add(grid);
        }
    }

}
