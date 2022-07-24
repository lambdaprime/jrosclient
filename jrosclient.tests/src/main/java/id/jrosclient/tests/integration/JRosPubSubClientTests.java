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
import id.pubsubtests.PubSubClientTests;
import id.pubsubtests.TestPubSubClient;
import java.util.function.Supplier;
import java.util.stream.Stream;

public abstract class JRosPubSubClientTests extends PubSubClientTests {

    private static Supplier<JRosClient> clientFactory;

    protected static void init(Supplier<JRosClient> clientFactory) {
        JRosPubSubClientTests.clientFactory = clientFactory;
    }

    private static TestPubSubClient createClient() {
        return new JRosTestPubSubClient(clientFactory.get());
    }

    static Stream<TestCase> dataProvider() {
        return Stream.of(new TestCase(JRosPubSubClientTests::createClient));
    }
}
