package com.atlassian.bitbucket.jenkins.internal.applink.oauth.fullapplink;

import hudson.Extension;
import hudson.model.Action;
import hudson.model.UnprotectedRootAction;

import javax.annotation.CheckForNull;

@Extension
public class BitbucketRestEndpoint implements UnprotectedRootAction {

    public Action getApplinks(String version) {
        // The version doesn't matter. If Bitbucket says it's okay, then it's okay
        return new ApplinkConfigurationEndpoint();
    }

    @CheckForNull
    @Override
    public String getIconFileName() {
        return null;
    }

    @CheckForNull
    @Override
    public String getDisplayName() {
        return "blah";  //this is horrible, but it was to prove the point that we were supporting urls not under /rest
    }

    @CheckForNull
    @Override
    public String getUrlName() {
        return "blah";
    }
}
