'use strict';

var SwaggerExpress = require('swagger-express-mw');
var app = require('express')();
var crypto = require('crypto');
var fs = require('fs');

//key files must exist
fs.readFile(process.argv[1] + '/../keys/private.pem', function (err, readPrK) {
  if (err) {  
    throw err;
  }
});
//key files must exist
fs.readFile(process.argv[1] + '/../keys/public.pem', function (err, readPuK) {
  if (err)
    throw err; 
});

// function generateKeys() {
//   var prime_length = 1024;
//   var diffHell = crypto.createDiffieHellman(prime_length);
//   diffHell.generateKeys('hex');
//   var prK = diffHell.getPrivateKey('hex');
//   var puK = diffHell.getPublicKey('hex');

//   fs.writeFile(process.argv[1] + '/../keys/privatekey', prK, { flag: 'wx' }, function (err) {
//     if (err) {
//       throw err;
//     }
//     console.log("wrote prK:\n" + prK);
//   });

//   fs.writeFile(process.argv[1] + '/../keys/publickey', puK, { flag: 'wx' }, function (err) {
//     if (err) {
//       throw err;
//     }
//     console.log("wrote puK:\n" + puK);
//   });
// }


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
