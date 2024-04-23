package com.xxl.job.admin.repository;

import com.xxl.job.admin.core.model.XxlJobLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.List;
import java.util.Map;

public interface XxlJobLogRepository extends JpaRepository<XxlJobLog, Long>, JpaSpecificationExecutor<XxlJobLog> {
    int deleteByJobId(Integer jobId);

    @Query(value = "select l.id from XxlJobLog l left join XxlJobRegistry r on l.executorAddress = r.registryValue " +
                   "where l.triggerCode = 200 and l.handleCode = 0 and l.triggerTime <= :losedTime and r.id is null")
    List<Long> findLostJobIds(@Param("losedTime") Date losedTime);

    @Query(value = "select count(l.handleCode) as triggerDayCount, " +
                   "sum(case when (l.triggerCode in (0,200) and l.handleCode = 0) then 1 else 0 end) as triggerDayCountRunning, " +
                   "sum(case when l.handleCode = 200 then 1 else 0 end) as triggerDayCountSuc " +
                   "from XxlJobLog l where l.triggerTime between :from and :to")
    Map<String, Object> findLogReport(@Param("from") Date from, @Param("to") Date to);
}
