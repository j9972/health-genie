package com.example.healthgenie.domain.user.entity;


import com.example.healthgenie.global.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.sql.Blob;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "uni_verify_tb")
public class UniVerify extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "uni_verify_id")
    private Long id;

    @Column(name ="student_number")
    private Long studentNumber;

    @Column(name = "uni_name")
    private String uniName;

    @Lob
    private Blob uniVerifyPic;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User member;
}
