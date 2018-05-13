/*
 * Copyright © 2018 Balciu
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

import com.google.auto.value.AutoValue;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.annotation.CheckReturnValue;
import javax.annotation.Nonnegative;

/**
 * An implementation of double Shamir's Secret Sharing which allows splitting into two types of secret parts.
 * Mandatory parts which presence in joining process is obligatory to retrieve a secret and optional parts
 * which aren't necessary, but count towards threshold.
 */
@AutoValue
public abstract class ExpandedScheme {

  /**
   * Creates a new {@link ExpandedScheme} instance.
   *
   * @param n the number of parts to produce (must be {@code > 2})
   * @param m the number of mandatory parts (must be {@code < k-1})
   * @param k the threshold of joinable parts (must be {@code <= n && > 2})
   * @return an {@code N}/{@code M}/{@code K} {@link ExpandedScheme}
   */
  @CheckReturnValue
  public static ExpandedScheme of(@Nonnegative int n, @Nonnegative int m, @Nonnegative int k) {
    Scheme.checkArgument(k > 2, "K must be > 2");
    Scheme.checkArgument(m > 0, "M must be > 0");
    Scheme.checkArgument(m < k - 1, "M must be < K - 1");
    Scheme.checkArgument(n >= k, "N must be >= K");
    Scheme.checkArgument(n <= 255, "N must be <= 255");
    return new AutoValue_ExpandedScheme(n, m, k);
  }

  /**
   * The number of parts the scheme will generate when splitting a secret.
   *
   * @return {@code N}
   */
  public abstract int n();

  /**
   * The number of mandatory parts the scheme will require to re-create a secret.
   *
   * @return {@code M}
   */
  public abstract int m();

  /**
   * Overall number of parts the scheme will require to re-create a secret (together with {@code M}).
   *
   * @return {@code K}
   */
  public abstract int k();

  private Map<Integer, byte[]> collectAllParts(
      Map<Integer, byte[]> mParts, Map<Integer, byte[]> kParts) {
    return Stream.concat(mParts.entrySet().stream(), kParts.entrySet().stream())
        .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (mPart, kPart) -> kPart));
  }

  private Map<Integer, byte[]> collectMandatoryParts(Map<Integer, byte[]> mParts, byte[] lastPart) {
    Map<Integer, byte[]> allParts = new HashMap<>(mParts);
    allParts.put(m() + 1, lastPart);
    return Collections.unmodifiableMap(allParts);
  }

  /**
   * Splits the given secret into {@code N} parts, of which {@code k} or more containing
   * {@code M} mandatory parts can be combined to recover the original secret.
   * Parts with ID from 1 to {@code M} are mandatory parts.
   * Parts with ID {@code > M} are optional parts.
   *
   * @param secret the secret to split
   * @return a map of {@code n} part IDs and their values
   * @throws NullPointerException if {@code parts} initial split fails to produce
   *    {@code M + 1} parts
   */
  @CheckReturnValue
  public Map<Integer, byte[]> split(byte[] secret) {
    // create scheme for initial split into {@code M + 1} parts from which
    // all of them are needed to recover the secret
    final Scheme mScheme = Scheme.of(m() + 1, m() + 1);
    // create scheme for second split of one of the initial parts into
    // {@code N - M} optional parts from which {@code K - M} are needed to recover
    // original initial part
    final Scheme kScheme = Scheme.of(n() - m(), k() - m());
    // split the secret into initial parts
    final Map<Integer, byte[]> mParts = mScheme.split(secret);
    // extract last part from the initial split
    final Optional<byte[]> kSecret = Optional.of(mParts.get(m() + 1));
    if (kSecret.isPresent()) {
      // split last part of the initial split into {@code N - M} optional parts
      final Map<Integer, byte[]> kParts = kScheme.split(kSecret.get(), m());
      return Collections.unmodifiableMap(collectAllParts(mParts, kParts));
    }
    throw new NullPointerException("Error calculating mParts split");
  }

  /**
   * Joins the given parts to recover the original secret.
   *
   * <p><b>N.B.:</b> There is no way to determine whether or not the returned value is actually the
   * original secret. If the parts are incorrect, or are under the threshold value used to split the
   * secret, a random value will be returned.
   *
   * @param parts a map of part IDs to part values
   * @return the original secret
   * @throws IllegalArgumentException if {@code parts} is empty or contains values of varying
   *     lengths
   * @throws IllegalStateException if splitting to {@code mParts} and {@code kParts} fails
   */
  @CheckReturnValue
  public byte[] join(Map<Integer, byte[]> parts) {
    Scheme.checkArgument(parts.size() > 0, "No parts provided");
    final int[] lengths = parts.values().stream().mapToInt(v -> v.length).distinct().toArray();
    Scheme.checkArgument(lengths.length == 1, "Varying lengths of part values");
    final Scheme mScheme = Scheme.of(m() + 1, m() + 1);
    final Scheme kScheme = Scheme.of(n() - m(), k() - m());
    // extract parts from the second split
    final Map<Integer, byte[]> kParts =
        parts
            .entrySet()
            .stream()
            .filter(entry -> entry.getKey() > m())
            .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    // extract parts from the initial split
    final Map<Integer, byte[]> mParts =
        parts
            .entrySet()
            .stream()
            .filter(entry -> entry.getKey() <= m())
            .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    // recover all initial split parts
    final Map<Integer, byte[]> allParts = (kParts.size() == 0) ? (mParts.size() == 0 ? new HashMap<>() : mParts) : (mParts.size() == 0 ? kParts : collectMandatoryParts(mParts, kScheme.join(kParts)));
    if (allParts.isEmpty()) {
      throw new IllegalStateException("No parts found before join");
    }
    // join to recover the original secret
    return mScheme.join(allParts);
  }
}
