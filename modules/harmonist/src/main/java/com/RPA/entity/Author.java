package com.RPA.entity;

import com.RPA.entity.id.UserScriptId;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "authors")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Author {
    @EmbeddedId
    private UserScriptId id;

    @ManyToOne
    @JoinColumn(name = "user_id", insertable = false, updatable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "script_id", insertable = false, updatable = false)
    private Script script;
}
