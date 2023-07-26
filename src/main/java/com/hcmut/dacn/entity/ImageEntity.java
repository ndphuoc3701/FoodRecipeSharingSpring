package com.hcmut.dacn.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "images")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ImageEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private byte[] data;

    @ManyToOne
    @JoinColumn(name = "instruction_id")
    private InstructionEntity instruction;

    @ManyToOne
    @JoinColumn(name = "evaluation_id")
    private EvaluationEntity evaluation;
}
