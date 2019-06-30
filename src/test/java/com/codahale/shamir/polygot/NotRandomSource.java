/*
 * Copyright © 2019 Simon Massey (massey1905@gmail.com)
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
package com.codahale.shamir.polygot;

import java.security.SecureRandom;

/**
 * This class ensures that unit tests can repeatedly initalise with 
 * the same polynomial. 
 */
public class NotRandomSource extends SecureRandom {

  @Override
  public void nextBytes(byte[] bytes) {
    for (int b = 0; b < bytes.length; b++) {
      bytes[b] = (byte) (b + 1);
    }
  }

  public byte[] notRandomBytes(int len) {
    byte[] bytes = new byte[len];
    this.nextBytes(bytes);
    return bytes;
  }
}
