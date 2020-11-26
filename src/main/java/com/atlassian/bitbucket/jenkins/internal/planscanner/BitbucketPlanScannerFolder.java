package com.atlassian.bitbucket.jenkins.internal.planscanner;

import com.atlassian.bitbucket.jenkins.internal.client.BitbucketClientFactoryProvider;
import com.atlassian.bitbucket.jenkins.internal.credentials.JenkinsToBitbucketCredentials;
import com.atlassian.bitbucket.jenkins.internal.model.BitbucketRepository;
import com.cloudbees.hudson.plugins.folder.AbstractFolderDescriptor;
import com.cloudbees.hudson.plugins.folder.computed.ChildObserver;
import com.cloudbees.hudson.plugins.folder.computed.ComputedFolder;
import hudson.Extension;
import hudson.model.*;
import jenkins.model.Jenkins;
import org.jenkinsci.plugins.workflow.multibranch.WorkflowMultiBranchProject;
import org.kohsuke.stapler.DataBoundConstructor;

import javax.inject.Inject;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

public class BitbucketPlanScannerFolder extends ComputedFolder<WorkflowMultiBranchProject> {

    private static final String TEST_PROJECT_KEY = "PROJECT_1";
    private static final String TEST_BASE_URL = "http://localhost:7990/bitbucket";

    @DataBoundConstructor
    public BitbucketPlanScannerFolder(
            ItemGroup parent, String name) {
        super(parent, name);
    }

    @Override
    protected void computeChildren(ChildObserver<WorkflowMultiBranchProject> observer,
                                   TaskListener listener) throws IOException, InterruptedException {
        DescriptorImpl descriptor = (DescriptorImpl) getDescriptor();
        BitbucketPlanScannerHelper planScannerHelper = descriptor.getPlanScannerHelper(TEST_BASE_URL, "fake-credentials-id", TEST_PROJECT_KEY);

        try {
            listener.getLogger().println("Starting branch scan... %n");

            // Get all repos with a Jenkinsfile in the root
            listener.getLogger().println("Getting all repos from attached Bitbucket...");
            List<BitbucketRepository> repositories = getBitbucketRepos(planScannerHelper);

            // Filter out to Jenkinsfile
//            listener.getLogger().println("Finding repos that has a Jenkinsfile...");
//            repositories = filterReposWithoutJenkinsfile(repositories, planScannerHelper);

            // Creating/deleting projects
            listener.getLogger().println("Creating/deleting projects (if there are any to create/delete)...");
            createChildren(repositories, listener);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            listener.getLogger().println("Yay! all done...");
        }
    }

    private List<BitbucketRepository> getBitbucketRepos(BitbucketPlanScannerHelper planScannerHelper) {
        // Hit this endpoint: https://docs.atlassian.com/bitbucket-server/rest/7.7.0/bitbucket-rest.html#idp175
        // Returns a list of repositories. Filter and turn into BitbucketRepository objects

        return planScannerHelper.getRepositories();
    }

    private List<BitbucketRepository> filterReposWithoutJenkinsfile(List<BitbucketRepository> repositories,
                                                                    BitbucketPlanScannerHelper planScannerHelper) {
        // Take that list of BitbucketRepositories from the previous step
        // Prepare the "Jenkinsfile" config into a payload
        // Run each one against this endpoint: https://docs.atlassian.com/bitbucket-server/rest/7.7.0/bitbucket-rest.html#idp264
        // Filter out each entry that doesn't succeed that check

        return repositories.stream()
                .filter(repository -> planScannerHelper.repositoryHasJenkinsfile(repository.getSlug()))
                .collect(Collectors.toList());
    }

    private void createChildren(List<BitbucketRepository> repositories, TaskListener listener) {
        // Take in the list of BitbucketRepositories from the previous step
        // We need two sets: Existing (E) and Discovered (D)
            // Delete all projects in E - D
            // Create new projects for D - E

        for (BitbucketRepository repository: repositories) {
            listener.getLogger().println("Creating project for matching repo: " + repository.getName());
            WorkflowMultiBranchProject generatedProject = new WorkflowMultiBranchProject(this, repository.getName());
            this.itemsPut(repository.getName(), generatedProject);
        }

    }

    public Descriptor getScmTemplateDescriptor() {
        return Jenkins.get().getDescriptorOrDie(BitbucketSCMSourceTemplate.class);
    }

    private boolean repoEqual(BitbucketRepository a, BitbucketRepository b) {
        return a.getSlug().equals(b.getSlug());
    }

    @Extension
    public static class DescriptorImpl extends AbstractFolderDescriptor {

        @Inject
        BitbucketClientFactoryProvider bitbucketClientFactoryProvider;
        private transient JenkinsToBitbucketCredentials jenkinsToBitbucketCredentials;

        @Inject
        public void setJenkinsToBitbucketCredentials(
                JenkinsToBitbucketCredentials jenkinsToBitbucketCredentials) {
            this.jenkinsToBitbucketCredentials = jenkinsToBitbucketCredentials;
        }

        @Override
        public String getDisplayName() {
            return "Bitbucket Plan Scanner Folder";
        }

        @Override
        public String getDescription() {
            return "Creates a folder that scans for all Jenkins repos in a Bitbucket project, and creates a Multibranch Pipeline project for them.";
        }

        @Override
        public TopLevelItem newInstance(ItemGroup parent, String name) {
            return new WorkflowMultiBranchProject(parent, name);
        }

        BitbucketPlanScannerHelper getPlanScannerHelper(String baseUrl, String credentialsId, String projectName) {
            return new BitbucketPlanScannerHelper(baseUrl, bitbucketClientFactoryProvider, credentialsId, jenkinsToBitbucketCredentials, projectName);
        }
    }
}
