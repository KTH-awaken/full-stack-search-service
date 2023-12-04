package com.example.Persistence;


import com.example.Entities.Account;
import com.example.Entities.Account$;
import com.example.Entities.Condition;
import com.example.Entities.Condition$;
import com.speedment.jpastreamer.application.JPAStreamer;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class AccountRepo {
    @Inject
    JPAStreamer jpaStreamer;


    public Optional<List<Account>> getUserAccountByEmail(String userEmail){
        List<Account> result = jpaStreamer.stream(Account.class)
                .filter(Account$.email.equal(userEmail))
                .toList();
        return Optional.ofNullable(result.isEmpty() ? null : result);
    }
}
