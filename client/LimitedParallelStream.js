'use strict';

const stream = require('stream');

/**
 * LimitedParallelStream wraps asynchronous operations,and
 * allows you to specify how many of those operations can
 * run concurrently.
 *
 * Your async operations should be defined in a function,
 * and passed as the userTransform argument in the
 * constructor.
 *
 * Your function should have the signature:
 *
 *   f(chunk, enc, push, done);
 *
 * In your function, call push(chunk) to pass your chunk down
 * the stream when you're finished with it. Call done() when
 * you're finished with your async task.
 *
 * For an example implementation of a LimitedParallelStream function,see:
 * https://github.com/rfcode/IntegrationServices/blob/master/lib/updater.js
 *
 * So how's it work?
 *
 * LimitedParallelStream is simpler than it looks.
 *
 * All node streams process one chunk, or in this case,
 * object at a time. Writable streams, and Transform
 * streams are Writable, will not process the next
 * chunk until their callback is executed. That callback
 * is defined as "done" below.
 *
 * In the _transform method below we take advantage of
 * this fact to enable throttling and concurrency. Remember,
 * async tasks, like network calls to the CenterScape API,
 * run concurrently, there's no blocking. So each time that
 * LimitedParallelStream executes your function, the async
 * code in the function will return immediately and we can kick
 * off another instance of your function immediately.
 *
 * The number of async functions that we can execute at one time
 * is specified by the concurrency argument in the constructor.
 *
 * In _transform we check to see whether we're executing fewer
 * of your async tasks than 'concurrency' allows. If we are, then
 * we execute it and call "done" immediately so that the next chunk
 * can come in.
 *
 * If we're full, we do NOT call done. We store it on the stream,
 * and we rely on YOUR function to call done, so that the next
 * chunk can come in and processing can continue.
 *
 * Note that when we set this.continueCallback, all streaming stops
 * until one of your pending async tasks calls done.
 */

module.exports = function(options) {

    let concurrency = options.concurrency || 1;
    let userTransform = options.transform;
    let running = 0;
    let terminateCallback = null;
    let continueCallback = null;

    function _onComplete(err) {
        running--;
        if (err) {
            throw err;
        }
        const tmpCallback = continueCallback;
        continueCallback = null;
        tmpCallback && tmpCallback();
        if (running === 0) {
            terminateCallback && terminateCallback();
        }
    }

    let str = new stream.Transform({
        objectMode: true,
        transform(payload, enc, done) {
            running++;
            userTransform(payload, this.push.bind(this), _onComplete.bind(this));
            if (running < concurrency) {
                done();
            } else {
                continueCallback = done;
            }
        },
        flush(done) {
            if (running > 0) {
                terminateCallback = done;
            } else {
                done();
            }
        }
    });

    str.currentRunCount = function() {
        return running;
    };

    userTransform = userTransform.bind(str);
    return str;

};
