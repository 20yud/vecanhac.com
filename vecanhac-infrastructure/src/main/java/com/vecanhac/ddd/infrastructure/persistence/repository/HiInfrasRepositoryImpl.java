package com.vecanhac.ddd.infrastructure.persistence.repository;

import com.vecanhac.ddd.domain.repository.HiDomainRepository;
import org.springframework.stereotype.Service;


@Service
public class HiInfrasRepositoryImpl implements HiDomainRepository {



    @Override
    public String sayHi(String who) {
        return "Hi InFrastruture";
    }
}
