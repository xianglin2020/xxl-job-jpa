package com.xxl.job.admin.repository;

import com.xxl.job.admin.core.model.XxlJobLogGlue;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.transaction.Transactional;
import java.util.List;

public interface XxlJobLogGlueRepository extends JpaRepository<XxlJobLogGlue, Integer> {
    List<XxlJobLogGlue> findAllByJobIdOrderByIdDesc(int jobId);

    Page<XxlJobLogGlue> findAllByJobIdOrderByUpdateTimeDesc(int jobId, Pageable pageable);

    @Transactional
    int deleteAllByJobId(int jobId);

    @Transactional
    int deleteAllByJobIdAndIdNotIn(int jobId, List<Integer> ids);
}
