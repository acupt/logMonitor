package com.acupt.service;

import com.acupt.util.JedisUtil;
import redis.clients.jedis.Jedis;

import java.util.Set;

/**
 * Created by liujie on 2017/3/19.
 */
public class RedisService {

    private Jedis getJedis() {
        return JedisUtil.getInstance().getJedis();
    }

    public Long putInSet(String key, String... members) {
        Jedis jedis = getJedis();
        try {
            return jedis.sadd(key, members);
        } finally {
            jedis.close();
        }
    }

    public Long removeFromSet(String key, String... members) {
        Jedis jedis = getJedis();
        try {
            return jedis.srem(key, members);
        } finally {
            jedis.close();
        }
    }

    public Boolean existInSet(String key, String member) {
        Jedis jedis = getJedis();
        try {
            return jedis.sismember(key, member);
        } finally {
            jedis.close();
        }
    }

    public Set<String> getSet(String key) {
        Jedis jedis = getJedis();
        try {
            return jedis.smembers(key);
        } finally {
            jedis.close();
        }
    }

    public Long incr(String key) {
        Jedis jedis = getJedis();
        try {
            return jedis.incr(key);
        } finally {
            jedis.close();
        }
    }

    public String set(String key, String value) {
        Jedis jedis = getJedis();
        try {
            return jedis.set(key, value);
        } finally {
            jedis.close();
        }
    }

    public String get(String key) {
        Jedis jedis = getJedis();
        try {
            return jedis.get(key);
        } finally {
            jedis.close();
        }
    }

    public String lpop(String key) {
        Jedis jedis = getJedis();
        try {
            return jedis.lpop(key);
        } finally {
            jedis.close();
        }
    }
}
