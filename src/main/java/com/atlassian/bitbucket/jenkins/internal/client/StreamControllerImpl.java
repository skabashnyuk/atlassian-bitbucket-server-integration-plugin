package com.atlassian.bitbucket.jenkins.internal.client;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Stream;

public class StreamControllerImpl<T> implements StreamController<T> {

    private final Stream<T> stream;
    private final AtomicBoolean valve;

    public StreamControllerImpl(Stream<T> stream, AtomicBoolean valve) {
        this.stream = stream;
        this.valve = valve;
    }

    @Override
    public Stream<T> getStream() {
        return stream;
    }

    @Override
    public void stopStream() {
        valve.set(false);
    }
}
