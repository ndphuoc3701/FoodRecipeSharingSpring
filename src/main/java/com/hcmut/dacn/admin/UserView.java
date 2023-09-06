package com.hcmut.dacn.admin;

import com.hcmut.dacn.dto.admin.RecipeAdmin;
import com.hcmut.dacn.dto.admin.UserAdmin;
import com.hcmut.dacn.mapper.RecipeMapper;
import com.hcmut.dacn.mapper.UserMapper;
import com.hcmut.dacn.repository.RecipeRepository;
import com.hcmut.dacn.repository.UserRepository;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.Route;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.stream.Collectors;

//@Route("user")
public class UserView extends VerticalLayout {

    private final UserRepository repo;
    private final UserMapper userMapper;
    private List<UserAdmin> userAdmins;
    private final UserEditor userEditor;
    private TextField filterByFullName;
    private TextField filterByUsername;
    private TextField filterByPassword;
    private TextField filterByMinCookLevel;
    private TextField filterByMaxCookLevel;
    private TextField filterByMinEvaluationLevel;
    private TextField filterByMaxEvaluationLevel;
    private final Button addNewBtn = new Button("New user", VaadinIcon.PLUS.create());
    private final Button searchBtn = new Button("Search", VaadinIcon.SEARCH.create());

    private final Button clearBtn = new Button("Clear");

    final Grid<UserAdmin> grid;

    public UserView(UserRepository repo, UserMapper userMapper, UserEditor userEditor) {
        this.userEditor = userEditor;
        this.repo = repo;
        this.userMapper = userMapper;
        this.grid = new Grid<>(UserAdmin.class);
        createFilterByName();
        createFilterByUserName();
        createFilterByPassword();
        createFilterByMaxCookLevel();
        createFilterByMaxEvaluationLevel();
        createFilterByMinCookLevel();
        createFilterByMinEvaluationLevel();
        createSearchBtn();
        createClearBtn();
        HorizontalLayout searchComponent1 = new HorizontalLayout(filterByFullName, filterByUsername, filterByPassword, clearBtn, searchBtn);
        HorizontalLayout searchComponent2 = new HorizontalLayout(filterByMinCookLevel, filterByMaxCookLevel, filterByMinEvaluationLevel, filterByMaxEvaluationLevel);
        createAddRecipeBtn();
        add(searchComponent1, searchComponent2, grid, userEditor, addNewBtn);
        grid.asSingleSelect().addValueChangeListener(e -> {
            userEditor.editCustomer(e.getValue());
        });

        userAdmins = userMapper.toAdminDtos(repo.findAll());
        grid.setItems(userAdmins);
    }

    private void createClearBtn() {
        clearBtn.addClickListener(e -> {
            filterByFullName.clear();
            filterByUsername.clear();
            filterByPassword.clear();
            filterByMinCookLevel.clear();
            filterByMaxCookLevel.clear();
            filterByMinEvaluationLevel.clear();
            filterByMaxEvaluationLevel.clear();
        });
    }

    private void createSearchBtn() {
        searchBtn.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        searchBtn.addClickListener((e) -> {
            List<UserAdmin> userAdmins = userMapper.toAdminDtos(repo.findAll());
            if (!filterByFullName.isEmpty()) {
                userAdmins = userAdmins.stream().filter(r -> r.getFullName().contains(filterByFullName.getValue())).collect(Collectors.toList());
            }
            if (!filterByUsername.isEmpty()) {
                userAdmins = userAdmins.stream().filter(r -> r.getUsername().toString().equals(filterByUsername.getValue())).collect(Collectors.toList());
            }
            if (!filterByPassword.isEmpty()) {
                userAdmins = userAdmins.stream().filter(r -> r.getPassword().toString().equals(filterByPassword.getValue())).collect(Collectors.toList());
            }
            if (!filterByMinEvaluationLevel.isEmpty()) {
                userAdmins = userAdmins.stream().filter(r -> r.getEvaluationLevel() >= Integer.parseInt(filterByMinEvaluationLevel.getValue())).collect(Collectors.toList());
            }
            if (!filterByMaxEvaluationLevel.isEmpty()) {
                userAdmins = userAdmins.stream().filter(r -> r.getEvaluationLevel() <= Integer.parseInt(filterByMaxEvaluationLevel.getValue())).collect(Collectors.toList());
            }
            if (!filterByMinCookLevel.isEmpty()) {
                userAdmins = userAdmins.stream().filter(r -> r.getCookLevel() >= Integer.parseInt(filterByMinCookLevel.getValue())).collect(Collectors.toList());
            }
            if (!filterByMaxCookLevel.isEmpty()) {
                userAdmins = userAdmins.stream().filter(r -> r.getCookLevel() <= Integer.parseInt(filterByMaxCookLevel.getValue())).collect(Collectors.toList());
            }
            grid.setItems(userAdmins);
        });
    }

    private void createAddRecipeBtn() {
        addNewBtn.addClickListener(e -> {
            userEditor.editCustomer(new UserAdmin());
            addNewBtn.setVisible(false);
        });
        userEditor.setChangeHandler(() -> {
            userEditor.setVisible(false);
            searchBtn.click();
            addNewBtn.setVisible(true);
        });
    }

    private void createFilterByName() {
        filterByFullName = new TextField();
        filterByFullName.setWidth("18em");
        filterByFullName.setPlaceholder("Filter by user full name");
        filterByFullName.setValueChangeMode(ValueChangeMode.LAZY);
        filterByFullName.addValueChangeListener(e -> searchBtn.click());
    }

    private void createFilterByUserName() {
        filterByUsername = new TextField();
        filterByUsername.setWidth("18em");
        filterByUsername.setPlaceholder("Filter by username");
        filterByUsername.setValueChangeMode(ValueChangeMode.LAZY);
        filterByUsername.addValueChangeListener(e -> searchBtn.click());
    }

    private void createFilterByPassword() {
        filterByPassword = new TextField();
        filterByPassword.setWidth("18em");
        filterByPassword.setPlaceholder("Filter by password");
        filterByPassword.setValueChangeMode(ValueChangeMode.LAZY);
        filterByPassword.addValueChangeListener(e -> searchBtn.click());
    }

    private void createFilterByMinCookLevel() {
        filterByMinCookLevel = new TextField();
        filterByMinCookLevel.setWidth("18em");
        filterByMinCookLevel.setPlaceholder("Filter by minimum cook level");
    }

    private void createFilterByMaxCookLevel() {
        filterByMaxCookLevel = new TextField();
        filterByMaxCookLevel.setWidth("18em");
        filterByMaxCookLevel.setPlaceholder("Filter by maximum cook level");
    }

    private void createFilterByMinEvaluationLevel() {
        filterByMinEvaluationLevel = new TextField();
        filterByMinEvaluationLevel.setWidth("18em");
        filterByMinEvaluationLevel.setPlaceholder("Filter by minimum evaluation level");
    }

    private void createFilterByMaxEvaluationLevel() {
        filterByMaxEvaluationLevel = new TextField();
        filterByMaxEvaluationLevel.setWidth("18em");
        filterByMaxEvaluationLevel.setPlaceholder("Filter by maximum evaluation level");
    }
}