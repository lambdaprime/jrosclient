/*
 * Copyright 2021 jrosclient project
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
package id.jrosclient.impl;

import id.xfunction.logging.XLogger;
import java.util.concurrent.Flow.Subscription;

/** @author lambdaprime intid@protonmail.com */
public class JRosClientSubscription implements Subscription {

    private static final XLogger LOGGER = XLogger.getLogger(JRosClientSubscription.class);

    private Subscription subscription;

    public JRosClientSubscription(Subscription subscription) {
        this.subscription = subscription;
    }

    @Override
    public void request(long n) {
        LOGGER.entering("request", n);
        subscription.request(n);
        LOGGER.exiting("request");
    }

    @Override
    public void cancel() {
        LOGGER.entering("cancel");
        subscription.cancel();
        LOGGER.exiting("cancel");
    }
}
