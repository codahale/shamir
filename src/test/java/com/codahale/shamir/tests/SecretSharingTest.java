package com.codahale.shamir.tests;

import com.codahale.shamir.SecretSharing;
import com.codahale.shamir.Share;
import com.google.common.collect.ImmutableSet;
import org.junit.Test;

import java.util.Collections;

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
}
