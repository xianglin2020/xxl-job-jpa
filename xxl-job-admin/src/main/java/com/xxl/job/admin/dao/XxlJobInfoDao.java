package com.xxl.job.admin.dao;

import com.xxl.job.admin.core.model.XxlJobInfo;
import com.xxl.job.admin.repository.XxlJobInfoRepository;

import javax.persistence.criteria.Predicate;

import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;


/**
 * job info
 *
 * @author xuxueli 2016-1-12 18:03:45
 */
@Repository
public class XxlJobInfoDao {
    private final XxlJobInfoRepository xxlJobInfoRepository;

    public XxlJobInfoDao(XxlJobInfoRepository xxlJobInfoRepository) {
        this.xxlJobInfoRepository = xxlJobInfoRepository;
    }

    public List<XxlJobInfo> pageList(int offset,
                                     int pagesize,
                                     int jobGroup,
                                     int triggerStatus,
                                     String jobDesc,
                                     String executorHandler,
                                     String author) {
        return page(offset, pagesize, jobGroup, triggerStatus, jobDesc, executorHandler, author).getContent();
    }

    public int pageListCount(int offset,
                             int pagesize,
                             int jobGroup,
                             int triggerStatus,
                             String jobDesc,
                             String executorHandler,
                             String author) {
        return (int) page(0, 1, jobGroup, triggerStatus, jobDesc, executorHandler, author).getTotalElements();
    }

    private Page<XxlJobInfo> page(int offset,
                                  int pagesize,
                                  int jobGroup,
                                  int triggerStatus,
                                  String jobDesc,
                                  String executorHandler,
                                  String author) {
        Specification<XxlJobInfo> specification = (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (jobGroup > 0) {
                predicates.add(cb.equal(root.get("jobGroup"), jobGroup));
            }
            if (triggerStatus >= 0) {
                predicates.add(cb.equal(root.get("triggerStatus"), triggerStatus));
            }

            if (StringUtils.hasText(jobDesc)) {
                predicates.add(cb.like(root.get("jobDesc"), "%" + jobDesc + "%"));
            }
            if (StringUtils.hasText(executorHandler)) {
                predicates.add(cb.like(root.get("executorHandler"), "%" + executorHandler + "%"));
            }
            if (StringUtils.hasText(author)) {
                predicates.add(cb.like(root.get("author"), "%" + author + "%"));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
        Pageable pageable = PageRequest.of(offset / pagesize, pagesize, Sort.Direction.ASC, "id");
        return xxlJobInfoRepository.findAll(specification, pageable);
    }

    public int save(XxlJobInfo info) {
        if (info.getJobGroup() == null) {
            info.setJobGroup(0);
        }
        if (info.getExecutorTimeout() == null) {
            info.setExecutorTimeout(0);
        }
        if (info.getExecutorFailRetryCount() == null) {
            info.setExecutorFailRetryCount(0);
        }
        if (info.getTriggerStatus() == null) {
            info.setTriggerStatus(0);
        }
        if (info.getTriggerLastTime() == null) {
            info.setTriggerLastTime(0L);
        }
        if (info.getTriggerNextTime() == null) {
            info.setTriggerNextTime(0L);
        }

        xxlJobInfoRepository.save(info);
        return 1;
    }

    public XxlJobInfo loadById(int id) {
        return xxlJobInfoRepository.findById(id).orElse(null);
    }

    public int update(XxlJobInfo xxlJobInfo) {
        if (xxlJobInfoRepository.existsById(xxlJobInfo.getId())) {
            xxlJobInfoRepository.save(xxlJobInfo);
            return 1;
        }
        return 0;
    }

    public int delete(int id) {
        if (xxlJobInfoRepository.existsById(id)) {
            xxlJobInfoRepository.deleteById(id);
            return 1;
        }
        return 0;
    }

    public List<XxlJobInfo> getJobsByGroup(int jobGroup) {
        XxlJobInfo probe = new XxlJobInfo();
        probe.setJobGroup(jobGroup);
        return xxlJobInfoRepository.findAll(Example.of(probe));
    }

    public int findAllCount() {
        return (int) xxlJobInfoRepository.count();
    }

    public List<XxlJobInfo> scheduleJobQuery(long maxNextTime, int pagesize) {
        Specification<XxlJobInfo> specification = (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            predicates.add(cb.equal(root.get("triggerStatus"), 1));
            predicates.add(cb.lessThanOrEqualTo(root.get("triggerNextTime"), maxNextTime));
            return cb.and(predicates.toArray(new Predicate[0]));
        };
        Pageable pageable = PageRequest.of(0, pagesize, Sort.Direction.ASC, "id");
        return xxlJobInfoRepository.findAll(specification, pageable).getContent();
    }

    public int scheduleUpdate(XxlJobInfo xxlJobInfo) {
        XxlJobInfo jobInfo = xxlJobInfoRepository.findById(xxlJobInfo.getId()).orElse(null);
        if (jobInfo == null) {
            return 0;
        }
        jobInfo.setTriggerLastTime(xxlJobInfo.getTriggerLastTime());
        jobInfo.setTriggerNextTime(xxlJobInfo.getTriggerNextTime());
        jobInfo.setTriggerStatus(xxlJobInfo.getTriggerStatus());
        xxlJobInfoRepository.save(jobInfo);
        return 1;
    }
}
