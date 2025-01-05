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

import id.jrosclient.metrics.JRosClientMetrics;
import id.jrosclient.utils.RosNameUtils;
import id.jrosmessages.Message;
import id.xfunction.XJson;
import id.xfunction.lang.XThread;
import id.xfunction.logging.XLogger;
import io.opentelemetry.api.GlobalOpenTelemetry;
import io.opentelemetry.api.common.Attributes;
import io.opentelemetry.api.metrics.LongCounter;
import io.opentelemetry.api.metrics.Meter;
import java.util.concurrent.Executor;
import java.util.concurrent.Flow;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.SubmissionPublisher;

/**
 * Topic publisher which is based on Java class {@link java.util.concurrent.SubmissionPublisher}.
 *
 * <p>It asynchronously issues submitted messages to current subscribers. All subscribers receive
 * messages in the same order.
 *
 * <p>By default for delivery to subscribers ForkJoinPool.commonPool() is used. Each subscriber uses
 * an independent queue.
 *
 * <p>If there is no subscribers available for a given topic then all published messages are
 * discarded.
 *
 * <p>See <a href="{@docRoot}/../index.html">Module documentation</a> for examples.
 */
public class TopicSubmissionPublisher<M extends Message> extends SubmissionPublisher<M>
        implements TopicPublisher<M> {
    private static final RosNameUtils nameUtils = new RosNameUtils();
    private final XLogger LOGGER = XLogger.getLogger(this);

    private final Meter METER =
            GlobalOpenTelemetry.getMeter(TopicSubmissionPublisher.class.getSimpleName());
    private final LongCounter TOPIC_PUBLISHER_OBJECTS_COUNT_METER =
            METER.counterBuilder(JRosClientMetrics.TOPIC_PUBLISHER_OBJECTS_COUNT_METRIC)
                    .setDescription(
                            JRosClientMetrics.TOPIC_PUBLISHER_OBJECTS_COUNT_METRIC_DESCRIPTION)
                    .build();
    private final LongCounter TOPIC_PUBLISHER_SUBMITTED_MESSAGE_COUNT_METER =
            METER.counterBuilder(JRosClientMetrics.TOPIC_PUBLISHER_SUBMITTED_MESSAGES_COUNT_METRIC)
                    .setDescription(
                            JRosClientMetrics
                                    .TOPIC_PUBLISHER_SUBMITTED_MESSAGES_COUNT_METRIC_DESCRIPTION)
                    .build();
    private final LongCounter TOPIC_PUBLISHER_ERRORS_COUNT_METER =
            METER.counterBuilder(JRosClientMetrics.TOPIC_PUBLISHER_ERRORS_COUNT_METRIC)
                    .setDescription(
                            JRosClientMetrics.TOPIC_PUBLISHER_ERRORS_COUNT_METRIC_DESCRIPTION)
                    .build();
    private Class<M> messageClass;
    private String topic;
    private Attributes metricAttributes;

    /**
     * Create topic publisher with {@link ForkJoinPool#commonPool()} executor and {@link
     * Flow#defaultBufferSize()} buffer size
     *
     * @param messageClass class of messages in the topic
     * @param topic topic name
     */
    public TopicSubmissionPublisher(Class<M> messageClass, String topic) {
        this(messageClass, topic, ForkJoinPool.commonPool(), Flow.defaultBufferSize());
    }

    /**
     * @param messageClass class of messages in the topic
     * @param topic topic name
     */
    public TopicSubmissionPublisher(
            Class<M> messageClass, String topic, Executor executor, int maxBufferCapacity) {
        super(executor, maxBufferCapacity);
        this.messageClass = messageClass;
        this.topic = topic;
        metricAttributes =
                Attributes.builder().put("topic", nameUtils.toAbsoluteName(topic)).build();
        TOPIC_PUBLISHER_OBJECTS_COUNT_METER.add(1, metricAttributes);
    }

    @Override
    public Class<M> getMessageClass() {
        return messageClass;
    }

    @Override
    public String getTopic() {
        return topic;
    }

    @Override
    public int submit(M item) {
        TOPIC_PUBLISHER_SUBMITTED_MESSAGE_COUNT_METER.add(1, metricAttributes);
        return super.submit(item);
    }

    @Override
    public void close() {
        LOGGER.entering("close");
        while (estimateMaximumLag() > 0) {
            LOGGER.fine("Some messages are still waiting to be delivered, sleeping...");
            XThread.sleep(100);
        }
        LOGGER.fine("There is no more messages waiting to be delivered, closing the publisher");
        super.close();
        LOGGER.exiting("close");
    }

    @Override
    public String toString() {
        return XJson.asString("topic", topic);
    }

    @Override
    public void onPublishError(Throwable exception) {
        LOGGER.severe("Error delivering message to the subscriber", exception);
        TOPIC_PUBLISHER_ERRORS_COUNT_METER.add(1, metricAttributes);
    }
}
