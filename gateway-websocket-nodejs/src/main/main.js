
const WebSocket = require('ws');
const amqp = require('amqplib/callback_api');
const axios = require('axios');
const winston = require('winston');

// logger
const logger = winston.createLogger({
    format: winston.format.combine(
        winston.format.timestamp(),
        winston.format.simple(),
        winston.format.splat()
    ),
    transports: [
        new winston.transports.Console()
    ]
});


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
            logger.error("[AMQP]", err.message);
            return setTimeout(startAmqp, 5000);
        }
        conn.on("error", function(err) {
            if (err.message !== "Connection closing") {
                logger.error("[AMQP] conn error", err.message);
            }
        });
        conn.on("close", function() {
            logger.error("[AMQP] reconnecting");
            return setTimeout(startAmqp, 5000);
        });

        logger.info("[AMQP] connected");
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
        logger.info("[AMQP] exchange receiveGDAXEvent created");
        amqpCh = ch;
    });
}

var ws = null;
function startwebSocketListener() {
    ws = new WebSocket(webSocketGdaxURL);

    ws.on('close', function close() {
        logger.warn('disconnected from GDAX WEBSOCKET');
        return setTimeout(startwebSocketListener, 5000);
    });

    ws.on('open', function open() {
        logger.info("New session established");
        logger.info("Sending subscribe request to the webSocket");

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

                logger.info('Sending subscriberequest => ' + JSON.stringify(subscribeRequest));
                ws.send(JSON.stringify(subscribeRequest));
                startListeningGDaxEvent();
            })
            .catch(function (error) {
                logger.error("Erreur lors du contact de gdaxAuthService " + error);
                ws.close();
                return setTimeout(startwebSocketListener, 2000);
            });
    });
}

function startListeningGDaxEvent() {
    ws.on('message', function incoming(data) {
        let opts = { contentType : 'text'};
        logger.debug("Receiving event {}", data);
        amqpCh.publish('receiveGDAXEvent', '', Buffer.from(data), opts);
    });
}


startAmqp();

