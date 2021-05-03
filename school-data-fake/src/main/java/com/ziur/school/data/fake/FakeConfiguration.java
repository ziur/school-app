package com.ziur.school.data.fake;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

@Configuration
public class FakeConfiguration {
    @Bean
    @Scope("singleton")
    public FakeBucketService bucketService() {
        return new FakeBucketService();
    }
}
