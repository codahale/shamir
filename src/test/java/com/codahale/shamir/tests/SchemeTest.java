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

import static com.codahale.shamir.Generators.byteStrings;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.quicktheories.quicktheories.QuickTheory.qt;
import static org.quicktheories.quicktheories.generators.SourceDSL.integers;

import com.codahale.shamir.Part;
import com.codahale.shamir.Scheme;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;
import java.util.Collections;
import okio.ByteString;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

class SchemeTest {

  @Test
  void hasProperties() throws Exception {
    final Scheme scheme = Scheme.of(5, 3);

    assertThat(scheme.n())
        .isEqualTo(5);
    assertThat(scheme.k())
        .isEqualTo(3);
  }

  @Test
  void tooManyShares() throws Exception {
    assertThatThrownBy(() -> Scheme.of(2_000, 3))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage("N must be <= 255");
  }

  @Test
  void thresholdTooLow() throws Exception {
    assertThatThrownBy(() -> Scheme.of(1, 1))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage("K must be > 1");
  }

  @Test
  void thresholdTooHigh() throws Exception {
    assertThatThrownBy(() -> Scheme.of(1, 2))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage("N must be >= K");
  }

  @Test
  void joinEmptyParts() throws Exception {
    assertThatThrownBy(() -> Scheme.of(3, 2).join(Collections.emptySet()))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage("No parts provided");
  }

  @Test
  void joinIrregularParts() throws Exception {
    final Part one = Part.of(1, ByteString.of((byte) 1));
    final Part two = Part.of(2, ByteString.of((byte) 1, (byte) 2));

    assertThatThrownBy(() -> Scheme.of(3, 2).join(ImmutableSet.of(one, two)))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage("Varying lengths of part values");
  }

  @Test
  void splitAndJoinSingleByteSecret() throws Exception {
    final Scheme scheme = Scheme.of(8, 3);
    final ByteString secret = ByteString.encodeUtf8("x");
    Assertions.assertThat(scheme.join(scheme.split(secret)))
              .isEqualTo(secret);
  }

  @Test
  void splitAndJoinMoreThanByteMaxValueParts() throws Exception {
    final Scheme scheme = Scheme.of(200, 3);
    final ByteString secret = ByteString.encodeUtf8("x");
    assertThat(scheme.join(scheme.split(secret)))
        .isEqualTo(secret);
  }

  @Test
  void splitAndJoinQuorate() throws Exception {
    // All distinct subsets of parts of cardinality greater than or equal to the threshold should
    // join to recover the original secret.
    qt().forAll(integers().between(2, 5), integers().between(2, 5), byteStrings(1, 300))
        .asWithPrecursor((k, extra, secret) -> Scheme.of(k + extra, k))
        .check((k, e, secret, scheme) -> Sets.powerSet(scheme.split(secret)).stream().parallel()
                                             .filter(s -> s.size() >= k)
                                             .map(scheme::join)
                                             .allMatch(s -> s.equals(secret)));
  }

  @Test
  void splitAndJoinInquorate() throws Exception {
    // All distinct subsets of parts of cardinality less than the threshold should never join to
    // recover the original secret. Only check larger secrets to avoid false positives.
    qt().forAll(integers().between(2, 5), integers().between(2, 5), byteStrings(3, 300))
        .asWithPrecursor((k, extra, secret) -> Scheme.of(k + extra, k))
        .check((k, e, secret, scheme) -> Sets.powerSet(scheme.split(secret)).stream().parallel()
                                             .filter(s -> s.size() < k && !s.isEmpty())
                                             .map(scheme::join)
                                             .noneMatch(s -> s.equals(secret)));
  }
}