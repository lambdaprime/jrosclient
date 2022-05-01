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
package id.jrosclient;

import id.jrosmessages.Message;
import java.io.IOException;
import java.util.EnumSet;
import java.util.concurrent.Flow.Publisher;
import java.util.concurrent.Flow.Subscriber;

/**
 * Main interface of the <b>jrosclient</b> which allows to interact with ROS.
 *
 * @author lambdaprime intid@protonmail.com
 */
public interface JRosClient extends AutoCloseable {

    /** Returns ROS versions which are supported by this client */
    EnumSet<RosVersion> getSupportedRosVersion();

    /**
     * Subscribe to ROS topic
     *
     * @param <M> type of messages in the topic
     * @param topic Name of the topic which messages current subscriber wants to receive.
     * @param messageClass class of the messages in this topic
     * @param subscriber is notified for any new message which gets published to given topic.
     */
    <M extends Message> void subscribe(
            String topic, Class<M> messageClass, Subscriber<M> subscriber) throws Exception;

    /**
     * Subscribe to ROS topic.
     *
     * <p>Shorter version of {@link #subscribe(String, Class, Subscriber)} which is based on {@link
     * TopicSubscriber}
     *
     * @param <M> type of messages in the topic
     * @param subscriber provides information about the topic to subscribe for. Once subscribed it
     *     will be notified for any new message which gets published to given topic.
     */
    <M extends Message> void subscribe(TopicSubscriber<M> subscriber) throws Exception;

    /**
     * Create a new topic and start publishing messages for it.
     *
     * @param <M> type of messages in the topic
     * @param topic Topic name
     * @param messageClass class of the messages in this topic
     * @param publisher is used to emit messages which will be sent to topic subscribers
     */
    <M extends Message> void publish(String topic, Class<M> messageClass, Publisher<M> publisher)
            throws Exception;

    /**
     * Create a new topic and start publishing messages for it.
     *
     * <p>Shorter version of {@link #publish(String, Class, Publisher)} which is based on {@link
     * TopicPublisher}
     *
     * @param <M> type of messages in the topic
     * @param publisher provides information about new topic. Once topic created publisher is used
     *     to emit messages which will be sent to topic subscribers
     */
    <M extends Message> void publish(TopicPublisher<M> publisher) throws Exception;

    /**
     * Unregister publisher and stop it from emitting new messages
     *
     * @param topic name of the topic used by the publisher
     */
    void unpublish(String topic) throws IOException;

    /** Check if there is any publisher available in the system for a given topic */
    boolean hasPublisher(String topic);
}
