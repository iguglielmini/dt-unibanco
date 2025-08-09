package br.com.itau.challenge.balance.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.http.nio.netty.NettyNioAsyncHttpClient;

import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.sqs.SqsAsyncClient;

import java.net.URI;

@Configuration
public class SqsConfig {

    @Bean
    public SqsAsyncClient sqsAsyncClient(
            @Value("${app.sqs.region}") String region,
            @Value("${app.sqs.endpoint}") String endpoint,
            @Value("${aws.accessKeyId}") String accessKey,
            @Value("${aws.secretAccessKey}") String secret) {
        return SqsAsyncClient.builder()
                .region(Region.of(region))
                .endpointOverride(URI.create(endpoint))
                .credentialsProvider(StaticCredentialsProvider.create(
                        AwsBasicCredentials.create(accessKey, secret)))
                .httpClientBuilder(NettyNioAsyncHttpClient.builder())
                .build();
    }
}
