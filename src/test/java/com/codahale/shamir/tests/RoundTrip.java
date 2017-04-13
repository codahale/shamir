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

import com.codahale.shamir.SecretSharing;
import com.codahale.shamir.Share;
import com.google.common.collect.Sets;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.security.SecureRandom;
import java.util.Arrays;
import java.util.Collection;
import java.util.Set;

import static org.junit.Assert.assertArrayEquals;

@RunWith(Parameterized.class)
public class RoundTrip {
    @Parameterized.Parameters
    public static Collection<Object[]> params() {
        return Arrays.asList(new Object[][]{
                {5, 3}, {7, 2}, {6, 4}, {10, 3}, {20, 19}
        });
    }

    private final int n;
    private final int k;

    public RoundTrip(int n, int k) {
        this.n = n;
        this.k = k;
    }

    @Test
    public void check() throws Exception {
        // generate a random secret
        final SecureRandom random = new SecureRandom();
        final byte[] secret = new byte[1000];
        random.nextBytes(secret);

        // split the secret
        final Set<Share> shares = SecretSharing.split(n, k, secret);

        // All distinct subsets of shares which are at or over the threshold
        // should combine to recover the original secret.
        for (Set<Share> i : Sets.powerSet(shares)) {
            if (i.size() >= k) {
                final byte[] recovered = SecretSharing.combine(i);
                assertArrayEquals(secret, recovered);
            }
        }
    }
}
