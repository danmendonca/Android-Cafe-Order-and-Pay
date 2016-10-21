'use strict';

var models = require('../../models');
var Costumer = models.costumer;

module.exports = {
    getCostumers : getCostumers
};

//sql costumer insertion INSERT INTO public.costumer VALUES ('daniel', 'dm', 'pass', '1234', '123456789', current_date, 'a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a11', now(), now());

function getCostumers(req, res){
    Costumer.findAll({
        attributes: ["username","uuid"]
    }).then(function(costumers){
        res.json(costumers);
    });
}