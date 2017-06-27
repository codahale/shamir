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

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

import com.codahale.shamir.Part;
import java.nio.charset.StandardCharsets;
import org.junit.jupiter.api.Test;

class PartTest {

  private final byte[] blah = "blah".getBytes(StandardCharsets.UTF_8);
  private final Part part = Part.of(1, blah);

  @Test
  void id() throws Exception {
    assertEquals(1, part.id());
  }

  @Test
  void value() throws Exception {
    assertArrayEquals(blah, part.value());
  }

  @Test
  void string() throws Exception {
    assertEquals("Part{id=1, value=[98, 108, 97, 104]}", part.toString());
  }
}