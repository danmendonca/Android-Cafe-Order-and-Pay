'use strict';

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

