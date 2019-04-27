const WebSocket = require('ws');

const axios = require('axios');
axios.defaults.headers.post['Content-Type'] = 'application/json';

const ws = new WebSocket('wss://ws-feed.pro.coinbase.com');

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

ws.on('open', function open() {
    console.log("New session established");
    console.log("Sending subscribe request to the webSocket");


    axios({
        method: 'post',
        url: 'http://localhost:28081/signature',
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
    console.log(data);
});
