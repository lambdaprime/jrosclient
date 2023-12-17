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
package id.jrosclient;

/**
 * List of metrics emitted by <b>jrosclient</b>.
 *
 * @author lambdaprime intid@protonmail.com
 */
public interface JRosClientMetrics {

    String PUBLISH_CALLS_METRIC = "publish_calls";
    String PUBLISH_CALLS_METRIC_DESCRIPTION = "Number of times JRosClient::publish is called";

    String SUBSCRIBE_CALLS_METRIC = "subscribe_calls";
    String SUBSCRIBE_CALLS_METRIC_DESCRIPTION = "Number of times JRosClient::subscribe is called";

    String CLIENT_OBJECTS_METRIC = "client_objects";
    String CLIENT_OBJECTS_METRIC_DESCRIPTION = "Number of jrosclient objects";

    String CLIENT_CLOSE_CALLS_METRIC = "client_close_calls";
    String CLIENT_CLOSE_CALLS_METRIC_DESCRIPTION =
            "Number of times JRosClient::close is called. It should match client_objects metric"
                    + " otherwise it means that some client objects are not closed.";

    String TOPIC_PUBLISHER_OBJECTS_METRIC = "topic_publisher_objects";
    String TOPIC_PUBLISHER_OBJECTS_METRIC_DESCRIPTION = "Number of TopicPublisher objects";

    String TOPIC_PUBLISHER_SUBMITTED_MESSAGES_METRIC = "topic_publisher_submitted_messages";
    String TOPIC_PUBLISHER_SUBMITTED_MESSAGES_METRIC_DESCRIPTION =
            "Number of messages submitted to TopicPublisher";

    String TOPIC_PUBLISHER_ERRORS_METRIC = "topic_publisher_errors";
    String TOPIC_PUBLISHER_ERRORS_METRIC_DESCRIPTION = "Number of submit errors in TopicPublisher";

    String TOPIC_SUBSCRIBER_OBJECTS_METRIC = "topic_subscriber_objects";
    String TOPIC_SUBSCRIBER_OBJECTS_METRIC_DESCRIPTION = "Number of TopicSubscriber objects";

    String TOPIC_SUBSCRIBER_MESSAGES_RECEIVED_METRIC = "topic_subscriber_messages_received";
    String TOPIC_SUBSCRIBER_MESSAGES_RECEIVED_METRIC_DESCRIPTION =
            "Number of messages received by TopicSubscriber";

    String TOPIC_SUBSCRIBER_MESSAGES_REQUESTED_METRIC = "topic_subscriber_messages_requested";
    String TOPIC_SUBSCRIBER_MESSAGES_REQUESTED_METRIC_DESCRIPTION =
            "Number of messages requested by TopicSubscriber";
}
