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

"use strict";

const add = function(a, b) {
    return a ^ b;
};

exports.add = add;

/* The Laws of Cryptograhy with Java Code by Neal R. Wagner
http://www.cs.utsa.edu/~wagner/lawsbookcolor/laws.pdf
Page 120 (134) section "20.3 Addition in GP(2^n)" is equal 
to subtraction. 
*/
const sub = add;

exports.sub = exports.sub;


const LOG = [
    parseInt("0xff"), parseInt("0x00"), parseInt("0x19"), parseInt("0x01"), parseInt("0x32"), parseInt("0x02"), parseInt("0x1a"),
    parseInt("0xc6"), parseInt("0x4b"), parseInt("0xc7"), parseInt("0x1b"), parseInt("0x68"), parseInt("0x33"), parseInt("0xee"),
    parseInt("0xdf"), parseInt("0x03"), parseInt("0x64"), parseInt("0x04"), parseInt("0xe0"), parseInt("0x0e"), parseInt("0x34"),
    parseInt("0x8d"), parseInt("0x81"), parseInt("0xef"), parseInt("0x4c"), parseInt("0x71"), parseInt("0x08"), parseInt("0xc8"),
    parseInt("0xf8"), parseInt("0x69"), parseInt("0x1c"), parseInt("0xc1"), parseInt("0x7d"), parseInt("0xc2"), parseInt("0x1d"),
    parseInt("0xb5"), parseInt("0xf9"), parseInt("0xb9"), parseInt("0x27"), parseInt("0x6a"), parseInt("0x4d"), parseInt("0xe4"),
    parseInt("0xa6"), parseInt("0x72"), parseInt("0x9a"), parseInt("0xc9"), parseInt("0x09"), parseInt("0x78"), parseInt("0x65"),
    parseInt("0x2f"), parseInt("0x8a"), parseInt("0x05"), parseInt("0x21"), parseInt("0x0f"), parseInt("0xe1"), parseInt("0x24"),
    parseInt("0x12"), parseInt("0xf0"), parseInt("0x82"), parseInt("0x45"), parseInt("0x35"), parseInt("0x93"), parseInt("0xda"),
    parseInt("0x8e"), parseInt("0x96"), parseInt("0x8f"), parseInt("0xdb"), parseInt("0xbd"), parseInt("0x36"), parseInt("0xd0"),
    parseInt("0xce"), parseInt("0x94"), parseInt("0x13"), parseInt("0x5c"), parseInt("0xd2"), parseInt("0xf1"), parseInt("0x40"),
    parseInt("0x46"), parseInt("0x83"), parseInt("0x38"), parseInt("0x66"), parseInt("0xdd"), parseInt("0xfd"), parseInt("0x30"),
    parseInt("0xbf"), parseInt("0x06"), parseInt("0x8b"), parseInt("0x62"), parseInt("0xb3"), parseInt("0x25"), parseInt("0xe2"),
    parseInt("0x98"), parseInt("0x22"), parseInt("0x88"), parseInt("0x91"), parseInt("0x10"), parseInt("0x7e"), parseInt("0x6e"),
    parseInt("0x48"), parseInt("0xc3"), parseInt("0xa3"), parseInt("0xb6"), parseInt("0x1e"), parseInt("0x42"), parseInt("0x3a"),
    parseInt("0x6b"), parseInt("0x28"), parseInt("0x54"), parseInt("0xfa"), parseInt("0x85"), parseInt("0x3d"), parseInt("0xba"),
    parseInt("0x2b"), parseInt("0x79"), parseInt("0x0a"), parseInt("0x15"), parseInt("0x9b"), parseInt("0x9f"), parseInt("0x5e"),
    parseInt("0xca"), parseInt("0x4e"), parseInt("0xd4"), parseInt("0xac"), parseInt("0xe5"), parseInt("0xf3"), parseInt("0x73"),
    parseInt("0xa7"), parseInt("0x57"), parseInt("0xaf"), parseInt("0x58"), parseInt("0xa8"), parseInt("0x50"), parseInt("0xf4"),
    parseInt("0xea"), parseInt("0xd6"), parseInt("0x74"), parseInt("0x4f"), parseInt("0xae"), parseInt("0xe9"), parseInt("0xd5"),
    parseInt("0xe7"), parseInt("0xe6"), parseInt("0xad"), parseInt("0xe8"), parseInt("0x2c"), parseInt("0xd7"), parseInt("0x75"),
    parseInt("0x7a"), parseInt("0xeb"), parseInt("0x16"), parseInt("0x0b"), parseInt("0xf5"), parseInt("0x59"), parseInt("0xcb"),
    parseInt("0x5f"), parseInt("0xb0"), parseInt("0x9c"), parseInt("0xa9"), parseInt("0x51"), parseInt("0xa0"), parseInt("0x7f"),
    parseInt("0x0c"), parseInt("0xf6"), parseInt("0x6f"), parseInt("0x17"), parseInt("0xc4"), parseInt("0x49"), parseInt("0xec"),
    parseInt("0xd8"), parseInt("0x43"), parseInt("0x1f"), parseInt("0x2d"), parseInt("0xa4"), parseInt("0x76"), parseInt("0x7b"),
    parseInt("0xb7"), parseInt("0xcc"), parseInt("0xbb"), parseInt("0x3e"), parseInt("0x5a"), parseInt("0xfb"), parseInt("0x60"),
    parseInt("0xb1"), parseInt("0x86"), parseInt("0x3b"), parseInt("0x52"), parseInt("0xa1"), parseInt("0x6c"), parseInt("0xaa"),
    parseInt("0x55"), parseInt("0x29"), parseInt("0x9d"), parseInt("0x97"), parseInt("0xb2"), parseInt("0x87"), parseInt("0x90"),
    parseInt("0x61"), parseInt("0xbe"), parseInt("0xdc"), parseInt("0xfc"), parseInt("0xbc"), parseInt("0x95"), parseInt("0xcf"),
    parseInt("0xcd"), parseInt("0x37"), parseInt("0x3f"), parseInt("0x5b"), parseInt("0xd1"), parseInt("0x53"), parseInt("0x39"),
    parseInt("0x84"), parseInt("0x3c"), parseInt("0x41"), parseInt("0xa2"), parseInt("0x6d"), parseInt("0x47"), parseInt("0x14"),
    parseInt("0x2a"), parseInt("0x9e"), parseInt("0x5d"), parseInt("0x56"), parseInt("0xf2"), parseInt("0xd3"), parseInt("0xab"),
    parseInt("0x44"), parseInt("0x11"), parseInt("0x92"), parseInt("0xd9"), parseInt("0x23"), parseInt("0x20"), parseInt("0x2e"),
    parseInt("0x89"), parseInt("0xb4"), parseInt("0x7c"), parseInt("0xb8"), parseInt("0x26"), parseInt("0x77"), parseInt("0x99"),
    parseInt("0xe3"), parseInt("0xa5"), parseInt("0x67"), parseInt("0x4a"), parseInt("0xed"), parseInt("0xde"), parseInt("0xc5"),
    parseInt("0x31"), parseInt("0xfe"), parseInt("0x18"), parseInt("0x0d"), parseInt("0x63"), parseInt("0x8c"), parseInt("0x80"),
    parseInt("0xc0"), parseInt("0xf7"), parseInt("0x70"), parseInt("0x07"),
  ];

