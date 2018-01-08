package com.system.design.tinyurl.infrastructure.url.hibernate;

import com.system.design.tinyurl.domain.url.TinyUrl;
import com.system.design.tinyurl.domain.url.TinyUrlId;
import com.system.design.tinyurl.domain.url.TinyUrlRepository;
import org.hibernate.SessionFactory;

import java.util.Optional;
import java.util.UUID;

public class HibernateTinyUrlRepository implements TinyUrlRepository {

    private final SessionFactory sessionFactory;

    public HibernateTinyUrlRepository(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public TinyUrlId nextIdentity() {
        return new TinyUrlId(UUID.randomUUID().toString());
    }

    @Override
    public void save(TinyUrl tinyUrl) {
        sessionFactory.getCurrentSession().save(tinyUrl);
    }

    @Override
    public Optional<TinyUrl> getById(TinyUrlId id) {
        final TinyUrl query = sessionFactory.getCurrentSession().get(TinyUrl.class, id);
        return Optional.ofNullable(query);
    }
}
