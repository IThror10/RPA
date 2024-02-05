package com.RPA.repository;

import com.RPA.entity.Group;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GroupRepository extends CrudRepository<Group, Long> {
    boolean existsByName(String name);
}
