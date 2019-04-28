const WebSocket = require('ws');
const amqp = require('amqplib/callback_api');
const axios = require('axios');

// axios configuration
axios.defaults.headers.post['Content-Type'] = 'application/json';

// amqp configuration
var amqpch = null;
amqp.connect('amqp://localhost', function (err, conn) {
    if (err) {
        console.log('Erreur de connection ' + JSON.stringify(err));
        process.exit(1);
    }
    conn.createChannel(function (err, ch) {
        amqpch = ch;
    });
});

// api gdax auth configuration
const gdaxauthservice = 'http://localhost:28081/signature';

// ws configuration
const webSocketGdaxURL = 'wss://ws-feed.pro.coinbase.com';
var ws = new WebSocket(webSocketGdaxURL);

const subscribeRequest = {
    "type": "subscribe",
    "product_ids": [
        "LTC-EUR",
        "BTC-EUR",
        "ETH-EUR"
    ],
    "channels": [
        "ticker",
        "user"
    ],
    "signature": "",
    "key": "",
    "passphrase": "",
    "timestamp": ""
};

const signatureRequest = {
    "path": "/users/self/verify",
    "methodType": "GET",
    "bodyContent": ""
};

ws.on('close', function close() {
    console.log('disconnected from GDAX WEBSOCKET');
    ws = new WebSocket(webSocketGdaxURL);
});

ws.on('open', function open() {
    console.log("New session established");
    console.log("Sending subscribe request to the webSocket");


    axios({
        method: 'post',
        url: gdaxauthservice,
        data: JSON.stringify(signatureRequest)
    })
        .then(function (response) {
            console.log(response);
            subscribeRequest.signature = response.data.cbAccessSign;
            subscribeRequest.key = response.data.cbAccessKey;
            subscribeRequest.passphrase = response.data.cbAccessPassphrase;
            subscribeRequest.timestamp = response.data.cbAccessTimestamp;

            console.log('Sending subscriberequest => ' + JSON.stringify(subscribeRequest));
            ws.send(JSON.stringify(subscribeRequest));
        })
        .catch(function (error) {
            console.log(error);
        });


});

ws.on('message', function incoming(data) {
    amqpch.publish('receiveGDAXEvent', '', Buffer.from(data));
});
