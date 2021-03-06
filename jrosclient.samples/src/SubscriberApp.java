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
/*
 * Authors:
 * - lambdaprime <intid@protonmail.com>
 */
import id.jrosclient.JRosClient;
import id.jrosclient.TopicSubscriber;
import id.jrosmessages.std_msgs.StringMessage;

/**
 * Demonstrates how to subscribe to ROS topics.
 */
public class SubscriberApp {

    public static void main(String[] args) throws Exception {
        // specify URL of the master node
        var client = new JRosClient("http://localhost:11311/");
        String topicName = "/helloRos";
        // register a new subscriber
        client.subscribe(new TopicSubscriber<>(StringMessage.class, topicName) {
            @Override
            public void onNext(StringMessage item) {
                System.out.println(item);
                // request next message
                request(1);
            }
        });
    }
}

