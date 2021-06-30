package com.nowcoder.community.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;

@Configuration
public class RedisConfig {

//    第三方bean
    @Bean
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory factory){
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        //具备访问数据库能力
        template.setConnectionFactory(factory);
        //java数据存到数据库序列化方式，指定
        //设置key的序列化方式
        template.setKeySerializer(RedisSerializer.string());
        //设置value的序列化方式,设置为json，因为是结构化的，容易恢复
        template.setValueSerializer(RedisSerializer.json());
        //设置hash的key的序列化方式
        template.setHashKeySerializer(RedisSerializer.string());
        //设置hash的value的序列化方式
        template.setHashKeySerializer(RedisSerializer.json());

        //设置生效
        template.afterPropertiesSet();
        return template;
    }
}
