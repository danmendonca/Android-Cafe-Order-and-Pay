module.exports = function (sequelize, DataTypes) {
    var Voucher = sequelize.define('voucher', {
        type: {
            type: 'BYTEA'
        },
        isused: {
            type: DataTypes.BOOLEAN,
            allowNull: false
        },
        key: {
            type: DataTypes.TEXT,
            allowNull: false,
            unique: 'VOUCHER_KEY_UNIQUE'
        },
        number: {
            type: DataTypes.INTEGER,
            allowNull: false
        }
    }, { tablename: 'voucher' });
    return Voucher;
}