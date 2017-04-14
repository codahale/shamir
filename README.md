# Shamir's Secret Sharing

[![Build Status](https://secure.travis-ci.org/codahale/shamir.svg)](http://travis-ci.org/codahale/shamir)
[![Maven Central](https://maven-badges.herokuapp.com/maven-central/com.codahale/shamir/badge.svg?style=flat)](https://maven-badges.herokuapp.com/maven-central/com.codahale/shamir)

A Java implementation of [Shamir's Secret Sharing
algorithm](http://en.wikipedia.org/wiki/Shamir's_Secret_Sharing) over GF(256).

## Add to your project

[See the latest version.](https://maven-badges.herokuapp.com/maven-central/com.codahale/shamir)

## Use the thing

```java
import com.codahale.shamir.SecretSharing;
import com.codahale.shamir.Share;
import java.util.Set;

class Example {
    void doIt() {
        final byte[] secret = "hello there".getBytes();
       
        // split into 5 shares, any 3 of which can be combined
        final Set<Share> shares = SecretSharing.split(5, 3, secret);
       
        // combine shares to recover the original secret
        final byte[] recovered = SecretSharing.combine(shares);
        
        System.out.println(new String(recovered));
    } 
}
```

## License

Copyright © 2017 Coda Hale

Distributed under the Apache License 2.0.
