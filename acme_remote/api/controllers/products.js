'use strict';

var models = require('../../models');
var Product = models.product;

module.exports = {
    getProducts: getProducts,
    createProduct: createProduct
};

function getProducts(req, res) {
    Product.findAll().then(function (values) {
        res.json(values);
    });
}

function createProduct(req, res) {
    if (!req.swagger.params.product.value.name){
        res.status(403);
        throw "invalid product name";
    }
    if (!req.swagger.params.product.value.active){
        res.status(403);
        throw "active param not set";
    }
    if (!req.swagger.params.product.value.unitprice){
        res.status(403);
        throw "invalid unitprice param";
    }

    Product.create({
        name: req.swagger.params.product.value.name,
        active: req.swagger.params.product.value.active,
        unitprice: req.swagger.params.product.value.unitprice
    }).then(function (value) {
        res.json(value);
    }).catch(function (err) {
        res.status(403);
        res.end(JSON.stringify(err));
    });
}
