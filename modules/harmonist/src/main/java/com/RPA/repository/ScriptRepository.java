package com.RPA.repository;

import com.RPA.entity.Script;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ScriptRepository extends CrudRepository<Script, Long> {
    boolean existsByName(String name);
}
