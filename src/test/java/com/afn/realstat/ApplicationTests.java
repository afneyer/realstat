package com.afn.realstat;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.afn.realstat.Application;
import com.afn.realstat.CustomerRepository;

import static org.assertj.core.api.BDDAssertions.then;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class,
        webEnvironment = SpringBootTest.WebEnvironment.NONE)
public class ApplicationTests {

    @Autowired
    private CustomerRepository repository;

    @Test
    // Sample test: TODO remove once a new test is written
    public void shouldFindTwoBauerCustomers() {
        then(this.repository.findByLastNameStartsWithIgnoreCase("Bauer")).hasSize(0);
    }
}
