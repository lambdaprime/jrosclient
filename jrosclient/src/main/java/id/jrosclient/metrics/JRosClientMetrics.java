/*
 * Copyright 2023 jrosclient project
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
package id.jrosclient.metrics;

/**
 * List of metrics emitted by <b>jrosclient</b>.
 *
 * <p><b>jrosclient</b> metrics are integrated with <a
 * href="https://opentelemetry.io/">OpenTelemetry</a>.
 *
 * <p>To receive OpenTelemetry metrics users suppose to configure OpenTelemetry exporter. List of
 * Java exporters can be found in <a
 * href="https://opentelemetry.io/ecosystem/registry/?language=java&component=exporter">OpenTelemetry
 * registry</a> or in <a
 * href="https://github.com/lambdaprime/opentelemetry-exporters-pack">opentelemetry-exporters-pack</a>
 *
 * <p>Example of Elasticsearch dashboard (with exporter from opentelemetry-exporters-pack):
 *
 * <p><img alt="" src="doc-files/elasticsearch.png"/>
 *
 * @author lambdaprime intid@protonmail.com
 */
public interface JRosClientMetrics {

    String PUBLISH_CALLS_COUNT_METRIC = "publish_calls_total";
    String PUBLISH_CALLS_COUNT_METRIC_DESCRIPTION =
            "Total number of times JRosClient::publish is called";

    String SUBSCRIBE_CALLS_COUNT_METRIC = "subscribe_calls_total";
    String SUBSCRIBE_CALLS_COUNT_METRIC_DESCRIPTION =
            "Total number of times JRosClient::subscribe is called";

    String CLIENT_OBJECTS_COUNT_METRIC = "client_objects_total";
    String CLIENT_OBJECTS_COUNT_METRIC_DESCRIPTION = "Total number of jrosclient objects";

    String CLIENT_CLOSE_CALLS_COUNT_METRIC = "client_close_calls_total";
    String CLIENT_CLOSE_CALLS_COUNT_METRIC_DESCRIPTION =
            "Total number of times JRosClient::close is called. It should match client_objects"
                    + " metric otherwise it means that some client objects are not closed.";

    String TOPIC_PUBLISHER_OBJECTS_COUNT_METRIC = "topic_publisher_objects_total";
    String TOPIC_PUBLISHER_OBJECTS_COUNT_METRIC_DESCRIPTION =
            "Total number of TopicPublisher objects";

    String TOPIC_PUBLISHER_SUBMITTED_MESSAGES_COUNT_METRIC =
            "topic_publisher_submitted_messages_total";
    String TOPIC_PUBLISHER_SUBMITTED_MESSAGES_COUNT_METRIC_DESCRIPTION =
            "Total number of messages submitted to TopicPublisher";

    String TOPIC_PUBLISHER_ERRORS_COUNT_METRIC = "topic_publisher_errors_total";
    String TOPIC_PUBLISHER_ERRORS_COUNT_METRIC_DESCRIPTION =
            "Total number of submit errors in TopicPublisher";

    String TOPIC_SUBSCRIBER_OBJECTS_COUNT_METRIC = "topic_subscriber_objects_total";
    String TOPIC_SUBSCRIBER_OBJECTS_COUNT_METRIC_DESCRIPTION =
            "Total number of TopicSubscriber objects";

    String TOPIC_SUBSCRIBER_MESSAGES_RECEIVED_COUNT_METRIC =
            "topic_subscriber_messages_received_total";
    String TOPIC_SUBSCRIBER_MESSAGES_RECEIVED_COUNT_METRIC_DESCRIPTION =
            "Total number of messages received by TopicSubscriber";

    String TOPIC_SUBSCRIBER_MESSAGES_REQUESTED_COUNT_METRIC =
            "topic_subscriber_messages_requested_total";
    String TOPIC_SUBSCRIBER_MESSAGES_REQUESTED_COUNT_METRIC_DESCRIPTION =
            "Total number of messages requested by TopicSubscriber";
}
