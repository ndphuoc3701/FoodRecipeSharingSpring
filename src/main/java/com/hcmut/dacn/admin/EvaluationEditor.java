package com.hcmut.dacn.admin;

import com.hcmut.dacn.dto.admin.EvaluationAdmin;
import com.hcmut.dacn.dto.admin.UserAdmin;
import com.hcmut.dacn.entity.EvaluationEntity;
import com.hcmut.dacn.entity.UserEntity;
import com.hcmut.dacn.mapper.EvaluationMapper;
import com.hcmut.dacn.mapper.UserMapper;
import com.hcmut.dacn.repository.EvaluationRepository;
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
public class EvaluationEditor extends VerticalLayout implements KeyNotifier {
    private final EvaluationRepository evaluationRepository;
    private final UserRepository userRepository;
    private final EvaluationMapper evaluationMapper;
    private EvaluationAdmin evaluationAdmin;
    TextField content = new TextField("Content");
    TextField numLike = new TextField("Num Like");
    TextField numDislike = new TextField("Num Dislike");
    TextField numStar = new TextField("Num Star");
    TextField note = new TextField("Note");
    TextField userId = new TextField("User Id");
    TextField recipeId = new TextField("Recipe Id");


    /* Action buttons */
    Button save = new Button("Save", VaadinIcon.CHECK.create());
    Button cancel = new Button("Cancel");
    Button delete = new Button("Delete", VaadinIcon.TRASH.create());
    HorizontalLayout actions = new HorizontalLayout(save, delete, cancel);
    HorizontalLayout editComponent1 = new HorizontalLayout(content, numLike, numDislike);
    HorizontalLayout editComponent2 = new HorizontalLayout(numStar, note, userId);
    Binder<EvaluationAdmin> binder = new Binder<>(EvaluationAdmin.class);
    private ChangeHandler changeHandler;

    @Autowired
    public EvaluationEditor(EvaluationMapper evaluationMapper, EvaluationRepository evaluationRepository,UserRepository userRepository) {
        this.evaluationRepository = evaluationRepository;
        this.evaluationMapper = evaluationMapper;
        this.userRepository=userRepository;
        add(editComponent1, editComponent2, actions);

        // bind using naming convention
        binder.bindInstanceFields(this);

        // Configure and style components
        setSpacing(true);

        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        delete.addThemeVariants(ButtonVariant.LUMO_ERROR);

        addKeyPressListener(Key.ENTER, e -> save());

        // wire action buttons to save, delete and reset
        save.addClickListener(e -> save());
        delete.addClickListener(e -> delete());
        cancel.addClickListener(e -> {
            this.setVisible(false);
            changeHandler.onChange();
        });
        setVisible(false);
    }
    void delete() {
        EvaluationEntity evaluationEntity = evaluationRepository.findById(evaluationAdmin.getId()).get();
        evaluationRepository.delete(evaluationEntity);
        changeHandler.onChange();
    }
    void save() {
        EvaluationEntity evaluationEntity = evaluationAdmin.getId() != null ? evaluationRepository.findById(evaluationAdmin.getId()).get() : new EvaluationEntity();
        evaluationEntity.setContent(evaluationAdmin.getContent());
        evaluationEntity.setNumLike(evaluationAdmin.getNumLike());
        evaluationEntity.setNumDislike(evaluationAdmin.getNumDislike());
        evaluationEntity.setNumStar(evaluationAdmin.getNumStar());
        evaluationEntity.setNote(evaluationAdmin.getNote());
        evaluationEntity.setUser(userRepository.findById(evaluationAdmin.getUserId()).get());

        evaluationRepository.save(evaluationEntity);
        changeHandler.onChange();
    }
    public interface ChangeHandler {
        void onChange();
    }
    public final void editCustomer(EvaluationAdmin c) {
        if (c == null) {
            setVisible(false);
            return;
        }
        final boolean persisted = c.getId() != null;
        if (persisted) {
            evaluationAdmin = evaluationMapper.toAdminDto(evaluationRepository.findById(c.getId()).get());
        } else {
            evaluationAdmin = c;
        }
        delete.setVisible(persisted);
        binder.setBean(evaluationAdmin);
        setVisible(true);
        content.focus();
    }
    public void setChangeHandler(ChangeHandler h) {
        changeHandler = h;
    }
}
