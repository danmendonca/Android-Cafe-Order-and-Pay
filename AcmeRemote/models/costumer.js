module.exports = function (sequelize, DataTypes) {
    var Costumer = sequelize.define('costumer', {
        name: {
            type: DataTypes.TEXT,
            allowNull: false
        },
        username: {
            type: DataTypes.TEXT,
            allowNull: false
        },
        password: {
            type: DataTypes.TEXT,
            allowNull: false
        },
        pin: {
            type: DataTypes.TEXT,
            allowNull: false
        },
        creditcardnumber: {
            type: DataTypes.TEXT,
            allowNull: false
        },
        creditcarddate: {
            type: DataTypes.DATEONLY,
            allowNull: true
        },
        uuid: {
            type: DataTypes.UUID,
            defaultValue: DataTypes.UUIDV1,
            primaryKey: true
        }
    }, { tableName: 'costumer' });
    return Costumer;
};