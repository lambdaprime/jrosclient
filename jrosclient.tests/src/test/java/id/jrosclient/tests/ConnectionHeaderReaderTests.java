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
package id.jrosclient.tests;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import id.jrosclient.ros.transport.ConnectionHeader;
import id.jrosclient.ros.transport.io.ConnectionHeaderReader;
import id.jrosclient.tests.ConnectionHeaderSamples.ConnectionHeaderSample;
import id.xfunction.XUtils;
import id.xfunction.function.Curry;
import id.xfunction.function.Unchecked;

public class ConnectionHeaderReaderTests {

    private static Stream<ConnectionHeaderSample> headerSamples() {
        return Stream.of(ConnectionHeaderSamples.HEADER);
    }

    @ParameterizedTest
    @MethodSource("headerSamples")
    public void test_happy(ConnectionHeaderSample header) throws Exception {
        byte[] b = readByteCodes(header.getResource());
        var in = new DataInputStream(new ByteArrayInputStream(b));
        var chr = new ConnectionHeaderReader(in);
        ConnectionHeader ch = chr.read();
        assertEquals(header.getHeader(), ch);
    }

    private byte[] readByteCodes(String resource) {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(bos);
        XUtils.readResourceAsStream(resource)
            .map(l -> Arrays.asList(l.split(" ")))
            .flatMap(List::stream)
            .map(Unchecked.wrapApply(Curry.curryApply2nd(Integer::parseInt, 16)))
            .forEach(Unchecked.wrapAccept(dos::writeByte));
        return bos.toByteArray();
    }

}
