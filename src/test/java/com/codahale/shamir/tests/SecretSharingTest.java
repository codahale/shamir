package com.codahale.shamir.tests;

import com.codahale.shamir.SecretSharing;
import com.codahale.shamir.Share;
import com.google.common.collect.Sets;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import java.security.SecureRandom;
import java.util.Arrays;
import java.util.Collection;
import java.util.Set;

import static org.junit.Assert.assertArrayEquals;

@RunWith(Parameterized.class)
public class SecretSharingTest {
    @Parameters
    public static Collection<Object[]> params() {
        return Arrays.asList(new Object[][]{
                {5, 3}, {7, 2}, {6, 4}, {10, 3}, {20, 19}
        });
    }

    private final int n;
    private final int k;

    public SecretSharingTest(int n, int k) {
        this.n = n;
        this.k = k;
    }

    @Test
    public void roundTrip() throws Exception {
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
