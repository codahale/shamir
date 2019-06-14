var test = require('tape');

const GF256 = require('../../main/js/GF256.js');

test('GF256Tests add', function (t) {
    t.plan(1);
    t.equal( GF256.add(100, 30), 122 );
});

test('GF256Tests sub', function (t) {
    t.plan(1);
    t.equal( GF256.sub(100, 30), 122 );
});

test('GF256Tests mul', function (t) {
    t.plan(4);
    t.equal( GF256.mul(90, 21), 254 );
    t.equal( GF256.mul(133, 5), 167 );
    t.equal( GF256.mul(0, 21), 0 );
    t.equal( GF256.mul(parseInt("0xb6"), parseInt("0x53")), parseInt("0x36") );
});

test('GF256Tests div', function (t) {
    t.plan(4);
    t.equal( GF256.div(90, 21), 189 );
    t.equal( GF256.div(6, 55), 151 );
    t.equal( GF256.div(22, 192), 138 );
    t.equal( GF256.div(0, 192), 0 );
});



// const JavaPI = Java.type('java.lang.Math').PI;

// test('PI test', function (t) {
//     t.plan(1);
//     t.equal( JavaPI.toString(), "3.141592653589793" );
// });
