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

package com.codahale.shamir.perf;

import com.codahale.shamir.Scheme;
import java.io.BufferedReader;
import java.io.InputStreamReader;

public class LoadHarness {

  @SuppressWarnings("InfiniteLoopStatement")
  public static void main(String[] args) throws Exception {
    System.out.println("Press enter to begin");
    new BufferedReader(new InputStreamReader(System.in)).readLine();
    System.out.println("Running...");
    final byte[] secret = new byte[10 * 1024];
    final Scheme scheme = new Scheme(200, 20);
    while (true) {
      scheme.join(scheme.split(secret));
    }
  }
}
