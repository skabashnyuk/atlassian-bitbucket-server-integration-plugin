package com.atlassian.bitbucket.jenkins.internal.applink.oauth.fullapplink;

import hudson.model.InvisibleAction;

public class ApplinkStatusEndpoint extends InvisibleAction {

    private final String applinkId;

    public ApplinkStatusEndpoint(String applinkId) {
        this.applinkId = applinkId;
    }
}