/* https://crypto.stackexchange.com/a/21174/13860 
*/
const EXP = [
    parseInt("0x01"), parseInt("0x03"), parseInt("0x05"), parseInt("0x0f"), parseInt("0x11"), parseInt("0x33"), parseInt("0x55"),
    parseInt("0xff"), parseInt("0x1a"), parseInt("0x2e"), parseInt("0x72"), parseInt("0x96"), parseInt("0xa1"), parseInt("0xf8"),
    parseInt("0x13"), parseInt("0x35"), parseInt("0x5f"), parseInt("0xe1"), parseInt("0x38"), parseInt("0x48"), parseInt("0xd8"),
    parseInt("0x73"), parseInt("0x95"), parseInt("0xa4"), parseInt("0xf7"), parseInt("0x02"), parseInt("0x06"), parseInt("0x0a"),
    parseInt("0x1e"), parseInt("0x22"), parseInt("0x66"), parseInt("0xaa"), parseInt("0xe5"), parseInt("0x34"), parseInt("0x5c"),
    parseInt("0xe4"), parseInt("0x37"), parseInt("0x59"), parseInt("0xeb"), parseInt("0x26"), parseInt("0x6a"), parseInt("0xbe"),
    parseInt("0xd9"), parseInt("0x70"), parseInt("0x90"), parseInt("0xab"), parseInt("0xe6"), parseInt("0x31"), parseInt("0x53"),
    parseInt("0xf5"), parseInt("0x04"), parseInt("0x0c"), parseInt("0x14"), parseInt("0x3c"), parseInt("0x44"), parseInt("0xcc"),
    parseInt("0x4f"), parseInt("0xd1"), parseInt("0x68"), parseInt("0xb8"), parseInt("0xd3"), parseInt("0x6e"), parseInt("0xb2"),
    parseInt("0xcd"), parseInt("0x4c"), parseInt("0xd4"), parseInt("0x67"), parseInt("0xa9"), parseInt("0xe0"), parseInt("0x3b"),
    parseInt("0x4d"), parseInt("0xd7"), parseInt("0x62"), parseInt("0xa6"), parseInt("0xf1"), parseInt("0x08"), parseInt("0x18"),
    parseInt("0x28"), parseInt("0x78"), parseInt("0x88"), parseInt("0x83"), parseInt("0x9e"), parseInt("0xb9"), parseInt("0xd0"),
    parseInt("0x6b"), parseInt("0xbd"), parseInt("0xdc"), parseInt("0x7f"), parseInt("0x81"), parseInt("0x98"), parseInt("0xb3"),
    parseInt("0xce"), parseInt("0x49"), parseInt("0xdb"), parseInt("0x76"), parseInt("0x9a"), parseInt("0xb5"), parseInt("0xc4"),
    parseInt("0x57"), parseInt("0xf9"), parseInt("0x10"), parseInt("0x30"), parseInt("0x50"), parseInt("0xf0"), parseInt("0x0b"),
    parseInt("0x1d"), parseInt("0x27"), parseInt("0x69"), parseInt("0xbb"), parseInt("0xd6"), parseInt("0x61"), parseInt("0xa3"),
    parseInt("0xfe"), parseInt("0x19"), parseInt("0x2b"), parseInt("0x7d"), parseInt("0x87"), parseInt("0x92"), parseInt("0xad"),
    parseInt("0xec"), parseInt("0x2f"), parseInt("0x71"), parseInt("0x93"), parseInt("0xae"), parseInt("0xe9"), parseInt("0x20"),
    parseInt("0x60"), parseInt("0xa0"), parseInt("0xfb"), parseInt("0x16"), parseInt("0x3a"), parseInt("0x4e"), parseInt("0xd2"),
    parseInt("0x6d"), parseInt("0xb7"), parseInt("0xc2"), parseInt("0x5d"), parseInt("0xe7"), parseInt("0x32"), parseInt("0x56"),
    parseInt("0xfa"), parseInt("0x15"), parseInt("0x3f"), parseInt("0x41"), parseInt("0xc3"), parseInt("0x5e"), parseInt("0xe2"),
    parseInt("0x3d"), parseInt("0x47"), parseInt("0xc9"), parseInt("0x40"), parseInt("0xc0"), parseInt("0x5b"), parseInt("0xed"),
    parseInt("0x2c"), parseInt("0x74"), parseInt("0x9c"), parseInt("0xbf"), parseInt("0xda"), parseInt("0x75"), parseInt("0x9f"),
    parseInt("0xba"), parseInt("0xd5"), parseInt("0x64"), parseInt("0xac"), parseInt("0xef"), parseInt("0x2a"), parseInt("0x7e"),
    parseInt("0x82"), parseInt("0x9d"), parseInt("0xbc"), parseInt("0xdf"), parseInt("0x7a"), parseInt("0x8e"), parseInt("0x89"),
    parseInt("0x80"), parseInt("0x9b"), parseInt("0xb6"), parseInt("0xc1"), parseInt("0x58"), parseInt("0xe8"), parseInt("0x23"),
    parseInt("0x65"), parseInt("0xaf"), parseInt("0xea"), parseInt("0x25"), parseInt("0x6f"), parseInt("0xb1"), parseInt("0xc8"),
    parseInt("0x43"), parseInt("0xc5"), parseInt("0x54"), parseInt("0xfc"), parseInt("0x1f"), parseInt("0x21"), parseInt("0x63"),
    parseInt("0xa5"), parseInt("0xf4"), parseInt("0x07"), parseInt("0x09"), parseInt("0x1b"), parseInt("0x2d"), parseInt("0x77"),
    parseInt("0x99"), parseInt("0xb0"), parseInt("0xcb"), parseInt("0x46"), parseInt("0xca"), parseInt("0x45"), parseInt("0xcf"),
    parseInt("0x4a"), parseInt("0xde"), parseInt("0x79"), parseInt("0x8b"), parseInt("0x86"), parseInt("0x91"), parseInt("0xa8"),
    parseInt("0xe3"), parseInt("0x3e"), parseInt("0x42"), parseInt("0xc6"), parseInt("0x51"), parseInt("0xf3"), parseInt("0x0e"),
    parseInt("0x12"), parseInt("0x36"), parseInt("0x5a"), parseInt("0xee"), parseInt("0x29"), parseInt("0x7b"), parseInt("0x8d"),
    parseInt("0x8c"), parseInt("0x8f"), parseInt("0x8a"), parseInt("0x85"), parseInt("0x94"), parseInt("0xa7"), parseInt("0xf2"),
    parseInt("0x0d"), parseInt("0x17"), parseInt("0x39"), parseInt("0x4b"), parseInt("0xdd"), parseInt("0x7c"), parseInt("0x84"),
    parseInt("0x97"), parseInt("0xa2"), parseInt("0xfd"), parseInt("0x1c"), parseInt("0x24"), parseInt("0x6c"), parseInt("0xb4"),
    parseInt("0xc7"), parseInt("0x52"), parseInt("0xf6"), parseInt("0x01"), parseInt("0x03"), parseInt("0x05"), parseInt("0x0f"),
    parseInt("0x11"), parseInt("0x33"), parseInt("0x55"), parseInt("0xff"), parseInt("0x1a"), parseInt("0x2e"), parseInt("0x72"),
    parseInt("0x96"), parseInt("0xa1"), parseInt("0xf8"), parseInt("0x13"), parseInt("0x35"), parseInt("0x5f"), parseInt("0xe1"),
    parseInt("0x38"), parseInt("0x48"), parseInt("0xd8"), parseInt("0x73"), parseInt("0x95"), parseInt("0xa4"), parseInt("0xf7"),
    parseInt("0x02"), parseInt("0x06"), parseInt("0x0a"), parseInt("0x1e"), parseInt("0x22"), parseInt("0x66"), parseInt("0xaa"),
    parseInt("0xe5"), parseInt("0x34"), parseInt("0x5c"), parseInt("0xe4"), parseInt("0x37"), parseInt("0x59"), parseInt("0xeb"),
    parseInt("0x26"), parseInt("0x6a"), parseInt("0xbe"), parseInt("0xd9"), parseInt("0x70"), parseInt("0x90"), parseInt("0xab"),
    parseInt("0xe6"), parseInt("0x31"), parseInt("0x53"), parseInt("0xf5"), parseInt("0x04"), parseInt("0x0c"), parseInt("0x14"),
    parseInt("0x3c"), parseInt("0x44"), parseInt("0xcc"), parseInt("0x4f"), parseInt("0xd1"), parseInt("0x68"), parseInt("0xb8"),
    parseInt("0xd3"), parseInt("0x6e"), parseInt("0xb2"), parseInt("0xcd"), parseInt("0x4c"), parseInt("0xd4"), parseInt("0x67"),
    parseInt("0xa9"), parseInt("0xe0"), parseInt("0x3b"), parseInt("0x4d"), parseInt("0xd7"), parseInt("0x62"), parseInt("0xa6"),
    parseInt("0xf1"), parseInt("0x08"), parseInt("0x18"), parseInt("0x28"), parseInt("0x78"), parseInt("0x88"), parseInt("0x83"),
    parseInt("0x9e"), parseInt("0xb9"), parseInt("0xd0"), parseInt("0x6b"), parseInt("0xbd"), parseInt("0xdc"), parseInt("0x7f"),
    parseInt("0x81"), parseInt("0x98"), parseInt("0xb3"), parseInt("0xce"), parseInt("0x49"), parseInt("0xdb"), parseInt("0x76"),
    parseInt("0x9a"), parseInt("0xb5"), parseInt("0xc4"), parseInt("0x57"), parseInt("0xf9"), parseInt("0x10"), parseInt("0x30"),
    parseInt("0x50"), parseInt("0xf0"), parseInt("0x0b"), parseInt("0x1d"), parseInt("0x27"), parseInt("0x69"), parseInt("0xbb"),
    parseInt("0xd6"), parseInt("0x61"), parseInt("0xa3"), parseInt("0xfe"), parseInt("0x19"), parseInt("0x2b"), parseInt("0x7d"),
    parseInt("0x87"), parseInt("0x92"), parseInt("0xad"), parseInt("0xec"), parseInt("0x2f"), parseInt("0x71"), parseInt("0x93"),
    parseInt("0xae"), parseInt("0xe9"), parseInt("0x20"), parseInt("0x60"), parseInt("0xa0"), parseInt("0xfb"), parseInt("0x16"),
    parseInt("0x3a"), parseInt("0x4e"), parseInt("0xd2"), parseInt("0x6d"), parseInt("0xb7"), parseInt("0xc2"), parseInt("0x5d"),
    parseInt("0xe7"), parseInt("0x32"), parseInt("0x56"), parseInt("0xfa"), parseInt("0x15"), parseInt("0x3f"), parseInt("0x41"),
    parseInt("0xc3"), parseInt("0x5e"), parseInt("0xe2"), parseInt("0x3d"), parseInt("0x47"), parseInt("0xc9"), parseInt("0x40"),
    parseInt("0xc0"), parseInt("0x5b"), parseInt("0xed"), parseInt("0x2c"), parseInt("0x74"), parseInt("0x9c"), parseInt("0xbf"),
    parseInt("0xda"), parseInt("0x75"), parseInt("0x9f"), parseInt("0xba"), parseInt("0xd5"), parseInt("0x64"), parseInt("0xac"),
    parseInt("0xef"), parseInt("0x2a"), parseInt("0x7e"), parseInt("0x82"), parseInt("0x9d"), parseInt("0xbc"), parseInt("0xdf"),
    parseInt("0x7a"), parseInt("0x8e"), parseInt("0x89"), parseInt("0x80"), parseInt("0x9b"), parseInt("0xb6"), parseInt("0xc1"),
    parseInt("0x58"), parseInt("0xe8"), parseInt("0x23"), parseInt("0x65"), parseInt("0xaf"), parseInt("0xea"), parseInt("0x25"),
    parseInt("0x6f"), parseInt("0xb1"), parseInt("0xc8"), parseInt("0x43"), parseInt("0xc5"), parseInt("0x54"), parseInt("0xfc"),
    parseInt("0x1f"), parseInt("0x21"), parseInt("0x63"), parseInt("0xa5"), parseInt("0xf4"), parseInt("0x07"), parseInt("0x09"),
    parseInt("0x1b"), parseInt("0x2d"), parseInt("0x77"), parseInt("0x99"), parseInt("0xb0"), parseInt("0xcb"), parseInt("0x46"),
    parseInt("0xca"), parseInt("0x45"), parseInt("0xcf"), parseInt("0x4a"), parseInt("0xde"), parseInt("0x79"), parseInt("0x8b"),
    parseInt("0x86"), parseInt("0x91"), parseInt("0xa8"), parseInt("0xe3"), parseInt("0x3e"), parseInt("0x42"), parseInt("0xc6"),
    parseInt("0x51"), parseInt("0xf3"), parseInt("0x0e"), parseInt("0x12"), parseInt("0x36"), parseInt("0x5a"), parseInt("0xee"),
    parseInt("0x29"), parseInt("0x7b"), parseInt("0x8d"), parseInt("0x8c"), parseInt("0x8f"), parseInt("0x8a"), parseInt("0x85"),
    parseInt("0x94"), parseInt("0xa7"), parseInt("0xf2"), parseInt("0x0d"), parseInt("0x17"), parseInt("0x39"), parseInt("0x4b"),
    parseInt("0xdd"), parseInt("0x7c"), parseInt("0x84"), parseInt("0x97"), parseInt("0xa2"), parseInt("0xfd"), parseInt("0x1c"),
    parseInt("0x24"), parseInt("0x6c"), parseInt("0xb4"), parseInt("0xc7"), parseInt("0x52"), parseInt("0xf6"),
  ];

