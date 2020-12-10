package com.atlassian.bitbucket.jenkins.internal.applink.oauth.fullapplink;

import hudson.model.InvisibleAction;
import hudson.util.XStream2;
import jenkins.model.Jenkins;
import org.kohsuke.stapler.HttpResponse;
import org.kohsuke.stapler.StaplerRequest;
import org.kohsuke.stapler.StaplerResponse;
import org.kohsuke.stapler.WebMethod;
import org.kohsuke.stapler.verb.GET;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class ApplinkEndpoint extends InvisibleAction {

    private static final String APPLINK_ID = "9f2d636e-c842-3388-8a66-17c1b951dd45";

    @GET
    @WebMethod(name = "manifest")
    public HttpResponse getManifest(StaplerRequest request, StaplerResponse response) throws IOException {

        Manifest manifest = new Manifest(APPLINK_ID, "Applinks Jenkins Test", Jenkins.get().getRootUrl());
        XStream2 xStream = new XStream2();
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        xStream.toXMLUTF8(manifest, outputStream);
        String body = outputStream.toString();

        return (req, rsp, node) -> {
            rsp.setContentType("application/xml;charset=UTF-8");
            rsp.getWriter().println(body);
        };
    }
}
