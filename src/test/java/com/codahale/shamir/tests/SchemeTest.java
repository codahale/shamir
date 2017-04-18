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

import static com.codahale.shamir.Generators.byteArrays;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.quicktheories.quicktheories.QuickTheory.qt;
import static org.quicktheories.quicktheories.generators.SourceDSL.integers;

import com.codahale.shamir.Scheme;
import com.codahale.shamir.Share;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;
import java.util.Arrays;
import java.util.Collections;
import org.assertj.core.api.Assertions;
import org.junit.Test;

public class SchemeTest {

  @Test
  public void tooManyShares() throws Exception {
    assertThatThrownBy(() -> new Scheme(2_000, 3))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage("N must be <= 255");
  }

  @Test
  public void thresholdTooLow() throws Exception {
    assertThatThrownBy(() -> new Scheme(1, 1))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage("K must be > 1");
  }

  @Test
  public void thresholdTooHigh() throws Exception {
    assertThatThrownBy(() -> new Scheme(1, 2))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage("N must be >= K");
  }

  @Test
  public void joinEmptyShares() throws Exception {
    assertThatThrownBy(() -> new Scheme(3, 2).join(Collections.emptySet()))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage("No shares provided");
  }

  @Test
  public void joinIrregularShares() throws Exception {
    final Share one = new Share(1, new byte[1]);
    final Share two = new Share(2, new byte[2]);

    assertThatThrownBy(() -> new Scheme(3, 2).join(ImmutableSet.of(one, two)))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage("Varying lengths of share values");
  }

  @Test
  public void splitAndJoinSingleByteSecret() throws Exception {
    final Scheme scheme = new Scheme(8, 3);
    final byte[] secret = new byte[]{-41};
    Assertions.assertThat(scheme.join(scheme.split(secret)))
              .isEqualTo(secret);
  }

  @Test
  public void splitAndJoinMoreThanByteMaxValueShares() throws Exception {
    final Scheme scheme = new Scheme(200, 3);
    final byte[] secret = new byte[]{-41};
    assertThat(scheme.join(scheme.split(secret)))
        .isEqualTo(secret);
  }

  @Test
  public void splitAndJoinQuorate() throws Exception {
    // All distinct subsets of shares of cardinality greater than or equal to the threshold should
    // join to recover the original secret.
    qt().forAll(integers().between(2, 5), integers().between(2, 5), byteArrays(1, 300))
        .asWithPrecursor((k, extra, secret) -> new Scheme(k + extra, k))
        .check((k, e, secret, scheme) -> Sets.powerSet(scheme.split(secret)).stream().parallel()
                                             .filter(s -> s.size() >= k)
                                             .map(scheme::join)
                                             .allMatch(s -> Arrays.equals(s, secret)));
  }

  @Test
  public void splitAndJoinInquorate() throws Exception {
    // All distinct subsets of shares of cardinality less than the threshold should never join to
    // recover the original secret. Only check larger secrets to avoid false positives.
    qt().forAll(integers().between(2, 5), integers().between(2, 5), byteArrays(3, 300))
        .asWithPrecursor((k, extra, secret) -> new Scheme(k + extra, k))
        .check((k, e, secret, scheme) -> Sets.powerSet(scheme.split(secret)).stream().parallel()
                                             .filter(s -> s.size() < k && !s.isEmpty())
                                             .map(scheme::join)
                                             .noneMatch(s -> Arrays.equals(s, secret)));
  }
}