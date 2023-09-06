package com.hcmut.dacn.admin;

import com.hcmut.dacn.dto.RecipeDto;
import com.hcmut.dacn.dto.admin.RecipeAdmin;
import com.hcmut.dacn.entity.RecipeEntity;
import com.hcmut.dacn.entity.UserEntity;
import com.hcmut.dacn.mapper.RecipeMapper;
import com.hcmut.dacn.repository.RecipeRepository;
import com.hcmut.dacn.repository.UserRepository;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.KeyNotifier;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;
import org.springframework.beans.factory.annotation.Autowired;

//@SpringComponent
//@UIScope
public class RecipeEditor extends VerticalLayout implements KeyNotifier {
    private final RecipeRepository recipeRepository;
    private final UserRepository userRepository;
    private final RecipeMapper recipeMapper;
    private RecipeAdmin recipe;
    TextField name = new TextField("Name");
    TextField numStar = new TextField("Num Star");
    TextField numEvaluation = new TextField("Num Evaluation");
    TextField numFavorite = new TextField("Num Favorite");
    TextField ownerId = new TextField("Owner Id");

    /* Action buttons */
    Button save = new Button("Save", VaadinIcon.CHECK.create());
    Button cancel = new Button("Cancel");
    Button delete = new Button("Delete", VaadinIcon.TRASH.create());
    HorizontalLayout actions = new HorizontalLayout(save, delete, cancel);
    HorizontalLayout editComponent = new HorizontalLayout(name, numEvaluation, numFavorite, numStar, ownerId);
    Binder<RecipeAdmin> binder = new Binder<>(RecipeAdmin.class);
    private ChangeHandler changeHandler;

    @Autowired
    public RecipeEditor(RecipeRepository recipeRepository, RecipeMapper recipeMapper, UserRepository userRepository) {
        this.recipeRepository = recipeRepository;
        this.userRepository = userRepository;
        this.recipeMapper = recipeMapper;

        add(editComponent, actions);

        binder.bindInstanceFields(this);

        setSpacing(true);

        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        delete.addThemeVariants(ButtonVariant.LUMO_ERROR);

        addKeyPressListener(Key.ENTER, e -> save());

        save.addClickListener(e -> save());
        delete.addClickListener(e -> delete());
        cancel.addClickListener(e -> {
            this.setVisible(false);
            changeHandler.onChange();
        });
        setVisible(false);
    }
    void delete() {
        RecipeEntity recipeEntity = recipeRepository.findById(recipe.getId()).get();
        recipeRepository.delete(recipeEntity);
        changeHandler.onChange();
    }
    void save() {
        RecipeEntity recipeEntity = recipe.getId() != null ? recipeRepository.findById(recipe.getId()).get() : new RecipeEntity();
        recipeEntity.setNumStar(recipe.getNumStar());
        recipeEntity.setNumFavorite(recipe.getNumFavorite());
        recipeEntity.setNumEvaluation(recipe.getNumEvaluation());
        UserEntity ownerEntity = userRepository.findById(recipe.getOwnerId()).get();
        recipeEntity.setOwner(ownerEntity);
        recipeEntity.setName(recipe.getName());

        recipeRepository.save(recipeEntity);
        changeHandler.onChange();
    }
    public interface ChangeHandler {
        void onChange();
    }
    public final void editCustomer(RecipeAdmin c) {
        if (c == null) {
            setVisible(false);
            return;
        }
        final boolean persisted = c.getId() != null;
        if (persisted) {
            recipe = recipeMapper.toAdminDto(recipeRepository.findById(c.getId()).get());
        } else {
            recipe = c;
        }
        delete.setVisible(persisted);
        binder.setBean(recipe);
        setVisible(true);
        name.focus();
    }
    public void setChangeHandler(ChangeHandler h) {
        changeHandler = h;
    }
}
