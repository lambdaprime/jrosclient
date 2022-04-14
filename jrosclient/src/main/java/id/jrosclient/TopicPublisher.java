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
import java.util.concurrent.Flow;

/**
 * Publisher responsible for publishing messages for particular topic. Other ROS nodes can see the
 * topic and subscribe for it so they will start receiving messages published to it.
 *
 * <p><b>JRosClient</b> provides {@link TopicSubmissionPublisher} as a default implementation for
 * publisher which can be used in most of the cases.
 *
 * @param <M> type of messages in the topic
 * @author lambdaprime intid@protonmail.com
 */
public interface TopicPublisher<M extends Message> extends Flow.Publisher<M>, AutoCloseable {

    /** @return class of messages which is published in this topic */
    Class<M> getMessageClass();

    /** @return Topic name */
    String getTopic();

    /**
     * Notifies publisher about an error which happened when trying to deliver message to one of the
     * subscribers
     */
    void onPublishError(Throwable throwable);

    /** Stop to emit any new messages */
    @Override
    void close() throws IOException;
}
