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
import static org.junit.Assert.assertArrayEquals;
import static org.quicktheories.quicktheories.QuickTheory.qt;
import static org.quicktheories.quicktheories.generators.SourceDSL.integers;

import com.codahale.shamir.SecretSharing;
import com.codahale.shamir.Share;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;
import java.util.Collections;
import java.util.Set;
import org.junit.Test;

public class SecretSharingTest {

  @Test(expected = IllegalArgumentException.class)
  public void thresholdTooLow() throws Exception {
    SecretSharing.split(1, 1, new byte[10]);
  }

  @Test(expected = IllegalArgumentException.class)
  public void thresholdTooHigh() throws Exception {
    SecretSharing.split(1, 2, new byte[10]);
  }

  @Test(expected = IllegalArgumentException.class)
  public void emptyShares() throws Exception {
    SecretSharing.combine(Collections.emptySet());
  }

  @Test(expected = IllegalArgumentException.class)
  public void irregularShares() throws Exception {
    SecretSharing.combine(ImmutableSet.of(
        new Share(1, new byte[1]),
        new Share(2, new byte[2])
    ));
  }

  @Test
  public void singleByteSecret() throws Exception {
    final byte[] secret = new byte[]{-41};
    final Set<Share> shares = SecretSharing.split(8, 3, secret);
    final byte[] recovered = SecretSharing.combine(shares);
    assertArrayEquals(secret, recovered);
  }

  @Test
  public void roundTrip() throws Exception {
    qt().withExamples(100)
        .forAll(integers().between(2, 5), integers().between(2, 5), byteArrays())
        .checkAssert((top, k, secret) -> {
          // split the secret
          final Set<Share> shares = SecretSharing.split(top + k, k, secret);

          // All distinct subsets of shares which are at or over the threshold
          // should combine to recover the original secret.
          for (Set<Share> subset : Sets.powerSet(shares)) {
            if (subset.size() >= k) {
              final byte[] recovered = SecretSharing.combine(subset);
              assertArrayEquals(secret, recovered);
            }
          }
        });
  }
}
