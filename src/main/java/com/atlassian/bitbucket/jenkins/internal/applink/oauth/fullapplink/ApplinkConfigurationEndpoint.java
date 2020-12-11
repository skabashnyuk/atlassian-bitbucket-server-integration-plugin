package com.atlassian.bitbucket.jenkins.internal.applink.oauth.fullapplink;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import hudson.model.Action;
import hudson.model.InvisibleAction;
import hudson.util.XStream2;
import jenkins.model.Jenkins;
import org.kohsuke.stapler.HttpResponse;
import org.kohsuke.stapler.WebMethod;
import org.kohsuke.stapler.verb.GET;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class ApplinkConfigurationEndpoint extends InvisibleAction {

    private static final String APPLINK_ID = "9f2d636e-c842-3388-8a66-17c1b951dd45";
    private static final String CAPABILITIES = "[\"STATUS_API\",\"MIGRATION_API\"]";

    @GET
    @WebMethod(name = "manifest")
    @SuppressFBWarnings("DM_DEFAULT_ENCODING")
    public HttpResponse getManifest() throws IOException {

        Manifest manifest = new Manifest(APPLINK_ID, "Applinks Jenkins Test", Jenkins.get().getRootUrl());
        XStream2 xStream = new XStream2();
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        xStream.toXMLUTF8(manifest, outputStream);
        String body = new String(outputStream.toByteArray(), StandardCharsets.UTF_8);
        String body2 = body.replace("?xml version=\"1.1\"", "?xml version=\"1.0\"");

        return (request, response, node) -> {
            response.setContentType("application/xml;charset=UTF-8");
            response.getWriter().println(body2);
        };
    }

    @GET
    @WebMethod(name = "capabilities")
    public HttpResponse getCapabilities() {
        return (request, response, node) -> {
            response.setContentType("application/json;charset=UTF-8");
            response.getWriter().println(CAPABILITIES);
        };
    }

    public Action getApplicationLink(String applinkId) {
        // TODO: Use applink identifier in a store
        return new ApplinkEndpoint(APPLINK_ID);
    }

    public Action getStatus(String applinkId) {
        // TODO: Use applink identifier in a store
        return new ApplinkStatusEndpoint(APPLINK_ID);
    }
}
