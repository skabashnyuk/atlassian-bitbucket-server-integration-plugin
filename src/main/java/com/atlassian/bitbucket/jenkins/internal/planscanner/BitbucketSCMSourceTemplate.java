package com.atlassian.bitbucket.jenkins.internal.planscanner;

import com.atlassian.bitbucket.jenkins.internal.client.BitbucketClientFactoryProvider;
import com.atlassian.bitbucket.jenkins.internal.config.BitbucketPluginConfiguration;
import com.atlassian.bitbucket.jenkins.internal.credentials.JenkinsToBitbucketCredentials;
import com.atlassian.bitbucket.jenkins.internal.scm.BitbucketScmFormFill;
import com.atlassian.bitbucket.jenkins.internal.scm.BitbucketScmFormFillDelegate;
import com.atlassian.bitbucket.jenkins.internal.scm.BitbucketScmFormValidation;
import com.atlassian.bitbucket.jenkins.internal.scm.BitbucketScmFormValidationDelegate;
import com.atlassian.bitbucket.jenkins.internal.trigger.RetryingWebhookHandler;
import hudson.model.Describable;
import hudson.model.Descriptor;
import hudson.model.Item;
import hudson.plugins.git.GitTool;
import hudson.plugins.git.extensions.GitSCMExtensionDescriptor;
import hudson.util.FormValidation;
import hudson.util.HttpResponses;
import hudson.util.ListBoxModel;
import jenkins.model.Jenkins;
import jenkins.plugins.git.GitSCMSource;
import jenkins.scm.api.trait.SCMSourceTrait;
import org.kohsuke.stapler.AncestorInPath;
import org.kohsuke.stapler.DataBoundConstructor;
import org.kohsuke.stapler.HttpResponse;
import org.kohsuke.stapler.QueryParameter;
import org.kohsuke.stapler.verb.POST;

import javax.annotation.CheckForNull;
import javax.inject.Inject;
import java.util.List;

public class BitbucketSCMSourceTemplate implements Describable<BitbucketSCMSourceTemplate> {

    private final String id;
    private final String credentialsId;
    private final String mirrorName;
    private final String repositoryName;
    private final String serverId;
    private final String sshCredentialsId;
    private final List<SCMSourceTrait> traits;

    @DataBoundConstructor
    public BitbucketSCMSourceTemplate(
            String id,
            @CheckForNull String credentialsId,
            @CheckForNull String sshCredentialsId,
            @CheckForNull List<SCMSourceTrait> traits,
            @CheckForNull String repositoryName,
            @CheckForNull String serverId,
            @CheckForNull String mirrorName) {
        this.id = id;
        this.credentialsId = credentialsId;
        this.sshCredentialsId = sshCredentialsId;
        this.traits = traits;
        this.repositoryName = repositoryName;
        this.serverId = serverId;
        this.mirrorName = mirrorName;
    }

    public String getId() {
        return id;
    }

    public String getCredentialsId() {
        return credentialsId;
    }

    public String getMirrorName() {
        return mirrorName;
    }

    public String getRepositoryName() {
        return repositoryName;
    }

    public String getServerId() {
        return serverId;
    }

    public String getSshCredentialsId() {
        return sshCredentialsId;
    }

    public List<SCMSourceTrait> getTraits() {
        return traits;
    }

    @Override
    public Descriptor<BitbucketSCMSourceTemplate> getDescriptor() {
        return (DescriptorImpl) Jenkins.get().getDescriptorOrDie(this.getClass());
    }

