module.exports = function (sequelize, DataTypes) {
    var Product = sequelize.define('product', {
        active: {
            type: DataTypes.BOOLEAN,
            allowNull: false
        },
        name: {
            type: DataTypes.TEXT,
            allowNull: false
        },
        unitprice: {
            type: DataTypes.FLOAT,
            allowNull: false
        }
    }, { tablename: 'product' });
    return Product;
}