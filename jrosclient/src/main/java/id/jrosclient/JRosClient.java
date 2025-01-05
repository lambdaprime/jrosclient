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

import id.jrosclient.exceptions.JRosClientException;
import id.jroscommon.RosName;
import id.jrosmessages.Message;
import id.jrosmessages.MessageDescriptor;
import java.io.IOException;
import java.util.EnumSet;
import java.util.concurrent.Flow.Publisher;
import java.util.concurrent.Flow.Subscriber;

/**
 * Main interface of the <b>jrosclient</b> which allows to interact with different versions of ROS.
 *
 * <p>We do not recommend using same object (instance) of JRosClient for publish and subscribe to
 * same topic. It is preferable to use two separate objects (see <a
 * href="https://github.com/lambdaprime/jros2client/blob/45418bf545a77515ddf27b8d95c9d0ace7376df8/jros2client.examples/src/PublisherSubscriberApp.java">PublisherSubscriberApp</a>
 * for ROS2 implementation)
 *
 * @author lambdaprime intid@protonmail.com
 */
public interface JRosClient extends AutoCloseable {

    /** Returns ROS versions which are supported by this client */
    EnumSet<RosVersion> getSupportedRosVersion();

    /**
     * Subscribe to ROS topic
     *
     * <p>By default, subscriber always runs on its own thread after it was subscribed.
     *
     * @param <M> type of messages in the topic
     * @param topic Name of the topic which messages current subscriber wants to receive.
     * @param messageDescriptor descriptor of the messages in this topic
     * @param subscriber is notified for any new message which gets published to given topic.
     */
    <M extends Message> void subscribe(
            RosName topic, MessageDescriptor<M> messageDescriptor, Subscriber<M> subscriber)
            throws JRosClientException;

    /**
     * Simplified version of {@link #subscribe(RosName, MessageDescriptor, Subscriber)} where topic
     * is converted to {@link RosName}
     */
    default <M extends Message> void subscribe(
            String topic, Class<M> messageClass, Subscriber<M> subscriber)
            throws JRosClientException {
        subscribe(new RosName(topic), new MessageDescriptor<>(messageClass), subscriber);
    }

    /**
     * Subscribe to ROS topic.
     *
     * <p>Simplified version of {@link #subscribe(RosName, MessageDescriptor, Subscriber)} which is
     * based on {@link TopicSubscriber}
     *
     * @param <M> type of messages in the topic
     * @param subscriber provides information about the topic to subscribe for. Once subscribed it
     *     will be notified for any new message which gets published to given topic.
     */
    default <M extends Message> void subscribe(TopicSubscriber<M> subscriber)
            throws JRosClientException {
        subscribe(subscriber.getTopic(), subscriber.getMessageDescriptor(), subscriber);
    }

    /**
     * Create a new topic and start publishing messages for it.
     *
     * <p>Publisher is user owned and it is used to publish messages for particular topic. Other ROS
     * nodes can see the topic and subscribe for it so they will start receiving messages published
     * to it.
     *
     * <p>This allows to use same instance of {@link Publisher} to publish messages on multiple of
     * different topics as long as their message types are similar.
     *
     * @param <M> type of messages in the topic
     * @param topic Topic name
     * @param messageClass class of the messages in this topic
     * @param publisher user owned publisher is used to emit messages which will be sent to ROS
     *     topic subscribers
     */
    default <M extends Message> void publish(
            RosName topic, MessageDescriptor<M> messageDescriptor, Publisher<M> publisher)
            throws JRosClientException {
        publish(
                new TopicPublisher<M>() {
                    @Override
                    public void subscribe(Subscriber<? super M> subscriber) {
                        publisher.subscribe(subscriber);
                    }

                    @Override
                    public MessageDescriptor<M> getMessageDescriptor() {
                        return messageDescriptor;
                    }

                    @Override
                    public RosName getTopic() {
                        return topic;
                    }

                    @Override
                    public void onPublishError(Throwable throwable) {
                        throwable.printStackTrace();
                    }

                    @Override
                    public void close() throws IOException {
                        // ignore as it is not Flow.Publisher method
                    }
                });
    }

    /**
     * Simplified version of {@link #publish(RosName, Class, Publisher)} where topic is converted to
     * {@link RosName}
     */
    default <M extends Message> void publish(
            String topic, Class<M> messageClass, Publisher<M> publisher)
            throws JRosClientException {
        publish(new RosName(topic), new MessageDescriptor<>(messageClass), publisher);
    }

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
    <M extends Message> void publish(TopicPublisher<M> publisher) throws JRosClientException;

    /**
     * Unregister publisher and stop it from emitting new messages
     *
     * @param topic name of the topic used by the publisher
     */
    <M extends Message> void unpublish(RosName topic, MessageDescriptor<M> messageDescriptor)
            throws JRosClientException;

    /**
     * Simplified version of {@link #unpublish(RosName, MessageDescriptor)} where topic is converted
     * to {@link RosName}
     */
    default <M extends Message> void unpublish(String topic, Class<M> messageClass)
            throws JRosClientException {
        unpublish(new RosName(topic), new MessageDescriptor<>(messageClass));
    }

    default <M extends Message> void unpublish(TopicPublisher<M> publisher)
            throws JRosClientException {
        unpublish(publisher.getTopic(), publisher.getMessageDescriptor());
    }

    /** Check if there is any publisher available in the system for a given topic */
    boolean hasPublisher(RosName topic);

    /** May block until there is no more pending messages in any of the internal queue */
    @Override
    void close();

    /**
     * @return true if {@link #close()} operation was requested
     */
    boolean isClosed();
}
