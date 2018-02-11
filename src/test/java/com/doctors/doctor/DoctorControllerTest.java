package com.doctors.doctor;

package com.doctors.doctor;


import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class DoctorControllerTest {

    @Autowired
    JpaDoctorRepository doctorRepository;

    @Autowired
    MockMvc mockMvc;


    @After
    public void cleanup(){
        doctorRepository.deleteAll();
    }

    @Test
    public void getAllDoctors() throws Exception{
        doctorRepository.save(new Doctor("Tommas", null, null));

        mockMvc.perform(get("/doctors")
                .header(HttpHeaders.AUTHORIZATION,"Basic T2xlZzpwYXNz"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(1)))
                .andExpect(jsonPath("$.content[0].name", is("Tommas")));
    }
}