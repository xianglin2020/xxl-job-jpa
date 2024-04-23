package com.xxl.job.admin.dao;

import com.xxl.job.admin.core.model.XxlJobLog;
import com.xxl.job.admin.repository.XxlJobLogRepository;

import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Subquery;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Repository;

import java.util.*;

/**
 * job log
 *
 * @author xuxueli 2016-1-12 18:03:06
 */
@Repository
public class XxlJobLogDao {
    private final XxlJobLogRepository xxlJobLogRepository;

    public XxlJobLogDao(XxlJobLogRepository xxlJobLogRepository) {
        this.xxlJobLogRepository = xxlJobLogRepository;
    }

    // exist jobId not use jobGroup, not exist use jobGroup
    public List<XxlJobLog> pageList(int offset,
                                    int pagesize,
                                    int jobGroup,
                                    int jobId,
                                    Date triggerTimeStart,
                                    Date triggerTimeEnd,
                                    int logStatus) {
        return page(offset, pagesize, jobGroup, jobId, triggerTimeStart, triggerTimeEnd, logStatus).getContent();
    }

    public int pageListCount(int offset,
                             int pagesize,
                             int jobGroup,
                             int jobId,
                             Date triggerTimeStart,
                             Date triggerTimeEnd,
                             int logStatus) {
        return (int) page(0, 1, jobGroup, jobId, triggerTimeStart, triggerTimeEnd, logStatus).getTotalElements();
    }

    private Page<XxlJobLog> page(int offset,
                                 int pagesize,
                                 int jobGroup,
                                 int jobId,
                                 Date triggerTimeStart,
                                 Date triggerTimeEnd,
                                 int logStatus) {
        Specification<XxlJobLog> specification = (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (jobId == 0 && jobGroup > 0) {
                predicates.add(cb.equal(root.get("jobGroup"), jobGroup));
            }
            if (jobId > 0) {
                predicates.add(cb.equal(root.get("jobId"), jobId));
            }
            if (triggerTimeStart != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("triggerTime"), triggerTimeStart));
            }
            if (triggerTimeEnd != null) {
                predicates.add(cb.lessThanOrEqualTo(root.get("triggerTime"), triggerTimeEnd));
            }
            if (logStatus == 1) {
                predicates.add(cb.equal(root.get("HandleStatus"), 200));
            } else if (logStatus == 2) {
                predicates.add(cb.or(root.get("triggerCode").in(0, 200).not(), root.get("handleStatus").in(0, 200).not()));
            } else if (logStatus == 3) {
                predicates.add(cb.equal(root.get("triggerCode"), 200));
                predicates.add(cb.equal(root.get("HandleStatus"), 0));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
        Pageable pageable = PageRequest.of(offset / pagesize, pagesize, Sort.Direction.DESC, "triggerTime");
        return xxlJobLogRepository.findAll(specification, pageable);
    }

    public XxlJobLog load(long id) {
        return xxlJobLogRepository.findById(id).orElse(null);
    }

    public long save(XxlJobLog xxlJobLog) {
        if (xxlJobLog.getJobGroup() == null) {
            xxlJobLog.setJobGroup(0);
        }
        if (xxlJobLog.getJobId() == null) {
            xxlJobLog.setJobId(0);
        }
        if (xxlJobLog.getExecutorFailRetryCount() == null) {
            xxlJobLog.setExecutorFailRetryCount(0);
        }
        if (xxlJobLog.getTriggerCode() == null) {
            xxlJobLog.setTriggerCode(0);
        }
        if (xxlJobLog.getHandleCode() == null) {
            xxlJobLog.setHandleCode(0);
        }
        if (xxlJobLog.getAlarmStatus() == null) {
            xxlJobLog.setAlarmStatus(0);
        }
        xxlJobLogRepository.save(xxlJobLog);
        return 1;
    }

