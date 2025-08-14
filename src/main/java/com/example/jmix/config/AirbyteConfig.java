package com.example.jmix.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import com.airbyte.api.Airbyte;
import com.airbyte.api.models.shared.*;
import org.springframework.beans.factory.annotation.Value;

@Configuration
public class AirbyteConfig {

//    @Value("${airbyte.auth.username}")
//    private String username;
//
//    @Value("${airbyte.auth.password}")
//    private String password;
//
//    @Value("${airbyte.server-url}")
//    private String serverUrl;

    @Bean
    public Airbyte airbyteClient() {
        return Airbyte.builder()
                .serverURL("http://localhost:8000/api/public/v1")
                .security(Security.builder()
                        .basicAuth(SchemeBasicAuth.builder()
                                .password("Luvmin2004")
                                .username("dathip04@gmail.com")
                                .build())
                        .build())
                .build();
    }

}
