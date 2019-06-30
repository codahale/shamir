/*
 * Copyright © 2019 Simon Massey (massey1905@gmail.com)
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
package com.codahale.shamir.polygot;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

/**
 * The GraalJS internals cannot be fooled into casting between unsigned 
 * JavaScript bytes and signed Java bytes. We only ever want to 
 * do this within unit tests to compare arrays in-memory. This class uses  
 * an inefficent workaround of building bidirectional maps. The keys are 
 * Integer to fit a signed byte. 
 */
public class JavaScriptUtils {
  // https://stackoverflow.com/a/12310078/329496
  public static String byteToBinaryString(final byte b) {
    return String.format("%8s", Integer.toBinaryString(b & 0xFF)).replace(' ', '0');
  }

  static Map<Integer, Integer> signedToUnsignedByteMap() {
    final Map<Integer, Integer> result = new HashMap<>();
    for (byte b = Byte.MIN_VALUE; ; b++) {
      final String bits = byteToBinaryString(b);
      final Integer i = Integer.parseInt(bits, 2) & 0xff;
      result.put((int)b, i);
      // here we avoid overflow on b++ causing an infinit loop
      if (b == Byte.MAX_VALUE) break;
    }
    return Collections.unmodifiableMap(result);
  }

  public static Map<Integer, Integer> signedToUnsignedByteMap = signedToUnsignedByteMap();

  public static Map<Integer, Integer> unsignedToSignedByteMap =
      signedToUnsignedByteMap.entrySet().stream()
          .collect(Collectors.toMap(Entry::getValue, Entry::getKey));;

}
