package com.hcmut.dacn.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Table(name = "Instruction")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class InstructionEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String content;

    @Column(nullable = false,name = "step_order")
    private int stepOrder;

    @ManyToOne
    @JoinColumn(name = "recipe_id",nullable = false)
    RecipeEntity recipe;

    @OneToMany(mappedBy = "instruction")
    private List<ImageInstructionEntity> imageInstructions;

}
