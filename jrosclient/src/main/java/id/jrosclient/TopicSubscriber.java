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
import id.jrosclient.utils.RosNameUtils;
import id.jrosmessages.Message;
import id.xfunction.Preconditions;
import id.xfunction.logging.XLogger;
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
    private static final RosNameUtils utils = new RosNameUtils();

    private Class<M> messageClass;
    private Optional<Subscription> subscription = Optional.empty();
    private String topic;
    private int initNumOfMessages = 1;
    private boolean muteDefaultHandlerDetails;

    /**
     * Creates subscriber for a topic which when first subscribed will request 1 message.
     *
     * @param messageClass class of the messages in this topic
     * @param topic Name of the topic which messages current subscriber wants to receive. Topic name
     *     which should start from '/'
     */
    public TopicSubscriber(Class<M> messageClass, String topic) {
        this.messageClass = messageClass;
        this.topic = utils.toAbsoluteName(topic);
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

    /**
     * Common throwable types:
     *
     * <ul>
     *   <li>EOFException - publisher unexpectedly closed the connection
     * </ul>
     */
    @Override
    public void onError(Throwable throwable) {
        LOGGER.severe("Default onError handler: {0}", throwable);
        System.err.println("Exception received by default TopicSubscriber::onError handler:");
        if (!muteDefaultHandlerDetails) {
            System.err.println(
                    "To change default onError handler, simply override onError of the subscriber");
            muteDefaultHandlerDetails = true;
        }
        throwable.printStackTrace();
    }

    /** Default implementation does nothing but it can be redefined. */
    @Override
    public void onComplete() {}

    /** Class of the messages in the current topic */
    public Class<M> getMessageClass() {
        return messageClass;
    }

    public Optional<Subscription> getSubscription() {
        return subscription;
    }

    /** Name of the topic of this subscriber */
    public String getTopic() {
        return topic;
    }
}
