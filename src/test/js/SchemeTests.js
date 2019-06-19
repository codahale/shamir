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

// https://codereview.stackexchange.com/a/3589/75693
function bytesToSring(bytes) {
    var chars = [];
    for(var i = 0, n = bytes.length; i < n;) {
        chars.push(((bytes[i++] & 0xff) << 8) | (bytes[i++] & 0xff));
    }
    return String.fromCharCode.apply(null, chars);
}

// https://codereview.stackexchange.com/a/3589/75693
function stringToBytes(str) {
    var bytes = [];
    for(var i = 0, n = str.length; i < n; i++) {
        var char = str.charCodeAt(i);
        bytes.push(char >>> 8, char & 0xFF);
    }
    return bytes;
}

test('SchemeTests roundtrip', function (t) {
    const parts = 3;
    const quorum = 2;

    // http://kermitproject.org/utf8.html
    // From the Anglo-Saxon Rune Poem (Rune version)    
    const secretUtf8 = `ᚠᛇᚻ᛫ᛒᛦᚦ᛫ᚠᚱᚩᚠᚢᚱ᛫ᚠᛁᚱᚪ᛫ᚷᛖᚻᚹᛦᛚᚳᚢᛗ
ᛋᚳᛖᚪᛚ᛫ᚦᛖᚪᚻ᛫ᛗᚪᚾᚾᚪ᛫ᚷᛖᚻᚹᛦᛚᚳ᛫ᛗᛁᚳᛚᚢᚾ᛫ᚻᛦᛏ᛫ᛞᚫᛚᚪᚾ
ᚷᛁᚠ᛫ᚻᛖ᛫ᚹᛁᛚᛖ᛫ᚠᚩᚱ᛫ᛞᚱᛁᚻᛏᚾᛖ᛫ᛞᚩᛗᛖᛋ᛫ᚻᛚᛇᛏᚪᚾ᛬`;

    var secret = stringToBytes(secretUtf8);

    const splits = split(randomBytes, parts, quorum, secret);
    t.equal(Object.keys(splits).length, parts);
    const joined = join(splits);
    t.equal( joined[0], secret[0] );
    t.equal( joined[1], secret[1] );
    const joinedUtf8 = bytesToSring(joined);
    t.equal( secretUtf8, joinedUtf8 );
    // console.log("The secret is: "+secretUtf8);
    // console.log("The decoded secret is: "+joinedUtf8);
    t.end();
});

