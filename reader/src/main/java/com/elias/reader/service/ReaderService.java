package com.elias.reader.service;

import com.elias.reader.config.RedisCacheOperator;
import com.elias.reader.util.CommonUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * @author chengrui
 * <p>create at: 2020-07-27 20:06</p>
 * <p>description: </p>
 */
@Slf4j
@Service
public class ReaderService {

    private static final int expires = 3600;
    private static final List<Person> persons;
    private final RedisCacheOperator redisCacheOperator;
    private static final String key = "library:reader";

    static {
        persons = new ArrayList<>();
        persons.add(new Person(CommonUtils.generateRandomString(6), "张三", "male", 23));
        persons.add(new Person(CommonUtils.generateRandomString(6), "莉丝", "female", 23));
        persons.add(new Person(CommonUtils.generateRandomString(6), "尤克里里", "male", 48));
        persons.add(new Person(CommonUtils.generateRandomString(6), "花都", "male", 1200));
        log.info("初始化persons: [{}]", persons);
    }

    public ReaderService(RedisCacheOperator redisCacheOperator) {
        this.redisCacheOperator = redisCacheOperator;
    }

    public Person getPerson(String id) {
        List<Object> objs = redisCacheOperator.hashOperations().multiGet(key, Collections.singletonList(id));
        List<Person> ps = objs.stream().filter(Objects::nonNull).map(v -> (Person)v).collect(Collectors.toList());
        if (!ps.isEmpty()) {
            log.info("getPerson命中缓存");
            return ps.get(0);
        }
        log.info("getPerson缓存未命中，执行方法......");
        Person person = persons.stream().filter(p -> p.getId().equals(id)).findFirst().orElse(null);
        if (person != null) {
            redisCacheOperator.hashOperations().put(key, person.getId(), person);
            redisCacheOperator.expire(key, expires, TimeUnit.SECONDS);
        }
        return person;
    }

    public List<Person> getAllPerson() {
        List<Person> ps = redisCacheOperator.hashOperations().values(key).stream().map(p -> (Person)p).collect(Collectors.toList());
        if (!ps.isEmpty()) {
            log.info("getAllPerson缓存命中，直接返回");
            return ps;
        }
        log.info("getAllPerson缓存未命中，执行方法......");
        Map<String, Person> personMap = new HashMap<>();
        for (Person person : persons) {
            personMap.put(person.getId(), person);
        }
        redisCacheOperator.hashOperations().putAll(key, personMap);
        redisCacheOperator.expire(key, expires, TimeUnit.SECONDS);
        return persons;
    }

    public void addPerson(Person person) {
        persons.add(person);
        redisCacheOperator.hashOperations().put(key, person.getId(), person);
        redisCacheOperator.expire(key, expires, TimeUnit.SECONDS);
    }

    public Person removePerson(String id) {
        Person person = null;
        Iterator<Person> itr = persons.iterator();
        while (itr.hasNext()) {
            Person p = itr.next();
            if (p.getId().equals(id)) {
                itr.remove();
                person = p;
                break;
            }
        }
        if (person != null) {
            redisCacheOperator.hashOperations().delete(key, person.getId());
        }
        return person;
    }

    public Person updatePerson(Person p) {
        Person rs = null;
        for (Person person : persons) {
            if (p.getId().equals(person.getId())) {
                BeanUtils.copyProperties(p, person);
                rs = person;
            }
        }
        if (rs != null) {
            redisCacheOperator.hashOperations().put(key, rs.getId(), rs);
        }
        return rs;
    }
}
