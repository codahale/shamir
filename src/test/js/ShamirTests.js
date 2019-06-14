var test = require('tape');

const GF256 = require('../../main/js/GF256.js');

test('GF256Tests add', function (t) {
    t.plan(1);

    console.log(GF256);

    t.equal( GF256.add(100, 30), 122 );
});

test('GF256Tests sub', function (t) {
    t.plan(1);

    console.log(GF256);

    t.equal( GF256.sub(100, 30), 122 );
});



// const JavaPI = Java.type('java.lang.Math').PI;

// test('PI test', function (t) {
//     t.plan(1);
//     t.equal( JavaPI.toString(), "3.141592653589793" );
// });
