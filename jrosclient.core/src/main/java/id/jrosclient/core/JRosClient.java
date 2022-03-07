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
package id.jrosclient.core;

import id.jrosmessages.Message;
import java.io.IOException;
import java.util.concurrent.Flow.Publisher;
import java.util.concurrent.Flow.Subscriber;

/**
 * Main interface of the library which allows to interact with ROS.
 *
 * <p>Each instance of JRosClient acts as a separate ROS node and listens to its own ports.
 *
 * @author lambdaprime intid@protonmail.com
 */
public interface JRosClient extends AutoCloseable {

    /**
     * Subscribe to ROS topic
     *
     * @param <M> type of messages in the topic
     * @param subscriber provides information about the topic to subscribe for. Once subscribed it
     *     will be notified for any new message which gets published to given topic.
     */
    <M extends Message> void subscribe(TopicSubscriber<M> subscriber) throws Exception;

    /**
     * Subscribe to ROS topic
     *
     * @param <M> type of messages in the topic
     * @param topic Name of the topic which messages current subscriber wants to receive. Topic name
     *     which should start from '/'
     * @param messageClass class of the messages in this topic
     * @param subscriber is notified for any new message which gets published to given topic.
     */
    public <M extends Message> void subscribe(
            String topic, Class<M> messageClass, Subscriber<M> subscriber) throws Exception;

    /**
     * Create a new topic and start publishing messages for it.
     *
     * @param <M> type of messages in the topic
     * @param publisher provides information about new topic. Once topic created publisher is used
     *     to emit messages which will be sent to topic subscribers
     */
    public <M extends Message> void publish(TopicPublisher<M> publisher) throws Exception;

    /**
     * Create a new topic and start publishing messages for it.
     *
     * @param <M> type of messages in the topic
     * @param topic Topic name
     * @param messageClass class of the messages in this topic
     * @param publisher is used to emit messages which will be sent to topic subscribers
     */
    public <M extends Message> void publish(
            String topic, Class<M> messageClass, Publisher<M> publisher) throws Exception;

    /**
     * Unregister publisher in Master node and stop publisher from emitting new messages
     *
     * @param topic name of the topic used by the publisher
     */
    public void unpublish(String topic) throws IOException;
}
