'use strict';

var SwaggerExpress = require('swagger-express-mw');
var app = require('express')();
var crypto = require('crypto');
var fs = require('fs');

fs.readFile(process.argv[1] + '/../keys/privatekey', function (err, readPrK) {
  if (err) {  
    generateKeys();
  }
  else {
    console.log("read prK:\n" + readPrK);
  }
});
fs.readFile(process.argv[1] + '/../keys/publickey', function (err, readPuK) {
  if (!err) {
    console.log("read puK:\n" + readPuK);
  }
});

function generateKeys() {
  var prime_length = 1024;
  var diffHell = crypto.createDiffieHellman(prime_length);
  diffHell.generateKeys('hex');
  var prK = diffHell.getPrivateKey('hex');
  var puK = diffHell.getPublicKey('hex');

  fs.writeFile(process.argv[1] + '/../keys/privatekey', prK, { flag: 'wx' }, function (err) {
    if (err) {
      throw err;
    }
    console.log("wrote prK:\n" + prK);
  });

  fs.writeFile(process.argv[1] + '/../keys/publickey', puK, { flag: 'wx' }, function (err) {
    if (err) {
      throw err;
    }
    console.log("wrote puK:\n" + puK);
  });
}


module.exports = app; // for testing

var config = {
  appRoot: __dirname // required config
};

SwaggerExpress.create(config, function(err, swaggerExpress) {
  if (err) { throw err; }

  // install middleware
  swaggerExpress.register(app);

  var port = process.env.PORT || 8080;
  app.listen(port);

  if (swaggerExpress.runner.swagger.paths['/hello']) {
    console.log('try this:\ncurl localhost:' + port + '/hello?name=Scott');
  }
});
