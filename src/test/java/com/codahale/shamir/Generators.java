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

import java.util.Arrays;
import org.quicktheories.quicktheories.core.Source;

public interface Generators {

  static Source<Byte> bytes(int minValue, int maxValue) {
    return Source.of((prng, step) -> ((byte) prng.nextInt(minValue, maxValue)));
  }

  static Source<Byte> bytes() {
    return bytes(0, 255);
  }

  static Source<byte[]> byteArrays(int minSize, int maxSize) {
    return Source.of((prng, step) -> {
      final byte[] bytes = new byte[prng.nextInt(minSize, maxSize)];
      for (int i = 0; i < bytes.length; i++) {
        bytes[i] = (byte) prng.nextInt(0, 255);
      }
      return bytes;
    }).describedAs(Arrays::toString);
  }
}
