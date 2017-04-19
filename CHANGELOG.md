# Change Log

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