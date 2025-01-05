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

import id.jrosclient.impl.JRosClientSubscription;
import id.jrosclient.metrics.JRosClientMetrics;
import id.jroscommon.RosName;
import id.jrosmessages.Message;
import id.jrosmessages.MessageDescriptor;
import id.xfunction.Preconditions;
import id.xfunction.logging.XLogger;
import io.opentelemetry.api.GlobalOpenTelemetry;
import io.opentelemetry.api.common.Attributes;
import io.opentelemetry.api.metrics.LongCounter;
import io.opentelemetry.api.metrics.Meter;
import java.util.Optional;
import java.util.concurrent.Flow;
import java.util.concurrent.Flow.Subscription;

/**
 * Subscriber receives messages for the topic it is subscribed for and passes them to the onNext
 * method.
 *
 * <p>This class simplifies implementation of new subscribers since it provides default
 * implementations for most methods except onNext. The onNext method needs to be implemented for
 * each subscriber separately.
 *
 * <p>Before subscriber can start to receive messages it needs to be subscribed for the topic.
 *
 * <p>See <a href="{@docRoot}/../index.html">Module documentation</a> for examples.
 *
 * @param <M> type of messages in the topic
 * @author lambdaprime intid@protonmail.com
 */
public abstract class TopicSubscriber<M extends Message> implements Flow.Subscriber<M> {
    private final XLogger LOGGER = XLogger.getLogger(this);

    private final Meter METER =
            GlobalOpenTelemetry.getMeter(TopicSubmissionPublisher.class.getSimpleName());
    private final LongCounter TOPIC_SUBSCRIBER_OBJECTS_COUNT_METER =
            METER.counterBuilder(JRosClientMetrics.TOPIC_SUBSCRIBER_OBJECTS_COUNT_METRIC)
                    .setDescription(
                            JRosClientMetrics.TOPIC_SUBSCRIBER_OBJECTS_COUNT_METRIC_DESCRIPTION)
                    .build();
    private final LongCounter TOPIC_SUBSCRIBER_MESSAGES_RECEIVED_COUNT_METER =
            METER.counterBuilder(JRosClientMetrics.TOPIC_SUBSCRIBER_MESSAGES_RECEIVED_COUNT_METRIC)
                    .setDescription(
                            JRosClientMetrics
                                    .TOPIC_SUBSCRIBER_MESSAGES_RECEIVED_COUNT_METRIC_DESCRIPTION)
                    .build();

    private MessageDescriptor<M> messageDescriptor;
    private Optional<Subscription> subscription = Optional.empty();
    private RosName topic;
    private int initNumOfMessages = 1;
    private boolean muteDefaultHandlerDetails;
    private Attributes metricAttributes;

    /**
     * Simplified version of {@link #TopicSubscriber(MessageDescriptor, RosName)} where topic is
     * converted to {@link RosName} and messageClass to {@link MessageDescriptor}
     */
    public TopicSubscriber(Class<M> messageClass, String topic) {
        this(new MessageDescriptor<>(messageClass), new RosName(topic));
    }

    /**
     * Creates subscriber for a topic which when first subscribed will request 1 message.
     *
     * @param messageDescriptor descriptor of the messages in this topic
     * @param topic Name of the topic which messages current subscriber wants to receive. Topic name
     *     which should start from '/'
     */
    public TopicSubscriber(MessageDescriptor<M> messageDescriptor, RosName topic) {
        this.messageDescriptor = messageDescriptor;
        this.topic = topic;
        metricAttributes = Attributes.builder().put("topic", topic.toString()).build();
        TOPIC_SUBSCRIBER_OBJECTS_COUNT_METER.add(1, metricAttributes);
    }

    /**
     * Allows to set how many messages to request once this subscriber will be first subscribed to
     * some topic. Default number is one.
     */
    public TopicSubscriber<M> withInitialRequest(int numOfMessages) {
        initNumOfMessages = numOfMessages;
        return this;
    }

    @Override
    public void onSubscribe(Subscription subscription) {
        Preconditions.isTrue(this.subscription.isEmpty(), "Already subscribed");
        this.subscription = Optional.of(new JRosClientSubscription(subscription));
        this.subscription.get().request(initNumOfMessages);
    }

    @Override
    public void onNext(M item) {
        TOPIC_SUBSCRIBER_MESSAGES_RECEIVED_COUNT_METER.add(1, metricAttributes);
    }

    /**
     * Common throwable types:
     *
     * <ul>
     *   <li>EOFException - publisher unexpectedly closed the connection
     * </ul>
     */
    @Override
    public void onError(Throwable throwable) {
        LOGGER.severe("Default onError handler", throwable);
        System.err.println("Exception received by default TopicSubscriber::onError handler:");
        if (!muteDefaultHandlerDetails) {
            System.err.println(
                    "To change default onError handler, simply override TopicSubscriber::onError of"
                            + " the subscriber");
            muteDefaultHandlerDetails = true;
        }
        throwable.printStackTrace();
    }

    /** Default implementation does nothing but it can be redefined. */
    @Override
    public void onComplete() {}

    /** Descriptor of the messages in the current topic */
    public MessageDescriptor<M> getMessageDescriptor() {
        return messageDescriptor;
    }

    public Optional<Subscription> getSubscription() {
        return subscription;
    }

    /** Name of the topic of this subscriber */
    public RosName getTopic() {
        return topic;
    }
}
