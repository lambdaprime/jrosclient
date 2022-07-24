/*
 * Copyright 2022 jrosclient project
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
import id.jrosmessages.std_msgs.StringMessage;
import id.pubsubtests.TestPubSubClient;
import id.xfunction.concurrent.SameThreadExecutorService;
import id.xfunction.concurrent.flow.TransformProcessor;
import id.xfunction.function.Unchecked;
import java.util.concurrent.Flow.Publisher;
import java.util.concurrent.Flow.Subscriber;

/**
 * @author lambdaprime intid@protonmail.com
 */
public class JRosTestPubSubClient implements TestPubSubClient {

    private JRosClient client;

    public JRosTestPubSubClient(JRosClient client) {
        this.client = client;
    }

    @Override
    public void close() {
        Unchecked.run(client::close);
    }

    @Override
    public void publish(String topic, Publisher<String> publisher) {
        var transformer =
                new TransformProcessor<String, StringMessage>(
                        str -> new StringMessage(str), new SameThreadExecutorService(), 1);
        publisher.subscribe(transformer);
        Unchecked.run(() -> client.publish(topic, StringMessage.class, transformer));
    }

    @Override
    public void subscribe(String topic, Subscriber<String> subscriber) {
        var transformer = new TransformProcessor<StringMessage, String>(m -> m.data);
        transformer.subscribe(subscriber);
        Unchecked.run(() -> client.subscribe(topic, StringMessage.class, transformer));
    }
}
