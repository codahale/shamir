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

import com.google.auto.value.AutoValue;
import javax.annotation.CheckReturnValue;

/**
 * A part of a split secret.
 */
@AutoValue
public abstract class Part {

  /**
   * Creates a new {@link Part} with the given ID and value.
   *
   * @param id the part's unique ID
   * @param value the part's value
   * @return a new {@link Part} instance
   */
  @CheckReturnValue
  public static Part of(int id, byte[] value) {
    return new AutoValue_Part((byte) id, value);
  }

  /**
   * Returns the part's ID.
   *
   * @return the part's ID
   */
  @CheckReturnValue
  public abstract byte id();

  /**
   * Returns the part's value.
   *
   * @return the part's value
   */
  @CheckReturnValue
  @SuppressWarnings("mutable")
  public abstract byte[] value();

  @CheckReturnValue
  int valueLength() {
    return value().length;
  }
}
