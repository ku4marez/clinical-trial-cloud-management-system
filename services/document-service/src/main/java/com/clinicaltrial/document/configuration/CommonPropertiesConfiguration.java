package com.clinicaltrial.document.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource("classpath:application-common.properties")
public class CommonPropertiesConfiguration {
}
