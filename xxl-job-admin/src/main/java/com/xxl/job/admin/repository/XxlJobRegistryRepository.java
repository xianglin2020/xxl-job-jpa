package com.xxl.job.admin.repository;

import com.xxl.job.admin.core.model.XxlJobRegistry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.util.Streamable;

import java.util.Date;
import java.util.List;

public interface XxlJobRegistryRepository extends JpaRepository<XxlJobRegistry, Integer> {
    Streamable<XxlJobRegistry> findAllByUpdateTimeBefore(Date updateTime);

    List<XxlJobRegistry> findAllByUpdateTimeAfter(Date updateTime);
}
