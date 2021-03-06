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
/*
 * Authors:
 * - lambdaprime <intid@protonmail.com>
 */
package id.jrosclient;

import java.util.concurrent.SubmissionPublisher;

import id.jrosclient.impl.Utils;
import id.jrosmessages.Message;
import id.xfunction.XJson;

/**
 * <p>Topic publisher which is based on Java class {@link java.util.concurrent.SubmissionPublisher}.</p>
 * 
 * <p>It asynchronously issues submitted messages to current subscribers.
 * All subscribers receive messages in the same order.</p>
 * 
 * <p>By default for delivery to subscribers ForkJoinPool.commonPool() is used.
 * Each subscriber uses an independent queue.</p>
 * 
 * <p>If there is no subscribers available for a given topic then all published
 * messages are discarded.</p>
 * 
 * <p>See <a href="{@docRoot}/../index.html">Module documentation</a> for examples.</p>
 * 
 */
public class TopicSubmissionPublisher<M extends Message> extends SubmissionPublisher<M> 
    implements TopicPublisher<M>
{
    private static final Utils utils = new Utils();

    private Class<M> messageClass;
    private String topic;

    /**
     * @param messageClass class of messages in the topic
     * @param topic topic name
     */
    public TopicSubmissionPublisher(Class<M> messageClass, String topic) {
        this.messageClass = messageClass;
        this.topic = utils.formatTopicName(topic);
    }
    
    public Class<M> getMessageClass() {
        return messageClass;
    }
    
    public String getTopic() {
        return topic;
    }
    
    @Override
    public String toString() {
        return XJson.asString("topic", topic);
    }

}
