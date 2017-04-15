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

import java.security.SecureRandom;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * Two static methods which use Shamir's Secret Sharing over {@code GF(256)} to
 * securely split secrets into {@code N} shares, of which {@code K} can be
 * combined to recover the original secret.
 *
 * @see <a href="https://en.wikipedia.org/wiki/Shamir%27s_Secret_Sharing">Shamir's Secret
 * Sharing</a>
 */
public final class SecretSharing {

  private SecretSharing() {
    throw new AssertionError("No SecretSharing instances for you!");
  }

  /**
   * Splits the given secret into {@code n} shares, of which any {@code k} or
   * more can be combined to recover the original secret.
   *
   * @param n the number of shares to produce (must be {@code >1})
   * @param k the threshold of combinable shares (must be {@code <= n})
   * @param secret the secret to split
   * @return a set of {@code n} {@link Share} instances
   */
  public static Set<Share> split(int n, int k, byte[] secret) {
    Objects.requireNonNull(secret, "secret must not be null");
    if (k <= 1) {
      throw new IllegalArgumentException("K must be > 1");
    }
    if (n < k) {
      throw new IllegalArgumentException("N must be >= K");
    }

    // generate shares
    final SecureRandom random = new SecureRandom();
    final byte[][] shares = new byte[n][secret.length];
    for (int i = 0; i < secret.length; i++) {
      // for each byte, generate a random polynomial, p
      final byte[] p = GF256.generate(random, k - 1, secret[i]);
      for (byte x = 1; x <= n; x++) {
        // each share's byte is p(shareId)
        shares[x - 1][i] = GF256.eval(p, x);
      }
    }

    // return as a set of objects
    return IntStream.range(0, n)
                    .mapToObj(i -> new Share(i + 1, shares[i]))
                    .collect(Collectors.toSet());
  }

  /**
   * Combines the given shares into the original secret.
   * <p>
   * <b>N.B.:</b> There is no way to determine whether or not the returned
   * value is actually the original secret. If the shares are incorrect, or
   * are under the threshold value used to split the secret, a random value
   * will be returned.
   *
   * @param shares a set of {@link Share} instances
   * @return the original secret
   * @throws IllegalArgumentException if {@code shares} is empty or contains values of varying
   * lengths
   */
  public static byte[] combine(Set<Share> shares) {
    final byte[] secret = new byte[secretLength(shares)];
    for (int i = 0; i < secret.length; i++) {
      final byte[][] points = new byte[shares.size()][2];
      int p = 0;
      for (Share share : shares) {
        points[p][0] = (byte) share.id;
        points[p][1] = share.value[i];
        p++;
      }
      secret[i] = GF256.interpolate(points);
    }
    return secret;
  }

  private static int secretLength(Set<Share> shares) {
    Objects.requireNonNull(shares, "Shares must not be null");
    final int[] l = shares.stream().mapToInt(s -> s.value.length).distinct().toArray();
    if (l.length == 0) {
      throw new IllegalArgumentException("No shares provided");
    }
    if (l.length != 1) {
      throw new IllegalArgumentException("Varying lengths of share values");
    }
    return l[0];
  }
}
