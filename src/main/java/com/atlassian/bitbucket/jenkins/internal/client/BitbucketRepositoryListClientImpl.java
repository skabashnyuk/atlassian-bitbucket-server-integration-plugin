package com.atlassian.bitbucket.jenkins.internal.client;

import com.atlassian.bitbucket.jenkins.internal.model.BitbucketRepository;
import com.atlassian.bitbucket.jenkins.internal.model.BitbucketRepositoryPage;
import okhttp3.HttpUrl;

import java.util.ArrayList;
import java.util.List;

import static java.util.Objects.requireNonNull;

public class BitbucketRepositoryListClientImpl implements BitbucketRepositoryListClient {

    private final BitbucketRequestExecutor bitbucketRequestExecutor;
    private final String projectKey;

    BitbucketRepositoryListClientImpl(BitbucketRequestExecutor bitbucketRequestExecutor, String projectKey) {
        this.bitbucketRequestExecutor = requireNonNull(bitbucketRequestExecutor, "bitbucketRequestExecutor");
        this.projectKey = requireNonNull(projectKey, "projectKey");
    }

    @Override
    public List<BitbucketRepository> getRepositoriesForProject() {
        List<BitbucketRepository> repositories = new ArrayList<>();

        HttpUrl.Builder urlBuilder = bitbucketRequestExecutor.getCoreRestPath().newBuilder()
                .addPathSegment("projects")
                .addPathSegment(projectKey)
                .addPathSegment("repos");

        BitbucketRepositoryPage repoPage;
        do {
            repoPage = bitbucketRequestExecutor.makeGetRequest(urlBuilder.build(), BitbucketRepositoryPage.class).getBody();
            repositories.addAll(repoPage.getValues());
        } while (!repoPage.isLastPage());

        return repositories;
    }
}
