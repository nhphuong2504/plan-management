package com.example.demo.student;

import java.time.LocalDate;
import java.time.Month;
import java.util.List;


import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class StudentConfig {

    @Bean
    CommandLineRunner commandLineRunner(StudentRepository repository) {
        return args -> {
            Student phuong = new Student(
                1L,
                "Phuong",
                "nhphuong@iastate.edu"
//                LocalDate.of(1999, Month.APRIL, 25)
                );

            Student alex = new Student(
                2L,
                "Alex",
                "asddsa@iastate.edu"
//                LocalDate.of(2003, Month.APRIL, 21)
                );

            repository.saveAll(
                List.of(phuong)
            );
        };
    };
}
