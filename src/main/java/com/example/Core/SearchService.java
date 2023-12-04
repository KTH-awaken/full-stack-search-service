package com.example.Core;

import com.example.Entities.Account;
import com.example.Entities.Condition;
import com.example.Entities.Encounter;
import com.example.View.ViewModels.SearchResult;
import com.example.View.ViewModels.SearchCriteria;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.hibernate.reactive.mutiny.Mutiny;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@ApplicationScoped
public class SearchService {

    @Inject
    Mutiny.SessionFactory session;




    public Uni<List<SearchResult>> search(SearchCriteria criteria) {
        return session.withTransaction(tx ->
                Account.listAll()
                        .onItem().transformToUni(acs -> {
                            List<Account> accounts = acs.stream()
                                    .filter(e -> e instanceof Account)
                                    .map(e -> (Account) e)
                                    .collect(Collectors.toList());

                            Set<SearchResult> results = processAccounts(accounts, criteria);


                            return Condition.listAll()
                                    .onItem().transformToUni(cons -> {
                                        List<Condition> conditions = cons.stream()
                                                .filter(e -> e instanceof Condition)
                                                .map(e -> (Condition) e)
                                                .toList();

                                        results.addAll(processConditions(conditions, criteria));

                                        return Encounter.listAll()
                                                .onItem().transformToUni(enctrs -> {
                                                    List<Encounter> encounters = enctrs.stream()
                                                            .filter(e -> e instanceof Encounter)
                                                            .map(e -> (Encounter) e)
                                                            .toList();
                                                    results.addAll(processEncounters(encounters, criteria));
                                                    return Uni.createFrom().item(new ArrayList<>(results));
                                                });
                                    });
                        })
        );
    }

    private Set<SearchResult> processAccounts(List<Account> accounts, SearchCriteria criteria) {
        return accounts.stream()
                .filter(a -> matchesCriteria(a, criteria))
                .map(a -> new SearchResult("account", a.getFirstName() + " " + a.getLastName(), "Account", "2022-12-20"))
                .collect(Collectors.toSet());
    }

    private Set<SearchResult> processConditions(List<Condition> conditions, SearchCriteria criteria) {
        return conditions.stream()
                .filter(c-> matchesCriteria(c, criteria))
                .map(c -> new SearchResult("condition", c.getDiagnosis(), "Condition", c.getTimestamp().toString()))
                .collect(Collectors.toSet());
    }

    private Set<SearchResult> processEncounters(List<Encounter> encounters, SearchCriteria criteria) {
        return encounters.stream()
                .filter(e-> matchesCriteria(e, criteria))
                .map(e -> new SearchResult("encounter", e.getTitle(), "Encounter", e.getTimestamp().toString()))
                .collect(Collectors.toSet());
    }


    private boolean matchesCriteria(Account a, SearchCriteria criteria) {
        return a.getEmail().equals(criteria.getEmail()) ||
                a.getFirstName().equals(criteria.getFirstName()) ||
                a.getLastName().equals(criteria.getLastName());
    }

    private boolean matchesCriteria(Condition c, SearchCriteria criteria) {
        return  c.getPatientEmail().equals(criteria.getEmail()) ||
                c.getDoctorEmail().equals(criteria.getEmail()) ||
                c.getDiagnosis().equals(criteria.getConditionTitle());
    }

    private boolean matchesCriteria(Encounter e, SearchCriteria criteria) {
        return e.getPatientEmail().equals(criteria.getEmail()) ||
                e.getDoctorEmail().equals(criteria.getEmail()) ||
                e.getTitle().equals(criteria.getEncounterTitle());
    }


}