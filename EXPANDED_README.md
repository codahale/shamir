# Expanded Shamir's Secret Sharing

[![Build Status](https://travis-ci.org/ba1ciu/shamir.svg?branch=master)](https://travis-ci.org/ba1ciu/shamir)

## Use the thing

```java
import com.codahale.shamir.ExpandedScheme;
import java.nio.charset.StandardCharsets;
import java.util.Map;

class Example {
  void doIt() {
    final ExpandedScheme scheme = ExpandedScheme.of(5, 1, 3);
    final byte[] secret = "hello there".getBytes(StandardCharsets.UTF_8);
    final Map<Integer, byte[]> parts = scheme.split(secret);
    final byte[] recovered = scheme.join(parts);
    System.out.println(new String(recovered, StandardCharsets.UTF_8));
  } 
}
```

## How it works

Expanded Secret Sharing allows to split secret `S` into `N` parts, of which `M` are mandatory
to reconstruct `S` and `K-M` are optional in the recovery process. Mandatory parts ID is always
between `1` and `M`. Optional parts ID is always greater than `M`.

Example:

Password must be split between one admin and four users and to recover it, admin and at least any
two of users must combine their parts. In this particular case, `ExpandedScheme[n=5, m=1, k=3]`
is needed. Part with ID `1` should be given to admin and the rest can be randomly distributed
between users.

## Implementation details

Expanded Secret Sharing uses original Coda Hale's split and join operations implementations, just twice. Firstly, secret is
splited into `M+1` parts, of which all are needed for reconstruction process. Parts with ID from `1` to `M` are the mandatory parts. Secondly, `M+1` part
is splited into `M-K` optional parts with IDs between `M+1` and `K`. `K-M` optional parts are needed to reconstruct a part from previous split.
This approach ensures that any `K` or more parts must include all of `M` mandatory parts to
retrieve the original secret.

## License

Copyright © 2018 Balciu

Distributed under the Apache License 2.0.
