package com.hcmut.dacn.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import jakarta.persistence.*;

@Entity
@Table(name = "favorite_recipe")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FavoriteRecipeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id",nullable = false)
    private UserEntity user;

    @ManyToOne
    @JoinColumn(name = "recipe_id",nullable = false)
    private RecipeEntity recipe;

    private String note;
    public FavoriteRecipeEntity(UserEntity user, RecipeEntity recipe, String note) {
        this.user = user;
        this.recipe = recipe;
        this.note = note;
    }
}
