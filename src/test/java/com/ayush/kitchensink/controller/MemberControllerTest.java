package com.ayush.kitchensink.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.ayush.kitchensink.model.Member;
import com.ayush.kitchensink.service.MemberRegistration;

@ExtendWith(MockitoExtension.class)
public class MemberControllerTest {

    @Mock
    private MemberRegistration memberRegistration;

    @InjectMocks
    private MemberController memberController;

    private MockMvc mockMvc;

    @Test
    public void testShowRegistrationForm() throws Exception {
        mockMvc = MockMvcBuilders.standaloneSetup(memberController).build();

        mockMvc.perform(get("/members/new"))
                .andExpect(status().isOk())
                .andExpect(view().name("register"))
                .andExpect(model().attributeExists("newMember"));
    }

    @Test
    public void testRegisterMemberSuccess() throws Exception {
        mockMvc = MockMvcBuilders.standaloneSetup(memberController).build();

        Member newMember = new Member();
        newMember.setName("Ayush Jha");
        newMember.setEmail("ayush@example.com");
        newMember.setPhoneNumber("1234556790");

     
        doNothing().when(memberRegistration).register(any(Member.class));

        mockMvc.perform(post("/members/register")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("name", newMember.getName())
                        .param("email", newMember.getEmail())
                        .param("phoneNumber", newMember.getPhoneNumber()))
                .andExpect(status().isOk())
                .andExpect(view().name("register"))
                .andExpect(model().attribute("message", "Registration successful!"));
    }

    @Test
    public void testRegisterMemberFailure() throws Exception {
        mockMvc = MockMvcBuilders.standaloneSetup(memberController).build();

        Member newMember = new Member();
        newMember.setName("Jane Doe");
        newMember.setEmail("jane.doe@example.com");
        newMember.setPhoneNumber("0987654321");

      
        doThrow(new RuntimeException("Registration failed")).when(memberRegistration).register(any(Member.class));

        mockMvc.perform(post("/members/register")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("name", newMember.getName())
                        .param("email", newMember.getEmail())
                        .param("phoneNumber", newMember.getPhoneNumber()))
                .andExpect(status().isOk())
                .andExpect(view().name("register"))
                .andExpect(model().attribute("errorMessage", "Registration failed: Registration failed"));
    }

    @Test
    public void testInvalidMemberData() throws Exception {
        mockMvc = MockMvcBuilders.standaloneSetup(memberController).build();

        mockMvc.perform(post("/members/register")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("name", "")
                        .param("email", "invalid-email")
                        .param("phoneNumber", ""))
                .andExpect(status().isOk())
                .andExpect(view().name("register"))
                .andExpect(model().attributeHasFieldErrors("newMember", "name", "email", "phoneNumber"));
    }
}
