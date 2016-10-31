var fs = require('fs');
var path = require('path');
var Sequelize = require('sequelize');
var db = {};
var env = process.env.NODE_ENV || "development";
var config;
try {
  config = require(__dirname + '/../config/config.json')[env];
  // do stuff
} catch (ex) {
  config = require(__dirname + '/../config/serverConfig.json')[env];
}

// var sequelize = new Sequelize('postgres://user:password@localhost:5432/AcmeCafeDB');
var sequelize = new Sequelize(config.database, config.username, config.password, config);

//load each file in this directory and get the exported variable
fs
  .readdirSync(__dirname)
  .filter(function (file) {
    return (file.indexOf('.') !== 0) && (file !== 'index.js')
  })
  .forEach(function (file) {
    var model = sequelize.import(path.join(__dirname, file))
    db[model.name] = model
  });

Object.keys(db).forEach(function (modelName) {
  if ("associate" in db[modelName]) {
    db[modelName].associate(db);
  }
});

sequelize
  .authenticate()
  .then(function (err) {
    console.log('Connection has been established successfully.');
    //1 to 0..1
    sequelize.models.costumer.hasMany(sequelize.models.blacklist);
    //many to many
    sequelize.models.product.belongsToMany(sequelize.models.request, { through: sequelize.models.requestline });
    sequelize.models.request.belongsToMany(sequelize.models.product, { through: sequelize.models.requestline });
    //one to many
    sequelize.models.costumer.hasMany(sequelize.models.request);
    sequelize.models.costumer.hasMany(sequelize.models.voucher);
    sequelize.models.request.hasMany(sequelize.models.voucher);
    //sync database and create tables if do not exist yet

    if (env.localeCompare("development") == 0) {
      sequelize.sync({
        force: true,
        // logging: console.log
      }).then(() => {
        //costumers
        sequelize.models.costumer.create({
          username: 'dmendonca',
          name: 'Daniel',
          pin: '0000',
          password: 'admin',
          uuid: '487d7210-9882-11e6-9d39-f7b6026b4be5',
          creditcardnumber: '12345678',
          creditcarddate: '2016-12-19T00:00:00.000Z',
        }).then((costumer) => {
          sequelize.models.product.create({
            active: true,
            name: 'Popcorn',
            unitprice: 1.5
          }).then((prdct) => {
            sequelize.models.request.create({
              costumerUuid: costumer.uuid,
              number: 0
            }).then((rq) => {
              //create requestline for request
              sequelize.models.requestline.create({
                quantity: 101,
                unitprice: prdct.unitprice,
                productId: prdct.id,
                requestId: rq.id
              }).then(() => {

              })

              sequelize.models.voucher.create({
                costumerUuid: costumer.uuid,
                type: 1,
                signature: "1",
                isused: false
              }).then(() => { })

              sequelize.models.voucher.create({
                costumerUuid: costumer.uuid,
                type: 3,
                signature: "2",
                isused: false
              }).then(() => { })

            })
          });
        });
        sequelize.models.costumer.create({
          username: 'mnunes',
          name: 'Miguel',
          pin: '0000',
          password: 'admin',
          uuid: '487d7210-9882-11e6-9d39-f7b6026b4be6',
          creditcardnumber: '12345678',
          creditcarddate: '2016-10-30T00:00:00.000Z',
        }).then(() => { });
        sequelize.models.costumer.create({
          username: 'listed',
          name: 'Black',
          pin: '0001',
          password: 'admin',
          uuid: '487d7210-9882-11e6-9d39-f7b6026b4be7',
          creditcardnumber: '12345678',
          creditcarddate: '2015-10-30T00:00:00.000Z',
        }).then(() => {
          //blacklist
          sequelize.models.blacklist.create({
            costumerUuid: '487d7210-9882-11e6-9d39-f7b6026b4be7',
            //timestamp: '2016-01-01'
          }).then(() => { });
        });
        //products
        sequelize.models.product.create({
          active: true,
          name: 'Coffee',
          unitprice: 0.8
        }).then(() => { });
        sequelize.models.product.create({
          active: false,
          name: 'Francesinha',
          unitprice: 10.0,
        }).then(() => { });
        sequelize.models.product.create({
          active: false,
          name: 'French Toast',
          unitprice: 2.5,
        }).then(() => { });
        sequelize.models.product.create({
          active: false,
          name: 'Coke',
          unitprice: 1.5,
        }).then(() => { });
        sequelize.models.product.create({
          active: false,
          name: 'Evian Water',
          unitprice: 1,
        }).then(() => { });
        sequelize.models.product.create({
          active: false,
          name: 'Chocolate Cake',
          unitprice: 3,
        }).then(() => { });
        sequelize.models.product.create({
          active: false,
          name: 'Cheesecake',
          unitprice: 3.2,
        }).then(() => { });
        sequelize.models.product.create({
          active: false,
          name: 'Super Bock',
          unitprice: 1.5,
        }).then(() => { });
      })
    }
    else {
      sequelize.sync({
        //force: true,
        // logging: console.log
      }).then(() => { });
    }

  })
  .catch(function (err) {
    console.log('Unable to connect to the database:', err);
  });

db.sequelize = sequelize;
db.Sequelize = Sequelize;

module.exports = db;