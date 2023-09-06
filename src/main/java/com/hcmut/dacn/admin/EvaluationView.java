package com.hcmut.dacn.admin;


import com.hcmut.dacn.dto.admin.EvaluationAdmin;
import com.hcmut.dacn.dto.admin.RecipeAdmin;
import com.hcmut.dacn.dto.admin.UserAdmin;
import com.hcmut.dacn.mapper.EvaluationMapper;
import com.hcmut.dacn.mapper.RecipeMapper;
import com.hcmut.dacn.mapper.UserMapper;
import com.hcmut.dacn.repository.EvaluationRepository;
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

//@Route("evaluation")
public class EvaluationView extends VerticalLayout {

    private final EvaluationRepository repo;
    private final EvaluationMapper evaluationMapper;
    private List<EvaluationAdmin> evaluationAdmins;
    private final EvaluationEditor evaluationEditor;
    private TextField filterByContent;
    private TextField filterByNote;
    private TextField filterByUserId;
    private TextField filterByMinNumLike;
    private TextField filterByMaxNumLike;
    private TextField filterByMinNumDislike;
    private TextField filterByMaxNumDislike;
    private TextField filterByMinNumStar;
    private TextField filterByMaxNumStar;
    private TextField filterByRecipeId;

    private final Button addNewBtn = new Button("New evaluation", VaadinIcon.PLUS.create());
    private final Button searchBtn = new Button("Search", VaadinIcon.SEARCH.create());

    private final Button clearBtn = new Button("Clear");

    final Grid<EvaluationAdmin> grid;

    public EvaluationView(EvaluationRepository repo, EvaluationMapper evaluationMapper, EvaluationEditor evaluationEditor) {
        this.evaluationEditor = evaluationEditor;
        this.repo = repo;
        this.evaluationMapper = evaluationMapper;
        this.grid = new Grid<>(EvaluationAdmin.class);
        createFilterByContent();
        createFilterByNote();
        createFilterByUserId();
        createFilterByMinNumLike();
        createFilterByMaxNumLike();
        createFilterByMinNumDislike();
        createFilterByMaxNumDislike();
        createFilterByMinNumStar();
        createFilterByMaxNumStar();
        createFilterRecipeId();
        createSearchBtn();
        createClearBtn();
        HorizontalLayout searchComponent1 = new HorizontalLayout(filterByContent, filterByNote, filterByUserId, filterByRecipeId, clearBtn, searchBtn);
        HorizontalLayout searchComponent2 = new HorizontalLayout(filterByMinNumLike, filterByMaxNumLike, filterByMinNumDislike, filterByMaxNumDislike);
        HorizontalLayout searchComponent3 = new HorizontalLayout(filterByMinNumStar, filterByMaxNumStar);
        createAddRecipeBtn();
        add(searchComponent1, searchComponent2, searchComponent3, grid, evaluationEditor, addNewBtn);
        grid.asSingleSelect().addValueChangeListener(e -> {
            evaluationEditor.editCustomer(e.getValue());
        });

        evaluationAdmins = evaluationMapper.toAdminDtos(repo.findAll());
        grid.setItems(evaluationAdmins);
    }

    private void createClearBtn() {
        clearBtn.addClickListener(e -> {
            filterByContent.clear();
            filterByMinNumLike.clear();
            filterByMaxNumLike.clear();
            filterByMinNumDislike.clear();
            filterByMaxNumDislike.clear();
            filterByMinNumStar.clear();
            filterByMaxNumStar.clear();
            filterByNote.clear();
            filterByUserId.clear();
            filterByRecipeId.clear();
        });
    }

