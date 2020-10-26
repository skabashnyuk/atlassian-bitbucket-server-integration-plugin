package com.atlassian.bitbucket.jenkins.internal.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class BitbucketFilesPage {

    private final boolean isLastPage;
    private final int limit;
    private final int size;
    private final int start;
    private final List<String> values;

    @JsonCreator
    public BitbucketFilesPage(
            @JsonProperty("size") int size,
            @JsonProperty("limit") int limit,
            @JsonProperty("isLastPage") boolean isLastPage,
            @JsonProperty("values") List<String> values,
            @JsonProperty("start") int start
    ) {
        this.isLastPage = isLastPage;
        this.limit = limit;
        this.size = size;
        this.start = start;
        this.values = values;
    }

    public boolean isLastPage() {
        return isLastPage;
    }

    public int getLimit() {
        return limit;
    }

    public int getSize() {
        return size;
    }

    public int getStart() {
        return start;
    }

    public List<String> getValues() {
        return values;
    }
}
