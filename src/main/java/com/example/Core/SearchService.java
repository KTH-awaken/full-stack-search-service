package com.example.Core;

import com.example.Entities.Account;
import com.example.Entities.Condition;
import com.example.Entities.Encounter;
import com.example.Entities.UserType;
import com.example.View.ViewModels.SearchResult;
import com.example.View.ViewModels.SearchCriteria;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.hibernate.reactive.mutiny.Mutiny;

import java.util.ArrayList;
import java.util.HashSet;
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
                                                    return Account.listAll()
                                                            .onItem().transformToUni(acsts -> {
                                                                Set<String> emails = acsts.stream()
                                                                        .filter(a -> a instanceof Account)
                                                                        .map(a -> (Account) a)
                                                                        .filter(a -> a.getFirstName().equals(criteria.getFirstName()) ||
                                                                                a.getLastName().equals(criteria.getLastName()))
                                                                        .map(Account::getEmail)
                                                                        .collect(Collectors.toSet());

                                                                        return searchWithEmails(emails, criteria)
                                                                                .onItem().transform(emailSearchResults -> {
                                                                                    results.addAll(emailSearchResults);
                                                                                    return new ArrayList<>(results);
                                                                                });

                                                            });

                                                });
                                    });
                        })
        );
    }

    private Uni<List<SearchResult>> searchWithEmails(Set<String> emails, SearchCriteria criteria) {
        Set<SearchResult> results = new HashSet<>();

        return Condition.listAll()
                .onItem().transformToUni(cons -> {
                    // Process conditions
                    List<Condition> conditions = cons.stream()
                            .filter(c -> c instanceof Condition)
                            .map(c -> (Condition) c)
                            .filter(c -> emails.contains(c.getPatientEmail()) || emails.contains(c.getDoctorEmail()))
                            .toList();

                    results.addAll(processConditions(conditions, criteria));

                    // Process encounters
                    return Encounter.listAll()
                            .onItem().transformToUni(enctrs -> {
                                List<Encounter> encounters = enctrs.stream()
                                        .filter(e -> e instanceof Encounter)
                                        .map(e -> (Encounter) e)
                                        .filter(e -> emails.contains(e.getPatientEmail()) || emails.contains(e.getDoctorEmail()))
                                        .toList();

                                results.addAll(processEncounters(encounters, criteria));
                                return Uni.createFrom().item(new ArrayList<>(results));
                            });
                });
    }

    private Set<SearchResult> processAccounts(List<Account> accounts, SearchCriteria criteria) {
        return accounts.stream()
                .filter(a -> matchesCriteria(a, criteria))
                .map(a -> new SearchResult(a.id,"Account", a.getFirstName() + " " + a.getLastName(), findKeyMatch(a,criteria), "2022-12-20"))
                .collect(Collectors.toSet());
    }

    private Set<SearchResult> processConditions(List<Condition> conditions, SearchCriteria criteria) {
        return conditions.stream()
                .filter(c-> matchesCriteria(c, criteria))
                .map(c -> new SearchResult(c.id,"Condition", c.getDiagnosis(), findKeyMatch(c,criteria), c.getTimestamp().toString()))
                .collect(Collectors.toSet());
    }

    private Set<SearchResult> processEncounters(List<Encounter> encounters, SearchCriteria criteria) {
        return encounters.stream()
                .filter(e-> matchesCriteria(e, criteria))
                .map(e -> new SearchResult(e.id, "Encounter", e.getTitle(), findKeyMatch(e,criteria), e.getTimestamp().toString()))
                .collect(Collectors.toSet());
    }


    private String findKeyMatch(Account e, SearchCriteria criteria){
        if(e.getLastName().equals(criteria.getEmail()) )
            if(e.getUserType() == UserType.PATIENT)
                return "Patient has lastname";
            else
                return "Doctor has lastname";
        if(e.getFirstName().equals(criteria.getEmail()))
            if(e.getUserType() == UserType.PATIENT)
                return "Patient has firstname";
            else
                return "Doctor has firstname";
        else
        if(e.getUserType() == UserType.PATIENT)
            return "Patient has email";
        else
            return "Doctor has email";
    }

    private String findKeyMatch(Encounter e, SearchCriteria criteria){
        if(e.getPatientEmail().equals(criteria.getEmail()) )
            return "Encounter has patient email";
        if(e.getDoctorEmail().equals(criteria.getEmail()))
            return "Encounter has doctor email";
        else
            return "Encounter title";
    }
    private String findKeyMatch(Condition e, SearchCriteria criteria){
        if(e.getPatientEmail().equals(criteria.getEmail()) )
            return "Condition has patient email";
        if(e.getDoctorEmail().equals(criteria.getEmail()))
            return "Condition is written by doctor with email";
        else
            return "Condition title";
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