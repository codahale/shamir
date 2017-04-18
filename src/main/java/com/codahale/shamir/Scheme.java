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
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * A class which uses Shamir's Secret Sharing over {@code GF(256)} to securely split secrets into
 * {@code N} shares, of which any {@code K} can be joined to recover the original secret.
 *
 * @see <a href="https://en.wikipedia.org/wiki/Shamir%27s_Secret_Sharing">Shamir's Secret
 * Sharing</a>
 * @see <a href="http://www.cs.utsa.edu/~wagner/laws/FFM.html">{@code GF(256)}</a>
 */
public class Scheme {

  private final int n, k;
  private final SecureRandom random;

  /**
   * Creates a new {@link Scheme} instance.
   *
   * @param n the number of shares to produce (must be {@code >1})
   * @param k the threshold of combinable shares (must be {@code <= n})
   */
  public Scheme(int n, int k) {
    checkArgument(k > 1, "K must be > 1");
    checkArgument(n >= k, "N must be >= K");
    checkArgument(n <= 255, "N must be <= 255");
    this.n = n;
    this.k = k;
    this.random = new SecureRandom();
  }

  private static void checkArgument(boolean condition, String message) {
    if (!condition) {
      throw new IllegalArgumentException(message);
    }
  }

  private static void checkNotNull(Object o, String message) {
    if (o == null) {
      throw new NullPointerException(message);
    }
  }

  /**
   * Splits the given secret into {@code n} shares, of which any {@code k} or more can be combined
   * to recover the original secret.
   *
   * @param secret the secret to split
   * @return a set of {@code n} {@link Share} instances
   */
  public Set<Share> split(byte[] secret) {
    checkNotNull(secret, "Secret must not be null");

    // generate share values
    final byte[][] values = new byte[n][secret.length];
    for (int i = 0; i < secret.length; i++) {
      // for each byte, generate a random polynomial, p
      final byte[] p = GF256.generate(random, k - 1, secret[i]);
      for (int x = 1; x <= n; x++) {
        // each share's byte is p(shareId)
        values[x - 1][i] = GF256.eval(p, (byte) x);
      }
    }

    // return as a set of objects
    final Set<Share> shares = new HashSet<>(n);
    for (int i = 0; i < values.length; i++) {
      shares.add(new Share(i + 1, values[i]));
    }
    return Collections.unmodifiableSet(shares);
  }

  /**
   * Joins the given shares to recover the original secret.
   * <p>
   * <b>N.B.:</b> There is no way to determine whether or not the returned value is actually the
   * original secret. If the shares are incorrect, or are under the threshold value used to split
   * the secret, a random value will be returned.
   *
   * @param shares a set of {@link Share} instances
   * @return the original secret
   * @throws IllegalArgumentException if {@code shares} is empty or contains values of varying
   * lengths
   */
  public byte[] join(Set<Share> shares) {
    checkNotNull(shares, "Shares must not be null");
    final int[] lengths = shares.stream().mapToInt(s -> s.value.length).distinct().toArray();
    checkArgument(lengths.length > 0, "No shares provided");
    checkArgument(lengths.length == 1, "Varying lengths of share values");
    final byte[] secret = new byte[lengths[0]];
    for (int i = 0; i < secret.length; i++) {
      final byte[][] points = new byte[shares.size()][2];
      int j = 0;
      for (Share share : shares) {
        points[j][0] = (byte) share.id;
        points[j][1] = share.value[i];
        j++;
      }
      secret[i] = GF256.interpolate(points);
    }
    return secret;
  }
}
