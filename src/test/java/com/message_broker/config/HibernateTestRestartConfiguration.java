package com.message_broker.config;


import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import java.util.Properties;

@Configuration
@EnableTransactionManagement
@ComponentScan({"com.message_broker"})
@PropertySource(value = {
        "classpath:application.properties",
        "classpath:application.test.properties"
})
public class HibernateTestRestartConfiguration extends HibernateConfiguration{

    @Override
    protected Properties hibernateProperties() {
        Properties properties = super.hibernateProperties();
        properties.put("hibernate.hbm2ddl.auto", "update");
        return properties;
    }
}
