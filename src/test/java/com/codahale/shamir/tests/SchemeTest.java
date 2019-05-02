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
package com.codahale.shamir.tests;

import static com.codahale.shamir.Generators.byteArrays;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.codahale.shamir.Scheme;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Sets;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import org.junit.jupiter.api.Test;
import org.quicktheories.WithQuickTheories;

class SchemeTest implements WithQuickTheories {

  @Test
  void hasProperties() {
    final Scheme scheme = new Scheme(new SecureRandom(), 5, 3);

    assertThat(scheme.n()).isEqualTo(5);
    assertThat(scheme.k()).isEqualTo(3);
  }

  @Test
  void tooManyShares() {
    assertThatThrownBy(() -> new Scheme(new SecureRandom(), 2_000, 3))
        .isInstanceOf(IllegalArgumentException.class);
  }

  @Test
  void thresholdTooLow() {
    assertThatThrownBy(() -> new Scheme(new SecureRandom(), 1, 1))
        .isInstanceOf(IllegalArgumentException.class);
  }

  @Test
  void thresholdTooHigh() {
    assertThatThrownBy(() -> new Scheme(new SecureRandom(), 1, 2))
        .isInstanceOf(IllegalArgumentException.class);
  }

  @Test
  void joinEmptyParts() {
    assertThatThrownBy(() -> new Scheme(new SecureRandom(), 3, 2).join(Collections.emptyMap()))
        .isInstanceOf(IllegalArgumentException.class);
  }

  @Test
  void joinIrregularParts() {
    final byte[] one = new byte[] {1};
    final byte[] two = new byte[] {1, 2};

    assertThatThrownBy(
            () -> new Scheme(new SecureRandom(), 3, 2).join(ImmutableMap.of(1, one, 2, two)))
        .isInstanceOf(IllegalArgumentException.class);
  }

  @Test
  void splitAndJoinSingleByteSecret() {
    final Scheme scheme = new Scheme(new SecureRandom(), 8, 3);
    final byte[] secret = "x".getBytes(StandardCharsets.UTF_8);

    assertThat(scheme.join(scheme.split(secret))).containsExactly(secret);
  }

  @Test
  void splitAndJoinMoreThanByteMaxValueParts() {
    final Scheme scheme = new Scheme(new SecureRandom(), 200, 3);
    final byte[] secret = "x".getBytes(StandardCharsets.UTF_8);

    assertThat(scheme.join(scheme.split(secret))).containsExactly(secret);
  }

  @Test
  void splitAndJoinQuorate() {
    // All distinct subsets of parts of cardinality greater than or equal to the threshold should
    // join to recover the original secret.
    qt().forAll(integers().between(2, 5), integers().between(1, 5), byteArrays(1, 300))
        .asWithPrecursor((k, extra, secret) -> new Scheme(new SecureRandom(), k + extra, k))
        .check(
            (k, e, secret, scheme) -> {
              final Map<Integer, byte[]> parts = scheme.split(secret);
              return Sets.powerSet(parts.entrySet()).stream()
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
    qt().forAll(integers().between(2, 5), integers().between(1, 5), byteArrays(3, 300))
        .asWithPrecursor((k, extra, secret) -> new Scheme(new SecureRandom(), k + extra, k))
        .check(
            (k, e, secret, scheme) -> {
              final Map<Integer, byte[]> parts = scheme.split(secret);
              return Sets.powerSet(parts.entrySet()).stream()
                  .parallel()
                  .filter(s -> s.size() < k && !s.isEmpty())
                  .map(entries -> join(scheme, entries))
                  .noneMatch(s -> Arrays.equals(s, secret));
            });
  }

  @Test
  void splitAndJoinSingleByteSecretPredefinedPoints() {
    final SecureRandom random = new SecureRandom();
    final Scheme scheme = new Scheme(random, 8, 3);
    final byte[] secret = new byte[32];
    random.nextBytes(secret);
    final Map<Integer, byte[]> points = new HashMap<>();
    final byte[] point1 = new byte[32];
    random.nextBytes(point1);
    points.put(1, point1);
    final byte[] point2 = new byte[32];
    random.nextBytes(point2);
    points.put(2, point2);

    Map<Integer, byte[]> shares = scheme.split(secret, points);
    assertThat(shares.size()).isEqualTo(8);
    assertThat(shares.get(1)).isEqualTo(point1);
    assertThat(shares.get(2)).isEqualTo(point2);

    Map<Integer, byte[]> joinShares = new HashMap<>();
    joinShares.put(1, shares.get(1));
    joinShares.put(2, shares.get(2));
    joinShares.put(5, shares.get(5));

    assertThat(scheme.join(joinShares)).containsExactly(secret);
  }

  @Test
  void tooManyPredefinedPoints() {
    final byte[] secret = "x".getBytes(StandardCharsets.UTF_8);
    final Map<Integer, byte[]> points = new HashMap<>();
    final byte[] point1 = new byte[] {0x12};
    points.put(1, point1);
    final byte[] point2 = new byte[] {0x34};
    points.put(2, point2);

    assertThatThrownBy(() -> new Scheme(new SecureRandom(), 2, 2).split(secret, points))
        .isInstanceOf(IllegalArgumentException.class);
  }

  private byte[] join(Scheme scheme, Set<Map.Entry<Integer, byte[]>> entries) {
    final Map<Integer, byte[]> m = new HashMap<>();
    entries.forEach(v -> m.put(v.getKey(), v.getValue()));
    return scheme.join(m);
  }
}
