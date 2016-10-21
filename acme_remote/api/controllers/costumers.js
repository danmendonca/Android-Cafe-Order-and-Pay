'use strict';

var models = require('../../models');
var Costumer = models.costumer;

module.exports = {
    getCostumers: getCostumers,
    createUser: createUser
};

//sql costumer insertion INSERT INTO public.costumer VALUES ('daniel', 'dm', 'pass', '1234', '123456789', current_date, 'a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a11', now(), now());

function getCostumers(req, res) {
    Costumer.findAll({
        attributes: ["username", "uuid"]
    }).then(function (costumers) {
        res.json(costumers);
    });
}

function createUser(req, res) {
    var generatedPin = Math.floor(1000 + Math.random() * 9000);

    Costumer.create({
        name: req.swagger.params.costumer.value.name,
        username: req.swagger.params.costumer.value.username,
        password: req.swagger.params.costumer.value.password,
        pin: generatedPin.toString(),
        creditcardnumber: req.swagger.params.costumer.value.creditcardnumber,
        creditcarddate: req.swagger.params.costumer.value.creditcarddate
    }).then(function (value) {
        res.setHeader('Content-Type', 'application/json');
        var jsonCostumer = JSON.stringify(value);
        console.log(jsonCostumer);
        res.send(jsonCostumer);
        //res.json(value);
    })
        .catch(function (err) {
            console.log(err);
            res.statusCode = 403;
            res.end(JSON.stringify(err));
        })
}