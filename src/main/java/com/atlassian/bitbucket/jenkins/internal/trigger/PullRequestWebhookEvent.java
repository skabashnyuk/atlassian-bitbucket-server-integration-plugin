package com.atlassian.bitbucket.jenkins.internal.trigger;

import com.atlassian.bitbucket.jenkins.internal.model.BitbucketPullRequest;
import com.atlassian.bitbucket.jenkins.internal.model.BitbucketUser;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.annotation.Nullable;
import java.util.Date;

import static java.util.Objects.requireNonNull;

@JsonIgnoreProperties(ignoreUnknown = true)
public class PullRequestWebhookEvent extends AbstractWebhookEvent {

    private final String eventKey;
    private final BitbucketPullRequest pullRequest;

    @JsonCreator
    public PullRequestWebhookEvent(
            @JsonProperty(value = "actor") @Nullable BitbucketUser actor,
            @JsonProperty(value = "eventKey", required = true) String eventKey,
            @JsonProperty(value = "date", required = true) Date date,
            @JsonProperty(value = "pullRequest", required = true) BitbucketPullRequest pullRequest) {
        super(actor, eventKey, date);
        this.pullRequest = requireNonNull(pullRequest, "pullRequest");
        this.eventKey = requireNonNull(eventKey, "eventKey");
    }

    @Override
    public String getEventKey() {
        return eventKey;
    }

    public BitbucketPullRequest getPullRequest() {
        return pullRequest;
    }
}

