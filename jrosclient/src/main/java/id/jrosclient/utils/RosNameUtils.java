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
package id.jrosclient.utils;

import id.jrosmessages.Message;
import id.jrosmessages.MessageMetadataAccessor;
import id.xfunction.Preconditions;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * @author lambdaprime intid@protonmail.com
 */
public class RosNameUtils {
    private MessageMetadataAccessor metadataAccessor = new MessageMetadataAccessor();

    /**
     * Returns Fully Qualified Name
     *
     * @see <a
     *     href="https://design.ros2.org/articles/topic_and_service_names.html#fully-qualified-names">Topic
     *     and Service name mapping to DDS</a>
     */
    public String toAbsoluteName(String topicName) {
        if (!topicName.startsWith("/")) topicName = "/" + topicName;
        return topicName;
    }

    /** Returns ROS message name split on tokens */
    public Path getMessageName(Class<? extends Message> messageClass) {
        var messageName = metadataAccessor.getName(messageClass);
        var path = Paths.get(messageName);
        Preconditions.equals(
                2, path.getNameCount(), "Message name format is invalid: " + messageName);
        return path;
    }
}
