# Shamir's Secret Sharing

[![Build Status](https://secure.travis-ci.org/codahale/shamir.svg)](http://travis-ci.org/codahale/shamir)

A Java implementation of [Shamir's Secret Sharing
algorithm](http://en.wikipedia.org/wiki/Shamir's_Secret_Sharing) over GF(256).

## Add to your project

```xml
<dependency>
  <groupId>com.codahale</groupId>
  <artifactId>shamir</artifactId>
  <version>0.2.0</version>
</dependency>
```

## Use the thing

```java
import com.codahale.shamir.Scheme;
import com.codahale.shamir.Share;
import java.util.Set;

class Example {
  void doIt() {
    // split into 5 shares, any 3 of which can be combined
    final Scheme scheme = new Scheme(5, 3);
    final byte[] secret = "hello there".getBytes();
       
    final Set<Share> shares = scheme.split(5, 3, secret);
       
    // join shares to recover the original secret
    final byte[] recovered = scheme.join(shares);
        
    System.out.println(new String(recovered));
  } 
}
```

## How it works

Shamir's Secret Sharing algorithm is a way to split an arbitrary secret `S` into `N` shares, of
which at least `K` are required to reconstruct `S`. For example, a root password can be split among
five people, and if three or more of them combine their shares, they can recover the root password.

### Splitting secrets

Splitting a secret works by encoding the secret as the constant in a random polynomial of `K`
degree. For example, if we're splitting the secret number `42` among five people with a threshold of
three (`N=5,K=3`), we might end up with the polynomial:

```
f(x) = 71x^3 - 87x^2 + 18x + 42
```

To generate shares, we evaluate this polynomial for values of `x` greater than zero:

```
f(1) =   44
f(2) =  298
f(3) = 1230
f(4) = 3266
f(5) = 6822
```

These `(x,y)` pairs are then handed out to the five people. 

### Combining shares

When three or more of them decide to recover the original secret, they pool their shares together:

```
f(1) =   44
f(3) = 1230
f(4) = 3266
```

Using these points, they construct a [Lagrange
polynomial](https://en.wikipedia.org/wiki/Lagrange_polynomial), `g`, and calculate `g(0)`. If the
number of shares is equal to or greater than the degree of the original polynomial (i.e. `K`), then
`f` and `g` will be exactly the same, and `f(0) = g(0) = 42`, the encoded secret. If the number of
shares is less than the threshold `K`, the polynomial will be different and `g(0)` will not be `42`.

### Implementation details

Shamir's Secret Sharing algorithm only works for finite fields, and this library performs all
operations in [GF(256)](http://www.cs.utsa.edu/~wagner/laws/FFM.html). Each byte of a secret is
encoded as a separate `GF(256)` polynomial, and the resulting shares are the aggregated values of
those polynomials.

Using `GF(256)` allows for secrets of arbitrary length and does not require additional parameters,
unlike `GF(Q)`, which requires a safe modulus. It's also **much** faster than `GF(Q)`: splitting and
combining a 1KiB secret into 8 shares with a threshold of 3 takes single-digit milliseconds, whereas
performing the same operation over `GF(Q)` takes several seconds, even using per-byte polynomials.
Treating the secret as a single `y` coordinate over `GF(Q)` is even slower, and requires a modulus
larger than the secret.

## License

Copyright © 2017 Coda Hale

Distributed under the Apache License 2.0.
