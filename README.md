# Shamir's Secret Sharing

[![CircleCI](https://circleci.com/gh/codahale/shamir.svg?style=svg)](https://circleci.com/gh/codahale/shamir)

A Java implementation of [Shamir's Secret Sharing
algorithm](http://en.wikipedia.org/wiki/Shamir's_Secret_Sharing) over GF(256).

## Add to your project

```xml
<dependency>
  <groupId>com.codahale</groupId>
  <artifactId>shamir</artifactId>
  <version>0.7.0</version>
</dependency>
```

*Note: module name for Java 9+ is `com.codahale.shamir`.*

## Use the thing

```java
import com.codahale.shamir.Scheme;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.util.Map;

class Example {
  void doIt() {
    final Scheme scheme = new Scheme(new SecureRandom(), 5, 3);
    final byte[] secret = "hello there".getBytes(StandardCharsets.UTF_8);
    final Map<Integer, byte[]> parts = scheme.split(secret);
    final byte[] recovered = scheme.join(parts);
    System.out.println(new String(recovered, StandardCharsets.UTF_8));
  } 
}
```

## How it works

Shamir's Secret Sharing algorithm is a way to split an arbitrary secret `S` into `N` parts, of which
at least `K` are required to reconstruct `S`. For example, a root password can be split among five
people, and if three or more of them combine their parts, they can recover the root password.

### Splitting secrets

Splitting a secret works by encoding the secret as the constant in a random polynomial of `K`
degree. For example, if we're splitting the secret number `42` among five people with a threshold of
three (`N=5,K=3`), we might end up with the polynomial:

```
f(x) = 71x^3 - 87x^2 + 18x + 42
```

To generate parts, we evaluate this polynomial for values of `x` greater than zero:

```
f(1) =   44
f(2) =  298
f(3) = 1230
f(4) = 3266
f(5) = 6822
```

These `(x,y)` pairs are then handed out to the five people. 

### Joining parts 

When three or more of them decide to recover the original secret, they pool their parts together:

```
f(1) =   44
f(3) = 1230
f(4) = 3266
```

Using these points, they construct a [Lagrange
polynomial](https://en.wikipedia.org/wiki/Lagrange_polynomial), `g`, and calculate `g(0)`. If the
number of parts is equal to or greater than the degree of the original polynomial (i.e. `K`), then
`f` and `g` will be exactly the same, and `f(0) = g(0) = 42`, the encoded secret. If the number of
parts is less than the threshold `K`, the polynomial will be different and `g(0)` will not be `42`.

### Implementation details

Shamir's Secret Sharing algorithm only works for finite fields, and this library performs all
operations in [GF(256)](http://www.cs.utsa.edu/~wagner/laws/FFM.html). Each byte of a secret is
encoded as a separate `GF(256)` polynomial, and the resulting parts are the aggregated values of
those polynomials.

Using `GF(256)` allows for secrets of arbitrary length and does not require additional parameters,
unlike `GF(Q)`, which requires a safe modulus. It's also **much** faster than `GF(Q)`: splitting and
combining a 1KiB secret into 8 parts with a threshold of 3 takes single-digit milliseconds, whereas
performing the same operation over `GF(Q)` takes several seconds, even using per-byte polynomials.
Treating the secret as a single `y` coordinate over `GF(Q)` is even slower, and requires a modulus
larger than the secret.

## Performance

It's fast. Plenty fast.

For a 1KiB secret split with a `n=4,k=3` scheme:

```
Benchmark         (n)  (secretSize)  Mode  Cnt     Score    Error  Units
Benchmarks.join     4          1024  avgt  200   196.787 ±  0.974  us/op
Benchmarks.split    4          1024  avgt  200   396.708 ±  1.520  us/op
```

**N.B.:** `split` is quadratic with respect to the number of shares being combined.

## Tiered sharing

Some usages of secret sharing involve levels of access: e.g. recovering a secret requires two admin
shares and three user shares. As @ba1ciu discovered, these can be implemented by building a tree of
shares:

```java
class BuildTree {
  public static void shareTree(String... args) {
    final byte[] secret = "this is a secret".getBytes(StandardCharsets.UTF_8);
    
    // tier 1 of the tree
    final Scheme adminScheme = new Scheme(new SecureRandom(), 5, 2);
    final Map<Integer, byte[]> admins = adminScheme.split(secret);

    // tier 2 of the tree
    final Scheme userScheme = Scheme.of(4, 3);
    final Map<Integer, Map<Integer, byte[]>> admins =
        users.entrySet()
            .stream()
            .collect(Collectors.toMap(Map.Entry::getKey, e -> userScheme.split(e.getValue())));
    
    System.out.println("Admin shares:");
    System.out.printf("%d = %s\n", 1, Arrays.toString(admins.get(1)));
    System.out.printf("%d = %s\n", 2, Arrays.toString(admins.get(2)));

    System.out.println("User shares:");
    System.out.printf("%d = %s\n", 1, Arrays.toString(users.get(3).get(1)));
    System.out.printf("%d = %s\n", 2, Arrays.toString(users.get(3).get(2)));
    System.out.printf("%d = %s\n", 3, Arrays.toString(users.get(3).get(3)));
    System.out.printf("%d = %s\n", 4, Arrays.toString(users.get(3).get(4)));
  }
}
```

By discarding the third admin share and the first two sets of user shares, we have a set of shares
which can be used to recover the original secret as long as either two admins or one admin and three
users agree.

## License

Copyright © 2017 Coda Hale

Distributed under the Apache License 2.0.
