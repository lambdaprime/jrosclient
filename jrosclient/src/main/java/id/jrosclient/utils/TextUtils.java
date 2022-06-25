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

import id.xfunction.text.Ellipsizer;
import java.util.Arrays;
import java.util.Objects;
import java.util.Optional;

/**
 * @author lambdaprime intid@protonmail.com
 */
public class TextUtils {

    private Optional<Ellipsizer> ellipsizerOpt = Optional.empty();

    public TextUtils withEllipsize(int maxLength) {
        ellipsizerOpt = Optional.of(new Ellipsizer(maxLength));
        return this;
    }

    /**
     * Converts array to string and truncates it in the middle if it is longer than maximum allowed
     * value
     */
    public String toString(Object obj) {
        var str = Objects.toString(obj);
        return ellipsizerOpt.map(e -> e.ellipsizeMiddle(str)).orElse(str);
    }

    public String toString(Object[] a) {
        return toString(Arrays.toString(a));
    }

    public String toString(byte[] obj) {
        return toString(Arrays.toString(obj));
    }
}
