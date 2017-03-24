const fs = require('fs');
const JSONStream = require('JSONStream');
const rp = require('request-promise');
const parExecutor = require('./LimitedParallelStream');

const getStream = () => {
    const stream = fs.createReadStream('cities.json', {encoding: 'utf8'});
    const parser = JSONStream.parse('*');
    return stream.pipe(parser);
};

const sendMsg = (data, push, done) => {
    const getObjectOpts = (id) => {
        return {
            "method": "GET",
            "url": `http://localhost:8080/capi/object/${id}`
        }
    };

    const country = data.country;
    const city = data.name;
    const lat = data.lat;
    const lng = data.lng;

    const countryOpts = {
        method: 'POST',
        uri: 'http://localhost:8080/capi/object',
        headers: {
            "Content-Type": "application/json"
        },
        body: {
            "cid": country,
            "cparent": "",
            "cname": country
        },
        json: true
    };

    const citytOpts = {
        method: 'POST',
        uri: 'http://localhost:8080/capi/object',
        headers: {
            "Content-Type": "application/json"
        },
        body: {
            "cid": city,
            "cparent": country,
            "cname": city,
            "lat": lat,
            "lng": lng
        },
        json: true
    };

    rp(getObjectOpts(country)).then((data) => {
        if (data) {
            return rp(citytOpts).then(() => done());
        } else {
            return rp(countryOpts).then(() => rp(citytOpts)).then(() => done());
        }
    })
};

getStream().pipe(parExecutor({concurrency: 50, transform: sendMsg}));
