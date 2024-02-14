package com.RPA.entity;

import com.RPA.entity.id.UserGroupId;
import com.RPA.entity.num.GroupRole;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "members")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Member {
    @EmbeddedId
    private UserGroupId id;

    @ManyToOne
    @JoinColumn(name = "user_id", insertable = false, updatable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "group_id", insertable = false, updatable = false)
    private Group group;

    @Enumerated(EnumType.ORDINAL)
    @Column(name = "status", nullable = false)
    private GroupRole role;
}
