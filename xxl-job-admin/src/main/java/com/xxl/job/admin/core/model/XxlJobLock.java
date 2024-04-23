package com.xxl.job.admin.core.model;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class XxlJobLock {
    @Id
    private String lockName;

    public String getLockName() {
        return lockName;
    }

    public void setLockName(String lockName) {
        this.lockName = lockName;
    }
}
