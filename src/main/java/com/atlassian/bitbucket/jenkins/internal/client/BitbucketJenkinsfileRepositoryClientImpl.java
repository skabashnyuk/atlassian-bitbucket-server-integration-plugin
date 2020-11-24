package com.atlassian.bitbucket.jenkins.internal.client;

import com.atlassian.bitbucket.jenkins.internal.client.paging.BitbucketPageStreamUtil;
import com.atlassian.bitbucket.jenkins.internal.model.BitbucketPage;
import com.atlassian.bitbucket.jenkins.internal.model.BitbucketRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import okhttp3.HttpUrl;

import java.util.List;

import static java.lang.String.valueOf;
import static java.util.Objects.requireNonNull;
import static java.util.stream.Collectors.toList;

public class BitbucketJenkinsfileRepositoryClientImpl implements BitbucketJenkinsfileRepositoryClient {

    private static final String JENKINSFILE_PATH = "Jenkinsfile";
    private final BitbucketRequestExecutor bitbucketRequestExecutor;
    private final String projectName;

    BitbucketJenkinsfileRepositoryClientImpl(BitbucketRequestExecutor bitbucketRequestExecutor, String projectName) {
        this.bitbucketRequestExecutor = requireNonNull(bitbucketRequestExecutor, "bitbucketRequestExecutor");
        this.projectName = requireNonNull(projectName, "projectName");
    }

    @Override
    public List<BitbucketRepository> getRepositoriesWithJenkinsfile() {
        BitbucketPage<BitbucketRepository> firstPage = bitbucketRequestExecutor.makeGetRequest(getUrlBuilder().build(),
                new TypeReference<BitbucketPage<BitbucketRepository>>() {}).getBody();
        return BitbucketPageStreamUtil.toStream(firstPage, previous -> bitbucketRequestExecutor.makeGetRequest(getUrlBuilder()
                        .addQueryParameter("start", valueOf(previous.getNextPageStart()))
                        .build(),
                new TypeReference<BitbucketPage<BitbucketRepository>>() {}).getBody())
                .flatMap(page -> page.getValues().stream())
                // TODO: We will have a full BitbucketProject from the constructor so compare on key instead
                .filter(repo -> repo.getProject().getName().equalsIgnoreCase(projectName))
                .collect(toList());
    }

    private HttpUrl.Builder getUrlBuilder() {
        return bitbucketRequestExecutor.getBaseUrl().newBuilder()
                .addPathSegment("rest")
                .addPathSegment("search")
                .addPathSegment("latest")
                .addPathSegment("repos-matching-root-file")
                .addQueryParameter("query", projectName)
                .addQueryParameter("rootFile", JENKINSFILE_PATH);
    }
}
