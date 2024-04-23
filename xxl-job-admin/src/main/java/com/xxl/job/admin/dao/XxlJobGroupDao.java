package com.xxl.job.admin.dao;

import com.xxl.job.admin.core.model.XxlJobGroup;
import com.xxl.job.admin.repository.XxlJobGroupRepository;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by xuxueli on 16/9/30.
 */
@Repository
public class XxlJobGroupDao {
    private final XxlJobGroupRepository xxlJobGroupRepository;

    public XxlJobGroupDao(XxlJobGroupRepository xxlJobGroupRepository) {
        this.xxlJobGroupRepository = xxlJobGroupRepository;
    }

    public List<XxlJobGroup> findAll() {
        return xxlJobGroupRepository.findAll(Sort.by(Sort.Direction.ASC, "appname", "title", "id"));
    }

    public List<XxlJobGroup> findByAddressType(int addressType) {
        XxlJobGroup probe = new XxlJobGroup();
        probe.setAddressType(addressType);
        return xxlJobGroupRepository.findAll(Example.of(probe), Sort.by(Sort.Direction.ASC, "appname", "title", "id"));
    }

    public int save(XxlJobGroup xxlJobGroup) {
        if (xxlJobGroup.getAddressType() == null) {
            xxlJobGroup.setAddressType(0);
        }
        xxlJobGroupRepository.save(xxlJobGroup);
        return 1;
    }

    public int update(XxlJobGroup xxlJobGroup) {
        if (xxlJobGroupRepository.existsById(xxlJobGroup.getId())) {
            xxlJobGroupRepository.save(xxlJobGroup);
            return 1;
        }
        return 0;
    }

    public int remove(int id) {
        if (xxlJobGroupRepository.existsById(id)) {
            xxlJobGroupRepository.deleteById(id);
            return 1;
        }
        return 0;
    }

    public XxlJobGroup load(int id) {
        return xxlJobGroupRepository.findById(id).orElse(null);
    }

    public List<XxlJobGroup> pageList(int offset,
                                      int pagesize,
                                      String appname,
                                      String title) {
        return page(offset, pagesize, appname, title).getContent();
    }

    public int pageListCount(int offset,
                             int pagesize,
                             String appname,
                             String title) {
        return (int) page(0, 1, appname, title).getTotalElements();
    }

    private Page<XxlJobGroup> page(int offset, int pagesize, String appname, String title) {
        XxlJobGroup probe = new XxlJobGroup();
        probe.setAppname(appname);
        probe.setTitle(title);
        ExampleMatcher matcher = ExampleMatcher.matching()
                .withMatcher("appname", ExampleMatcher.GenericPropertyMatchers.contains())
                .withMatcher("title", ExampleMatcher.GenericPropertyMatchers.contains());
        Pageable pageable = PageRequest.of(offset / pagesize, pagesize, Sort.by(Sort.Direction.ASC, "appname", "title", "id"));
        return xxlJobGroupRepository.findAll(Example.of(probe, matcher), pageable);
    }
}
