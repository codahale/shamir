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

import com.codahale.shamir.ExpandedScheme;
import com.codahale.shamir.Scheme;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Sets;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.junit.jupiter.api.Test;
import org.quicktheories.WithQuickTheories;

class ExpandedSchemeTest implements WithQuickTheories {

    @Test
    void hasProperties() {
        final ExpandedScheme scheme = ExpandedScheme.of(10, 2, 5);

        assertThat(scheme.n()).isEqualTo(10);
        assertThat(scheme.m()).isEqualTo(2);
        assertThat(scheme.k()).isEqualTo(5);
    }

    @Test
    @SuppressWarnings("ResultOfMethodCallIgnored")
    void tooManyShares() {
        assertThatThrownBy(() -> ExpandedScheme.of(2_000, 3, 10)).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @SuppressWarnings("ResultOfMethodCallIgnored")
    void thresholdTooLow() {
        assertThatThrownBy(() -> ExpandedScheme.of(1, 1, 1)).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @SuppressWarnings("ResultOfMethodCallIgnored")
    void thresholdTooHigh() {
        assertThatThrownBy(() -> ExpandedScheme.of(2, 1, 4)).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @SuppressWarnings("ResultOfMethodCallIgnored")
    void tooManyMandatoryShares() {
        assertThatThrownBy(() -> ExpandedScheme.of(5, 2, 3)).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @SuppressWarnings("ResultOfMethodCallIgnored")
    void joinEmptyParts() {
        assertThatThrownBy(() -> ExpandedScheme.of(4, 1, 3).join(Collections.emptyMap()))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @SuppressWarnings("ResultOfMethodCallIgnored")
    void joinIrregularParts() {
        final byte[] one = new byte[] {1};
        final byte[] two = new byte[] {1, 2};

        assertThatThrownBy(() -> ExpandedScheme.of(4, 1, 3).join(ImmutableMap.of(1, one, 2, two)))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @SuppressWarnings("ResultOfMethodCallIgnored")
    void joinNotAllMandatoryParts() {
        final Map<Integer, byte[]> parts = ImmutableMap.of(1, "x".getBytes(StandardCharsets.UTF_8),
                3, "y".getBytes(StandardCharsets.UTF_8),
                4, "z".getBytes(StandardCharsets.UTF_8),
                5, "u".getBytes(StandardCharsets.UTF_8));
        assertThatThrownBy(() -> ExpandedScheme.of(5, 2, 4).join(parts))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void splitAndJoinSingleByteSecret() {
        final ExpandedScheme scheme = ExpandedScheme.of(8, 2, 5);
        final byte[] secret = "x".getBytes(StandardCharsets.UTF_8);

        assertThat(scheme.join(scheme.split(secret))).containsExactly(secret);
    }

    @Test
    void splitAndJoinMoreThanByteMaxValueParts() {
        final ExpandedScheme scheme = ExpandedScheme.of(200, 1, 3);
        final byte[] secret = "x".getBytes(StandardCharsets.UTF_8);

        assertThat(scheme.join(scheme.split(secret))).containsExactly(secret);
    }

//    @Test
//    void splitAndJoinQuorate() {
//        // All distinct subsets of parts of cardinality greater than or equal to the threshold
//        // and mandatory parts cardinality between one and threshold minus two
//        // should join to recover the original secret.
//        qt().forAll(integers().between(1, 8), integers().between(3, 10), integers().between(0, 5), byteArrays(1, 300))
//                .asWithPrecursor((m, k, extra, secret) -> {
//                    if (m < k - 1) {
//                        return ExpandedScheme.of(k + extra, m, k);
//                    }
//                    return ExpandedScheme.of(20, 1, 3);
//                })
//                .check(
//                        (m, k, e, secret, scheme) -> {
//                            if (scheme.n() == 20) {
//                                return true;
//                            }
//                            final Map<Integer, byte[]> parts = scheme.split(secret);
//                            return Sets.powerSet(parts.entrySet())
//                                    .stream()
//                                    .parallel()
//                                    .filter(s -> s.size() >= k && mPartsPresent(s, scheme.m()))
//                                    .map(entries -> join(scheme, entries))
//                                    .allMatch(s -> Arrays.equals(s, secret));
//                        });
//    }

    @Test
    void splitAndJoinInquorate() {
        // All distinct subsets of parts of cardinality less than the threshold should never join to
        // recover the original secret. Only check larger secrets to avoid false positives.
        qt().forAll(integers().between(1, 8), integers().between(3, 10), integers().between(0, 5), byteArrays(3, 300))
                .asWithPrecursor((m, k, extra, secret) -> {
                    if (m < k - 1) {
                        return ExpandedScheme.of(k + extra, m, k);
                    }
                    return ExpandedScheme.of(20, 1, 3);
                })
                .check(
                        (m, k, e, secret, scheme) -> {
                            if (scheme.n() == 20) {
                                return true;
                            }
                            final Map<Integer, byte[]> parts = scheme.split(secret);
                            return Sets.powerSet(parts.entrySet())
                                    .stream()
                                    .parallel()
                                    .filter(s -> s.size() < k && !s.isEmpty() && mPartsPresent(s, scheme.m()))
                                    .map(entries -> join(scheme, entries))
                                    .noneMatch(s -> Arrays.equals(s, secret));
                        });
    }

    private boolean mPartsPresent(Set<Map.Entry<Integer, byte[]>> parts, int m) {
        return parts
                .stream()
                .map(Map.Entry::getKey)
                .collect(Collectors.toSet())
                .containsAll(IntStream.rangeClosed(1, m).boxed().collect(Collectors.toSet()));
    }

    private byte[] join(ExpandedScheme scheme, Set<Map.Entry<Integer, byte[]>> entries) {
        final Map<Integer, byte[]> m = new HashMap<>();
        entries.forEach(v -> m.put(v.getKey(), v.getValue()));
        return scheme.join(m);
    }
}
