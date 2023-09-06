package com.hcmut.dacn.admin;

import com.hcmut.dacn.dto.admin.RecipeAdmin;
import com.hcmut.dacn.mapper.RecipeMapper;
import com.hcmut.dacn.repository.RecipeRepository;
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

//@Route
public class MainView extends VerticalLayout {

    private final RecipeRepository repo;
    private List<RecipeAdmin> recipeAdmins;
    private final RecipeMapper recipeMapper;
    private final RecipeEditor recipeEditor;
    private TextField filterByName;
    private TextField filterByOwnerId;
    private TextField filterByMinNumStar;
    private TextField filterByMaxNumStar;
    private final Button addNewBtn = new Button("New recipe", VaadinIcon.PLUS.create());
    private final Button searchBtn = new Button("Search", VaadinIcon.SEARCH.create());
    private final Button clearBtn = new Button("Clear");
    final Grid<RecipeAdmin> grid;

    public MainView(RecipeRepository repo, RecipeMapper recipeMapper, RecipeEditor recipeEditor) {
        this.recipeEditor = recipeEditor;
        this.repo = repo;
        this.recipeMapper = recipeMapper;
        this.grid = new Grid<>(RecipeAdmin.class);
        createFilterByName();
        createFilterByOwnerId();
        createFilterByMinNumStar();
        createFilterByMaxNumStar();
        createSearchBtn();
        createClearBtn();
        HorizontalLayout searchComponent = new HorizontalLayout(filterByName, filterByOwnerId, filterByMinNumStar, filterByMaxNumStar, clearBtn, searchBtn);
        createAddRecipeBtn();
        add(searchComponent, grid, recipeEditor, addNewBtn);
        grid.asSingleSelect().addValueChangeListener(e -> {
            recipeEditor.editCustomer(e.getValue());
        });
        recipeAdmins = recipeMapper.toAdminDtos(repo.findAll());
        grid.setItems(recipeAdmins);
    }

    private void createClearBtn() {
        clearBtn.addClickListener(e -> {
            filterByName.clear();
            filterByOwnerId.clear();
            filterByMaxNumStar.clear();
            filterByMinNumStar.clear();
        });
    }

    private void createSearchBtn() {
        searchBtn.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        searchBtn.addClickListener((e) -> {
            List<RecipeAdmin> recipeAdmins = recipeMapper.toAdminDtos(repo.findAll());
            if (!filterByName.isEmpty()) {
                recipeAdmins = recipeAdmins.stream().filter(r -> r.getName().contains(filterByName.getValue())).collect(Collectors.toList());
            }
            if (!filterByOwnerId.isEmpty()) {
                recipeAdmins = recipeAdmins.stream().filter(r -> r.getOwnerId().toString().equals(filterByOwnerId.getValue())).collect(Collectors.toList());
            }
            if (!filterByMinNumStar.isEmpty()) {
                recipeAdmins = recipeAdmins.stream().filter(r -> r.getNumStar() >= Integer.parseInt(filterByMinNumStar.getValue())).collect(Collectors.toList());
            }
            if (!filterByMaxNumStar.isEmpty()) {
                recipeAdmins = recipeAdmins.stream().filter(r -> r.getNumStar() <= Integer.parseInt(filterByMaxNumStar.getValue())).collect(Collectors.toList());
            }
            grid.setItems(recipeAdmins);
        });
    }

    private void createAddRecipeBtn() {
        addNewBtn.addClickListener(e -> {
            recipeEditor.editCustomer(new RecipeAdmin());
            addNewBtn.setVisible(false);
        });
        recipeEditor.setChangeHandler(() -> {
            recipeEditor.setVisible(false);
            searchBtn.click();
            addNewBtn.setVisible(true);
        });
    }

    private void createFilterByName() {
        filterByName = new TextField();
        filterByName.setPlaceholder("Filter by recipe name");
        filterByName.setValueChangeMode(ValueChangeMode.LAZY);
        filterByName.addValueChangeListener(e -> searchBtn.click());
    }

    private void createFilterByMinNumStar() {
        filterByMinNumStar = new TextField();
        filterByMinNumStar.setPlaceholder("Filter by minimum num star");
    }

    private void createFilterByMaxNumStar() {
        filterByMaxNumStar = new TextField();
        filterByMaxNumStar.setPlaceholder("Filter by maximum num star");
    }

    private void createFilterByOwnerId() {
        filterByOwnerId = new TextField();
        filterByOwnerId.setPlaceholder("Filter by owner id");
    }
}