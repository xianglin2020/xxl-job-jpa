package com.xxl.job.admin.dao;

import com.xxl.job.admin.core.model.XxlJobLogReport;
import com.xxl.job.admin.repository.XxlJobLogReportRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * job log
 *
 * @author xuxueli 2019-11-22
 */
@Repository
public class XxlJobLogReportDao {
    private final XxlJobLogReportRepository xxlJobLogReportRepository;

    public XxlJobLogReportDao(XxlJobLogReportRepository xxlJobLogReportRepository) {
        this.xxlJobLogReportRepository = xxlJobLogReportRepository;
    }

    public int save(XxlJobLogReport xxlJobLogReport) {
        if (xxlJobLogReport.getRunningCount() == null) {
            xxlJobLogReport.setRunningCount(0);
        }
        if (xxlJobLogReport.getFailCount() == null) {
            xxlJobLogReport.setFailCount(0);
        }
        xxlJobLogReportRepository.save(xxlJobLogReport);
        return 1;
    }

    public int update(XxlJobLogReport xxlJobLogReport) {
        List<XxlJobLogReport> list = xxlJobLogReportRepository.findAllByTriggerDay(xxlJobLogReport.getTriggerDay());
        for (final XxlJobLogReport report : list) {
            report.setRunningCount(xxlJobLogReport.getRunningCount());
            report.setSucCount(xxlJobLogReport.getSucCount());
            report.setFailCount(xxlJobLogReport.getFailCount());
        }
        xxlJobLogReportRepository.saveAll(list);
        return list.size();
    }

    public List<XxlJobLogReport> queryLogReport(Date triggerDayFrom,
                                                Date triggerDayTo) {
        return xxlJobLogReportRepository.findAllByTriggerDayBetweenOrderByTriggerDayDesc(triggerDayFrom, triggerDayTo);
    }

    public XxlJobLogReport queryLogReportTotal() {
        Map<String, Object> map = xxlJobLogReportRepository.queryLogReportTotal();
        XxlJobLogReport report = new XxlJobLogReport();
        Object runningCount = map.get("runningCount");
        if (runningCount != null) {
            report.setRunningCount(Integer.parseInt(String.valueOf(runningCount)));
        }
        Object sucCount = map.get("sucCount");
        if (sucCount != null) {
            report.setSucCount(Integer.parseInt(String.valueOf(sucCount)));
        }
        Object failCount = map.get("failCount");
        if (failCount != null) {
            report.setFailCount(Integer.parseInt(String.valueOf(failCount)));
        }
        return report;
    }
}
