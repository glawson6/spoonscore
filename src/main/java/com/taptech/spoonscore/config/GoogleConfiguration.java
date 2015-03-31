package com.taptech.spoonscore.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import javax.inject.Inject;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by tap on 3/30/15.
 */
@Configuration
public class GoogleConfiguration {
    @Inject
    Environment env;

    @Bean(name = "GoogleConfig")
    public Map<String, Object> googleConfig(){
        Map<String, Object> config = new HashMap<String, Object>();
        config.put("mykey","testKey");
        String GOOGLE_API_KEY = env.getProperty("GOOGLE_API_KEY");
        config.put("GOOGLE_API_KEY",GOOGLE_API_KEY);
        return config;
    }
}
