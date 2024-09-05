/*
 * Copyright 2020 jrosclient project
 * 
 * Website: https://github.com/lambdaprime/jrosclient
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package id.jrosclient.tests.integration;

import id.jrosclient.JRosClient;
import id.jrosclient.TopicSubmissionPublisher;
import id.jrosmessages.std_msgs.EmptyMessage;
import id.pubsubtests.PubSubClientTestCase;
import id.pubsubtests.PubSubClientTests;
import id.xfunction.concurrent.flow.FixedCollectorSubscriber;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ForkJoinPool;
import java.util.function.Supplier;
import java.util.stream.Stream;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * @author lambdaprime intid@protonmail.com
 */
public abstract class JRosPubSubClientTests extends PubSubClientTests {

    private static List<TestCase> testCases;
    private static Supplier<JRosClient> clientFactory;

    public record TestCase(
            String testCaseName,
            Supplier<JRosClient> clientFactory,
            Duration discoveryDuration,
            int publisherQueueSize) {}

    protected static void init(Supplier<JRosClient> clientFactory, TestCase... testCases) {
        JRosPubSubClientTests.clientFactory = clientFactory;
        JRosPubSubClientTests.testCases = Arrays.asList(testCases);
    }

    static Stream<PubSubClientTestCase> dataProvider() {
        return testCases.stream()
                .map(
                        tc ->
                                new PubSubClientTestCase(
                                        tc.testCaseName,
                                        () -> new JRosTestPubSubClient(tc.clientFactory.get()),
                                        tc.discoveryDuration,
                                        tc.publisherQueueSize));
    }

    @Test
    public void test_empty_messages() {
        String topic = "/testTopic1";
        try (var publisher = new TopicSubmissionPublisher<>(EmptyMessage.class, topic);
                var pubClient = clientFactory.get();
                var subClient = clientFactory.get(); ) {
            pubClient.publish(publisher);
            var collector = new FixedCollectorSubscriber<>(new ArrayList<EmptyMessage>(), 111);
            subClient.subscribe(topic, EmptyMessage.class, collector);
            ForkJoinPool.commonPool()
                    .submit(
                            () -> {
                                while (!pubClient.isClosed()) {
                                    publisher.submit(new EmptyMessage());
                                }
                            });
            try {
                var actual = collector.getFuture().get();
                Assertions.assertEquals(Collections.nCopies(111, new EmptyMessage()), actual);
                System.out.println("All received");
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        }
    }
}
