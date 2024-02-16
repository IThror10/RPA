package com.RPA.entity;

import com.RPA.entity.id.GroupScriptId;
import com.RPA.entity.id.UserScriptId;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "executors")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Executor {
    @EmbeddedId
    private GroupScriptId id;

    @ManyToOne
    @JoinColumn(name = "group_id", insertable = false, updatable = false)
    private Group group;

    @ManyToOne
    @JoinColumn(name = "script_id", insertable = false, updatable = false)
    private Script script;
}
