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

const GF256 = require('./GF256.js');

/**
 * Splits the given secret into {@code n} parts, of which any {@code k} or more can be combined to
 * recover the original secret.
 * @param  {function int -> array[Uint8Array]} randomBytes Takes a length and returns a Uint8Array of that length
 * @param  {Number} n the number of parts to produce (must be {@code >1})
 * @param  {Number} k the threshold of joinable parts (must be {@code <= n})
 * @param  {array[Uint8Array]} secret The secret to split as an array of bytes
 * @return {object} an associative array of {@code n} part IDs and their values as array[Uint8Array]
 */
exports.split = function (randomBytes, n, k, secret) {
  if (k < 1) throw "K must be > 1";
  if (n <= k) throw "N must be >= K";
  if (n >= 255) throw "N must be <= 255";
  //if( !secret.isArray()) throw "secret must be an array";

    // // generate part values
    // final byte[][] values = new byte[n][secret.length];
    // for (int i = 0; i < secret.length; i++) {
    //   // for each byte, generate a random polynomial, p
    //   final byte[] p = GF256.generate(random, k - 1, secret[i]);
    //   for (int x = 1; x <= n; x++) {
    //     // each part's byte is p(partId)
    //     values[x - 1][i] = GF256.eval(p, (byte) x);
    //   }
    // }
  
  console.log("n:"+n+", k:"+k+" secret.length:"+secret.length);

  const values = [];
  for ( i = 0; i < secret.length; i++ ){
    console.log("before i:"+ i + " "+  secret.length);
    const p = GF256.generate(randomBytes, k - 1, secret[0]);
    console.log("after  i:"+ i + " "+ secret.length);
    // const parts = [];
    // for ( x = 1; x <= n; x++ ) {
    //   parts.push(GF256.eval(p, x))
    // }
    // values.push(parts);
  }
  
    // // return as a set of objects
    // final Map<Integer, byte[]> parts = new HashMap<>(n());
    // for (int i = 0; i < values.length; i++) {
    //   parts.put(i + 1, values[i]);
    // }

  return {};
};

/**
   * Joins the given parts to recover the original secret.
   *
   * <p><b>N.B.:</b> There is no way to determine whether or not the returned value is actually the
   * original secret. If the parts are incorrect, or are under the threshold value used to split the
   * secret, a random value will be returned.
   * 
   * @param {object} an associative array of {@code n} part IDs and their values as array[Uint8Array]
   * @return {array[Uint8Array]} the original secret
   * 
 */
exports.join = function(){
  return new Uint8Array();
};