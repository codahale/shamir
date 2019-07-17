const { split, join } = require('../../main/js/Scheme.js');

const { randomBytes } = require('crypto');

const secret = new Uint8Array(16);

for (let i = 0; i < secret.length; i++) {
    secret[i] = i % 255;
}

const mainrun = 200;

const parts = 4;
const quorum = 3;

var splits = split(randomBytes, parts, quorum, secret);

function benchmarkSplit() {
  for( let i = 0; i < mainrun; i++ ) {
    splits = split(randomBytes, parts, quorum, secret);
  }
}

// https://nodejs.org/docs/latest-v10.x/api/perf_hooks.html#perf_hooks_performance_now
const {
  performance,
  PerformanceObserver
} = require('perf_hooks');
const wrappedSplit = performance.timerify(benchmarkSplit);
const obsSplit = new PerformanceObserver((list) => {
  const timed = list.getEntries()[0].duration / mainrun;
  console.log(`split ${timed} ms`);
  obsSplit.disconnect();
});
obsSplit.observe({ entryTypes: ['function'] });
wrappedSplit();

var recovered = join(splits);

function benchmarkJoin() {
  for( let i = 0; i < mainrun; i++ ) {
    recovered = join(splits);
  }
}

const wrappedJoin = performance.timerify(benchmarkJoin);
const obsJoin = new PerformanceObserver((list) => {
  const timed = list.getEntries()[0].duration / mainrun
  console.log(`join ${timed} ms`);
  obsJoin.disconnect();
});
obsJoin.observe({ entryTypes: ['function'] });
wrappedJoin();