    public int updateTriggerInfo(XxlJobLog xxlJobLog) {
        XxlJobLog jobLog = xxlJobLogRepository.findById(xxlJobLog.getId()).orElse(null);
        if (jobLog == null) {
            return 0;
        }
        jobLog.setTriggerTime(xxlJobLog.getTriggerTime());
        jobLog.setTriggerCode(xxlJobLog.getTriggerCode());
        jobLog.setTriggerMsg(xxlJobLog.getTriggerMsg());
        jobLog.setExecutorAddress(xxlJobLog.getExecutorAddress());
        jobLog.setExecutorHandler(xxlJobLog.getExecutorHandler());
        jobLog.setExecutorParam(xxlJobLog.getExecutorParam());
        jobLog.setExecutorShardingParam(xxlJobLog.getExecutorShardingParam());
        jobLog.setExecutorFailRetryCount(xxlJobLog.getExecutorFailRetryCount());
        xxlJobLogRepository.save(jobLog);
        return 1;
    }

    public int updateHandleInfo(XxlJobLog xxlJobLog) {
        XxlJobLog jobLog = xxlJobLogRepository.findById(xxlJobLog.getId()).orElse(null);
        if (jobLog == null) {
            return 0;
        }
        jobLog.setHandleTime(xxlJobLog.getHandleTime());
        jobLog.setHandleCode(xxlJobLog.getHandleCode());
        jobLog.setHandleMsg(xxlJobLog.getHandleMsg());
        xxlJobLogRepository.save(jobLog);
        return 1;
    }

    public int delete(int jobId) {
        return xxlJobLogRepository.deleteByJobId(jobId);
    }

    public Map<String, Object> findLogReport(Date from,
                                             Date to) {
        Map<String, Object> logReport = new HashMap<>(xxlJobLogRepository.findLogReport(from, to));
        logReport.putIfAbsent("triggerDayCountRunning", 0);
        logReport.putIfAbsent("triggerDayCountSuc", 0);
        return logReport;
    }

    public List<Long> findClearLogIds(int jobGroup,
                                      int jobId,
                                      Date clearBeforeTime,
                                      int clearBeforeNum,
                                      int pagesize) {
        Specification<XxlJobLog> specification = (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (jobGroup > 0) {
                predicates.add(cb.equal(root.get("jobGroup"), jobGroup));
            }
            if (jobId > 0) {
                predicates.add(cb.equal(root.get("jobId"), jobId));
            }
            if (clearBeforeTime != null) {
                predicates.add(cb.lessThanOrEqualTo(root.get("triggerTime"), clearBeforeTime));
            }
            if (clearBeforeNum > 0) {
                // TODO
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
        Pageable pageable = PageRequest.of(0, pagesize, Sort.Direction.ASC, "id");
        return xxlJobLogRepository.findAll(specification, pageable).map(XxlJobLog::getId).getContent();
    }

    public int clearLog(List<Long> logIds) {
        xxlJobLogRepository.deleteAllById(logIds);
        return logIds.size();
    }

    public List<Long> findFailJobLogIds(int pagesize) {
        Specification<XxlJobLog> specification = (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            predicates.add(cb.equal(root.get("alarmStatus"), 0));
            predicates.add(cb.or(cb.and(root.get("triggerCode").in(0, 200), cb.equal(root.get("handleCode"), 0)),
                    cb.equal(root.get("handleCode"), 200)).not());

            return cb.and(predicates.toArray(new Predicate[0]));
        };
        Pageable pageable = PageRequest.of(0, pagesize, Sort.Direction.ASC, "id");
        return xxlJobLogRepository.findAll(specification, pageable).map(XxlJobLog::getId).getContent();
    }

    public int updateAlarmStatus(long logId,
                                 int oldAlarmStatus,
                                 int newAlarmStatus) {
        XxlJobLog jobLog = xxlJobLogRepository.findById(logId).filter(xxlJobLog -> xxlJobLog.getAlarmStatus().equals(oldAlarmStatus)).orElse(null);
        if (jobLog == null) {
            return 0;
        }
        jobLog.setAlarmStatus(newAlarmStatus);
        xxlJobLogRepository.save(jobLog);
        return 1;
    }

    public List<Long> findLostJobIds(Date losedTime) {
        return xxlJobLogRepository.findLostJobIds(losedTime);
    }
}
