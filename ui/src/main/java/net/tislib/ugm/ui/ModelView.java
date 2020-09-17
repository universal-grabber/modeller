package net.tislib.ugm.ui;

import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.ClientCallable;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.router.BeforeEvent;
import com.vaadin.flow.router.HasUrlParameter;
import com.vaadin.flow.router.Route;
import lombok.RequiredArgsConstructor;
import net.tislib.ugm.lib.markers.base.model.Model;
import net.tislib.ugm.service.ModelService;
import net.tislib.ugm.ui.pages.ExtractedDataPage;
import net.tislib.ugm.ui.pages.FrameViewPage;
import net.tislib.ugm.ui.pages.MarkersPage;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Route("model")
@RequiredArgsConstructor
public class ModelView extends VerticalLayout
		implements HasUrlParameter<String> {

	private final ModelService modelService;
	private final FrameViewPage frameViewPage;
	private final MarkersPage markersPage;
	private final ExtractedDataPage extractedDataPage;

	private Button saveButton;
	private Button reloadButton;
	private String modelName;

	@PostConstruct
	public void init() {

		initUI();
	}

	@Override
	protected void onAttach(AttachEvent attachEvent) {
		super.onAttach(attachEvent);

		Model model = modelService.get(modelName);
		render(model);

		saveButton.addClickListener(event -> {
			modelService.saveModel(model);
			render(model);
		});

		reloadButton.addClickListener(event -> {
			onAttach(attachEvent);
		});
	}

	private void render(Model model) {
		frameViewPage.render(model);
		markersPage.render(model);
		extractedDataPage.render(model);
	}

	private void initUI() {
		Tab markersTab = new Tab("Markers");
		markersPage.setVisible(false);

		Tab frameViewTab = new Tab("View");
		frameViewPage.setVisible(false);

		Tab extractedDataTab = new Tab("Extracted Data");
		extractedDataPage.setVisible(false);

		Map<Tab, Component> tabsToPages = new HashMap<>();
		tabsToPages.put(markersTab, markersPage);
		tabsToPages.put(frameViewTab, frameViewPage);
		tabsToPages.put(extractedDataTab, extractedDataPage);
		Tabs tabs = new Tabs(markersTab, frameViewTab, extractedDataTab);
		Div pages = new Div(markersPage, frameViewPage, extractedDataPage);
		Set<Component> pagesShown = Stream.of(markersPage)
				.collect(Collectors.toSet());

		tabs.addSelectedChangeListener(event -> {
			pagesShown.forEach(page -> page.setVisible(false));
			pagesShown.clear();
			Component selectedPage = tabsToPages.get(tabs.getSelectedTab());
			selectedPage.setVisible(true);
			pagesShown.add(selectedPage);
		});

		markersTab.setSelected(true);
		markersPage.setVisible(true);

		add(tabs);
		add(pages);

		setSizeFull();
		pages.setSizeFull();

		HorizontalLayout actions = new HorizontalLayout();
		add(actions);
		actions.setWidthFull();
		Label space = new Label();
		space.setWidthFull();
		actions.add(space);

		saveButton = new Button("Save");
		reloadButton = new Button("Reload");
		saveButton.setWidth("30px");
		reloadButton.setWidth("30px");
		actions.add(saveButton);
		actions.add(reloadButton);
	}

	@ClientCallable
	public void greet(Object name) {
		System.out.println("Hi, " + name);
	}

	@Override
	public void setParameter(BeforeEvent event, String parameter) {
		this.modelName = parameter.replaceAll("_", "/");
	}
}