const mul = function(a, b) {
    if (a == 0 || b == 0) {
        return 0;
    }
    return EXP[LOG[a] + LOG[b]];
};

exports.mul = mul;

const div = function(a, b) {
    // multiply by the inverse of b
    return mul(a, EXP[255 - LOG[b]]);
};

exports.div = div;

const degree = function(p) {
    for (var i = p.length - 1; i >= 1; i--) {
      if (p[i] != 0) {
        return i;
      }
    }
    return 0;
};

exports.degree = degree;

/**
 * Calculates f(0) of the given points using Lagrangian interpolation. 
 * @param  {array[Uint8Array]} points The supplied point. 
 */
exports.interpolate = function(points) {
    const x = 0;
    var y = 0;
    for (var i = 0; i < points.length; i++) {
      const aX = points[i][0];
      const aY = points[i][1];
      var li = 1;
      for (var j = 0; j < points.length; j++) {
        const bX = points[j][0];
        if (i != j) {
          li = mul(li, div(sub(x, bX), sub(aX, bX)));
        }
      }
      y = add(y, mul(li, aY));
    }
    return y;
  }

/**
 * Generates a random polynomal of the correct degree and sets x as the first coefficient.  
 * @param  {function int -> array[Uint8Array]} randomBytes Takes a length and returns a Uint8Array of that length.
 * @param  {Number} d The degree of the polynomial driven by the number shares and join threshold. 
 * @param {Number} x The point to hide.
 * @return {Uint8Array} The random polynomial with x as the fist coefficient.
 */
exports.generate = function(randomBytes, d, x){
    var p = null;
    // generate random polynomials until we find one of the given degree
    do {
      p = randomBytes(d + 1);
    } while (degree(p) != d);

    // set y intercept
    p[0] = x;

    return p;
  }


/**
 * Evaluates a polynomal at point x using Horner's method.  
 * @param  {Uint8Array} p The polynomial
 * @return {Number} x The point to evaluate.
 */
exports.eval = function(p, x){
    var result = 0
    for( var i = p.length -1; i >=0; i--){
        result = add(mul(result, x), p[i]);
    }
    return result;
  }
