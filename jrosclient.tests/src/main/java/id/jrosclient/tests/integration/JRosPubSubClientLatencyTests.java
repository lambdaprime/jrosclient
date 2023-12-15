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
import id.pubsubtests.PubSubClientLatencyTestCase;
import id.pubsubtests.PubSubClientLatencyTests;
import java.time.Duration;
import java.util.Arrays;
import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Stream;

/**
 * @author lambdaprime intid@protonmail.com
 */
public abstract class JRosPubSubClientLatencyTests extends PubSubClientLatencyTests {
    private static List<TestCase> testCases;

    public record TestCase(
            String testCaseName,
            Supplier<JRosClient> clientFactory,
            Duration discoveryDuration,
            Duration maxTestDuration,
            int messageSizeInBytes,
            Duration expectedMaxLatency,
            int expectedMinReceivedMessageCount) {}

    protected static void init(TestCase... testCases) {
        JRosPubSubClientLatencyTests.testCases = Arrays.asList(testCases);
    }

    static Stream<PubSubClientLatencyTestCase> dataProvider() {
        return testCases.stream()
                .map(
                        tc ->
                                new PubSubClientLatencyTestCase(
                                        tc.testCaseName,
                                        () -> new JRosTestPubSubClient(tc.clientFactory.get()),
                                        tc.discoveryDuration,
                                        tc.maxTestDuration,
                                        tc.messageSizeInBytes,
                                        tc.expectedMaxLatency,
                                        tc.expectedMinReceivedMessageCount));
    }
}
