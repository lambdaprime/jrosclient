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
package id.jrosmessages.primitives;

import id.kineticstreamer.annotations.Streamed;
import id.xfunction.XJson;

public class Duration {

    /**
     * Seconds (stamp_secs) since epoch
     */
    @Streamed
    public int sec;
    
    /**
     * Nanoseconds since this.sec
     */
    @Streamed
    public int nsec;

    public Duration() {

    }
    
    public Duration(int sec, int nsec) {
        this.sec = sec;
        this.nsec = nsec;
    }

    @Override
    public String toString() {
        return XJson.asString("sec", sec,
                "nsec", nsec).toString();
    }
    
    @Override
    public int hashCode() {
        return sec + nsec;
    }
    
    @Override
    public boolean equals(Object obj) {
        Duration other = (Duration) obj;
        return sec == other.sec
                && nsec == other.nsec;
    }

}
