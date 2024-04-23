package com.xxl.job.admin.dao;

import com.xxl.job.admin.core.model.XxlJobUser;
import com.xxl.job.admin.repository.XxlJobUserRepository;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * @author xuxueli 2019-05-04 16:44:59
 */
@Repository
public class XxlJobUserDao {
    private final XxlJobUserRepository xxlJobUserRepository;

    public XxlJobUserDao(XxlJobUserRepository xxlJobUserRepository) {
        this.xxlJobUserRepository = xxlJobUserRepository;
    }

    public List<XxlJobUser> pageList(int offset,
                                     int pagesize,
                                     String username,
                                     int role) {
        return page(offset, pagesize, username, role).getContent();
    }

    public int pageListCount(int offset,
                             int pagesize,
                             String username,
                             int role) {
        return (int) page(0, 1, username, role).getTotalElements();
    }

    private Page<XxlJobUser> page(int offset,
                                  int pagesize,
                                  String username,
                                  int role) {
        XxlJobUser probe = new XxlJobUser();
        if (StringUtils.hasLength(username)) {
            probe.setUsername(username);
        }
        if (role > -1) {
            probe.setRole(role);
        }

        Pageable pageable = PageRequest.of(offset / pagesize, pagesize, Sort.Direction.ASC, "username");
        ExampleMatcher matcher = ExampleMatcher.matching()
                .withMatcher("username", ExampleMatcher.GenericPropertyMatchers.contains());
        return xxlJobUserRepository.findAll(Example.of(probe, matcher), pageable);
    }

    public XxlJobUser loadByUserName(String username) {
        return xxlJobUserRepository.findByUsername(username).orElse(null);
    }

    public int save(XxlJobUser xxlJobUser) {
        if (xxlJobUser.getRole() == null) {
            xxlJobUser.setRole(0);
        }
        xxlJobUserRepository.save(xxlJobUser);
        return 1;
    }

    public int update(XxlJobUser xxlJobUser) {
        XxlJobUser jobUser = xxlJobUserRepository.findById(xxlJobUser.getId()).orElse(null);
        if (jobUser == null) {
            return 0;
        }
        jobUser.setRole(xxlJobUser.getRole());
        jobUser.setPermission(xxlJobUser.getPermission());
        if (StringUtils.hasText(xxlJobUser.getPassword())) {
            jobUser.setPassword(xxlJobUser.getPassword());
        }
        xxlJobUserRepository.save(jobUser);
        return 1;
    }

    public int delete(int id) {
        xxlJobUserRepository.deleteById(id);
        return 1;
    }
}
