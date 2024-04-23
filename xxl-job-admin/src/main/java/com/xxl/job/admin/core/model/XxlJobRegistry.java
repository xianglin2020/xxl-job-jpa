package com.xxl.job.admin.core.model;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by xuxueli on 16/9/30.
 */
@Entity
@Table(indexes = {
        @Index(name = "i_g_k_v", columnList = "registryGroup, registryKey, registryValue")
})
public class XxlJobRegistry {
    @Id
    @GeneratedValue
    private Integer id;
    private String registryGroup;
    private String registryKey;
    private String registryValue;
    private Date updateTime;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getRegistryGroup() {
        return registryGroup;
    }

    public void setRegistryGroup(String registryGroup) {
        this.registryGroup = registryGroup;
    }

    public String getRegistryKey() {
        return registryKey;
    }

    public void setRegistryKey(String registryKey) {
        this.registryKey = registryKey;
    }

    public String getRegistryValue() {
        return registryValue;
    }

    public void setRegistryValue(String registryValue) {
        this.registryValue = registryValue;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }
}
