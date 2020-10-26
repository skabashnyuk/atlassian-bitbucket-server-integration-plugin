package com.atlassian.bitbucket.jenkins.internal.planscanner;

import com.atlassian.bitbucket.jenkins.internal.client.BitbucketClientFactory;
import com.atlassian.bitbucket.jenkins.internal.client.BitbucketClientFactoryProvider;
import com.atlassian.bitbucket.jenkins.internal.credentials.JenkinsToBitbucketCredentials;
import com.atlassian.bitbucket.jenkins.internal.credentials.JenkinsToBitbucketCredentialsImpl;
import com.atlassian.bitbucket.jenkins.internal.model.BitbucketRepository;

import java.util.List;

public class BitbucketPlanScannerHelper {

    private final BitbucketClientFactory clientFactory;
    private final String projectKey;

    public BitbucketPlanScannerHelper(String bitbucketBaseUrl,
                                      BitbucketClientFactoryProvider clientFactoryProvider,
                                      String credentialsId,
                                      JenkinsToBitbucketCredentials jenkinsToBitbucketCredentials,
                                      String projectKey) {
        this.projectKey = projectKey;
        clientFactory = clientFactoryProvider.getClient(bitbucketBaseUrl,
                //jenkinsToBitbucketCredentials.toBitbucketCredentials(credentialsId));
                JenkinsToBitbucketCredentialsImpl.getBasicCredentials("admin", "admin"));
    }

    public List<BitbucketRepository> getRepositories() {
        return clientFactory.getRepositoryListClient(projectKey)
                .getRepositoriesForProject();
    }

    public boolean repositoryHasJenkinsfile(String repositorySlug) {
        return clientFactory.getFilesClient(projectKey, repositorySlug)
                .getFilesForRepository()
                .stream()
                //Or whatever mapping we want to use!
                .anyMatch("Jenkinsfile"::equals);
    }
}
