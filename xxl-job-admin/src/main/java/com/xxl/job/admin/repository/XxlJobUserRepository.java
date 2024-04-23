package com.xxl.job.admin.repository;

import com.xxl.job.admin.core.model.XxlJobUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface XxlJobUserRepository extends JpaRepository<XxlJobUser, Integer> {
    Optional<XxlJobUser> findByUsername(String username);
}
