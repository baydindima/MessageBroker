package com.message_broker.config;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { HibernateTestConfiguration.class,
    HibernateConfiguration.class })
@TestPropertySource(value = {"classpath:application.test.properties"})
public class HibernateTestConfiguration extends HibernateConfiguration {

    @Test
    public void test() {
        System.out.println(environment.getProperty("jdbc.url"));
    }
}
