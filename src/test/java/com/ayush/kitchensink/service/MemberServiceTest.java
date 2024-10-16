package com.ayush.kitchensink.service;

import com.ayush.kitchensink.model.Member;
import com.ayush.kitchensink.repo.MemberRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(MockitoExtension.class)
public class MemberServiceTest {

    private static final Logger log = LoggerFactory.getLogger(MemberServiceTest.class);

    @Mock
    private MemberRepository memberRepository;

    @InjectMocks
    private MemberRegistration memberRegistration;

    @Test
    public void testRegisterMember() throws Exception {
        // create a dummy member object
        Member member = new Member();
        member.setName("Ayush Jha");
        member.setEmail("ayush@example.com");
        member.setPhoneNumber("12474867890");

        // call the register method
        memberRegistration.register(member);

        //  verify that the save method was called once
        verify(memberRepository, times(1)).save(any(Member.class));
    }

    @Test
    public void testRegisterMemberDoesNotThrowException() {
        // create a dummy member
    	 Member member = new Member();
         member.setName("Ayush Jha");
         member.setEmail("ayush@example.com");
         member.setPhoneNumber("12474867890");

        // ensure no exception is thrown during the registration process
        assertDoesNotThrow(() -> memberRegistration.register(member));
    }
}
