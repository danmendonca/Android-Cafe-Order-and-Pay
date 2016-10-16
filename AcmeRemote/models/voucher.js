/* jshint indent: 2 */

module.exports = function(sequelize, DataTypes) {
  return sequelize.define('voucher', {
    voucherid: {
      type: DataTypes.INTEGER,
      allowNull: false,
      primaryKey: true
    },
    key: {
      type: DataTypes.TEXT,
      allowNull: false
    },
    number: {
      type: DataTypes.INTEGER,
      allowNull: false
    },
    type: {
      type: 'BYTEA',
      allowNull: false
    },
    costumerid: {
      type: DataTypes.INTEGER,
      allowNull: false,
      references: {
        model: 'costumer',
        key: 'costumerid'
      }
    },
    isused: {
      type: DataTypes.BOOLEAN,
      allowNull: false
    }
  }, {
    tableName: 'voucher'
  });
};
