
exports.add = function(a, b) {
        console.log('From GF256 add function');
        return a ^ b;
    };

/* The Laws of Cryptograhy with Java Code by Neal R. Wagner
Page 120 (134) section "20.3 Addition in GP(2^n)" is equal 
to subtraction. 
*/
exports.sub = exports.add;