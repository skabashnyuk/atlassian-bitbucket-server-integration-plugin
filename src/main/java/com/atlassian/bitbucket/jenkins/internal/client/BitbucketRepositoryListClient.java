package com.atlassian.bitbucket.jenkins.internal.client;

import com.atlassian.bitbucket.jenkins.internal.model.BitbucketRepository;

import java.util.List;

public interface BitbucketRepositoryListClient {

    List<BitbucketRepository> getRepositoriesForProject();
}
