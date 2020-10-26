package com.atlassian.bitbucket.jenkins.internal.client;

import java.util.List;

public interface BitbucketFilesClient {

    List<String> getFilesForRepository();
}