    private void createSearchBtn() {
        searchBtn.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        searchBtn.addClickListener((e) -> {
            List<EvaluationAdmin> evaluationAdmins = evaluationMapper.toAdminDtos(repo.findAll());
            if (!filterByContent.isEmpty()) {
                evaluationAdmins = evaluationAdmins.stream().filter(r -> r.getContent().contains(filterByContent.getValue())).collect(Collectors.toList());
            }
            if (!filterByNote.isEmpty()) {
                evaluationAdmins = evaluationAdmins.stream().filter(r -> r.getNote().equals(filterByNote.getValue())).collect(Collectors.toList());
            }
            if (!filterByUserId.isEmpty()) {
                evaluationAdmins = evaluationAdmins.stream().filter(r -> r.getUserId() == Long.parseLong(filterByUserId.getValue())).collect(Collectors.toList());
            }
            if (!filterByRecipeId.isEmpty()) {
                evaluationAdmins = evaluationAdmins.stream().filter(r -> r.getRecipeId() == Long.parseLong(filterByRecipeId.getValue())).collect(Collectors.toList());
            }
            if (!filterByMinNumLike.isEmpty()) {
                evaluationAdmins = evaluationAdmins.stream().filter(r -> r.getNumLike() >= Integer.parseInt(filterByMinNumLike.getValue())).collect(Collectors.toList());
            }
            if (!filterByMaxNumLike.isEmpty()) {
                evaluationAdmins = evaluationAdmins.stream().filter(r->r.getNumLike() <= Integer.parseInt(filterByMaxNumLike.getValue())).collect(Collectors.toList());
            }

            if (!filterByMinNumDislike.isEmpty()) {
                evaluationAdmins = evaluationAdmins.stream().filter(r -> r.getNumDislike() >= Integer.parseInt(filterByMinNumDislike.getValue())).collect(Collectors.toList());
            }
            if (!filterByMaxNumDislike.isEmpty()) {
                evaluationAdmins = evaluationAdmins.stream().filter(r->r.getNumDislike() <= Integer.parseInt(filterByMaxNumDislike.getValue())).collect(Collectors.toList());
            }

            if (!filterByMinNumStar.isEmpty()) {
                evaluationAdmins = evaluationAdmins.stream().filter(r -> r.getNumStar() >= Integer.parseInt(filterByMinNumStar.getValue())).collect(Collectors.toList());
            }
            if (!filterByMaxNumStar.isEmpty()) {
                evaluationAdmins = evaluationAdmins.stream().filter(r->r.getNumStar() <= Integer.parseInt(filterByMaxNumStar.getValue())).collect(Collectors.toList());
            }
            grid.setItems(evaluationAdmins);
        });
    }

    private void createAddRecipeBtn() {
        addNewBtn.addClickListener(e -> {
            evaluationEditor.editCustomer(new EvaluationAdmin());
            addNewBtn.setVisible(false);
        });
        evaluationEditor.setChangeHandler(() -> {
            evaluationEditor.setVisible(false);
            searchBtn.click();
            addNewBtn.setVisible(true);
        });
    }

    private void createFilterByContent() {
        filterByContent = new TextField();
        filterByContent.setWidth("18em");
        filterByContent.setPlaceholder("Filter by content");
        filterByContent.setValueChangeMode(ValueChangeMode.LAZY);
        filterByContent.addValueChangeListener(e -> searchBtn.click());
    }

    private void createFilterByNote() {
        filterByNote = new TextField();
        filterByNote.setWidth("18em");
        filterByNote.setPlaceholder("Filter by note");
        filterByNote.setValueChangeMode(ValueChangeMode.LAZY);
        filterByNote.addValueChangeListener(e -> searchBtn.click());
    }

    private void createFilterByUserId() {
        filterByUserId = new TextField();
        filterByUserId.setWidth("18em");
        filterByUserId.setPlaceholder("Filter by user id");
    }

    private void createFilterByMinNumLike() {
        filterByMinNumLike = new TextField();
        filterByMinNumLike.setWidth("18em");
        filterByMinNumLike.setPlaceholder("Filter by minimum num like");
    }
    private void createFilterByMaxNumLike() {
        filterByMaxNumLike = new TextField();
        filterByMaxNumLike.setWidth("18em");
        filterByMaxNumLike.setPlaceholder("Filter by maximum num like");
    }

    private void createFilterByMinNumDislike() {
        filterByMinNumDislike = new TextField();
        filterByMinNumDislike.setWidth("18em");
        filterByMinNumDislike.setPlaceholder("Filter by minimum num dislike");
    }
    private void createFilterByMaxNumDislike() {
        filterByMaxNumDislike = new TextField();
        filterByMaxNumDislike.setWidth("18em");
        filterByMaxNumDislike.setPlaceholder("Filter by maximum num dislike");
    }

    private void createFilterByMinNumStar() {
        filterByMinNumStar = new TextField();
        filterByMinNumStar.setWidth("18em");
        filterByMinNumStar.setPlaceholder("Filter by minimum num star");
    }
    private void createFilterByMaxNumStar() {
        filterByMaxNumStar = new TextField();
        filterByMaxNumStar.setWidth("18em");
        filterByMaxNumStar.setPlaceholder("Filter by maximum num star");
    }

    private void createFilterRecipeId() {
        filterByRecipeId = new TextField();
        filterByRecipeId.setWidth("18em");
        filterByRecipeId.setPlaceholder("Filter by recipe id");
    }
}