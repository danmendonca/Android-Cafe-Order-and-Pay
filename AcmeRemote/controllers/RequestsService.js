'use strict';

exports.costumerRequestsPOST = function(args, res, next) {
  /**
   * parameters expected in the args:
  * costumerUuid (String)
  * pin (String)
  **/
    var examples = {};
  examples['application/json'] = [ {
  "number" : 123,
  "costumerUuid" : "aeiou",
  "id" : 123,
  "vouchers" : [ {
    "number" : 123,
    "requestId" : 123,
    "costumerUuid" : "aeiou",
    "id" : 123,
    "type" : "",
    "isUsed" : true,
    "key" : "aeiou"
  } ],
  "requestLines" : [ {
    "unitPrice" : 1.3579000000000001069366817318950779736042022705078125,
    "quantity" : 123,
    "productId" : 123,
    "requestId" : 123
  } ]
} ];
  if(Object.keys(examples).length > 0) {
    res.setHeader('Content-Type', 'application/json');
    res.end(JSON.stringify(examples[Object.keys(examples)[0]] || {}, null, 2));
  }
  else {
    res.end();
  }
  
}

