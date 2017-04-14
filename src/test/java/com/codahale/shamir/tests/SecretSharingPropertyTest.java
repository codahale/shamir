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
import com.pholser.junit.quickcheck.Property;
import com.pholser.junit.quickcheck.When;
import com.pholser.junit.quickcheck.generator.InRange;
import com.pholser.junit.quickcheck.runner.JUnitQuickcheck;
import org.junit.runner.RunWith;

import java.util.Set;

import static org.junit.Assert.assertArrayEquals;

@RunWith(JUnitQuickcheck.class)
public class SecretSharingPropertyTest {
    @Property(trials = 500)
    public void roundTrip(@InRange(min = "2", max = "5") int top,
                          @InRange(min = "2", max = "5") int k,
                          @When(satisfies = "#_.length > 0 && #_.length < 1000") byte[] secret) throws Exception {
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
    }
}
