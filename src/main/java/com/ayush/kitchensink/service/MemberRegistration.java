package com.ayush.kitchensink.service;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ayush.kitchensink.model.Member;
import com.ayush.kitchensink.repo.MemberRepository;

import jakarta.transaction.Transactional;

@Service
public class MemberRegistration {

	private static final Logger log = LoggerFactory.getLogger(MemberRegistration.class);

    @Autowired
    private MemberRepository memberRepository;

    @Transactional
    public void register(Member member) throws Exception {
        log.info("Registering " + member.getName());
        memberRepository.save(member);
    }
}



