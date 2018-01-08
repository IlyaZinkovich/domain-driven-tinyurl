package com.system.design.tinyurl;

import com.system.design.tinyurl.domain.url.TinyUrl;
import com.system.design.tinyurl.domain.url.TinyUrlId;
import com.system.design.tinyurl.domain.url.TinyUrlRepository;
import com.system.design.tinyurl.infrastructure.url.hibernate.HibernatePersistenceContextConfiguration;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.transaction.Transactional;

import static org.junit.Assert.assertEquals;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = HibernatePersistenceContextConfiguration.class)
@Transactional
public class HibernateTinyUrlRepositoryTest {

    @Autowired
    private TinyUrlRepository tinyUrlRepository;

    @Test
    public void test() {
        final TinyUrlId tinyUrlId = tinyUrlRepository.nextIdentity();
        final TinyUrl tinyUrl = new TinyUrl(tinyUrlId, "urlHash", "originalUrl");
        tinyUrlRepository.save(tinyUrl);
        final TinyUrl persistedTinyUrl = tinyUrlRepository.getById(tinyUrlId).orElseThrow(AssertionError::new);
        assertEquals(tinyUrl, persistedTinyUrl);
    }
}
