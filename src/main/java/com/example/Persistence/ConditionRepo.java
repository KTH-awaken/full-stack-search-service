package com.example.Persistence;


import com.example.Entities.Condition;
import com.example.Entities.Condition$;
import com.example.Entities.Encounter;
import com.example.Entities.Encounter$;
import com.speedment.jpastreamer.application.JPAStreamer;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class ConditionRepo {
    @Inject
    JPAStreamer jpaStreamer;


    public Optional<List<Condition>> getConditionsByTitle(String title) {
        List<Condition> result = jpaStreamer.stream(Condition.class)
                .filter(Condition$.diagnosis.equal(title))
                .toList();
        return Optional.ofNullable(result.isEmpty() ? null : result);
    }
}
