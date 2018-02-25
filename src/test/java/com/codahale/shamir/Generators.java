/*
 * Copyright © 2017 Coda Hale (coda.hale@gmail.com)
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
package com.codahale.shamir;

import java.util.Arrays;
import org.quicktheories.core.Gen;
import org.quicktheories.impl.Constraint;

public interface Generators {

  static Gen<Byte> bytes(int minValue, int maxValue) {
    return prng -> ((byte) prng.next(Constraint.between(minValue, maxValue)));
  }

  static Gen<Byte> bytes() {
    return bytes(0, 255);
  }

  static Gen<byte[]> byteArrays(int minSize, int maxSize) {
    final Gen<byte[]> gen =
        prng -> {
          final byte[] bytes = new byte[(int) prng.next(Constraint.between(minSize, maxSize))];
          for (int i = 0; i < bytes.length; i++) {
            bytes[i] = (byte) prng.next(Constraint.between(0, 255));
          }
          return bytes;
        };
    return gen.describedAs(Arrays::toString);
  }
}
