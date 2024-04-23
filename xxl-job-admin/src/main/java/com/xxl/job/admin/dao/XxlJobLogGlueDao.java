package com.xxl.job.admin.dao;

import com.xxl.job.admin.core.model.XxlJobLogGlue;
import com.xxl.job.admin.repository.XxlJobLogGlueRepository;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * job log for glue
 *
 * @author xuxueli 2016-5-19 18:04:56
 */
@Repository
public class XxlJobLogGlueDao {
    private final XxlJobLogGlueRepository xxlJobLogGlueRepository;

    public XxlJobLogGlueDao(XxlJobLogGlueRepository xxlJobLogGlueRepository) {
        this.xxlJobLogGlueRepository = xxlJobLogGlueRepository;
    }

    public int save(XxlJobLogGlue xxlJobLogGlue) {
        if (xxlJobLogGlue.getJobId() == null) {
            xxlJobLogGlue.setJobId(0);
        }
        xxlJobLogGlueRepository.save(xxlJobLogGlue);
        return 1;
    }

    public List<XxlJobLogGlue> findByJobId(int jobId) {
        return xxlJobLogGlueRepository.findAllByJobIdOrderByIdDesc(jobId);
    }

    public int removeOld(int jobId, int limit) {
        List<Integer> list = xxlJobLogGlueRepository.findAllByJobIdOrderByUpdateTimeDesc(jobId, Pageable.ofSize(limit))
                .map(XxlJobLogGlue::getId).getContent();
        return xxlJobLogGlueRepository.deleteAllByJobIdAndIdNotIn(jobId, list);
    }

    public int deleteByJobId(int jobId) {
        return xxlJobLogGlueRepository.deleteAllByJobId(jobId);
    }
}
