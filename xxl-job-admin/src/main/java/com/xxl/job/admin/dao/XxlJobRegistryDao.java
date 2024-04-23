package com.xxl.job.admin.dao;

import com.xxl.job.admin.core.model.XxlJobRegistry;
import com.xxl.job.admin.repository.XxlJobRegistryRepository;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Repository;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by xuxueli on 16/9/30.
 */
@Repository
public class XxlJobRegistryDao {
    private final XxlJobRegistryRepository xxlJobRegistryRepository;

    public XxlJobRegistryDao(XxlJobRegistryRepository xxlJobRegistryRepository) {
        this.xxlJobRegistryRepository = xxlJobRegistryRepository;
    }

    public List<Integer> findDead(int timeout,
                                  Date nowTime) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(nowTime);
        calendar.add(Calendar.SECOND, -timeout);
        nowTime = calendar.getTime();
        return xxlJobRegistryRepository.findAllByUpdateTimeBefore(nowTime).map(XxlJobRegistry::getId).toList();
    }

    public int removeDead(List<Integer> ids) {
        xxlJobRegistryRepository.deleteAllById(ids);
        return ids.size();
    }

    public List<XxlJobRegistry> findAll(int timeout,
                                        Date nowTime) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(nowTime);
        calendar.add(Calendar.SECOND, -timeout);
        nowTime = calendar.getTime();
        return xxlJobRegistryRepository.findAllByUpdateTimeAfter(nowTime);
    }

    public int registryUpdate(String registryGroup,
                              String registryKey,
                              String registryValue,
                              Date updateTime) {
        XxlJobRegistry probe = new XxlJobRegistry();
        probe.setRegistryGroup(registryGroup);
        probe.setRegistryKey(registryKey);
        probe.setRegistryValue(registryValue);
        List<XxlJobRegistry> list = xxlJobRegistryRepository.findAll(Example.of(probe));
        for (final XxlJobRegistry registry : list) {
            registry.setUpdateTime(updateTime);
        }
        xxlJobRegistryRepository.saveAll(list);
        return list.size();
    }

    public int registrySave(String registryGroup,
                            String registryKey,
                            String registryValue,
                            Date updateTime) {
        XxlJobRegistry xxlJobRegistry = new XxlJobRegistry();
        xxlJobRegistry.setRegistryGroup(registryGroup);
        xxlJobRegistry.setRegistryKey(registryKey);
        xxlJobRegistry.setRegistryValue(registryValue);
        xxlJobRegistry.setUpdateTime(updateTime);
        xxlJobRegistryRepository.save(xxlJobRegistry);
        return 1;
    }

    public int registryDelete(String registryGroup,
                              String registryKey,
                              String registryValue) {
        XxlJobRegistry probe = new XxlJobRegistry();
        probe.setRegistryGroup(registryGroup);
        probe.setRegistryKey(registryKey);
        probe.setRegistryValue(registryValue);
        List<Integer> list = xxlJobRegistryRepository.findAll(Example.of(probe)).stream().map(XxlJobRegistry::getId).collect(Collectors.toList());
        xxlJobRegistryRepository.deleteAllById(list);
        return list.size();
    }
}
