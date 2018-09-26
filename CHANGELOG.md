# Change Log

## v0.7.0: 2018-09-25

* Removed Jetbrains annotations.
* Moved away from AutoValue classes to more regular API.

## v0.6.1: 2018-08-23

* Updated dependencies.
* JAR now specifies an automatic module name of `com.codahale.shamir`.

## v0.6.0: 2017-06-27

* Moved to a `Map`-based API.

## v0.5.0: 2017-06-26

* Removed okio as a dependency.

## v0.4.0: 2017-04-23

* Converted `Scheme` into an auto-value class.

## v0.3.0: 2017-04-18

* Renamed `Share` to `Part`, revamped API.
* Changed API to use `okio`'s `ByteString`.
* Improved efficiency of GF256 operations.

## v0.2.0: 2017-04-18

* Move static methods into `Share` class.

## v0.1.6: 2017-04-17

* Small simplifications.

## v0.1.5: 2017-04-15

* Fix share splitting for >127 shares.

## v0.1.4: 2017-04-14

* Simplify some internals.
* Re-use a single `SecureRandom` instance while splitting.
* Improve error messages on null parameters.

## v0.1.3: 2017-04-14

* Remove dependency on FindBugs annotations.

## v0.1.2: 2017-04-13

* Fix `#combine()` for single-byte secrets.

## v0.1.1: 2017-04-13

* Remove accidental array copies in `#combine()`.

## v0.1.0: 2017-04-13

* Initial release.