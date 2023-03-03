/*
 * Copyright 2022 jrosclient project
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
package id.jrosclient.exceptions;

/**
 * Main unchecked exception for all functionality related to <b>jrosclient</b>
 *
 * @author lambdaprime intid@protonmail.com
 */
public class JRosClientException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public JRosClientException(String fmt, Object... args) {
        super(String.format(fmt, args));
    }

    public JRosClientException(Exception e) {
        super(e);
    }
}
