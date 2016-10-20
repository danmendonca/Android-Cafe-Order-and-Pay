'use strict';

exports.costumersRegisterPOST = function(args, res, next) {
  /**
   * parameters expected in the args:
  * username (String)
  * name (String)
  * password (String)
  * creditCardNr (String)
  * creditCardDate (date)
  **/
    var examples = {};
  examples['application/json'] = {
  "password" : "aeiou",
  "pin" : "aeiou",
  "creditCardNumber" : "aeiou",
  "creditCardDate" : "2000-01-23T04:56:07.000+00:00",
  "name" : "aeiou",
  "requests" : [ {
    "number" : 123,
    "costumerUuid" : "aeiou",
    "id" : 123,
    "vouchers" : [ "" ],
    "requestLines" : [ {
      "unitPrice" : 1.3579000000000001069366817318950779736042022705078125,
      "quantity" : 123,
      "productId" : 123,
      "requestId" : 123
    } ]
  } ],
  "vouchers" : [ {
    "number" : 123,
    "requestId" : 123,
    "costumerUuid" : "aeiou",
    "id" : 123,
    "type" : "",
    "isUsed" : true,
    "key" : "aeiou"
  } ],
  "uuid" : "aeiou",
  "username" : "aeiou"
};
  if(Object.keys(examples).length > 0) {
    res.setHeader('Content-Type', 'application/json');
    res.end(JSON.stringify(examples[Object.keys(examples)[0]] || {}, null, 2));
  }
  else {
    res.end();
  }
  
}

