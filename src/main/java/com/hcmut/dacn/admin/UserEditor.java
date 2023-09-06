package com.hcmut.dacn.admin;

import com.hcmut.dacn.dto.admin.UserAdmin;
import com.hcmut.dacn.entity.UserEntity;
import com.hcmut.dacn.mapper.UserMapper;
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
//
//@SpringComponent
//@UIScope
public class UserEditor extends VerticalLayout implements KeyNotifier {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private UserAdmin userAdmin;
    TextField fullName = new TextField("Full Name");
    TextField username = new TextField("Username");
    TextField password = new TextField("Password");
    TextField cookLevel = new TextField("Cook Level");
    TextField evaluationLevel = new TextField("Evaluation Level");

    /* Action buttons */
    Button save = new Button("Save", VaadinIcon.CHECK.create());
    Button cancel = new Button("Cancel");
    Button delete = new Button("Delete", VaadinIcon.TRASH.create());
    HorizontalLayout actions = new HorizontalLayout(save, delete, cancel);
    HorizontalLayout editComponent = new HorizontalLayout(fullName, username, password, cookLevel, evaluationLevel);
    Binder<UserAdmin> binder = new Binder<>(UserAdmin.class);
    private ChangeHandler changeHandler;

    @Autowired
    public UserEditor(UserMapper userMapper, UserRepository userRepository) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;

        add(editComponent, actions);

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
        UserEntity userEntity = userRepository.findById(userAdmin.getId()).get();
        userRepository.delete(userEntity);
        changeHandler.onChange();
    }
    void save() {
        UserEntity userEntity = userAdmin.getId() != null ? userRepository.findById(userAdmin.getId()).get() : new UserEntity();
        userEntity.setFullName(userAdmin.getFullName());
        userEntity.setUsername(userAdmin.getUsername());
        userEntity.setPassword(userAdmin.getPassword());
        userEntity.setCookLevel(userAdmin.getCookLevel());
        userEntity.setEvaluationLevel(userAdmin.getEvaluationLevel());

        userRepository.save(userEntity);
        changeHandler.onChange();
    }
    public interface ChangeHandler {
        void onChange();
    }
    public final void editCustomer(UserAdmin c) {
        if (c == null) {
            setVisible(false);
            return;
        }
        final boolean persisted = c.getId() != null;
        if (persisted) {
            userAdmin = userMapper.toAdminDto(userRepository.findById(c.getId()).get());
        } else {
            userAdmin = c;
        }
        delete.setVisible(persisted);
        binder.setBean(userAdmin);
        setVisible(true);
        fullName.focus();
    }
    public void setChangeHandler(ChangeHandler h) {
        changeHandler = h;
    }
}
