module.exports = function (sequelize, DataTypes) {
    var Request = sequelize.define('request', {
        number: {
            type: DataTypes.INTEGER,
            allowNull: false
        }
    }, { tablename: 'request' });
    return Request;
}