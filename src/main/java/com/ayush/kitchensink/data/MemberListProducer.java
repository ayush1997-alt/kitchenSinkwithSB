package com.ayush.kitchensink.data;

import java.util.List;

import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import com.ayush.kitchensink.model.Member;
import com.ayush.kitchensink.repo.MemberRepository;

@Component
public class MemberListProducer {

    private final MemberRepository memberRepository;
    private List<Member> members;

    public MemberListProducer(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    public List<Member> getMembers() {
        return members;
    }

    @EventListener
    public void onMemberListChanged(Member member) {
        retrieveAllMembersOrderedByName();
    }

    @jakarta.annotation.PostConstruct
    public void retrieveAllMembersOrderedByName() {
        members = memberRepository.findAllOrderedByName();
    }
}
