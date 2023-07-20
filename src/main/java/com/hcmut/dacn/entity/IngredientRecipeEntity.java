package com.hcmut.dacn.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "ingredient_recipe")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class IngredientRecipeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String quantity;

    private String name;

    @ManyToOne
    @JoinColumn(name = "recipe_id",nullable = false)
    RecipeEntity recipe;
}
