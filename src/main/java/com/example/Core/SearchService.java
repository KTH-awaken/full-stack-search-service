package com.example.Core;

import com.example.Entities.Account;
import com.example.Entities.Condition;
import com.example.Entities.Encounter;
import com.example.Entities.UserType;
import com.example.Persistence.AccountRepo;
import com.example.Persistence.ConditionRepo;
import com.example.Persistence.EncounterRepo;
import com.example.View.ViewModels.SearchResult;
import com.example.View.ViewModels.SearchCriteria;
import com.speedment.jpastreamer.application.JPAStreamer;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;
@ApplicationScoped
public class SearchService {

    @Inject
    JPAStreamer jpaStreamer;

    public List<SearchResult> search(SearchCriteria criteria) {
        Set<SearchResult> results = new LinkedHashSet<>();


        Stream<Account> accountStream = jpaStreamer.stream(Account.class);
        Stream<Condition> conditionStream = jpaStreamer.stream(Condition.class);
        Stream<Encounter> encounterStream = jpaStreamer.stream(Encounter.class);
        if (criteria.getEmail() != null) {
            accountStream = accountStream.filter(a ->
                    a.getEmail().equals(criteria.getEmail()) ||
                    a.getFirstName().equals(criteria.getFirstName()) ||
                    a.getLastName().equals(criteria.getLastName()));

            conditionStream = conditionStream.filter(c ->
                    c.getPatientEmail().equals(criteria.getEmail()) ||
                    c.getDoctorEmail().equals(criteria.getEmail()) ||
                    c.getDiagnosis().equals(criteria.getConditionTitle())
            );

            encounterStream = encounterStream.filter(e ->
                    e.getPatientEmail().equals(criteria.getEmail()) ||
                    e.getDoctorEmail().equals(criteria.getEmail()) ||
                    e.getTitle().equals(criteria.getEncounterTitle())
            );
        }
        fillResult(accountStream,conditionStream,encounterStream,results);







        return new ArrayList<>(results);

    }

    private void fillResult( Stream<Account> accountStream, Stream<Condition> conditionStream,Stream<Encounter> encounterStream, Set<SearchResult>  results){
        accountStream.map(a -> new SearchResult(
                "account",
                a.getFirstName() + " " + a.getLastName(),
                a.getUserType() == UserType.PATIENT ? "Account: Patient" : "Account: Doctor",
                "2022-12-22")).forEach(results::add);

        conditionStream.map(c -> new SearchResult(
                "condition",
                c.getDiagnosis(),
                "Condition",
                c.getTimestamp().toString())).forEach(results::add);

        encounterStream.map(e -> new SearchResult(
                "encounter",
                e.getTitle(),
                "Encounter",
                e.getTimestamp().toString()
        )).forEach(results::add);
    }
}