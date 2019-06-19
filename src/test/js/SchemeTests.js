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

    // string length is only 117 characters but the byte length is 234.
    var secret = stringToBytes(secretUtf8);

    const splits = split(randomBytes, parts, quorum, secret);
    t.equal(Object.keys(splits).length, parts);
    const joined = join(splits);
    t.equal( joined[200], secret[200] );
    t.equal( joined[201], secret[201] );
    const joinedUtf8 = bytesToSring(joined);
    t.equal( secretUtf8, joinedUtf8 );
    // console.log("The secret is: "+secretUtf8);
    // console.log("The decoded secret is: "+joinedUtf8);
    t.end();
});

test('SchemeTests roundtrip two parts', function (t) {
    const parts = 3;
    const quorum = 2;

    const secretUtf8 = `ᚠᛇᚻ`;
    const secret = stringToBytes(secretUtf8);

    for( var i = 1; i <=3; i++) {
        const splits = split(randomBytes, parts, quorum, secret);
        delete splits[""+i];
        const joinedUtf8 = bytesToSring(join(splits));
        t.equal( secretUtf8, joinedUtf8 );
    }
    
    t.end();
});

test('SchemeTests split input validation', function (t) {
    const secretUtf8 = `ᚠᛇᚻ`;
    const secret = stringToBytes(secretUtf8);

    t.plan(3);

    try {
        const splits = split(randomBytes, 256, 2, secret);
        t.notOk(true);
    } catch (e) {
        t.ok(e.toString().includes('N must be <= 255'), e);
    }

    try {
        const splits = split(randomBytes, 3, 1, secret);
        t.notOk(true);
    } catch (e) {
        t.ok(e.toString().includes('K must be > 1'), e);
    }

    try {
        const splits = split(randomBytes, 2, 3, secret);
        t.notOk(true);
    } catch (e) {
        t.ok(e.toString().includes('N must be >= K'), e);
    }

    t.end();
});

test('SchemeTests join input validation', function (t) {
    
//    const splits = split(randomBytes, 3, 2, stringToBytes(`ᚠᛇᚻ`));

    try {
        join({});
        t.notOk(true);
    } catch (e) {
        t.ok(e.toString().includes('No parts provided'), e);
    }

    
    
    t.end();
});
