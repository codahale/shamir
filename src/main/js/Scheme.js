/*
 * Copyright © 2019 Simon Massey (massey1905@gmail.com)
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
 * @param  {function int -> Uint8Array} randomBytes Takes a length and returns a random
 * Uint8Array of that length
 * @param  {Number} n the number of parts to produce (must be {@code >1})
 * @param  {Number} k the threshold of joinable parts (must be {@code <= n})
 * @param  {array[Uint8Array]} secret The secret to split as an array of bytes
 * @return {Object.<string, Uint8Array>} an map of {@code n} parts that are arrays of bytes of the
 * secret length
 */
function split(randomBytes, n, k, secret) {
  if (k <= 1) throw new Error('K must be > 1');
  if (n < k) throw new Error('N must be >= K');
  if (n > 255) throw new Error('N must be <= 255');

  const values = new Array(n)
    .fill(0)
    .map(() => new Uint8Array(secret.length).fill(0));
  // eslint-disable-next-line no-plusplus
  for (let i = 0; i < secret.length; i++) {
    const p = GF256.generate(randomBytes, k - 1, secret[i]);
    // eslint-disable-next-line no-plusplus
    for (let x = 1; x <= n; x++) {
      values[x - 1][i] = GF256.eval(p, x);
    }
  }

  const parts = {};

  // eslint-disable-next-line no-plusplus
  for (let i = 0; i < values.length; i++) {
    const part = `${i + 1}`;
    parts[part] = values[i];
  }

  return parts;
}

exports.split = split;

/**
 * Joins the given parts to recover the original secret.
 *
 * <p><b>N.B.:</b> There is no way to determine whether or not the returned value is actually the
 * original secret. If the parts are incorrect, or are under the threshold value used to split the
 * secret, a random value will be returned.
 *
 * @param {Object.<string, Uint8Array>} parts an map of {@code n} parts that are arrays of bytes
 * of the secret length
 * @return {Uint8Array} the original secret
 *
 */
function join(parts) {
  if (Object.keys(parts).length === 0) throw new Error('No parts provided');
  const lengths = Object.values(parts).map(x => x.length);
  const max = Math.max.apply(null, lengths);
  const min = Math.min.apply(null, lengths);
  if (max !== min) {
    throw new Error(`Varying lengths of part values. Min ${min}, Max ${max}`);
  }
  const secret = new Uint8Array(max);
  // eslint-disable-next-line no-plusplus
  for (let i = 0; i < secret.length; i++) {
    const keys = Object.keys(parts);
    const points = new Array(keys.length)
      .fill(0)
      .map(() => new Uint8Array(2).fill(0));
    // eslint-disable-next-line no-plusplus
    for (let j = 0; j < keys.length; j++) {
      const key = keys[j];
      const k = Number(key);
      points[j][0] = k;
      points[j][1] = parts[key][i];
    }
    secret[i] = GF256.interpolate(points);
  }

  return secret;
}

exports.join = join;
