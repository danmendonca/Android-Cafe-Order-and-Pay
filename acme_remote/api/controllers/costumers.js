'use strict';

var models = require('../../models');
var Costumer = models.costumer;

module.exports = {
    getCostumers: getCostumers,
    createUser: createUser,
    logMe: logMe
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
        name: req.swagger.params.name.value,
        username: req.swagger.params.username.value,
        password: req.swagger.params.password.value,
        pin: generatedPin.toString(),
        creditcardnumber: req.swagger.params.creditcardnumber.value,
        creditcarddate: req.swagger.params.creditcarddate.value
    }).then(function (value) {
        //res.setHeader('Content-Type', 'application/json');
        //var jsonCostumer = JSON.stringify(value);
        //console.log(jsonCostumer);
        //res.send(jsonCostumer);
        res.json(value);
    })
        .catch(function (err) {
            console.log(err);
            res.statusCode = 403;
            res.end(JSON.stringify(err));
        })
}

function logMe(req, res) {
    var uname = req.swagger.params.username.value;
    var pw = req.swagger.params.password.value;
    Costumer.findOne({
        where: {
            username: uname,
            password: pw
        }
    }).then((validC) => {
        if (validC)
            res.json(validC);
        else {
            var ErrorResponse = {};
            ErrorResponse.message = "Invalid Login";
            res.statusCode = 403;
            res.json(ErrorResponse);
        }
    });

}