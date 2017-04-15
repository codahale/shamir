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

import java.util.Arrays;
import java.util.Objects;
import java.util.StringJoiner;

/**
 * A share of a split secret.
 */
public class Share {

  final int id;
  final byte[] value;

  /**
   * Create a new {@link Share}.
   *
   * @param id the share's unique identifier
   * @param value the share's value
   */
  public Share(int id, byte[] value) {
    this.id = id;
    this.value = Arrays.copyOf(value, value.length);
  }

  /**
   * Returns the share's unique identifier.
   *
   * @return the share's unique identifier
   */
  public int getId() {
    return id;
  }

  /**
   * Returns the share's value.
   *
   * @return the share's value
   */
  public byte[] getValue() {
    return Arrays.copyOf(value, value.length);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    final Share that = (Share) o;
    return this.id == that.id && Arrays.equals(this.value, that.value);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, Arrays.hashCode(value));
  }

  @Override
  public String toString() {
    return new StringJoiner(", ", this.getClass().getSimpleName() + "[", "]")
        .add("id = " + id)
        .add("value = " + Arrays.toString(value))
        .toString();
  }
}
