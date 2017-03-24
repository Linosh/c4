const fs = require('fs');
const JSONStream = require('JSONStream');
const rp = require('request-promise');
const parExecutor = require('./LimitedParallelStream');

const getStream = () => {
    const stream = fs.createReadStream('cities.json', {encoding: 'utf8'});
    const parser = JSONStream.parse('*');
    return stream.pipe(parser);
};

const createFieldOpts = (name) => {
    return {
        method: 'POST',
        url: 'http://localhost:8080/mapi/field',
        headers: {
            'content-type': 'application/json'
        },
        body: {
            id: name,
            hidden: false
        },
        json: true
    }
};

const sendMsg = (data, push, done) => {
    const getObjectOpts = (id) => {
        return {
            "method": "GET",
            "url": `http://localhost:8080/mapi/object/${id}`
        }
    };

    const country = data.country;
    const city = data.name;
    const lat = data.lat;
    const lng = data.lng;

    const countryOpts = {
        method: 'POST',
        uri: 'http://localhost:8080/mapi/object',
        headers: {
            "Content-Type": "application/json"
        },
        body: {
            "id": country,
            "parent": "",
            "fields": {
                "name": country
            }
        },
        json: true
    };

    const citytOpts = {
        method: 'POST',
        uri: 'http://localhost:8080/mapi/object',
        headers: {
            "Content-Type": "application/json"
        },
        body: {
            "id": city,
            "parent": country,
            "fields": {
                "name": city,
                "lat": lat,
                "lng": lng
            }
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

rp(createFieldOpts('country'))
    .then(() => rp(createFieldOpts('city')))
    .then(() => rp(createFieldOpts('name')))
    .then(() => rp(createFieldOpts('lat')))
    .then(() => rp(createFieldOpts('lng')))
    .then(() => getStream().pipe(parExecutor({concurrency: 50, transform: sendMsg})))
    .catch(err => console.error(err));
