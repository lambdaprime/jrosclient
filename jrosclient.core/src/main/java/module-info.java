/*
 * Copyright 2020 jrosclient project
 *
 * Website: https://github.com/lambdaprime/jrosclient
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */

/**
 * <b>jrosclient</b> interfaces and classes which are agnostic to version of ROS (Robot Operating
 * System).
 *
 * @see <a href= "https://github.com/lambdaprime/jrosclient-core/releases">Download</a>
 * @see <a href="https://github.com/lambdaprime/jrosclient-core">GitHub repository</a>
 * @author lambdaprime intid@protonmail.com
 */
module jrosclient.core {
    requires id.xfunction;
    requires transitive jrosmessages;

    exports id.jrosclient.core;
    exports id.jrosclient.core.utils;
}
