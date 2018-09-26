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
package com.codahale.shamir.benchmarks;

import com.codahale.shamir.Scheme;
import java.security.SecureRandom;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Param;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;

@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.MICROSECONDS)
@State(Scope.Benchmark)
public class Benchmarks {

  @Param({"100", "1024"})
  private int secretSize = 1024;

  @Param({"2", "4", "8", "16", "32"})
  private int n = 2;

  private final int k = (n / 2) + 1;
  private byte[] secret = new byte[secretSize];
  private Scheme scheme = new Scheme(new SecureRandom(), n, k);
  private Map<Integer, byte[]> parts = new HashMap<>();

  @Setup
  public void setup() {
    parts.putAll(scheme.split(secret));
    while (parts.size() > k) {
      parts.entrySet().iterator().remove();
    }
  }

  @Benchmark
  public Map<Integer, byte[]> split() {
    return scheme.split(secret);
  }

  @Benchmark
  public byte[] join() {
    return scheme.join(parts);
  }
}
