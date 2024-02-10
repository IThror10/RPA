package com.RPA.repository;

import com.RPA.entity.Member;
import com.RPA.entity.id.UserGroupId;
import com.RPA.response.GroupWithRoleResponse;
import com.RPA.response.MemberWithRoleResponse;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public interface MemberRepository extends CrudRepository<Member, UserGroupId> {
    @Query("SELECT new com.RPA.response.GroupWithRoleResponse" +
            "(m.group.id, m.group.description, m.group.name, m.role) FROM Member m WHERE m.user.id = :userId")
    List<GroupWithRoleResponse> findUserMembershipById(@Param("userId") Long userId);

    @Query("SELECT new com.RPA.response.MemberWithRoleResponse" +
            "(m.user.username, m.user.phone, m.user.email, m.role) FROM Member m WHERE m.group.id = :groupId")
    List<MemberWithRoleResponse> findMembersByGroupId(@Param("groupId") Long groupId);

    @Query("SELECT m.group.id FROM Member m WHERE m.user.id = :userId")
    Set<Long> findGroupsByUserId(@Param("userId") Long userId);

    @Modifying
    @Transactional
    @Query("DELETE FROM Member m WHERE m.id.groupId = :groupId")
    void deleteMembersByGroupId(@Param("groupId") Long groupId);
}
