package com.RPA.repository;

import com.RPA.entity.Author;
import com.RPA.entity.Script;
import com.RPA.entity.User;
import com.RPA.entity.id.UserScriptId;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface AuthorRepository extends CrudRepository<Author, UserScriptId> {
    @Modifying
    @Transactional
    @Query("DELETE FROM Author a WHERE a.id.scriptId = :scriptId")
    void deleteAuthorsByScriptId(@Param("scriptId") Long scriptId);

    @Query("SELECT a.script FROM Author a WHERE a.user.id = :userId")
    List<Script> findScriptsByUserId(@Param("userId") Long userId);

    @Query("SELECT a.user FROM Author a WHERE a.script.id = :scriptId")
    List<User> findAuthorsByScriptId(@Param("scriptId") Long scriptId);
}