    public static class DescriptorImpl extends Descriptor<BitbucketSCMSourceTemplate> implements BitbucketScmFormValidation,
            BitbucketScmFormFill {

        private final GitSCMSource.DescriptorImpl gitScmSourceDescriptor;
        @Inject
        private BitbucketClientFactoryProvider bitbucketClientFactoryProvider;
        @Inject
        private BitbucketPluginConfiguration bitbucketPluginConfiguration;
        @Inject
        private BitbucketScmFormFillDelegate formFill;
        @Inject
        private BitbucketScmFormValidationDelegate formValidation;
        @Inject
        private JenkinsToBitbucketCredentials jenkinsToBitbucketCredentials;

        @Inject
        private RetryingWebhookHandler retryingWebhookHandler;

        public DescriptorImpl() {
            super();
            gitScmSourceDescriptor = new GitSCMSource.DescriptorImpl();
        }

        @Override
        @POST
        public FormValidation doCheckCredentialsId(@AncestorInPath Item context,
                                                   @QueryParameter String credentialsId) {
            return formValidation.doCheckCredentialsId(context, credentialsId);
        }

        @Override
        @POST
        public FormValidation doCheckProjectName(@AncestorInPath Item context,
                                                 @QueryParameter String serverId,
                                                 @QueryParameter String credentialsId,
                                                 @QueryParameter String projectName) {
            return formValidation.doCheckProjectName(context, serverId, credentialsId, projectName);
        }

        @Override
        @POST
        public FormValidation doCheckRepositoryName(@AncestorInPath Item context,
                                                    @QueryParameter String serverId,
                                                    @QueryParameter String credentialsId,
                                                    @QueryParameter String projectName,
                                                    @QueryParameter String repositoryName) {
            return FormValidation.ok();
        }

        @Override
        @POST
        public FormValidation doCheckServerId(@AncestorInPath Item context,
                                              @QueryParameter String serverId) {
            return formValidation.doCheckServerId(context, serverId);
        }

        @Override
        public FormValidation doCheckSshCredentialsId(@AncestorInPath Item context,
                                                      @QueryParameter String sshCredentialsId) {
            return formValidation.doCheckSshCredentialsId(context, sshCredentialsId);
        }

        @Override
        @POST
        public ListBoxModel doFillCredentialsIdItems(@AncestorInPath Item context,
                                                     @QueryParameter String baseUrl,
                                                     @QueryParameter String credentialsId) {
            return formFill.doFillCredentialsIdItems(context, baseUrl, credentialsId);
        }

        @Override
        @POST
        public ListBoxModel doFillMirrorNameItems(@AncestorInPath Item context,
                                                  @QueryParameter String serverId,
                                                  @QueryParameter String credentialsId,
                                                  @QueryParameter String projectName,
                                                  @QueryParameter String repositoryName,
                                                  @QueryParameter String mirrorName) {
            return formFill.doFillMirrorNameItems(context, serverId, credentialsId, projectName, repositoryName,
                    mirrorName);
        }

        @Override
        @POST
        public HttpResponse doFillProjectNameItems(@AncestorInPath Item context,
                                                   @QueryParameter String serverId,
                                                   @QueryParameter String credentialsId,
                                                   @QueryParameter String projectName) {
            return formFill.doFillProjectNameItems(context, serverId, credentialsId, projectName);
        }

        @Override
        public HttpResponse doFillRepositoryNameItems(Item context, String serverId,
                                                      String credentialsId, String projectName, String repositoryName) {
            return HttpResponses.ok();
        }

        @Override
        @POST
        public ListBoxModel doFillServerIdItems(@AncestorInPath Item context,
                                                @QueryParameter String serverId) {
            return formFill.doFillServerIdItems(context, serverId);
        }

        @Override
        public ListBoxModel doFillSshCredentialsIdItems(@AncestorInPath Item context,
                                                        @QueryParameter String baseUrl,
                                                        @QueryParameter String sshCredentialsId) {
            return formFill.doFillSshCredentialsIdItems(context, baseUrl, sshCredentialsId);
        }

        @Override
        public FormValidation doTestConnection(Item context, String serverId,
                                               String credentialsId, String projectName, String repositoryName,
                                               String mirrorName) {
            return null;
        }

        @Override
        public List<GitSCMExtensionDescriptor> getExtensionDescriptors() {
            return null;
        }

        @Override
        public List<GitTool> getGitTools() {
            return null;
        }

        @Override
        public boolean getShowGitToolOptions() {
            return false;
        }
    }
}
