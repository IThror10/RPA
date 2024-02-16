package com.RPA.entity;

import com.RPA.entity.num.OperatingSystem;

import java.util.List;

public record InMemoryRobot(
        OperatingSystem os,
        String version,
        String versionFrom,
        String hostPort,
        List<Long> groups,
        Boolean isInteractive
) { }
