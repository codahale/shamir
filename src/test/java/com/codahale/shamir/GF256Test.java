package com.codahale.shamir;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class GF256Test {
    @Test
    public void add() throws Exception {
        assertEquals((byte) 122, GF256.add((byte) 100, (byte) 30));
    }

    @Test
    public void degree() throws Exception {
        assertEquals(1, GF256.degree(new byte[] {1, 2}));
        assertEquals(1, GF256.degree(new byte[] {1, 2, 0}));
        assertEquals(2, GF256.degree(new byte[] {1, 2, 3}));
        assertEquals(0, GF256.degree(new byte[4]));
    }

    @Test
    public void eval() throws Exception {
        assertEquals((byte) 17, GF256.eval(new byte[]{1, 0, 2, 3}, (byte) 2));
    }

    @Test
    public void mul() throws Exception {
        assertEquals((byte) 254, GF256.mul((byte) 90, (byte) 21));
        assertEquals((byte) 167, GF256.mul((byte) 133, (byte) 5));
        assertEquals((byte) 0, GF256.mul((byte) 0, (byte) 21));
    }

    @Test
    public void div() throws Exception {
        assertEquals((byte) 189, GF256.div((byte) 90, (byte) 21));
        assertEquals((byte) 151, GF256.div((byte) 6, (byte) 55));
        assertEquals((byte) 138, GF256.div((byte) 22, (byte) 192));
        assertEquals((byte) 0, GF256.div((byte) 0, (byte) 192));
    }

    @Test(expected = ArithmeticException.class)
    public void divByZero() throws Exception {
        GF256.div((byte) 100, (byte) 0);
    }

    @Test
    public void generate() throws Exception {
        final byte[] p = GF256.generate(5, (byte) 20);
        assertEquals(p[0], 20);
        assertTrue(p[p.length - 1] != 0);
    }

    @Test
    public void interpolate() throws Exception {
        assertEquals(0, GF256.interpolate(new byte[][]{
                new byte[]{1, 1},
                new byte[]{2, 2},
                new byte[]{3, 3},
        }));

        assertEquals(30, GF256.interpolate(new byte[][]{
                new byte[]{1, 80},
                new byte[]{2, 90},
                new byte[]{3, 20},
        }));

        assertEquals(107, GF256.interpolate(new byte[][]{
                new byte[]{1, 43},
                new byte[]{2, 22},
                new byte[]{3, 86},
        }));
    }
}