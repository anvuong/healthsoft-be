package com.anvuong.healthsoft.models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;

import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.GenericGenerator;

import lombok.Data;

@Data
@Entity
@Table(name= "patients",
       indexes = {
           @Index(name = "first_name_idx", columnList = "first_name"),
           @Index(name = "last_name_idx", columnList = "last_name"),
           @Index(name = "patient_id_idx", columnList = "patient_id", unique = true),
           @Index(name = "dob_idx", columnList = "dob"),
           @Index(name = "gender_idx", columnList = "gender"),
           @Index(name = "soft_deleted_idx", columnList = "soft_deleted"),
       })
public class Patient {
	@Id
    @GeneratedValue(generator = "UUID")
	@GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name="id")
    private String id;

    @Column(name="first_name", nullable = false)
    @NotEmpty(message="firstName should not be empty")
    private String firstName;

    @Column(name="middle_name", nullable = true)
    private String middleName;

    @Column(name="last_name", nullable = false)
    @NotEmpty(message="lastName should not be empty")
    private String lastName;

    @Column(name="patient_id", nullable = false)
    @NotEmpty(message="patientId should not be empty")
    private String patientId;

    @Column(name="dob", nullable = false)
    @NotEmpty(message="Date of birth should not be empty")
    private String dob;

    @Column(name="gender", nullable = false)
    @Pattern(regexp = "M|F|O", message="gender should be M, F or O")
    private String gender;

    @Column(name="soft_deleted", nullable = false)
    @ColumnDefault("false")
    private boolean softDeleted;
}
