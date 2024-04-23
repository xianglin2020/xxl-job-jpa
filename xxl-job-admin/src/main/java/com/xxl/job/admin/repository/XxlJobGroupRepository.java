package com.xxl.job.admin.repository;

import com.xxl.job.admin.core.model.XxlJobGroup;
import org.springframework.data.jpa.repository.JpaRepository;

public interface XxlJobGroupRepository extends JpaRepository<XxlJobGroup, Integer> {
    boolean existsByAppname(String appname);
}
