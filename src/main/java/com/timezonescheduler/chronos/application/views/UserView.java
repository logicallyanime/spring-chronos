package com.timezonescheduler.chronos.application.views;

import com.timezonescheduler.chronos.application.User;
import com.timezonescheduler.chronos.application.UserRepo;
import com.timezonescheduler.chronos.application.UserService;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.JsModule;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.littemplate.LitTemplate;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

/**
 * A Designer generated component for the user-view template.
 *
 * Designer will add and remove fields with @Id mappings but
 * does not overwrite or otherwise change this file.
 */
@PageTitle("User")
@Route("/user")
public class UserView extends VerticalLayout {
    Grid<User> grid = new Grid<>(User.class);
    TextField filterText = new TextField();
    private UserRepo repo;
    private UserService service;

    public UserView(UserRepo repo){
        service = new UserService(repo);
        addClassName("list-view");
        setSizeFull();

        configureGrid();
        add(
                getToolbar(),
                grid
        );

        updateUsers();

    }

    private void updateUsers() {
        grid.setItems(service.getUsers());
    }

    private Component getToolbar() {
        filterText.setPlaceholder("Filter by name...");
        filterText.setClearButtonVisible(true);
        filterText.setValueChangeMode(ValueChangeMode.LAZY);

        Button addContact = new Button("Add Contact");

        HorizontalLayout toolbar = new HorizontalLayout(filterText, addContact);
        toolbar.addClassName("toolbar");

        return toolbar;
    }

    private void configureGrid() {
        grid.addClassName("user-grid");
        grid.setSizeFull();
        grid.setColumns("name", "email");
        grid.getColumns().forEach(col -> col.setAutoWidth(true));
    }


}
