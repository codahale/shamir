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
package com.codahale.shamir;

import com.google.auto.value.AutoValue;
import javax.annotation.CheckReturnValue;
import javax.annotation.Nonnegative;
import java.util.Collections;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

@AutoValue
public abstract class ExpandedScheme {

  @CheckReturnValue
  public static ExpandedScheme of(@Nonnegative int n, @Nonnegative int m, @Nonnegative int k) {
    Scheme.checkArgument(k > 2, "K must be > 2");
    Scheme.checkArgument(m > 0, "M must be > 0");
    Scheme.checkArgument(m < k - 1, "M must be < K - 1");
    Scheme.checkArgument(n >= k, "N must be >= K");
    Scheme.checkArgument(n <= 255, "N must be <= 255");
    return new AutoValue_ExpandedScheme(n, m, k);
  }

  public abstract int n();

  public abstract int m();

  public abstract int k();

  private Map<Integer, byte[]> collectParts(Map<Integer, byte[]> mParts, Map<Integer, byte[]> kParts) {
    return Stream.concat(mParts.entrySet().stream(), kParts.entrySet().stream())
            .collect(Collectors.toMap(
                    Map.Entry::getKey,
                    Map.Entry::getValue,
                    (mPart, kPart) -> kPart
            ));
  }

  private boolean mPartsPresent(Map<Integer, byte[]> parts) {
    return parts.entrySet().stream()
            .map(Map.Entry::getKey).collect(Collectors.toSet())
            .containsAll(IntStream.rangeClosed(1, m()).boxed().collect(Collectors.toSet()));
  }

  @CheckReturnValue
  public Map<Integer, byte[]> split(byte[] secret) {
    final Scheme mScheme = Scheme.of(m() + 1, m() + 1);
    final Scheme kScheme = Scheme.of(n() - m(), k() - m());
    final Map<Integer, byte[]> mParts = mScheme.split(secret);
    final Map<Integer, byte[]> kParts = kScheme.split(mParts.get(m() + 1), m());
    return Collections.unmodifiableMap(collectParts(mParts, kParts));
  }

  @CheckReturnValue
  public byte[] join(Map<Integer, byte[]> parts) {
    Scheme.checkArgument(parts.size() > 0, "No parts provided");
    final int[] lengths = parts.values().stream().mapToInt(v -> v.length).distinct().toArray();
    Scheme.checkArgument(lengths.length == 1, "Varying lengths of part values");
    Scheme.checkArgument(mPartsPresent(parts), "Missing mandatory parts");
    return new byte[1];
  }

}