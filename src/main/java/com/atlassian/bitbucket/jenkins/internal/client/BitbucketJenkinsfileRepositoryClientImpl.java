package com.atlassian.bitbucket.jenkins.internal.client;

import com.atlassian.bitbucket.jenkins.internal.model.BitbucketPage;
import com.atlassian.bitbucket.jenkins.internal.model.BitbucketRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import okhttp3.HttpUrl;

import java.util.ArrayList;
import java.util.List;

import static java.util.Objects.requireNonNull;

public class BitbucketJenkinsfileRepositoryClientImpl implements BitbucketJenkinsfileRepositoryClient {

    private static final String JENKINSFILE_PATH = "Jenkinsfile";
    private final BitbucketRequestExecutor bitbucketRequestExecutor;
    private final HttpUrl.Builder url;

    BitbucketJenkinsfileRepositoryClientImpl(BitbucketRequestExecutor bitbucketRequestExecutor, String projectName) {
        this.bitbucketRequestExecutor = requireNonNull(bitbucketRequestExecutor, "bitbucketRequestExecutor");
        requireNonNull(projectName, "projectKey");

        url = bitbucketRequestExecutor.getBaseUrl().newBuilder()
                .addPathSegment("rest")
                .addPathSegment("search")
                .addPathSegment("latest")
                .addPathSegment("repos-matching-root-file")
                .addQueryParameter("query", projectName)
                .addQueryParameter("rootFile", JENKINSFILE_PATH);
    }

    @Override
    public List<BitbucketRepository> getRepositoriesMatchingRootFile() {
        List<BitbucketRepository> repositories = new ArrayList<>();

        BitbucketPage<BitbucketRepository> repoPage;
        do {
            repoPage =
                    bitbucketRequestExecutor.makeGetRequest(url.build(), new TypeReference<BitbucketPage<BitbucketRepository>>() {}).getBody();
            repositories.addAll(repoPage.getValues());
        } while (!repoPage.isLastPage());

        return repositories;
    }
}
