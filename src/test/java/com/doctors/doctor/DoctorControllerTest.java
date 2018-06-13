package com.doctors.doctor;


import com.google.common.io.Resources;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertFalse;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class DoctorControllerTest {

    @Autowired
    JpaDoctorRepository doctorRepository;

    @Autowired
    MockMvc mockMvc;

    @Before
    public void cleanupBefore() {
        doctorRepository.deleteAll();
    }

    @After
    public void cleanupAfter() {
        doctorRepository.deleteAll();
    }

    @Test
    public void getAllDoctors() throws Exception {
        doctorRepository.save(new Doctor("Andrew", null, null));

        mockMvc.perform(get("/doctors")
                .header(HttpHeaders.AUTHORIZATION, "Basic T2xlZzpwYXNz"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(1)))
                .andExpect(jsonPath("$.content[0].name", is("Andrew")));
    }

    @Test
    public void sortByName() throws Exception {
        doctorRepository.save(new Doctor("Bernard", null, null));
        doctorRepository.save(new Doctor("Hugh", null, null));

        mockMvc.perform(get("/doctors")
                .header(HttpHeaders.AUTHORIZATION, "Basic T2xlZzpwYXNz")
                .param("sort", "name"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(2)))
                .andExpect(jsonPath("$.content[0].name", is("Bernard")))
                .andExpect(jsonPath("$.content[1].name", is("Hugh")));
    }

    @Test
    public void getDoctorById() throws Exception {
        Integer id = doctorRepository.save(new Doctor("Andrew", null, null)).getId();

        mockMvc.perform(get("/doctors/{id}", id)
                .header(HttpHeaders.AUTHORIZATION, "Basic T2xlZzpwYXNz"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(id)))
                .andExpect(jsonPath("$.name", is("Andrew")))
                .andExpect(jsonPath("$.*", hasSize(4)));
    }

    @Test
    public void petNotFound() throws Exception {
        mockMvc.perform(get("/doctors/3")
                .header(HttpHeaders.AUTHORIZATION, "Basic T2xlZzpwYXNz"))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void createDoctor() throws Exception {
        String file = "doctor.json";
        String body = readFile(file);

        mockMvc.perform(post(("/doctors"))
                .header(HttpHeaders.AUTHORIZATION, "Basic T2xlZzpwYXNz")
                .content(body)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", containsString("/doctors/")));

        List<Doctor> all = doctorRepository.findAll();
        assertThat(all, hasSize(1));
    }

    @Test
    public void updateDoctor() throws Exception {
        Integer id = doctorRepository.save(new Doctor("Andrew", null, null)).getId();

        String file = "doctor.json";
        String body = readFile(file);

        mockMvc.perform(put("/doctors/{id}", id)
                .header(HttpHeaders.AUTHORIZATION, "Basic T2xlZzpwYXNz")
                .contentType(MediaType.APPLICATION_JSON)
                .content(body)).andExpect(status().isNoContent());

        Doctor doctor = doctorRepository.findById(id).get();

        assertThat(doctor.getName(), is("Andrew"));
    }

    @Test
    public void deleteDoctor() throws Exception {
        Integer id = doctorRepository.save(new Doctor("Andrew", null, null)).getId();

        mockMvc.perform(delete("/doctors/{id}", id)
                .header(HttpHeaders.AUTHORIZATION, "Basic T2xlZzpwYXNz"))
                .andExpect(status().isNoContent());

        Optional<Doctor> maybeDoctor = doctorRepository.findById(id);

        assertFalse(maybeDoctor.isPresent());

    }

    @Test
    public void createAppointment() throws Exception {
        String file = "appointment.json";
        String body = readFile(file);

        Integer doctorId = doctorRepository.save(new Doctor("Andrew", null, null)).getId();
        String date="2018-07-07";
        String time="10:00";

        mockMvc.perform(post("/doctors/{id}/schedule/{date}/{time}",doctorId,date,time)
                .header(HttpHeaders.AUTHORIZATION, "Basic T2xlZzpwYXNz")
                .content(body)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", containsString("/doctors/"+doctorId+"/2018-07-07")));

        Set<Appointment> all = doctorRepository.findById(doctorId).get().getAppointments();
        assertThat(all, hasSize(1));
    }


    private String readFile(String resourceName) throws IOException {
        return Resources.toString(Resources.getResource(resourceName), Charset.defaultCharset());
    }
}