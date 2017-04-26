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

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.codahale.shamir.Part;
import com.google.common.base.Charsets;
import okio.ByteString;
import org.junit.jupiter.api.Test;

class PartTest {

  private final ByteString blah = ByteString.encodeString("blah", Charsets.UTF_8);
  private final Part part = Part.of(1, blah);

  @Test
  void id() throws Exception {
    assertEquals(1, part.id());
  }

  @Test
  void value() throws Exception {
    assertEquals(blah, part.value());
  }

  @Test
  void string() throws Exception {
    assertEquals("Part{id=1, value=[text=blah]}", part.toString());
  }
}