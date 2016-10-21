'use strict';

var models = require('../../models');
var Costumer = models.costumer;

module.exports = {
    getCostumers : getCostumers
};

function getCostumers(req, res){
    Costumer.findAll({
        attributes: ["username","uuid"]
    }).then(function(costumers){
        res.json(JSON.stringify(costumers));
    });
}