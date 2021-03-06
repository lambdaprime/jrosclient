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
package id.jrosclient.ros.transport.io;

import java.io.DataOutput;
import java.io.IOException;
import java.util.Optional;

import id.jrosclient.ros.transport.ConnectionHeader;

import static id.jrosclient.ros.transport.ConnectionHeader.*;

public class ConnectionHeaderWriter {

    private DataOutput out;
    private Utils utils = new Utils();

    public ConnectionHeaderWriter(DataOutput out) {
        this.out = out;
    }

    public void write(ConnectionHeader header) throws IOException {
        int totalLen = 0;
        int len = 0;
        
        len = len(CALLER_ID, header.callerId);
        if (len > 0)
            totalLen += len + 4;
        
        len = len(TOPIC, header.topic);
        if (len > 0)
            totalLen += len + 4;
        
        len = len(TYPE, header.type);
        if (len > 0)
            totalLen += len + 4;
        
        len = len(MESSAGE_DEFINITION, header.messageDefinition);
        if (len > 0)
            totalLen += len + 4;
        
        len = len(MD5_SUM, header.md5sum);
        if (len > 0)
            totalLen += len + 4;
        
        if (totalLen == 0) return;
        utils.writeLen(out, totalLen);        
        
        writeField(CALLER_ID, header.callerId);
        writeField(TOPIC, header.topic);
        writeField(TYPE, header.type);
        writeField(MESSAGE_DEFINITION, header.messageDefinition);
        writeField(MD5_SUM, header.md5sum);
    }
    
    private void writeField(String field, Optional<String> value) throws IOException {
        if (value.isEmpty()) return;
        utils.writeLen(out, len(field, value));
        out.write(field.getBytes());
        out.write('=');
        out.write(value.get().getBytes());
    }

    private int len(String field, Optional<String> value) {
        if (value.isEmpty()) return 0;
        int len = lenField(value, field.length());
        if (len == 0) return 0;
        len += 1; // '='
        return len;
    }

    private int lenField(Optional<String> field, int len) {
        return field.map(String::length).orElse(0) +
                (field.isPresent()? len: 0);
    }

}
