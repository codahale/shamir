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

import static com.codahale.shamir.Generators.bytes;
import static org.assertj.core.api.Assertions.assertThat;
import static org.quicktheories.quicktheories.QuickTheory.qt;

import java.security.SecureRandom;
import org.junit.Test;

public class GF256Test {

  @Test
  public void add() throws Exception {
    assertThat(GF256.add((byte) 100, (byte) 30))
        .isEqualTo((byte) 122);
  }

  @Test
  public void sub() throws Exception {
    assertThat(GF256.sub((byte) 100, (byte) 30))
        .isEqualTo((byte) 122);
  }

  @Test
  public void mul() throws Exception {
    assertThat(GF256.mul((byte) 90, (byte) 21))
        .isEqualTo((byte) 254);
    assertThat(GF256.mul((byte) 133, (byte) 5))
        .isEqualTo((byte) 167);
    assertThat(GF256.mul((byte) 0, (byte) 21))
        .isEqualTo((byte) 0);
    assertThat(GF256.mul((byte) 0xb6, (byte) 0x53))
        .isEqualTo((byte) 0x36);
  }

  @Test
  public void div() throws Exception {
    assertThat(GF256.div((byte) 90, (byte) 21))
        .isEqualTo((byte) 189);
    assertThat(GF256.div((byte) 6, (byte) 55))
        .isEqualTo((byte) 151);
    assertThat(GF256.div((byte) 22, (byte) 192))
        .isEqualTo((byte) 138);
    assertThat(GF256.div((byte) 0, (byte) 192))
        .isEqualTo((byte) 0);
  }

  @Test
  public void mulIsCommutative() {
    qt().forAll(bytes(), bytes())
        .check((x, y) -> GF256.mul(x, y) == GF256.mul(y, x));
  }

  @Test
  public void addIsCommutative() {
    qt().forAll(bytes(), bytes())
        .check((x, y) -> GF256.add(x, y) == GF256.add(y, x));
  }

  @Test
  public void subIsTheInverseOfAdd() {
    qt().forAll(bytes(), bytes())
        .check((x, y) -> GF256.sub(GF256.add(x, y), y) == x);
  }

  @Test
  public void divIsTheInverseOfMul() {
    qt().forAll(bytes(), bytes(1, 255))
        .check((x, y) -> GF256.div(GF256.mul(x, y), y) == x);
  }

  @Test
  public void mulIsTheInverseOfDiv() {
    qt().forAll(bytes(), bytes(1, 255))
        .check((x, y) -> GF256.mul(GF256.div(x, y), y) == x);
  }

  @Test
  public void degree() throws Exception {
    assertThat(GF256.degree(new byte[]{1, 2}))
        .isEqualTo(1);
    assertThat(GF256.degree(new byte[]{1, 2, 0}))
        .isEqualTo(1);
    assertThat(GF256.degree(new byte[]{1, 2, 3}))
        .isEqualTo(2);
    assertThat(GF256.degree(new byte[4]))
        .isEqualTo(0);
  }

  @Test
  public void eval() throws Exception {
    assertThat(GF256.eval(new byte[]{1, 0, 2, 3}, (byte) 2))
        .isEqualTo((byte) 17);
  }

  @Test
  public void generate() throws Exception {
    final SecureRandom random = new SecureRandom();
    final byte[] p = GF256.generate(random, 5, (byte) 20);
    assertThat(p[0])
        .isEqualTo((byte) 20);
    assertThat(p)
        .hasSize(6);
    assertThat(p[p.length - 1])
        .isNotZero();
  }

  @Test
  public void interpolate() throws Exception {
    assertThat(GF256.interpolate(new byte[][]{{1, 1}, {2, 2}, {3, 3}}))
        .isEqualTo((byte) 0);
    assertThat(GF256.interpolate(new byte[][]{{1, 80}, {2, 90}, {3, 20}}))
        .isEqualTo((byte) 30);
    assertThat(GF256.interpolate(new byte[][]{{1, 43}, {2, 22}, {3, 86}}))
        .isEqualTo((byte) 107);
  }
}
