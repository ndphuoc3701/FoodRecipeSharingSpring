package com.hcmut.dacn.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Type;

@Entity
@Table(name = "image_evaluation")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ImageEvaluationEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private byte[] imageData;

    @ManyToOne
    @JoinColumn(name = "evaluation_id",nullable = false)
    private EvaluationEntity evaluation;
}
