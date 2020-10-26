package com.atlassian.bitbucket.jenkins.internal.client;

import com.atlassian.bitbucket.jenkins.internal.model.BitbucketFilesPage;
import okhttp3.HttpUrl;

import java.util.ArrayList;
import java.util.List;

public class BitbucketFilesClientImpl implements BitbucketFilesClient {

    private final BitbucketRequestExecutor bitbucketRequestExecutor;
    private final String projectKey;
    private final String repoSlug;

    BitbucketFilesClientImpl(BitbucketRequestExecutor bitbucketRequestExecutor, String projectKey, String repoSlug) {
        this.bitbucketRequestExecutor = bitbucketRequestExecutor;
        this.projectKey = projectKey;
        this.repoSlug = repoSlug;
    }

    @Override
    public List<String> getFilesForRepository() {
        List<String> files = new ArrayList<>();

        HttpUrl.Builder urlBuilder = bitbucketRequestExecutor.getCoreRestPath().newBuilder()
                .addPathSegment("projects")
                .addPathSegment(projectKey)
                .addPathSegment("repos")
                .addPathSegment(repoSlug)
                .addPathSegment("files");

        BitbucketFilesPage filesPage;
        do {
            filesPage = bitbucketRequestExecutor.makeGetRequest(urlBuilder.build(), BitbucketFilesPage.class).getBody();
            files.addAll(filesPage.getValues());
        } while (!filesPage.isLastPage());

        return files;
    }
}
