package com.atlassian.bitbucket.jenkins.internal.applink.oauth.fullapplink;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamImplicit;

import java.util.Collections;
import java.util.List;

enum TypeId {
    GENERIC("generic"),
    JENKINS("jenkins");

    private final String value;

    TypeId(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}

@XStreamAlias("manifest")
public class Manifest {

    private final String id;
    private final String name;
    private final String typeId = TypeId.JENKINS.getValue();
    private final String version = "2.1.1";
    private final String buildNumber = "201001";
    private final String applinkVersion = "7.2.0";

    @XStreamImplicit(itemFieldName = "incomingAuthenticationType")
    private final List incomingAuthenticationType = Collections.singletonList(
            "com.atlassian.applinks.api.auth.types.OAuthAuthenticationProvider"
    );

    @XStreamImplicit(itemFieldName = "outgoingAuthenticationType")
    private final List outgoingAuthenticationType = Collections.emptyList();

    private final boolean publicSignup = false;
    private final String url;
    private final String iconUrl;
    private final String iconUri;

    public Manifest(String id, String name, String url) {
        this.id = id;
        this.name = name;
        this.url = url;
        this.iconUrl = url + "/static/f70b2c22/images/headshot.png";
        this.iconUri = url + "/static/f70b2c22/images/headshot.png";
    }

    public String getApplinkVersion() {
        return applinkVersion;
    }

    public String getBuildNumber() {
        return buildNumber;
    }

    public String getIconUrl() {
        return iconUrl;
    }

    public String getId() {
        return id;
    }

    public List<String> getIncomingAuthenticationTypes() {
        return incomingAuthenticationType;
    }

    public String getName() {
        return name;
    }

    public List<String> getOutgoingAuthenticationTypes() {
        return outgoingAuthenticationType;
    }

    public String getTypeId() {
        return typeId;
    }

    public String getUrl() {
        return url;
    }

    public String getIconUri() {
        return iconUri;
    }

    public String getVersion() {
        return version;
    }

    public boolean isPublicSignup() {
        return publicSignup;
    }
}