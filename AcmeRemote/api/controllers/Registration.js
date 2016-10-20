'use strict';

var url = require('url');


var Registration = require('./RegistrationService');


module.exports.costumersRegisterPOST = function costumersRegisterPOST (req, res, next) {
  Registration.costumersRegisterPOST(req.swagger.params, res, next);
};
