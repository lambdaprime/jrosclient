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

import id.jroscommon.RosName;
import id.jrosmessages.Message;
import id.jrosmessages.MessageDescriptor;
import java.io.IOException;
import java.util.concurrent.Flow;

/**
 * Optional version of Java standard {@link Flow.Publisher} with few additions for JRosClient.
 *
 * <p><b>JRosClient</b> provides {@link TopicSubmissionPublisher} as a default implementation for
 * publisher which can be used in most of the cases.
 *
 * @param <M> type of messages in the topic
 * @author lambdaprime intid@protonmail.com
 */
public interface TopicPublisher<M extends Message> extends Flow.Publisher<M>, AutoCloseable {

    /**
     * @return descriptor of the messages which are published in this topic
     */
    MessageDescriptor<M> getMessageDescriptor();

    /**
     * @return Topic name
     */
    RosName getTopic();

    /**
     * Notifies publisher about an error which happened when trying to deliver message to one of the
     * subscribers
     */
    void onPublishError(Throwable throwable);

    /**
     * Stop to emit any new messages.
     *
     * <p>Many {@link Flow.Publisher} implementations allow to publish messages asynchronously which
     * in most cases require to maintain internal queue. This method puts a constraint to such
     * implementations and requires them to block until there is no more pending messages in its
     * internal queue and all messages are sent to subscribers.
     */
    @Override
    void close() throws IOException;
}
