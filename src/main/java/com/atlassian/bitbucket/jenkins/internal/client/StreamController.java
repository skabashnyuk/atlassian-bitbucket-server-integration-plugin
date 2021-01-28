package com.atlassian.bitbucket.jenkins.internal.client;

import java.util.stream.Stream;

public interface StreamController<T> {

    Stream<T> getStream();

    /**
     * stop stream but doesn't stop fetching immediately
     */
    void stopStream();
}
