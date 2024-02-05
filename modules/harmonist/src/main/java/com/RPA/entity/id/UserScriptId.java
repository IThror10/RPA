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
public class UserScriptId implements Serializable {
    @Column(name = "user_id")
    private Long userId;

    @Column(name = "script_id")
    private Long scriptId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserScriptId userScriptId = (UserScriptId) o;
        return Objects.equals(userId, userScriptId.userId) &&
                Objects.equals(scriptId, userScriptId.scriptId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, scriptId);
    }
}
