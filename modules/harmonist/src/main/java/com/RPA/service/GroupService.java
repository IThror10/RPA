package com.RPA.service;

import com.RPA.entity.*;
import com.RPA.entity.id.UserGroupId;
import com.RPA.entity.num.GroupRole;
import com.RPA.exception.ConflictException;
import com.RPA.exception.ForbiddenException;
import com.RPA.exception.NotFoundException;
import com.RPA.repository.GroupRepository;
import com.RPA.repository.MemberRepository;
import com.RPA.request.ChangeGroupDataRequest;
import com.RPA.request.CreateGroupRequest;
import com.RPA.request.UsernameRequest;
import com.RPA.response.GroupResponse;
import com.RPA.response.GroupWithRoleResponse;
import com.RPA.response.MemberWithRoleResponse;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class GroupService {
    private final GroupRepository groupRepository;
    private final MemberRepository memberRepository;
    private final UserService userService;

    @Transactional
    public GroupResponse createGroup(Long uid, CreateGroupRequest request) {
        User user = User.builder().id(uid).build();
        Group group = Group.builder()
                .name(request.name())
                .description(request.description())
                .leaderId(user)
                .build();

        if (groupRepository.existsByName(request.name()))
            throw new ConflictException("Such name is already occupied");

        group = groupRepository.save(group);
        Member member = new Member(
                new UserGroupId(uid, group.getId()),
                user,
                group,
                GroupRole.LEADER
        );

        memberRepository.save(member);
        return new GroupResponse(group);
    }

    public GroupResponse updateGroup(Long uid, Long gid, ChangeGroupDataRequest request) {
        Group group = getManagingGroup(uid, gid);

        if (request.getName() != null && request.getName() != group.getName()) {
            if (groupRepository.existsByName(request.getName()))
                throw new ConflictException("Such name is already occupied");
            group.setName(request.getName());
        }

        if (request.getDescription() != null && request.getDescription() != group.getDescription())
            group.setDescription(request.getDescription());

        return new GroupResponse(groupRepository.save(group));
    }

    @Transactional
    public void deleteGroup(Long uid, Long gid) {
        Group group = getManagingGroup(uid, gid);
        memberRepository.deleteMembersByGroupId(gid);
        groupRepository.delete(group);
    }

    public List<GroupWithRoleResponse> getUsersGroups(Long uid) {
        return memberRepository.findUserMembershipById(uid);
    }

    public List<MemberWithRoleResponse> getGroupsMembers(Long uid, Long gid) {
        getManagingGroup(uid, gid);
        return memberRepository.findMembersByGroupId(gid);
    }

    @Transactional
    public void inviteUser(Long uid, Long gid, UsernameRequest request) {
        Group group = getManagingGroup(uid, gid);
        User user = userService.getByUsername(request.getUsername());

        UserGroupId userGroupId = new UserGroupId(user.getId(), group.getId());
        if (memberRepository.findById(userGroupId).isPresent())
            throw new ConflictException("User already in the group");

        Member member = new Member(userGroupId, user, group, GroupRole.INVITED);
        memberRepository.save(member);
    }

    @Transactional
    public void acceptInvitation(Long uid, Long gid) {
        UserGroupId userGroupId = new UserGroupId(uid, gid);
        Member member = memberRepository.findById(userGroupId).orElseThrow(() -> new NotFoundException("Invitation not found"));

        if (member.getRole() != GroupRole.INVITED)
            throw new ConflictException("User is not in Invited status");
        member.setRole(GroupRole.MEMBER);
        memberRepository.save(member);
    }

    @Transactional
    public void declineInvitation(Long uid, Long gid) {
        UserGroupId userGroupId = new UserGroupId(uid, gid);
        Member member = memberRepository.findById(userGroupId).orElseThrow(() -> new NotFoundException("Invitation not found"));

        if (member.getRole() != GroupRole.INVITED)
            throw new ConflictException("User is not in Invited status");
        memberRepository.delete(member);
    }

    @Transactional
    public void leaveGroup(Long uid, Long gid) {
        UserGroupId userGroupId = new UserGroupId(uid, gid);
        Member member = memberRepository.findById(userGroupId).orElseThrow(() -> new NotFoundException("Membership not found"));

        if (member.getRole() != GroupRole.MEMBER)
            throw new ConflictException("User is not in Member status");
        memberRepository.delete(member);
    }

    public void excludeUser(Long uid, Long gid, UsernameRequest request) {
        Group group = getManagingGroup(uid, gid);
        User user = userService.getByUsername(request.getUsername());

        UserGroupId userGroupId = new UserGroupId(user.getId(), gid);
        Member member = memberRepository.findById(userGroupId).orElseThrow(() -> new NotFoundException("Member not found"));
        if (member.getRole() == GroupRole.LEADER)
            throw new ConflictException("You can't exclude yourself. Delete the group");
        memberRepository.delete(member);
    }

    private Group getManagingGroup(Long uid, Long gid) {
        Group group = getGroupById(gid);

        if (group.getLeaderId().getId() != uid)
            throw new ForbiddenException("Access denied");
        return group;
    }

    private Group getGroupById(Long gid) {
        Group group = groupRepository.findById(gid).orElseThrow(() -> new NotFoundException("Group not found"));
        return group;
    }

    public Set<Long> getUserMembership(Long userId) {
        return memberRepository.findGroupsByUserId(userId);
    }
}
