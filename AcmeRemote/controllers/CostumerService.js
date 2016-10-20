'use strict';

exports.blacklistPOST = function(args, res, next) {
  /**
   * parameters expected in the args:
  **/
    var examples = {};
  examples['application/json'] = {
  "costumerUuid" : "aeiou",
  "id" : 123,
  "timestamp" : "2000-01-23T04:56:07.000+00:00"
};
  if(Object.keys(examples).length > 0) {
    res.setHeader('Content-Type', 'application/json');
    res.end(JSON.stringify(examples[Object.keys(examples)[0]] || {}, null, 2));
  }
  else {
    res.end();
  }
  
}

exports.costumerMakerequestPOST = function(args, res, next) {
  /**
   * parameters expected in the args:
  * username (String)
  * pin (String)
  * requestLines (List)
  **/
    var examples = {};
  examples['application/json'] = "aeiou";
  if(Object.keys(examples).length > 0) {
    res.setHeader('Content-Type', 'application/json');
    res.end(JSON.stringify(examples[Object.keys(examples)[0]] || {}, null, 2));
  }
  else {
    res.end();
  }
  
}

