package com.vecanhac.ddd.application.service.event.impl;

import com.vecanhac.ddd.application.service.event.EventAppService;
import com.vecanhac.ddd.domain.service.HiDomainService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class EventAppServiceImpl implements EventAppService {

    //Call domain service
    @Autowired
    private HiDomainService hiDomainService;
    @Override
    public String sayHi(String who) {
        return hiDomainService.sayHi(who);
    }
}
