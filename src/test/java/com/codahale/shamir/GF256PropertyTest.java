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

import com.pholser.junit.quickcheck.Property;
import com.pholser.junit.quickcheck.When;
import com.pholser.junit.quickcheck.runner.JUnitQuickcheck;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertEquals;

@RunWith(JUnitQuickcheck.class)
public class GF256PropertyTest {
    @Property
    public void mulIsCommutative(byte x, byte y) {
        assertEquals(GF256.mul(x, y), GF256.mul(y, x));
    }

    @Property
    public void addIsCommutative(byte x, byte y) {
        assertEquals(GF256.add(x, y), GF256.add(y, x));
    }

    @Property
    public void addIsTheInverseOfAdd(byte x, byte y) {
        assertEquals(x, GF256.add(GF256.add(x, y), y));
    }

    @Property
    public void divIsTheInverseOfMul(byte x, @When(satisfies = "#_ != 0") byte y) {
        assertEquals(x, GF256.div(GF256.mul(x, y), y));
    }

    @Property
    public void mulIsTheInverseOfDiv(byte x, @When(satisfies = "#_ != 0") byte y) {
        assertEquals(x, GF256.mul(GF256.div(x, y), y));
    }
}
