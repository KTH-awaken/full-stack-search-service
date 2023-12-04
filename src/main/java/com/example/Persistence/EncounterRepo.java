package com.example.Persistence;

import com.example.Entities.Encounter;
import com.example.Entities.Encounter$;
import com.speedment.jpastreamer.application.JPAStreamer;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.List;
import java.util.Optional;


@ApplicationScoped
public class EncounterRepo {
    @Inject
    JPAStreamer jpaStreamer;

    public Optional<List<Encounter>> getEncounterByPatientEmail(String patientEmail) {
        List<Encounter> result = jpaStreamer.stream(Encounter.class)
                .filter(Encounter$.patientEmail.equal(patientEmail))
                .toList();
        return Optional.ofNullable(result.isEmpty() ? null : result);

    }
    public Optional<List<Encounter>> getEncounterByDoctorEmail(String doctorEmail) {
        List<Encounter> result = jpaStreamer.stream(Encounter.class)
                .filter(Encounter$.doctorEmail.equal(doctorEmail))
                .toList();
        return Optional.ofNullable(result.isEmpty() ? null : result);

    }

    public Optional<List<Encounter>> getEncounterByTitle(String title) {
        List<Encounter> result = jpaStreamer.stream(Encounter.class)
                .filter(Encounter$.title.equal(title))
                .toList();
        return Optional.ofNullable(result.isEmpty() ? null : result);

    }
}
