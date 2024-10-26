package com.cloudclub.openstackservice.common.config;

import org.openstack4j.api.OSClient.OSClientV3;
import org.openstack4j.api.exceptions.AuthenticationException;
import org.openstack4j.model.common.Identifier;
import org.openstack4j.openstack.OSFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenStackConfig {

    @Value("${openstack.auth.username}")
    private String username;

    @Value("${openstack.auth.password}")
    private String password;

    @Value("${openstack.auth.auth-url}")
    private String authUrl;

    @Value("${openstack.auth.domain}")
    private String domain;

    @Bean
    public OSClientV3 osClient() {
        try {
            return OSFactory.builderV3()
                .endpoint(authUrl)
                .credentials(username, password, Identifier.byId(domain))
                .authenticate();
        } catch (AuthenticationException e) {
            throw new RuntimeException("OpenStack 인증에 실패했습니다.", e);
        }
    }
}