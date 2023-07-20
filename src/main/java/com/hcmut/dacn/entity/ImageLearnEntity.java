package com.hcmut.dacn.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "image_learn")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ImageLearnEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private byte[] imageData;

    @ManyToOne
    @JoinColumn(name = "learnt_recipe_id",nullable = false)
    private LearntRecipeEntity learntRecipe;
}
