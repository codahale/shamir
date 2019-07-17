/*
 * Copyright © 2019 Simon Massey (massey1905@gmail.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

const test = require('tape');
const { randomBytes } = require('crypto');

const GF256 = require('../../main/js/GF256.js');

/* eslint-disable prefer-arrow-callback */
/* eslint-disable func-names */
/* eslint-disable no-plusplus */
test('GF256Tests add', function (t) {
  t.plan(1);
  t.equal(GF256.add(100, 30), 122);
});

test('GF256Tests sub', function (t) {
  t.plan(1);
  t.equal(GF256.sub(100, 30), 122);
});

test('GF256Tests mul', function (t) {
  t.plan(4);
  t.equal(GF256.mul(90, 21), 254);
  t.equal(GF256.mul(133, 5), 167);
  t.equal(GF256.mul(0, 21), 0);
  t.equal(GF256.mul(0xb6, 0x53), 0x36);
});

test('GF256Tests div', function (t) {
  t.plan(4);
  t.equal(GF256.div(90, 21), 189);
  t.equal(GF256.div(6, 55), 151);
  t.equal(GF256.div(22, 192), 138);
  t.equal(GF256.div(0, 192), 0);
});

test('GF256Tests degree', function (t) {
  t.plan(4);
  t.equal(GF256.degree([1, 2]), 1);
  t.equal(GF256.degree([1, 2, 0]), 1);
  t.equal(GF256.degree([1, 2, 3]), 2);
  t.equal(GF256.degree([0, 0, 0]), 0);
});


const bytes = new Uint8Array(function () {
  const returned = [];
  for (let i = 1; i <= 255; i++) {
    returned.push(i);
  }
  return returned;
}());

function makePairs(arr) {
  const res = [];
  const l = arr.length;
  for (let i = 0; i < l; ++i) {
    for (let j = i + 1; j < l; ++j) res.push([arr[i], arr[j]]);
  }
  return res;
}

const pairs = makePairs(bytes);

test('GF256Tests mul is commutative', function (t) {
  pairs.forEach(function (pair) {
    if (GF256.mul(pair[0], pair[1]) !== GF256.mul(pair[1], pair[0])) {
      throw new Error(`mul not commutative for pair ${pair}`);
    }
  });
  t.end();
});

test('GF256Tests add is commutative', function (t) {
  pairs.forEach(function (pair) {
    if (GF256.add(pair[0], pair[1]) !== GF256.add(pair[1], pair[0])) {
      throw new Error(`add not commutitive for pair ${pair}`);
    }
  });
  t.end();
});

test('GF256Tests sub is the inverse of add', function (t) {
  pairs.forEach(function (pair) {
    if (GF256.sub(GF256.add(pair[0], pair[1]), pair[1]) !== pair[0]) {
      throw new Error(`sub is not the inverse of add for pair ${pair}`);
    }
  });
  t.end();
});

test('GF256Tests div is the inverse of mul', function (t) {
  pairs.forEach(function (pair) {
    if (GF256.div(GF256.mul(pair[0], pair[1]), pair[1]) !== pair[0]) {
      throw new Error(`div is not the inverse of mul for pair ${pair}`);
    }
  });
  t.end();
});

test('GF256Tests mul is the inverse of div', function (t) {
  pairs.forEach(function (pair) {
    if (GF256.mul(GF256.div(pair[0], pair[1]), pair[1]) !== pair[0]) {
      throw new Error(`mul is not the inverse of div for pair ${pair}`);
    }
  });
  t.end();
});

test('GF256Tests eval', function (t) {
  t.equal(GF256.eval([1, 0, 2, 3], 2), 17);
  t.end();
});

test('GF256Tests interpolate', function (t) {
  t.equal(GF256.interpolate([[1, 1], [2, 2], [3, 3]]), 0);
  t.equal(GF256.interpolate([[1, 80], [2, 90], [3, 20]]), 30);
  t.equal(GF256.interpolate([[1, 43], [2, 22], [3, 86]]), 107);
  t.end();
});

let countDownFrom2 = 2;

const zeroLastByteFirstTWoAttemptsRandomBytes = function (length) {
  const p = randomBytes(length);
  if (countDownFrom2 >= 0) {
    p[p.length - 1] = 0;
    countDownFrom2--;
  }
  return p;
};

test('GF256Tests generate', function (t) {
  const p = GF256.generate(zeroLastByteFirstTWoAttemptsRandomBytes, 5, 20);
  t.equal(p[0], 20);
  t.equal(p.length, 6);
  t.notOk(p[p.length - 1] === 0);
  t.end();
});

test('GF256Tests eval', function (t) {
  t.plan(1);
  const v = GF256.eval([1, 0, 2, 3], 2);
  t.equal(v, 17);
});
