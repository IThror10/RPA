package com.RPA.repository;

import com.RPA.entity.Robot;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RobotRepository extends CrudRepository<Robot, Long> {
    boolean existsByVersion(String version);
}
