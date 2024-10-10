package ru.zinovievbank.customerservice.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.io.Serializable;
import java.time.Duration;

@Configuration
@EnableCaching
@AutoConfigureAfter(RedisAutoConfiguration.class)
public class CacheConfiguration {

  @Value("${spring.data.redis.host}")
  private String redisHost;
  @Value("${spring.data.redis.port}")
  private int redisPort;
  private static final int DURATION_CACHE_IN_MINUTES = 1;

  @Bean
  public JedisConnectionFactory jedisConnectionFactory() {
    return new JedisConnectionFactory(new RedisStandaloneConfiguration(redisHost, redisPort));
  }

  @Bean
  public RedisTemplate<String, Serializable> redisCacheTemplate(
      RedisConnectionFactory redisConnectionFactory) {
    RedisTemplate<String, Serializable> template = new RedisTemplate<>();
    template.setKeySerializer(new StringRedisSerializer());
    template.setValueSerializer(new GenericJackson2JsonRedisSerializer());
    template.setConnectionFactory(redisConnectionFactory);
    return template;
  }

  @Bean
  public CacheManager cacheManager(RedisConnectionFactory factory) {
    RedisCacheConfiguration redisCacheConfiguration = RedisCacheConfiguration.defaultCacheConfig()
        .serializeKeysWith(
            RedisSerializationContext.SerializationPair.fromSerializer(new StringRedisSerializer()))
        .serializeValuesWith(RedisSerializationContext.SerializationPair
            .fromSerializer(new GenericJackson2JsonRedisSerializer()))
        .entryTtl(Duration.ofMinutes(DURATION_CACHE_IN_MINUTES))
        .enableTimeToIdle();

    return RedisCacheManager.builder(factory).cacheDefaults(redisCacheConfiguration)
        .build();
  }
}
