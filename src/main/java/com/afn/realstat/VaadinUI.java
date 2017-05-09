package com.afn.realstat;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;

import com.vaadin.annotations.Theme;
import com.vaadin.annotations.Widgetset;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.VaadinRequest;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.ui.Button;
import com.vaadin.ui.Grid;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;

@SuppressWarnings("serial")
@SpringUI(path="/sandbox")
@Theme("realstatTheme")
@Widgetset("AppWidgetset")
public class VaadinUI extends UI {

	private final CustomerRepository repo;

	private final CustomerEditor editor;

	final Grid grid;

	final TextField filter;

	private final Button addNewBtn;

	@Autowired
	public VaadinUI(CustomerRepository repo, CustomerEditor editor) {
		this.repo = repo;
		this.editor = editor;
		this.grid = new Grid();
		this.filter = new TextField();
		this.addNewBtn = new Button("New customer", FontAwesome.PLUS);
	}

	@Override
	protected void init(VaadinRequest request) {
		// build layout
		HorizontalLayout actions = new HorizontalLayout(filter, addNewBtn);
		VerticalLayout mainLayout = new VerticalLayout(actions, grid, editor);
		setContent(mainLayout);

		// Configure layouts and components
		actions.setSpacing(true);
		mainLayout.setMargin(true);
		mainLayout.setSpacing(true);

		grid.setHeight(300, Unit.PIXELS);
		grid.setColumns("id", "firstName", "lastName");

		// TODO fix filter.setInputPrompt("Filter by last name");

		// Hook logic to components

		// Replace listing with filtered content when user changes filter
		// TODO fix filter.addTextChangeListener(e -> listCustomers(e.getText()));

		// Connect selected Customer to editor or hide if none is selected
		/* TODO fix
		grid.addSelectionListener(e -> {
			if (e.getSelected().isEmpty()) {
				editor.setVisible(false);
			}
			else {
				editor.editCustomer((Customer) grid.getSelectedRow());
			}
		});
		*/

		// Instantiate and edit new Customer the new button is clicked
		addNewBtn.addClickListener(e -> editor.editCustomer(new Customer("", "")));

		// Listen changes made by the editor, refresh data from backend
		editor.setChangeHandler(() -> {
			editor.setVisible(false);
			listCustomers(filter.getValue());
		});

		// Initialize listing
		listCustomers(null);
	}

	// tag::listCustomers[]
	@SuppressWarnings({ "rawtypes", "unchecked" })
	void listCustomers(String text) {
		if (StringUtils.isEmpty(text)) {
			// TODO fix grid.setContainerDataSource(
			//		new BeanItemContainer(Customer.class, repo.findAll()));
		}
		else {
			// TODO fix grid.setContainerDataSource(new BeanItemContainer(Customer.class,
			// TODO fix		repo.findByLastNameStartsWithIgnoreCase(text)));
		}
	}
	// end::listCustomers[]

}
