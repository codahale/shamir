/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.codahale.shamir.tests;

import com.codahale.shamir.Share;
import org.junit.Test;

import static java.nio.charset.StandardCharsets.UTF_8;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

public class ShareTest {
    private final Share share = new Share(1, "blah".getBytes(UTF_8));

    @Test
    public void id() throws Exception {
        assertEquals(1, share.getId());
    }

    @Test
    public void value() throws Exception {
        assertArrayEquals("blah".getBytes(UTF_8), share.getValue());
    }

    @Test
    public void string() throws Exception {
        assertEquals("Share[id = 1, value = [98, 108, 97, 104]]", share.toString());
    }
}