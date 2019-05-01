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

import static com.codahale.shamir.GF256.EXP;
import static com.codahale.shamir.Generators.bytes;
import static java.lang.Byte.toUnsignedInt;
import static org.assertj.core.api.Assertions.assertThat;

import java.security.SecureRandom;
import org.junit.jupiter.api.Test;
import org.quicktheories.WithQuickTheories;

class GF256Test implements WithQuickTheories {

  @Test
  void add() {
    assertThat(GF256.add((byte) 100, (byte) 30)).isEqualTo((byte) 122);
  }

  @Test
  void sub() {
    assertThat(GF256.sub((byte) 100, (byte) 30)).isEqualTo((byte) 122);
  }

  @Test
  void mul() {
    assertThat(GF256.mul((byte) 90, (byte) 21)).isEqualTo((byte) 254);
    assertThat(GF256.mul((byte) 133, (byte) 5)).isEqualTo((byte) 167);
    assertThat(GF256.mul((byte) 0, (byte) 21)).isEqualTo((byte) 0);
    assertThat(GF256.mul((byte) 0xb6, (byte) 0x53)).isEqualTo((byte) 0x36);
  }

  @Test
  void div() {
    assertThat(GF256.div((byte) 90, (byte) 21)).isEqualTo((byte) 189);
    assertThat(GF256.div((byte) 6, (byte) 55)).isEqualTo((byte) 151);
    assertThat(GF256.div((byte) 22, (byte) 192)).isEqualTo((byte) 138);
    assertThat(GF256.div((byte) 0, (byte) 192)).isEqualTo((byte) 0);
  }

  @Test
  void mulIsCommutative() {
    qt().forAll(bytes(), bytes()).check((x, y) -> GF256.mul(x, y) == GF256.mul(y, x));
  }

  @Test
  void addIsCommutative() {
    qt().forAll(bytes(), bytes()).check((x, y) -> GF256.add(x, y) == GF256.add(y, x));
  }

  @Test
  void subIsTheInverseOfAdd() {
    qt().forAll(bytes(), bytes()).check((x, y) -> GF256.sub(GF256.add(x, y), y) == x);
  }

  @Test
  void divIsTheInverseOfMul() {
    qt().forAll(bytes(), bytes(1, 255)).check((x, y) -> GF256.div(GF256.mul(x, y), y) == x);
  }

  @Test
  void mulIsTheInverseOfDiv() {
    qt().forAll(bytes(), bytes(1, 255)).check((x, y) -> GF256.mul(GF256.div(x, y), y) == x);
  }

  @Test
  void degree() {
    assertThat(GF256.degree(new byte[] {1, 2})).isEqualTo(1);
    assertThat(GF256.degree(new byte[] {1, 2, 0})).isEqualTo(1);
    assertThat(GF256.degree(new byte[] {1, 2, 3})).isEqualTo(2);
    assertThat(GF256.degree(new byte[4])).isEqualTo(0);
  }

  @Test
  void eval() {
    assertThat(GF256.eval(new byte[] {1, 0, 2, 3}, (byte) 2)).isEqualTo((byte) 17);
  }

  @Test
  void generate() {
    final SecureRandom random = new SecureRandom();
    final byte[] p = GF256.generate(random, 5, (byte) 20);
    assertThat(p[0]).isEqualTo((byte) 20);
    assertThat(p.length).isEqualTo(6);
    assertThat(p[p.length - 1]).isNotZero();
  }

  @Test
  void interpolate() {
    assertThat(GF256.interpolate(new byte[][] {{1, 1}, {2, 2}, {3, 3}})).isEqualTo((byte) 0);
    assertThat(GF256.interpolate(new byte[][] {{1, 80}, {2, 90}, {3, 20}})).isEqualTo((byte) 30);
    assertThat(GF256.interpolate(new byte[][] {{1, 43}, {2, 22}, {3, 86}})).isEqualTo((byte) 107);
  }

  private byte ffMul(byte a, byte b) {
    byte aa = a, bb = b, r = 0, t;
    while (aa != 0) {
      if ((aa & 1) != 0) {
        r = (byte) (r ^ bb);
      }
      t = (byte) (bb & 0x80);
      bb = (byte) (bb << 1);
      if (t != 0) {
        bb = (byte) (bb ^ 0x1b);
      }
      aa = (byte) ((aa & 0xff) >> 1);
    }
    return r;
  }

  @Test
  void expTable() {
    byte r = 1;
    final byte G = 3;
    for (int idx = 1; idx < GF256.EXP.length; idx++) {
      r = ffMul(r, G);
      assertThat(GF256.EXP[idx]).isEqualTo(r);
    }
  }

  @Test
  void logTable() {
    for (int idx = 1; idx < GF256.LOG.length; idx++) {
      assertThat(toUnsignedInt(EXP[toUnsignedInt(GF256.LOG[idx])])).isEqualTo(idx);
    }
  }

  @Test
  void polyMulScalar() {
    final SecureRandom random = new SecureRandom();
    final byte[] p = GF256.generate(random, 10);
    final byte x = (byte) random.nextInt();
    final byte px = GF256.eval(p, x);
    final byte a = (byte) random.nextInt();
    final byte[] ap = GF256.mul(a, p);
    final byte apx = GF256.eval(ap, x);
    assertThat(apx).isEqualTo(GF256.mul(px, a));
  }

  @Test
  void polyDivScalar() {
    final SecureRandom random = new SecureRandom();
    final byte[] p = GF256.generate(random, 10);
    final byte x = (byte) random.nextInt();
    final byte px = GF256.eval(p, x);
    final byte a = (byte) random.nextInt();
    final byte[] ap = GF256.div(a, p);
    final byte apx = GF256.eval(ap, x);
    assertThat(apx).isEqualTo(GF256.div(px, a));
  }

  @Test
  void polyAdd() {
    final SecureRandom random = new SecureRandom();
    final byte[] p1 = GF256.generate(random, 10);
    final byte[] p2 = GF256.generate(random, 15);
    final byte[] p = GF256.add(p1, p2);
    assertThat(p.length).isEqualTo(p2.length);
    final byte x = (byte) random.nextInt();
    final byte p1x = GF256.eval(p1, x);
    final byte p2x = GF256.eval(p2, x);
    final byte px = GF256.eval(p, x);
    assertThat(px).isEqualTo(GF256.add(p1x, p2x));
  }

  @Test
  void polyMul() {
    final SecureRandom random = new SecureRandom();
    final byte[] p1 = GF256.generate(random, 10);
    final byte[] p2 = GF256.generate(random, 15);
    final byte[] p = GF256.mul(p1, p2);
    assertThat(p.length).isEqualTo(p2.length + p1.length - 1);
    final byte x = (byte) random.nextInt();
    final byte p1x = GF256.eval(p1, x);
    final byte p2x = GF256.eval(p2, x);
    final byte px = GF256.eval(p, x);
    assertThat(px).isEqualTo(GF256.mul(p1x, p2x));
  }

  @Test
  void testInterpolation() {
    final byte[][] points = new byte[10][2];
    final SecureRandom random = new SecureRandom();
    for (int idx = 0; idx < points.length; idx++) {
      points[idx][0] = (byte) idx;
      points[idx][1] = (byte) random.nextInt();
    }
    final byte[] p = GF256.generate(random, points.length - 1, points);
    for (int idx = 0; idx < points.length; idx++) {
      byte x = (byte) idx;
      byte px = GF256.eval(p, x);
      assertThat(px).isEqualTo(points[idx][1]);
    }
  }
}
