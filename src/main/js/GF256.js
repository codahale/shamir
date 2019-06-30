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

exports.sub = exports.add;

const LOG = new Uint8Array([
    parseInt('0xff', 16), parseInt('0x00', 16), parseInt('0x19', 16), parseInt('0x01', 16), parseInt('0x32', 16), parseInt('0x02', 16), parseInt('0x1a', 16),
    parseInt('0xc6', 16), parseInt('0x4b', 16), parseInt('0xc7', 16), parseInt('0x1b', 16), parseInt('0x68', 16), parseInt('0x33', 16), parseInt('0xee', 16),
    parseInt('0xdf', 16), parseInt('0x03', 16), parseInt('0x64', 16), parseInt('0x04', 16), parseInt('0xe0', 16), parseInt('0x0e', 16), parseInt('0x34', 16),
    parseInt('0x8d', 16), parseInt('0x81', 16), parseInt('0xef', 16), parseInt('0x4c', 16), parseInt('0x71', 16), parseInt('0x08', 16), parseInt('0xc8', 16),
    parseInt('0xf8', 16), parseInt('0x69', 16), parseInt('0x1c', 16), parseInt('0xc1', 16), parseInt('0x7d', 16), parseInt('0xc2', 16), parseInt('0x1d', 16),
    parseInt('0xb5', 16), parseInt('0xf9', 16), parseInt('0xb9', 16), parseInt('0x27', 16), parseInt('0x6a', 16), parseInt('0x4d', 16), parseInt('0xe4', 16),
    parseInt('0xa6', 16), parseInt('0x72', 16), parseInt('0x9a', 16), parseInt('0xc9', 16), parseInt('0x09', 16), parseInt('0x78', 16), parseInt('0x65', 16),
    parseInt('0x2f', 16), parseInt('0x8a', 16), parseInt('0x05', 16), parseInt('0x21', 16), parseInt('0x0f', 16), parseInt('0xe1', 16), parseInt('0x24', 16),
    parseInt('0x12', 16), parseInt('0xf0', 16), parseInt('0x82', 16), parseInt('0x45', 16), parseInt('0x35', 16), parseInt('0x93', 16), parseInt('0xda', 16),
    parseInt('0x8e', 16), parseInt('0x96', 16), parseInt('0x8f', 16), parseInt('0xdb', 16), parseInt('0xbd', 16), parseInt('0x36', 16), parseInt('0xd0', 16),
    parseInt('0xce', 16), parseInt('0x94', 16), parseInt('0x13', 16), parseInt('0x5c', 16), parseInt('0xd2', 16), parseInt('0xf1', 16), parseInt('0x40', 16),
    parseInt('0x46', 16), parseInt('0x83', 16), parseInt('0x38', 16), parseInt('0x66', 16), parseInt('0xdd', 16), parseInt('0xfd', 16), parseInt('0x30', 16),
    parseInt('0xbf', 16), parseInt('0x06', 16), parseInt('0x8b', 16), parseInt('0x62', 16), parseInt('0xb3', 16), parseInt('0x25', 16), parseInt('0xe2', 16),
    parseInt('0x98', 16), parseInt('0x22', 16), parseInt('0x88', 16), parseInt('0x91', 16), parseInt('0x10', 16), parseInt('0x7e', 16), parseInt('0x6e', 16),
    parseInt('0x48', 16), parseInt('0xc3', 16), parseInt('0xa3', 16), parseInt('0xb6', 16), parseInt('0x1e', 16), parseInt('0x42', 16), parseInt('0x3a', 16),
    parseInt('0x6b', 16), parseInt('0x28', 16), parseInt('0x54', 16), parseInt('0xfa', 16), parseInt('0x85', 16), parseInt('0x3d', 16), parseInt('0xba', 16),
    parseInt('0x2b', 16), parseInt('0x79', 16), parseInt('0x0a', 16), parseInt('0x15', 16), parseInt('0x9b', 16), parseInt('0x9f', 16), parseInt('0x5e', 16),
    parseInt('0xca', 16), parseInt('0x4e', 16), parseInt('0xd4', 16), parseInt('0xac', 16), parseInt('0xe5', 16), parseInt('0xf3', 16), parseInt('0x73', 16),
    parseInt('0xa7', 16), parseInt('0x57', 16), parseInt('0xaf', 16), parseInt('0x58', 16), parseInt('0xa8', 16), parseInt('0x50', 16), parseInt('0xf4', 16),
    parseInt('0xea', 16), parseInt('0xd6', 16), parseInt('0x74', 16), parseInt('0x4f', 16), parseInt('0xae', 16), parseInt('0xe9', 16), parseInt('0xd5', 16),
    parseInt('0xe7', 16), parseInt('0xe6', 16), parseInt('0xad', 16), parseInt('0xe8', 16), parseInt('0x2c', 16), parseInt('0xd7', 16), parseInt('0x75', 16),
    parseInt('0x7a', 16), parseInt('0xeb', 16), parseInt('0x16', 16), parseInt('0x0b', 16), parseInt('0xf5', 16), parseInt('0x59', 16), parseInt('0xcb', 16),
    parseInt('0x5f', 16), parseInt('0xb0', 16), parseInt('0x9c', 16), parseInt('0xa9', 16), parseInt('0x51', 16), parseInt('0xa0', 16), parseInt('0x7f', 16),
    parseInt('0x0c', 16), parseInt('0xf6', 16), parseInt('0x6f', 16), parseInt('0x17', 16), parseInt('0xc4', 16), parseInt('0x49', 16), parseInt('0xec', 16),
    parseInt('0xd8', 16), parseInt('0x43', 16), parseInt('0x1f', 16), parseInt('0x2d', 16), parseInt('0xa4', 16), parseInt('0x76', 16), parseInt('0x7b', 16),
    parseInt('0xb7', 16), parseInt('0xcc', 16), parseInt('0xbb', 16), parseInt('0x3e', 16), parseInt('0x5a', 16), parseInt('0xfb', 16), parseInt('0x60', 16),
    parseInt('0xb1', 16), parseInt('0x86', 16), parseInt('0x3b', 16), parseInt('0x52', 16), parseInt('0xa1', 16), parseInt('0x6c', 16), parseInt('0xaa', 16),
    parseInt('0x55', 16), parseInt('0x29', 16), parseInt('0x9d', 16), parseInt('0x97', 16), parseInt('0xb2', 16), parseInt('0x87', 16), parseInt('0x90', 16),
    parseInt('0x61', 16), parseInt('0xbe', 16), parseInt('0xdc', 16), parseInt('0xfc', 16), parseInt('0xbc', 16), parseInt('0x95', 16), parseInt('0xcf', 16),
    parseInt('0xcd', 16), parseInt('0x37', 16), parseInt('0x3f', 16), parseInt('0x5b', 16), parseInt('0xd1', 16), parseInt('0x53', 16), parseInt('0x39', 16),
    parseInt('0x84', 16), parseInt('0x3c', 16), parseInt('0x41', 16), parseInt('0xa2', 16), parseInt('0x6d', 16), parseInt('0x47', 16), parseInt('0x14', 16),
    parseInt('0x2a', 16), parseInt('0x9e', 16), parseInt('0x5d', 16), parseInt('0x56', 16), parseInt('0xf2', 16), parseInt('0xd3', 16), parseInt('0xab', 16),
    parseInt('0x44', 16), parseInt('0x11', 16), parseInt('0x92', 16), parseInt('0xd9', 16), parseInt('0x23', 16), parseInt('0x20', 16), parseInt('0x2e', 16),
    parseInt('0x89', 16), parseInt('0xb4', 16), parseInt('0x7c', 16), parseInt('0xb8', 16), parseInt('0x26', 16), parseInt('0x77', 16), parseInt('0x99', 16),
    parseInt('0xe3', 16), parseInt('0xa5', 16), parseInt('0x67', 16), parseInt('0x4a', 16), parseInt('0xed', 16), parseInt('0xde', 16), parseInt('0xc5', 16),
    parseInt('0x31', 16), parseInt('0xfe', 16), parseInt('0x18', 16), parseInt('0x0d', 16), parseInt('0x63', 16), parseInt('0x8c', 16), parseInt('0x80', 16),
    parseInt('0xc0', 16), parseInt('0xf7', 16), parseInt('0x70', 16), parseInt('0x07', 16),
  ]);

