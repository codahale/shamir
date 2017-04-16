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

import com.codahale.shamir.SecretSharing;
import com.codahale.shamir.Share;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;
import java.util.Arrays;
import java.util.Collections;
import org.junit.Test;

public class SecretSharingTest {

  @Test
  public void thresholdTooLow() throws Exception {
    assertThatThrownBy(() -> SecretSharing.split(1, 1, new byte[10]))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage("K must be > 1");
  }

  @Test
  public void thresholdTooHigh() throws Exception {
    assertThatThrownBy(() -> SecretSharing.split(1, 2, new byte[10]))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage("N must be >= K");
  }

  @Test
  public void emptyShares() throws Exception {
    assertThatThrownBy(() -> SecretSharing.combine(Collections.emptySet()))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage("No shares provided");
  }

  @Test
  public void irregularShares() throws Exception {
    final Share one = new Share(1, new byte[1]);
    final Share two = new Share(2, new byte[2]);

    assertThatThrownBy(() -> SecretSharing.combine(ImmutableSet.of(one, two)))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage("Varying lengths of share values");
  }

  @Test
  public void singleByteSecret() throws Exception {
    final byte[] secret = new byte[]{-41};
    assertThat(SecretSharing.combine(SecretSharing.split(8, 3, secret)))
        .isEqualTo(secret);
  }

  @Test
  public void moreThanByteMaxValueShares() throws Exception {
    final byte[] secret = new byte[]{-41};
    assertThat(SecretSharing.combine(SecretSharing.split(200, 3, secret)))
        .isEqualTo(secret);
  }

  @Test
  public void tooManyShares() throws Exception {
    final byte[] secret = new byte[]{-41};
    assertThatThrownBy(() -> SecretSharing.split(2_000, 3, secret))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage("N must be <= 255");
  }

  @Test
  public void roundTrip() throws Exception {
    // All distinct subsets of shares of cardinality greater than or equal to the threshold should
    // combine to recover the original secret.
    qt().forAll(integers().between(2, 5), integers().between(2, 5), byteArrays())
        .asWithPrecursor((top, k, secret) -> SecretSharing.split(top + k, k, secret))
        .check((top, k, secret, shares) -> Sets.powerSet(shares).stream()
                                               .filter(s -> s.size() >= k)
                                               .map(SecretSharing::combine)
                                               .allMatch(s -> Arrays.equals(s, secret)));
  }

  @Test
  public void minorityOpinions() throws Exception {
    // All distinct subsets of shares of cardinality less than the threshold should never combine to
    // recover the original secret.
    qt().forAll(integers().between(2, 5), integers().between(2, 5), byteArrays())
        .asWithPrecursor((top, k, secret) -> SecretSharing.split(top + k, k, secret))
        .check((top, k, secret, shares) -> Sets.powerSet(shares).stream()
                                               .filter(s -> s.size() < k && !s.isEmpty())
                                               .map(SecretSharing::combine)
                                               .noneMatch(s -> Arrays.equals(s, secret)));
  }
}
