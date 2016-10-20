'use strict';

exports.productsGET = function(args, res, next) {
  /**
   * parameters expected in the args:
  **/
    var examples = {};
  examples['application/json'] = [ {
  "unitPrice" : 1.3579000000000001069366817318950779736042022705078125,
  "product_id" : 123,
  "name" : "aeiou",
  "active" : true
} ];
  if(Object.keys(examples).length > 0) {
    res.setHeader('Content-Type', 'application/json');
    res.end(JSON.stringify(examples[Object.keys(examples)[0]] || {}, null, 2));
  }
  else {
    res.end();
  }
  
}

