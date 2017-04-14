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

import org.quicktheories.quicktheories.core.Source;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.quicktheories.quicktheories.generators.SourceDSL.integers;
import static org.quicktheories.quicktheories.generators.SourceDSL.lists;

public interface Generators {
    static Source<Byte> bytes() {
        return integers().between(0, 255).as(Integer::byteValue, Byte::intValue);
    }

    static Source<Byte> nonZeroBytes() {
        return integers().between(1, 255).as(Integer::byteValue, Byte::intValue);
    }

    static Source<byte[]> byteArrays() {
        return lists().arrayListsOf(bytes())
                      .ofSizeBetween(1, 1000).as(
                        l -> {
                            final byte[] bytes = new byte[l.size()];
                            for (int i = 0; i < l.size(); i++) {
                                bytes[i] = l.get(i);
                            }
                            return bytes;
                        },
                        a -> {
                            final List<Byte> bytes = new ArrayList<>(a.length);
                            for (int i = 0; i < a.length; i++) {
                                bytes.set(i, a[i]);
                            }
                            return bytes;
                        })
                      .describedAs(Arrays::toString);
    }
}
