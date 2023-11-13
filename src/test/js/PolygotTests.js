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

const test = require('tape-catch');
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

// eslint-disable-next-line no-undef
const ByteArray = Java.type('byte[]');
// eslint-disable-next-line no-undef
const JavaScriptUtils = Java.type('com.codahale.shamir.polygot.JavaScriptUtils');

function jsByteArrayToJavaByteArray(jsarray) {
  const jbarray = new ByteArray(jsarray.length);
  for (let i = 0; i < jsarray.length; i++) {
    jbarray[i] = JavaScriptUtils.unsignedToSignedByteMap.get(jsarray[i]);
  }
  return jbarray;
}

function jBytesArrayToJavaScriptByteArray(barray) {
  const jsarray = [];
  for (let i = 0; i < barray.length; i++) {
    jsarray.push(JavaScriptUtils.signedToUnsignedByteMap.get(barray[i]));
  }
  return jsarray;
}

test('PolygotTests Java and JavaScript byte array roundtrip', function (t) {
  t.plan(1);
  const secretUtf8 = 'ᚠᛇᚻ';
  const jsBytes = stringToBytes(secretUtf8);
  const jbytes = jsByteArrayToJavaByteArray(jsBytes);
  const jsRoundTripBytes = jBytesArrayToJavaScriptByteArray(jbytes);
  t.equal(secretUtf8, bytesToSring(jsRoundTripBytes));
});

// eslint-disable-next-line no-undef
const Scheme = Java.type('com.codahale.shamir.Scheme');
// eslint-disable-next-line no-undef
const SecureRandom = Java.type('java.security.SecureRandom');
const secureRandom = new SecureRandom();

test('PolygotTests JavaScript strings with all Java logic', function (t) {
  t.plan(1);
  const scheme = new Scheme(secureRandom, 5, 3);
  const secretUtf8 = 'ᚠᛇᚻ';

  const parts = scheme.split(jsByteArrayToJavaByteArray(stringToBytes(secretUtf8)));
  const joined = scheme.join(parts);

  t.equal(secretUtf8, bytesToSring(jBytesArrayToJavaScriptByteArray(joined)));
});

const { split } = require('../../main/js/Scheme.js');

// eslint-disable-next-line no-undef
const HashMap = Java.type('java.util.HashMap');

function javaScriptToJavaParts(parts) {
  const map = new HashMap();
  // eslint-disable-next-line no-restricted-syntax, guard-for-in
  for (const key in parts) {
    const bytes = parts[key];
    const jbarr = jsByteArrayToJavaByteArray(bytes);
    map.put(Number(key), jbarr);
  }
  return map;
}

function javaToJavaScriptParts(javaMap) {
  const result = {};
  const entrySetIterator = javaMap.entrySet().iterator();
  while (entrySetIterator.hasNext()) {
    const pair = entrySetIterator.next();
    const key = pair.getKey();
    const value = pair.getValue();
    result[key] = jBytesArrayToJavaScriptByteArray(value);
  }
  return result;
}

// eslint-disable-next-line no-undef
const Collectors = Java.type('java.util.stream.Collectors');

function equalParts(jParts, jsParts) {
  // js keys are strings
  const jsKeysNumbers = Object.keys(jsParts);
  // j keys are java integers that we map to strings
  const jKeysSet = jParts
    .keySet()
    .stream()
    .map(n => n.toString())
    .collect(Collectors.toList());

  // check that all js keys are in the j keys
  // eslint-disable-next-line no-restricted-syntax
  for (const jsk of jsKeysNumbers) {
    if (!jKeysSet.contains(jsk)) {
      throw new Error(`jKeysSet ${jKeysSet} does not contain jsk ${jsk}`);
    }
  }

  // check that all j keys are in the js keys
  // eslint-disable-next-line no-restricted-syntax
  for (const jk of jKeysSet) {
    if (!jsKeysNumbers.includes(jk)) {
      throw new Error(`jsKeysNumbers ${jsKeysNumbers} does not contain jk ${jk}`);
    }
  }

  // eslint-disable-next-line no-restricted-syntax
  for (const k of Object.keys(jsParts)) {
    const jArray = jBytesArrayToJavaScriptByteArray(jParts.get(Number(k)));
    const jsArray = jsParts[k];
    if (jArray.length !== jsArray.length) {
      throw new Error(`unequal lengths ${jArray.length} != ${jsArray.length}`);
    }
    for (let l = 0; l < jArray.length; l++) {
      if (jArray[l] !== jsArray[l]) {
        throw new Error(`at index ${l}: ${jArray[l]} != ${jsArray[l]}`);
      }
    }
  }

  return true;
}

test('PolygotTests roundrip parts between JavaScript and Java', function (t) {
  t.plan(1);
  const secretUtf8 = 'ᚠᛇᚻ';
  const secret = stringToBytes(secretUtf8);
  const parts = split(randomBytes, 3, 2, secret);
  const jParts = javaScriptToJavaParts(parts);
  const jsParts = javaToJavaScriptParts(jParts);
  t.ok(equalParts(jParts, jsParts), 'roundtrip parts');
});

// eslint-disable-next-line no-undef
const NotRandomSource = Java.type('com.codahale.shamir.polygot.NotRandomSource');
const notRandomSource = new NotRandomSource();
function notRandomSourceJavaScript(len) {
  const bytes = [];
  for (let i = 0; i < len; i++) {
    bytes[i] = i + 1;
  }
  return bytes;
}

test('PolygotTests compare Java and JavaScript split', function (t) {
  t.plan(1);
  const secretUtf8 = 'ᚠᛇᚻ';
  const secret = stringToBytes(secretUtf8);

  const jsParts = split(notRandomSourceJavaScript, 3, 2, secret);

  const jscheme = new Scheme(notRandomSource, 3, 2);
  const jParts = jscheme.split(jsByteArrayToJavaByteArray(secret));
  t.ok(equalParts(jParts, jsParts), 'splits match');
});

test('PolygotTests JavaScript split with Java join', function (t) {
  t.plan(1);
  const secretUtf8 = 'ᚠᛇᚻ';

  const secret = stringToBytes(secretUtf8);
  const jsParts = split(randomBytes, 3, 2, secret);
  const jscheme = new Scheme(secureRandom, 3, 2);
  const joined = jscheme.join(javaScriptToJavaParts(jsParts));

  t.equal(
    bytesToSring(jBytesArrayToJavaScriptByteArray(joined)),
    secretUtf8,
    'java joined js parts',
  );
});

const { join } = require('../../main/js/Scheme.js');

test('PolygotTests Java split with JavaScript join', function (t) {
  t.plan(1);
  const secretUtf8 = 'ᚠᛇᚻ';

  const secret = stringToBytes(secretUtf8);
  const jscheme = new Scheme(secureRandom, 3, 2);
  const jParts = jscheme.split(jsByteArrayToJavaByteArray(secret));
  const joined = join(javaToJavaScriptParts(jParts));

  t.equal(bytesToSring(joined), secretUtf8, 'java joined js parts');
});
