package com.hcmut.dacn.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import jakarta.persistence.*;

@Entity
@Table(name = "Users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(name = "image_url", nullable = false)
    private String imageUrl;

    @Column(name = "cook_level", nullable = false, columnDefinition = "float8 default 0")
    private Double cookLevel=(double)0;

    @Column(nullable = false, columnDefinition = "float8 default 0")
    private Double reputation=(double)0;
}
