package com.atlassian.bitbucket.jenkins.internal.applink.oauth.fullapplink;

import hudson.model.InvisibleAction;
import org.kohsuke.stapler.HttpResponse;
import org.kohsuke.stapler.StaplerRequest;
import org.kohsuke.stapler.StaplerResponse;
import org.kohsuke.stapler.WebMethod;
import org.kohsuke.stapler.verb.GET;

import javax.servlet.ServletException;
import java.io.IOException;

public class ApplinkEndpoint extends InvisibleAction {

    String manifestXML = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\n" +
                         "<manifest>\n" +
                         "\t<id>9f2d636e-c842-3388-8a66-17c1b951dd45</id>\n" +
                         "\t<name>Applink Jenkins Test</name>\n" +
                         "\t<typeId>generic</typeId>\n" +
                         "\t<version>7.12.3</version>\n" +
                         "\t<buildNumber>712004</buildNumber>\n" +
                         "\t<applinksVersion>5.4.5</applinksVersion>\n" +

                         "\t<inboundAuthenticationTypes>com.atlassian.applinks.api.auth.types.TwoLeggedOAuthAuthenticationProvider</inboundAuthenticationTypes>\n" +
                         "\t<inboundAuthenticationTypes>com.atlassian.applinks.api.auth.types.OAuthAuthenticationProvider</inboundAuthenticationTypes>\n" +
                         "\t<inboundAuthenticationTypes>com.atlassian.applinks.api.auth.types.TwoLeggedOAuthWithImpersonationAuthenticationProvider</inboundAuthenticationTypes>\n" +
                         "\t<inboundAuthenticationTypes>com.atlassian.applinks.api.auth.types.BasicAuthenticationProvider</inboundAuthenticationTypes>\n" +
                         "\t<inboundAuthenticationTypes>com.atlassian.applinks.api.auth.types.TrustedAppsAuthenticationProvider</inboundAuthenticationTypes>\n" +

                         "\t<outboundAuthenticationTypes>com.atlassian.applinks.api.auth.types.TwoLeggedOAuthAuthenticationProvider</outboundAuthenticationTypes>\n" +
                         "\t<outboundAuthenticationTypes>com.atlassian.applinks.api.auth.types.OAuthAuthenticationProvider</outboundAuthenticationTypes>\n" +
                         "\t<outboundAuthenticationTypes>com.atlassian.applinks.api.auth.types.TwoLeggedOAuthWithImpersonationAuthenticationProvider</outboundAuthenticationTypes>\n" +
                         "\t<outboundAuthenticationTypes>com.atlassian.applinks.api.auth.types.BasicAuthenticationProvider</outboundAuthenticationTypes>\n" +
                         "\t<outboundAuthenticationTypes>placeholder.to.ensure.backwards.compatibility</outboundAuthenticationTypes>\n" +
                         "\t<outboundAuthenticationTypes>com.atlassian.applinks.api.auth.types.TrustedAppsAuthenticationProvider</outboundAuthenticationTypes>\n" +

                         // OMG so very, very false
                         "\t<publicSignup>false</publicSignup>\n" +
                         "\t<url>http://localhost:8080/jenkins/bitbucket</url>\n" +
                         "\t<iconUrl>http://localhost:8080/jenkins/static/f70b2c22/images/headshot.png</iconUrl>\n" +
                         "</manifest>";

    @GET
    @WebMethod(name = "manifest")
    public HttpResponse getManifest(StaplerRequest request, StaplerResponse response) throws IOException {
        return new HttpResponse() {
            public void generateResponse(StaplerRequest req, StaplerResponse rsp, Object node) throws IOException, ServletException {
                rsp.setContentType("application/xml;charset=UTF-8");
                rsp.getWriter().println(manifestXML);
            }
        };
    }
}
