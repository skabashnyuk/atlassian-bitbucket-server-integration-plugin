package com.atlassian.bitbucket.jenkins.internal.trigger.register;

import com.atlassian.bitbucket.jenkins.internal.model.BitbucketPullState;

class MinimalPullRequest {

    private final long id;
    private final BitbucketPullState state;
    private final String fromRefDisplayId;
    private final long updatedDate;

    public MinimalPullRequest(long id, BitbucketPullState state, String fromRefDisplayId, long updatedDate) {
        this.id = id;
        this.state = state;
        this.fromRefDisplayId = fromRefDisplayId;
        this.updatedDate = updatedDate;
    }

    public long getId() {
        return id;
    }

    public BitbucketPullState getState() {
        return state;
    }

    public String getFromRefDisplayId() {
        return fromRefDisplayId;
    }

    public long getUpdatedDate() {
        return updatedDate;
    }
}
