package com.hcmut.dacn.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Type;

@Entity
@Table(name = "image_instruction")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ImageInstructionEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private byte[] image;

    @ManyToOne
    @JoinColumn(name = "instruction_id",nullable = false)
    private InstructionEntity instruction;
}
