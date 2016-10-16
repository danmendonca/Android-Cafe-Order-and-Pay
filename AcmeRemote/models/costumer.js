/* jshint indent: 2 */

module.exports = function(sequelize, DataTypes) {
  return sequelize.define('costumer', {
    costumerid: {
      type: DataTypes.INTEGER,
      allowNull: false,
      primaryKey: true
    },
    name: {
      type: DataTypes.TEXT,
      allowNull: false
    },
    pin: {
      type: DataTypes.TEXT,
      allowNull: false
    },
    creditcard: {
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
    creditcardexpiration: {
      type: DataTypes.TIME,
      allowNull: true
    }
  }, {
    tableName: 'costumer'
  });
};
