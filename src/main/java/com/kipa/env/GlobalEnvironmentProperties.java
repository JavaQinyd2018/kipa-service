package com.kipa.env;

import org.springframework.core.io.support.ResourcePropertySource;

import java.io.IOException;

public class GlobalEnvironmentProperties extends ResourcePropertySource {

    public GlobalEnvironmentProperties(String location) throws IOException {
        super(location);
    }
}
