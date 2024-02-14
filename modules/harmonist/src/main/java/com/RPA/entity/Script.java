package com.RPA.entity;

import com.RPA.entity.num.OperatingSystem;
import com.fasterxml.jackson.databind.JsonNode;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.sql.Timestamp;

@Entity
@Table(name="scripts")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Script {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "scripts_id_seq")
    @SequenceGenerator(name = "scripts_id_seq", sequenceName = "scripts_id_seq", allocationSize = 1)
    private Long id;

    @Column(name = "code", nullable = false)
    private String code;

    @Column(name = "name", nullable = false, unique = true)
    private String name;

    @Column(name = "description", nullable = false)
    private String description;

    @Column(name = "input_data", columnDefinition = "jsonb", nullable = false)
    @JdbcTypeCode(SqlTypes.JSON)
    private JsonNode inputData;

    @Column(name = "version", nullable = false)
    private String version;

    @Enumerated(EnumType.STRING)
    @Column(name = "os", nullable = false)
    private OperatingSystem os;

    @Column(name = "created", nullable = false)
    private Timestamp created;

    @ManyToOne
    @JoinColumn(name = "creator", referencedColumnName = "id")
    private User creator;
}