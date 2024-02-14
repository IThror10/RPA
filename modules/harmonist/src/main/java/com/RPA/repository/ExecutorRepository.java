package com.RPA.repository;

import com.RPA.entity.Executor;
import com.RPA.entity.Group;
import com.RPA.entity.Script;
import com.RPA.entity.id.GroupScriptId;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ExecutorRepository extends CrudRepository<Executor, GroupScriptId> {
    @Modifying
    @Transactional
    @Query("DELETE FROM Executor e WHERE e.id.scriptId = :scriptId")
    void deleteExecutorsByScriptId(@Param("scriptId") Long scriptId);

    @Query("SELECT DISTINCT e.script FROM Executor e " +
            "JOIN Member m ON m.group.id = e.group.id " +
            "WHERE m.user.id = :userId")
    List<Script> findScriptsByUserInGroup(@Param("userId") Long userId);

    @Query("SELECT e.group FROM Executor e WHERE e.script.id = :scriptId")
    List<Group> findGroupsByScriptId(@Param("scriptId") Long scriptId);
}
