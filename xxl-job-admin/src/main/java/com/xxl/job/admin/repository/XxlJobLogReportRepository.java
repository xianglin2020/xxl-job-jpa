package com.xxl.job.admin.repository;

import com.xxl.job.admin.core.model.XxlJobLogReport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Date;
import java.util.List;
import java.util.Map;

public interface XxlJobLogReportRepository extends JpaRepository<XxlJobLogReport, Integer> {
    List<XxlJobLogReport> findAllByTriggerDay(Date triggerDay);

    List<XxlJobLogReport> findAllByTriggerDayBetweenOrderByTriggerDayDesc(Date startDate, Date endDate);

    @Query(value = "select sum(r.runningCount) as runningCount, sum(r.sucCount) as sucCount, sum(r.failCount) as failCount from XxlJobLogReport r ")
    Map<String, Object> queryLogReportTotal();
}
