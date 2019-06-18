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

const split = require('../../main/js/Scheme.js').split;
const join = require('../../main/js/Scheme.js').join;

const randomBytes = require('tweetnacl').randomBytes;

test('SchemeTests roundtrip', function (t) {
    const parts = 3;
    const quorum = 2;
    const secret = [];
    secret[0] = 1;
    secret[1] = 2;
    // secret[2] = 3;
    const splits = split(randomBytes, parts, quorum, secret);
    t.equal(Object.keys(splits).length, parts);
    //console.log(splits);
    const joined = join(splits);
    t.equal( joined[0], secret[0] );
    t.equal( joined[1], secret[1] );
    // t.equal( joined[2], secret[2] );
    t.end();
});

