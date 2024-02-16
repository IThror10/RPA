package com.RPA.entity.id;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Objects;

@Data
@Embeddable
@NoArgsConstructor
@AllArgsConstructor
public class GroupScriptId implements Serializable {
    @Column(name = "group_id")
    private Long groupId;

    @Column(name = "script_id")
    private Long scriptId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GroupScriptId groupScriptId = (GroupScriptId) o;
        return Objects.equals(scriptId, groupScriptId.scriptId) &&
                Objects.equals(groupId, groupScriptId.groupId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(scriptId, groupId);
    }
}
