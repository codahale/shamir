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

package com.codahale.shamir.benchmarks;

import com.codahale.shamir.Part;
import com.codahale.shamir.Scheme;
import java.io.IOException;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import okio.ByteString;
import org.openjdk.jmh.Main;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.runner.RunnerException;

@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.MICROSECONDS)
public class Benchmarks {

  private static final ByteString SECRET = ByteString.of(new byte[1024]);
  private static final Scheme SCHEME = new Scheme(5, 3);
  private static final Set<Part> PARTS = SCHEME.split(SECRET);

  public static void main(String[] args) throws IOException, RunnerException {
    Main.main(args);
  }

  @Benchmark
  public Set<Part> split() {
    return SCHEME.split(SECRET);
  }

  @Benchmark
  public ByteString join() {
    return SCHEME.join(PARTS);
  }
}
