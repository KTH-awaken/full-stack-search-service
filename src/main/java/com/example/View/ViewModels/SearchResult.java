package com.example.View.ViewModels;

import java.util.Objects;

public class SearchResult {
    private String type;
    private String title;
    private String matchKey;
    private String createdAt;

    public SearchResult() {
    }

    public SearchResult(String type, String title, String matchKey, String createdAt) {
        this.type = type;
        this.title = title;
        this.matchKey = matchKey;
        this.createdAt = createdAt;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMatchKey() {
        return matchKey;
    }

    public void setMatchKey(String matchKey) {
        this.matchKey = matchKey;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public String toString() {
        return "SearchResult{" +
                "type='" + type + '\'' +
                ", title='" + title + '\'' +
                ", matchKey='" + matchKey + '\'' +
                ", createdAt='" + createdAt + '\'' +
                '}';
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SearchResult that = (SearchResult) o;
        return Objects.equals(type, that.type) &&
                Objects.equals(title, that.title) &&
                Objects.equals(createdAt, that.createdAt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(type, title, createdAt);
    }
}
