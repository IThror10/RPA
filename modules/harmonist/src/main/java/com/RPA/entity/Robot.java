package com.RPA.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name="robots")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Robot {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "robots_id_seq")
    @SequenceGenerator(name = "robots_id_seq", sequenceName = "robots_id_seq", allocationSize = 1)
    private Long id;

    @Column(name = "ver", unique = true, nullable = false)
    private String version;

    @Column(name = "ver_from", nullable = false)
    private String version_from;

    @Column(name = "help", nullable = false)
    private String manual;
}
