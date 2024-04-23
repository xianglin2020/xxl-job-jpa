package com.xxl.job.admin.repository;

import com.xxl.job.admin.core.model.XxlJobLock;
import org.springframework.data.jpa.repository.JpaRepository;

public interface XxlJobLockRepository extends JpaRepository<XxlJobLock, String> {
}
