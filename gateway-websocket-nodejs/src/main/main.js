const WebSocket = require('ws');
const amqp = require('amqplib/callback_api');
const axios = require('axios');

// axios configuration
axios.defaults.headers.post['Content-Type'] = 'application/json';


// api gdax auth configuration
const gdaxauthservice = 'http://gdax-auth-service:28081/signature';
// const gdaxauthservice = 'http://localhost:28081/signature';
const amqpUri = 'amqp://rabbitmq-management:5672';
// const amqpUri = 'amqp://localhost:5672';
// ws configuration
const webSocketGdaxURL = 'wss://ws-feed.pro.coinbase.com';

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

// amqp configuration
var amqpconn = null;
var amqpCh = null;
function startAmqp() {
    amqp.connect(amqpUri, function(err, conn) {
        if (err) {
            console.error("[AMQP]", err.message);
            return setTimeout(startAmqp, 5000);
        }
        conn.on("error", function(err) {
            if (err.message !== "Connection closing") {
                console.error("[AMQP] conn error", err.message);
            }
        });
        conn.on("close", function() {
            console.error("[AMQP] reconnecting");
            return setTimeout(startAmqp, 5000);
        });

        console.log("[AMQP] connected");
        amqpconn = conn;

        whenAmqpConnected();
    });
}

function whenAmqpConnected() {
    initAmqpPublisher();
    startwebSocketListener();
}

function initAmqpPublisher() {
    amqpconn.createChannel(function (err, ch) {
        ch.assertExchange('receiveGDAXEvent', 'fanout', {durable: true});
        console.log("[AMQP] exchange receiveGDAXEvent created");
        amqpCh = ch;
    });
}

var ws = null;
function startwebSocketListener() {
    ws = new WebSocket(webSocketGdaxURL);

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
                subscribeRequest.signature = response.data.cbAccessSign;
                subscribeRequest.key = response.data.cbAccessKey;
                subscribeRequest.passphrase = response.data.cbAccessPassphrase;
                subscribeRequest.timestamp = response.data.cbAccessTimestamp;

                console.log('Sending subscriberequest => ' + JSON.stringify(subscribeRequest));
                ws.send(JSON.stringify(subscribeRequest));
                startListeningGDaxEvent();
            })
            .catch(function (error) {
                console.log("Erreur lors du contact de gdaxAuthService " + error);
                ws.close();
                return setTimeout(startwebSocketListener, 2000);
            });
    });
}

function startListeningGDaxEvent() {
    ws.on('message', function incoming(data) {
        let opts = { contentType : 'text'};
        amqpCh.publish('receiveGDAXEvent', '', Buffer.from(data), opts);
    });
}


startAmqp();

