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

package com.codahale.shamir;

import static org.quicktheories.quicktheories.generators.SourceDSL.integers;
import static org.quicktheories.quicktheories.generators.SourceDSL.lists;

import com.google.common.primitives.Bytes;
import okio.ByteString;
import org.quicktheories.quicktheories.core.Source;

public interface Generators {

  static Source<Byte> bytes() {
    return integers().between(0, 255)
                     .as(Integer::byteValue, Byte::intValue);
  }

  static Source<Byte> nonZeroBytes() {
    return integers().between(1, 255)
                     .as(Integer::byteValue, Byte::intValue);
  }

  static Source<ByteString> byteStrings(int minSize, int maxSize) {
    return lists().arrayListsOf(bytes())
                  .ofSizeBetween(minSize, maxSize)
                  .as(l -> ByteString.of(Bytes.toArray(l)), s -> Bytes.asList(s.toByteArray()))
                  .describedAs(ByteString::toString);
  }
}
