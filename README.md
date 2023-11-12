# Shamir's Secret Sharing

A implementation of [Shamir's Secret Sharing
algorithm](http://en.wikipedia.org/wiki/Shamir's_Secret_Sharing) over GF(256) in both Java and JavaScript. The Java code is from 
the archive Codahale's [shamir](https://github.com/codahale/shamir) implementation. The 
Javascript version is original and is crossed checked against the Java version. 

You can use docker build both codebases and run cross-checks between them using:

`docker build -f Dockerfile.graaljs . `

*Note: module name for Java 9+ is `com.codahale.shamir`.*

## Add to your JavaScript project

```sh
npm i shamir
```

## Use the thing in JavaScript

```javascript
const { split, join } = require('shamir');
const { randomBytes } = require('crypto');

const PARTS = 5;
const QUORUM = 3;

function doIt() {
    const secret = 'hello there';
    // you can use any polyfill to covert between strings and Uint8Array
    const utf8Encoder = new TextEncoder();
    const utf8Decoder = new TextDecoder();
    const secretBytes = utf8Encoder.encode(secret);
    // parts is a map of part numbers to Uint8Array
    const parts = split(randomBytes, PARTS, QUORUM, secretBytes);
    // we only need QUORUM of the parts to recover the secret
    delete parts['2'];
    delete parts['3'];
    // recovered is an Unit8Array
    const recovered = join(parts);
    // prints 'hello there'
    console.log(utf8Decoder.decode(recovered));
}
```

## [Optional] Add to your Java project

The Java version is available as the orginal Codahale distribution:

```xml
<dependency>
  <groupId>com.codahale</groupId>
  <artifactId>shamir</artifactId>
  <version>0.7.0</version>
</dependency>
```

## Use the thing in Java

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

## Java Performance

It's fast. Plenty fast.

For a 1KiB secret split with a `n=4,k=3` scheme:

```
Benchmark         (n)  (secretSize)  Mode  Cnt     Score    Error  Units
Benchmarks.join     4          1024  avgt  200   196.787 ±  0.974  us/op
Benchmarks.split    4          1024  avgt  200   396.708 ±  1.520  us/op
```

**N.B.:** `split` is quadratic with respect to the number of shares being combined.

## JavaScript Performance

For a 1KiB secret split with a `n=4,k=3` scheme running on NodeJS v10.16.0:

```
Benchmark         (n)  (secretSize)  Cnt   Score   Units
Benchmarks.join     4          1024  200   2.08    ms/op
Benchmarks.split    4          1024  200   2.78    ms/op
```

Split is dominated by the calls to `Crypto.randomBytes` to get random polynomials to encode each byte of the secet. Using a more realistic 128 bit secret with `n=4,k=3` scheme running on NodeJS v10.16.0:

```
Benchmark         (n)  (secretSize)  Cnt   Score   Units
Benchmarks.join     5            16  200   0.083    ms/op
Benchmarks.split    5            16  200   0.081    ms/op
```

## Tiered Sharing Java

Some usages of secret sharing involve levels of access: e.g. recovering a secret requires two admin
shares and three user shares. As @ba1ciu discovered, these can be implemented by building a tree of
shares:

```java
class BuildTree {
  public static void shareTree(String... args) {
    final byte[] secret = "this is a secret".getBytes(StandardCharsets.UTF_8);
    
    // tier 1 of the tree
    final Scheme adminScheme = new Scheme(new SecureRandom(), 3, 2);
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

## Tiered Sharing JavaScript

Sharing a secret requiring either two admins or one admin and three users to recover:

```javascript
  const secret = new Unit8Array([1, 2, 3]);

  const adminParts = 3;
  const adminQuorum = 2;
  const adminSplits = split(randomBytes, adminParts, adminQuorum, secret);

  const userParts = 4;
  const userQuorum = 3;
  const usersSplits = split(randomBytes, userParts, userQuorum, adminSplits['3'] );

  // throw away third share that is split into 4 user parts
  delete adminSplits['3'];

  console.log('Admin Shares:');
  console.log(`1 = ${adminSplits['1']}`);
  console.log(`2 = ${adminSplits['2']}`);

  console.log('User Shares:');
  console.log(`1 = ${usersSplits['1']}`);
  console.log(`2 = ${usersSplits['2']}`);
  console.log(`3 = ${usersSplits['3']}`);
  console.log(`4 = ${usersSplits['4']}`);

  // throw away an admin share and one user share
  delete adminSplits['2'];
  delete usersSplits['1'];

  // reconstruct the deleted third admin share from the three user shares 
  const joinedUserShares = join(usersSplits);
  // use the first admin share and the recovered third share 
  const recoverdSecret = join({ '1': adminSplits['1'], '3': joinedUserShares } );
```

There is a unit test for this in `src/test/js/TieredSharing.js`. 

## License

Copyright © 2017 Coda Hale

Copyright © 2019 Simon Massey

Distributed under the Apache License 2.0.
