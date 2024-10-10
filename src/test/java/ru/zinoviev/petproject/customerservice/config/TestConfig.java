package ru.customer.petproject.customerservice.config;

import com.redis.testcontainers.RedisContainer;
import net.devh.boot.grpc.client.autoconfigure.GrpcClientAutoConfiguration;
import net.devh.boot.grpc.server.autoconfigure.GrpcServerAutoConfiguration;
import net.devh.boot.grpc.server.autoconfigure.GrpcServerFactoryAutoConfiguration;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.testcontainers.containers.KafkaContainer;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.utility.DockerImageName;

@TestConfiguration(proxyBeanMethods = false)
@ImportAutoConfiguration({
        GrpcServerAutoConfiguration.class,
        GrpcServerFactoryAutoConfiguration.class,
        GrpcClientAutoConfiguration.class,
})
public class TestConfig {

    @Bean
    @ServiceConnection
    PostgreSQLContainer<?> postgresContainer(DynamicPropertyRegistry registry) {
        PostgreSQLContainer<?> postgres =
                new PostgreSQLContainer<>(DockerImageName.parse("postgres:latest"));
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        return postgres;
    }

    @Bean
    @ServiceConnection
    KafkaContainer kafkaContainer(DynamicPropertyRegistry registry) {
        KafkaContainer kafka =
                new KafkaContainer(DockerImageName.parse("confluentinc/cp-kafka:latest"));
        registry.add("spring.kafka.bootstrap-servers", kafka::getBootstrapServers);
        return kafka;
    }

    @Bean
    RedisContainer redisContainer(DynamicPropertyRegistry registry) {
        RedisContainer redis =
                new RedisContainer(DockerImageName.parse("redis:latest")).withExposedPorts(6379);
        registry.add("spring.data.redis.host", redis::getHost);
        registry.add("spring.data.redis.port", () -> redis.getMappedPort(6379));
        return redis;
    }
}