/* https://crypto.stackexchange.com/a/21174/13860 
*/
const EXP = new Uint8Array([
    parseInt('0x01', 16), parseInt('0x03', 16), parseInt('0x05', 16), parseInt('0x0f', 16), parseInt('0x11', 16), parseInt('0x33', 16), parseInt('0x55', 16),
    parseInt('0xff', 16), parseInt('0x1a', 16), parseInt('0x2e', 16), parseInt('0x72', 16), parseInt('0x96', 16), parseInt('0xa1', 16), parseInt('0xf8', 16),
    parseInt('0x13', 16), parseInt('0x35', 16), parseInt('0x5f', 16), parseInt('0xe1', 16), parseInt('0x38', 16), parseInt('0x48', 16), parseInt('0xd8', 16),
    parseInt('0x73', 16), parseInt('0x95', 16), parseInt('0xa4', 16), parseInt('0xf7', 16), parseInt('0x02', 16), parseInt('0x06', 16), parseInt('0x0a', 16),
    parseInt('0x1e', 16), parseInt('0x22', 16), parseInt('0x66', 16), parseInt('0xaa', 16), parseInt('0xe5', 16), parseInt('0x34', 16), parseInt('0x5c', 16),
    parseInt('0xe4', 16), parseInt('0x37', 16), parseInt('0x59', 16), parseInt('0xeb', 16), parseInt('0x26', 16), parseInt('0x6a', 16), parseInt('0xbe', 16),
    parseInt('0xd9', 16), parseInt('0x70', 16), parseInt('0x90', 16), parseInt('0xab', 16), parseInt('0xe6', 16), parseInt('0x31', 16), parseInt('0x53', 16),
    parseInt('0xf5', 16), parseInt('0x04', 16), parseInt('0x0c', 16), parseInt('0x14', 16), parseInt('0x3c', 16), parseInt('0x44', 16), parseInt('0xcc', 16),
    parseInt('0x4f', 16), parseInt('0xd1', 16), parseInt('0x68', 16), parseInt('0xb8', 16), parseInt('0xd3', 16), parseInt('0x6e', 16), parseInt('0xb2', 16),
    parseInt('0xcd', 16), parseInt('0x4c', 16), parseInt('0xd4', 16), parseInt('0x67', 16), parseInt('0xa9', 16), parseInt('0xe0', 16), parseInt('0x3b', 16),
    parseInt('0x4d', 16), parseInt('0xd7', 16), parseInt('0x62', 16), parseInt('0xa6', 16), parseInt('0xf1', 16), parseInt('0x08', 16), parseInt('0x18', 16),
    parseInt('0x28', 16), parseInt('0x78', 16), parseInt('0x88', 16), parseInt('0x83', 16), parseInt('0x9e', 16), parseInt('0xb9', 16), parseInt('0xd0', 16),
    parseInt('0x6b', 16), parseInt('0xbd', 16), parseInt('0xdc', 16), parseInt('0x7f', 16), parseInt('0x81', 16), parseInt('0x98', 16), parseInt('0xb3', 16),
    parseInt('0xce', 16), parseInt('0x49', 16), parseInt('0xdb', 16), parseInt('0x76', 16), parseInt('0x9a', 16), parseInt('0xb5', 16), parseInt('0xc4', 16),
    parseInt('0x57', 16), parseInt('0xf9', 16), parseInt('0x10', 16), parseInt('0x30', 16), parseInt('0x50', 16), parseInt('0xf0', 16), parseInt('0x0b', 16),
    parseInt('0x1d', 16), parseInt('0x27', 16), parseInt('0x69', 16), parseInt('0xbb', 16), parseInt('0xd6', 16), parseInt('0x61', 16), parseInt('0xa3', 16),
    parseInt('0xfe', 16), parseInt('0x19', 16), parseInt('0x2b', 16), parseInt('0x7d', 16), parseInt('0x87', 16), parseInt('0x92', 16), parseInt('0xad', 16),
    parseInt('0xec', 16), parseInt('0x2f', 16), parseInt('0x71', 16), parseInt('0x93', 16), parseInt('0xae', 16), parseInt('0xe9', 16), parseInt('0x20', 16),
    parseInt('0x60', 16), parseInt('0xa0', 16), parseInt('0xfb', 16), parseInt('0x16', 16), parseInt('0x3a', 16), parseInt('0x4e', 16), parseInt('0xd2', 16),
    parseInt('0x6d', 16), parseInt('0xb7', 16), parseInt('0xc2', 16), parseInt('0x5d', 16), parseInt('0xe7', 16), parseInt('0x32', 16), parseInt('0x56', 16),
    parseInt('0xfa', 16), parseInt('0x15', 16), parseInt('0x3f', 16), parseInt('0x41', 16), parseInt('0xc3', 16), parseInt('0x5e', 16), parseInt('0xe2', 16),
    parseInt('0x3d', 16), parseInt('0x47', 16), parseInt('0xc9', 16), parseInt('0x40', 16), parseInt('0xc0', 16), parseInt('0x5b', 16), parseInt('0xed', 16),
    parseInt('0x2c', 16), parseInt('0x74', 16), parseInt('0x9c', 16), parseInt('0xbf', 16), parseInt('0xda', 16), parseInt('0x75', 16), parseInt('0x9f', 16),
    parseInt('0xba', 16), parseInt('0xd5', 16), parseInt('0x64', 16), parseInt('0xac', 16), parseInt('0xef', 16), parseInt('0x2a', 16), parseInt('0x7e', 16),
    parseInt('0x82', 16), parseInt('0x9d', 16), parseInt('0xbc', 16), parseInt('0xdf', 16), parseInt('0x7a', 16), parseInt('0x8e', 16), parseInt('0x89', 16),
    parseInt('0x80', 16), parseInt('0x9b', 16), parseInt('0xb6', 16), parseInt('0xc1', 16), parseInt('0x58', 16), parseInt('0xe8', 16), parseInt('0x23', 16),
    parseInt('0x65', 16), parseInt('0xaf', 16), parseInt('0xea', 16), parseInt('0x25', 16), parseInt('0x6f', 16), parseInt('0xb1', 16), parseInt('0xc8', 16),
    parseInt('0x43', 16), parseInt('0xc5', 16), parseInt('0x54', 16), parseInt('0xfc', 16), parseInt('0x1f', 16), parseInt('0x21', 16), parseInt('0x63', 16),
    parseInt('0xa5', 16), parseInt('0xf4', 16), parseInt('0x07', 16), parseInt('0x09', 16), parseInt('0x1b', 16), parseInt('0x2d', 16), parseInt('0x77', 16),
    parseInt('0x99', 16), parseInt('0xb0', 16), parseInt('0xcb', 16), parseInt('0x46', 16), parseInt('0xca', 16), parseInt('0x45', 16), parseInt('0xcf', 16),
    parseInt('0x4a', 16), parseInt('0xde', 16), parseInt('0x79', 16), parseInt('0x8b', 16), parseInt('0x86', 16), parseInt('0x91', 16), parseInt('0xa8', 16),
    parseInt('0xe3', 16), parseInt('0x3e', 16), parseInt('0x42', 16), parseInt('0xc6', 16), parseInt('0x51', 16), parseInt('0xf3', 16), parseInt('0x0e', 16),
    parseInt('0x12', 16), parseInt('0x36', 16), parseInt('0x5a', 16), parseInt('0xee', 16), parseInt('0x29', 16), parseInt('0x7b', 16), parseInt('0x8d', 16),
    parseInt('0x8c', 16), parseInt('0x8f', 16), parseInt('0x8a', 16), parseInt('0x85', 16), parseInt('0x94', 16), parseInt('0xa7', 16), parseInt('0xf2', 16),
    parseInt('0x0d', 16), parseInt('0x17', 16), parseInt('0x39', 16), parseInt('0x4b', 16), parseInt('0xdd', 16), parseInt('0x7c', 16), parseInt('0x84', 16),
    parseInt('0x97', 16), parseInt('0xa2', 16), parseInt('0xfd', 16), parseInt('0x1c', 16), parseInt('0x24', 16), parseInt('0x6c', 16), parseInt('0xb4', 16),
    parseInt('0xc7', 16), parseInt('0x52', 16), parseInt('0xf6', 16), parseInt('0x01', 16), parseInt('0x03', 16), parseInt('0x05', 16), parseInt('0x0f', 16),
    parseInt('0x11', 16), parseInt('0x33', 16), parseInt('0x55', 16), parseInt('0xff', 16), parseInt('0x1a', 16), parseInt('0x2e', 16), parseInt('0x72', 16),
    parseInt('0x96', 16), parseInt('0xa1', 16), parseInt('0xf8', 16), parseInt('0x13', 16), parseInt('0x35', 16), parseInt('0x5f', 16), parseInt('0xe1', 16),
    parseInt('0x38', 16), parseInt('0x48', 16), parseInt('0xd8', 16), parseInt('0x73', 16), parseInt('0x95', 16), parseInt('0xa4', 16), parseInt('0xf7', 16),
    parseInt('0x02', 16), parseInt('0x06', 16), parseInt('0x0a', 16), parseInt('0x1e', 16), parseInt('0x22', 16), parseInt('0x66', 16), parseInt('0xaa', 16),
    parseInt('0xe5', 16), parseInt('0x34', 16), parseInt('0x5c', 16), parseInt('0xe4', 16), parseInt('0x37', 16), parseInt('0x59', 16), parseInt('0xeb', 16),
    parseInt('0x26', 16), parseInt('0x6a', 16), parseInt('0xbe', 16), parseInt('0xd9', 16), parseInt('0x70', 16), parseInt('0x90', 16), parseInt('0xab', 16),
    parseInt('0xe6', 16), parseInt('0x31', 16), parseInt('0x53', 16), parseInt('0xf5', 16), parseInt('0x04', 16), parseInt('0x0c', 16), parseInt('0x14', 16),
    parseInt('0x3c', 16), parseInt('0x44', 16), parseInt('0xcc', 16), parseInt('0x4f', 16), parseInt('0xd1', 16), parseInt('0x68', 16), parseInt('0xb8', 16),
    parseInt('0xd3', 16), parseInt('0x6e', 16), parseInt('0xb2', 16), parseInt('0xcd', 16), parseInt('0x4c', 16), parseInt('0xd4', 16), parseInt('0x67', 16),
    parseInt('0xa9', 16), parseInt('0xe0', 16), parseInt('0x3b', 16), parseInt('0x4d', 16), parseInt('0xd7', 16), parseInt('0x62', 16), parseInt('0xa6', 16),
    parseInt('0xf1', 16), parseInt('0x08', 16), parseInt('0x18', 16), parseInt('0x28', 16), parseInt('0x78', 16), parseInt('0x88', 16), parseInt('0x83', 16),
    parseInt('0x9e', 16), parseInt('0xb9', 16), parseInt('0xd0', 16), parseInt('0x6b', 16), parseInt('0xbd', 16), parseInt('0xdc', 16), parseInt('0x7f', 16),
    parseInt('0x81', 16), parseInt('0x98', 16), parseInt('0xb3', 16), parseInt('0xce', 16), parseInt('0x49', 16), parseInt('0xdb', 16), parseInt('0x76', 16),
    parseInt('0x9a', 16), parseInt('0xb5', 16), parseInt('0xc4', 16), parseInt('0x57', 16), parseInt('0xf9', 16), parseInt('0x10', 16), parseInt('0x30', 16),
    parseInt('0x50', 16), parseInt('0xf0', 16), parseInt('0x0b', 16), parseInt('0x1d', 16), parseInt('0x27', 16), parseInt('0x69', 16), parseInt('0xbb', 16),
    parseInt('0xd6', 16), parseInt('0x61', 16), parseInt('0xa3', 16), parseInt('0xfe', 16), parseInt('0x19', 16), parseInt('0x2b', 16), parseInt('0x7d', 16),
    parseInt('0x87', 16), parseInt('0x92', 16), parseInt('0xad', 16), parseInt('0xec', 16), parseInt('0x2f', 16), parseInt('0x71', 16), parseInt('0x93', 16),
    parseInt('0xae', 16), parseInt('0xe9', 16), parseInt('0x20', 16), parseInt('0x60', 16), parseInt('0xa0', 16), parseInt('0xfb', 16), parseInt('0x16', 16),
    parseInt('0x3a', 16), parseInt('0x4e', 16), parseInt('0xd2', 16), parseInt('0x6d', 16), parseInt('0xb7', 16), parseInt('0xc2', 16), parseInt('0x5d', 16),
    parseInt('0xe7', 16), parseInt('0x32', 16), parseInt('0x56', 16), parseInt('0xfa', 16), parseInt('0x15', 16), parseInt('0x3f', 16), parseInt('0x41', 16),
    parseInt('0xc3', 16), parseInt('0x5e', 16), parseInt('0xe2', 16), parseInt('0x3d', 16), parseInt('0x47', 16), parseInt('0xc9', 16), parseInt('0x40', 16),
    parseInt('0xc0', 16), parseInt('0x5b', 16), parseInt('0xed', 16), parseInt('0x2c', 16), parseInt('0x74', 16), parseInt('0x9c', 16), parseInt('0xbf', 16),
    parseInt('0xda', 16), parseInt('0x75', 16), parseInt('0x9f', 16), parseInt('0xba', 16), parseInt('0xd5', 16), parseInt('0x64', 16), parseInt('0xac', 16),
    parseInt('0xef', 16), parseInt('0x2a', 16), parseInt('0x7e', 16), parseInt('0x82', 16), parseInt('0x9d', 16), parseInt('0xbc', 16), parseInt('0xdf', 16),
    parseInt('0x7a', 16), parseInt('0x8e', 16), parseInt('0x89', 16), parseInt('0x80', 16), parseInt('0x9b', 16), parseInt('0xb6', 16), parseInt('0xc1', 16),
    parseInt('0x58', 16), parseInt('0xe8', 16), parseInt('0x23', 16), parseInt('0x65', 16), parseInt('0xaf', 16), parseInt('0xea', 16), parseInt('0x25', 16),
    parseInt('0x6f', 16), parseInt('0xb1', 16), parseInt('0xc8', 16), parseInt('0x43', 16), parseInt('0xc5', 16), parseInt('0x54', 16), parseInt('0xfc', 16),
    parseInt('0x1f', 16), parseInt('0x21', 16), parseInt('0x63', 16), parseInt('0xa5', 16), parseInt('0xf4', 16), parseInt('0x07', 16), parseInt('0x09', 16),
    parseInt('0x1b', 16), parseInt('0x2d', 16), parseInt('0x77', 16), parseInt('0x99', 16), parseInt('0xb0', 16), parseInt('0xcb', 16), parseInt('0x46', 16),
    parseInt('0xca', 16), parseInt('0x45', 16), parseInt('0xcf', 16), parseInt('0x4a', 16), parseInt('0xde', 16), parseInt('0x79', 16), parseInt('0x8b', 16),
    parseInt('0x86', 16), parseInt('0x91', 16), parseInt('0xa8', 16), parseInt('0xe3', 16), parseInt('0x3e', 16), parseInt('0x42', 16), parseInt('0xc6', 16),
    parseInt('0x51', 16), parseInt('0xf3', 16), parseInt('0x0e', 16), parseInt('0x12', 16), parseInt('0x36', 16), parseInt('0x5a', 16), parseInt('0xee', 16),
    parseInt('0x29', 16), parseInt('0x7b', 16), parseInt('0x8d', 16), parseInt('0x8c', 16), parseInt('0x8f', 16), parseInt('0x8a', 16), parseInt('0x85', 16),
    parseInt('0x94', 16), parseInt('0xa7', 16), parseInt('0xf2', 16), parseInt('0x0d', 16), parseInt('0x17', 16), parseInt('0x39', 16), parseInt('0x4b', 16),
    parseInt('0xdd', 16), parseInt('0x7c', 16), parseInt('0x84', 16), parseInt('0x97', 16), parseInt('0xa2', 16), parseInt('0xfd', 16), parseInt('0x1c', 16),
    parseInt('0x24', 16), parseInt('0x6c', 16), parseInt('0xb4', 16), parseInt('0xc7', 16), parseInt('0x52', 16), parseInt('0xf6', 16),
  ]);

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
  for (let i = p.length - 1; i >= 1; i--) {
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
  let y = 0;
  for (let i = 0; i < points.length; i++) {
    const aX = points[i][0];
    const aY = points[i][1];
    let li = 1;
    for (let j = 0; j < points.length; j++) {
      const bX = points[j][0];
      if (i != j) {
        li = mul(li, div(sub(x, bX), sub(aX, bX)));
      }
    }
    y = add(y, mul(li, aY));
  }
  return y;
};

/**
 * Generates a random polynomal of the correct degree and sets x as the first coefficient.
 * @param  {function int -> array[Uint8Array]} randomBytes Takes a length and returns a Uint8Array of that length.
 * @param  {Number} d The degree of the polynomial driven by the number shares and join threshold.
 * @param {Number} x The point to hide.
 * @return {Uint8Array} The random polynomial with x as the fist coefficient.
 */
exports.generate = function(randomBytes, d, x) {
  let p = null;
  // generate random polynomials until we find one of the given degree
  do {
    p = randomBytes(d + 1);
  } while (degree(p) != d);

  // set y intercept
  p[0] = x;

  return p;
};

/**
 * Evaluates a polynomal at point x using Horner's method.
 * @param  {Uint8Array} p The polynomial
 * @return {Number} x The point to evaluate.
 */
exports.eval = function(p, x) {
  let result = 0;
  for (let i = p.length - 1; i >= 0; i--) {
    result = add(mul(result, x), p[i]);
  }
  return result;
};
