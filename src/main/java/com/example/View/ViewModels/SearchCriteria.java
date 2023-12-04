package com.example.View.ViewModels;

public class SearchCriteria {
    private String firstName;
    private String lastName;
    private String email;
    private String conditionTitle;
    private String encounterTitle;

    public SearchCriteria() {
    }

    public SearchCriteria(String firstName, String lastName, String email, String conditionTitle, String encounterTitle) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.conditionTitle = conditionTitle;
        this.encounterTitle = encounterTitle;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getConditionTitle() {
        return conditionTitle;
    }

    public void setConditionTitle(String conditionTitle) {
        this.conditionTitle = conditionTitle;
    }

    public String getEncounterTitle() {
        return encounterTitle;
    }

    public void setEncounterTitle(String encounterTitle) {
        this.encounterTitle = encounterTitle;
    }

    @Override
    public String toString() {
        return "SearchCriteria{" +
                "firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", email='" + email + '\'' +
                ", conditionTitle='" + conditionTitle + '\'' +
                ", encounterTitle='" + encounterTitle + '\'' +
                '}';
    }
}
