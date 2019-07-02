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

const test = require('tape');

const { split, join } = require('../../main/js/Scheme.js');

const { randomBytes } = require('crypto');

/* eslint-disable prefer-arrow-callback */
/* eslint-disable func-names */
/* eslint-disable no-plusplus */
/* eslint-disable no-bitwise */

// https://codereview.stackexchange.com/a/3589/75693
function bytesToSring(bytes) {
  const chars = [];
  for (let i = 0, n = bytes.length; i < n;) {
    chars.push(((bytes[i++] & 0xff) << 8) | (bytes[i++] & 0xff));
  }
  return String.fromCharCode.apply(null, chars);
}


// https://codereview.stackexchange.com/a/3589/75693
function stringToBytes(str) {
  const bytes = [];
  for (let i = 0, n = str.length; i < n; i++) {
    const char = str.charCodeAt(i);
    bytes.push(char >>> 8, char & 0xff);
  }
  return bytes;
}

test('TieredSharing roundtrip', function (t) {

  const secretUtf8 = 'ᚠᛇᚻ';
  const secret = stringToBytes(secretUtf8);

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

  // reconstruct the secret with two admin shares
  const joinedAdminShares = join(adminSplits);
  t.equal(secretUtf8, bytesToSring(joinedAdminShares));

  // throw away second admin share we only have one remaining
  delete adminSplits['2'];
  // throw away one user share as we only need three
  delete usersSplits['1'];

  // reconstruct the third admin share from the three user shares 
  const joinedUserShares = join(usersSplits);
  
  // use the first admin share and the recovered third share 
  const mixedShares = { '1': adminSplits['1'], '3': joinedUserShares };
  const joinedMixedShares = join(mixedShares);
  t.equal(secretUtf8, bytesToSring(joinedMixedShares));
  t.end();
});
