'use strict';

var models = require('../../models');
var Product = models.product;

module.exports = {
    getProducts: getProducts,
    createProduct: createProduct
};

function getProducts(req, res) {
    Product.findAll({
        where: {
            active: true
        }
    }).then(function (values) {
    	var prods = {};
    	prods['products'] = values;
        res.json(prods);
    });
}

function createProduct(req, res) {
    var uprice = req.swagger.params.unitprice.value;
    var name = req.swagger.params.name.value;
    var active =req.swagger.params.active.value;   
    if (!name){
        res.status(403);
        throw "invalid product name";
    }
    if (!active){
        res.status(403);
        throw "active param not set";
    }
    if (!uprice){
        res.status(403);
        throw "invalid unitprice param";
    }

    Product.create({
        name: name,
        active: active,
        unitprice: uprice
    }).then(function (value) {
        res.json(value);
    }).catch(function (err) {
        res.status(403);
        res.end(JSON.stringify(err));
    });
}
