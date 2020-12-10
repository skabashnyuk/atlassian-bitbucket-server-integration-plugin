package com.atlassian.bitbucket.jenkins.internal.applink.oauth.fullapplink;

import hudson.model.InvisibleAction;
import hudson.util.HttpResponses;
import org.kohsuke.stapler.HttpResponse;
import org.kohsuke.stapler.verb.GET;

public class ApplinkEndpoint extends InvisibleAction {

    private static final String PROVIDER = "provider";
    private static final String CONSUMER = "consumer";

    private final String applinkId;

    public ApplinkEndpoint(String applinkId) {
        this.applinkId = applinkId;
    }

    @GET
    public HttpResponse getAuthentication(String type) {
        //TODO: This needs to be according to each applink

        if (PROVIDER.equals(type)) {
            return (req, rsp, node) -> {
                rsp.setContentType("application/xml;charset=UTF-8");
                rsp.getWriter().println("");
            };
        }
        if (CONSUMER.equals(type)) {
            return HttpResponses.status(204);
        }
        return HttpResponses.status(400);
    }
}
