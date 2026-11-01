package com.kafka.consumidor.Config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import software.amazon.awssdk.auth.credentials.AwsSessionCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;

@Configuration 
public class DynamoDbConfig {
    @Value("${aws.region}")
    private String region;
    @Value("${aws.access-key-id}")
    private String accessKey;
    @Value("${aws.secret-acces-key}")
    private String secretKey;
    @Value("${aws.session-token}")
    private String sesionToken;

    @Bean 
    public DynamoDbClient dynamoDbClient(){
        AwsSessionCredentials credentials= AwsSessionCredentials.create(
            accessKey, secretKey, sesionToken);

        return DynamoDbClient.builder()
        .region(Region.of(region))
        .credentialsProvider(
            StaticCredentialsProvider.create(credentials))
            .build();
    }

    @Bean 
    public DynamoDbEnhancedClient dynamoDbEnhancedClient(DynamoDbClient dynamoDbClient){
        return DynamoDbEnhancedClient.builder()
        .dynamoDbClient(dynamoDbClient)
        .build();
    }
}
