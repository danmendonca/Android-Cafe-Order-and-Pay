'use strict';

var url = require('url');


var Blacklist = require('./BlacklistService');


module.exports.blacklistPOST = function blacklistPOST (req, res, next) {
  Blacklist.blacklistPOST(req.swagger.params, res, next);
};
