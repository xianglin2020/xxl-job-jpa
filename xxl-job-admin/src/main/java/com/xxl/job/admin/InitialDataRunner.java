package com.xxl.job.admin;

import com.xxl.job.admin.core.model.XxlJobGroup;
import com.xxl.job.admin.core.model.XxlJobLock;
import com.xxl.job.admin.core.model.XxlJobUser;
import com.xxl.job.admin.repository.XxlJobGroupRepository;
import com.xxl.job.admin.repository.XxlJobLockRepository;
import com.xxl.job.admin.repository.XxlJobUserRepository;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * 执行原脚本中添加默认执行器和默认用户的语句
 *
 * @author xianglin
 */
@Component
public class InitialDataRunner implements ApplicationRunner {
    private final XxlJobGroupRepository xxlJobGroupRepository;
    private final XxlJobUserRepository xxlJobUserRepository;
    private final XxlJobLockRepository xxlJobLockRepository;

    public InitialDataRunner(XxlJobGroupRepository xxlJobGroupRepository, XxlJobUserRepository xxlJobUserRepository, XxlJobLockRepository xxlJobLockRepository) {
        this.xxlJobGroupRepository = xxlJobGroupRepository;
        this.xxlJobUserRepository = xxlJobUserRepository;
        this.xxlJobLockRepository = xxlJobLockRepository;
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        if (!xxlJobGroupRepository.existsByAppname("xxl-job-executor-sample")) {
            XxlJobGroup xxlJobGroup = new XxlJobGroup();
            xxlJobGroup.setId(1);
            xxlJobGroup.setAppname("xxl-job-executor-sample");
            xxlJobGroup.setTitle("示例执行器");
            xxlJobGroup.setAddressType(0);
            xxlJobGroup.setUpdateTime(new Date(1541254891000L));
            xxlJobGroupRepository.save(xxlJobGroup);
        }

        if (!xxlJobUserRepository.findByUsername("admin").isPresent()) {
            XxlJobUser jobUser = new XxlJobUser();
            jobUser.setId(1);
            jobUser.setUsername("admin");
            jobUser.setPassword("e10adc3949ba59abbe56e057f20f883e");
            jobUser.setRole(1);
            xxlJobUserRepository.save(jobUser);
        }

        if (xxlJobLockRepository.count() == 0) {
            XxlJobLock xxlJobLock = new XxlJobLock();
            xxlJobLock.setLockName("schedule_lock");
            xxlJobLockRepository.save(xxlJobLock);
        }
    }
}
