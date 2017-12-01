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

import com.codahale.shamir.Scheme;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Sets;
import org.junit.jupiter.api.Test;
import org.quicktheories.WithQuickTheories;

import java.nio.charset.StandardCharsets;
import java.util.*;

import static com.codahale.shamir.Generators.byteArrays;
import static org.junit.jupiter.api.Assertions.*;

class SchemeTest implements WithQuickTheories {

  @Test
  void hasProperties() {
    final Scheme scheme = Scheme.of(5, 3);

    assertEquals(5, scheme.n());
    assertEquals(3, scheme.k());
  }

  @Test
  @SuppressWarnings("ResultOfMethodCallIgnored")
  void tooManyShares() {
    assertThrows(IllegalArgumentException.class, () -> Scheme.of(2_000, 3));
  }

  @Test
  @SuppressWarnings("ResultOfMethodCallIgnored")
  void thresholdTooLow() {
    assertThrows(IllegalArgumentException.class, () -> Scheme.of(1, 1));
  }

  @Test
  @SuppressWarnings("ResultOfMethodCallIgnored")
  void thresholdTooHigh() {
    assertThrows(IllegalArgumentException.class, () -> Scheme.of(1, 2));
  }

  @SuppressWarnings("ResultOfMethodCallIgnored")
  @Test
  void joinEmptyParts() {
    assertThrows(
        IllegalArgumentException.class, () -> Scheme.of(3, 2).join(Collections.emptyMap()));
  }

  @SuppressWarnings("ResultOfMethodCallIgnored")
  @Test
  void joinIrregularParts() {
    final byte[] one = new byte[] {1};
    final byte[] two = new byte[] {1, 2};

    assertThrows(
        IllegalArgumentException.class,
        () -> Scheme.of(3, 2).join(ImmutableMap.of(1, one, 2, two)));
  }

  @Test
  void splitAndJoinSingleByteSecret() {
    final Scheme scheme = Scheme.of(8, 3);
    final byte[] secret = "x".getBytes(StandardCharsets.UTF_8);

    assertArrayEquals(secret, scheme.join(scheme.split(secret)));
  }

  @Test
  void splitAndJoinMoreThanByteMaxValueParts() {
    final Scheme scheme = Scheme.of(200, 3);
    final byte[] secret = "x".getBytes(StandardCharsets.UTF_8);

    assertArrayEquals(secret, scheme.join(scheme.split(secret)));
  }

  @Test
  void splitAndJoinQuorate() {
    // All distinct subsets of parts of cardinality greater than or equal to the threshold should
    // join to recover the original secret.
    qt().forAll(integers().between(2, 5), integers().between(2, 5), byteArrays(1, 300))
        .asWithPrecursor((k, extra, secret) -> Scheme.of(k + extra, k))
        .check(
            (k, e, secret, scheme) -> {
              final Map<Integer, byte[]> parts = scheme.split(secret);
              return Sets.powerSet(parts.entrySet())
                  .stream()
                  .parallel()
                  .filter(s -> s.size() >= k)
                  .map(entries -> join(scheme, entries))
                  .allMatch(s -> Arrays.equals(s, secret));
            });
  }

  @Test
  void splitAndJoinInquorate() {
    // All distinct subsets of parts of cardinality less than the threshold should never join to
    // recover the original secret. Only check larger secrets to avoid false positives.
    qt().forAll(integers().between(2, 5), integers().between(2, 5), byteArrays(3, 300))
        .asWithPrecursor((k, extra, secret) -> Scheme.of(k + extra, k))
        .check(
            (k, e, secret, scheme) -> {
              final Map<Integer, byte[]> parts = scheme.split(secret);
              return Sets.powerSet(parts.entrySet())
                  .stream()
                  .parallel()
                  .filter(s -> s.size() < k && !s.isEmpty())
                  .map(entries -> join(scheme, entries))
                  .noneMatch(s -> Arrays.equals(s, secret));
            });
  }

  private byte[] join(Scheme scheme, Set<Map.Entry<Integer, byte[]>> entries) {
    final Map<Integer, byte[]> m = new HashMap<>();
    entries.forEach(v -> m.put(v.getKey(), v.getValue()));
    return scheme.join(m);
  }
}
