/* jshint indent: 2 */

module.exports = function(sequelize, DataTypes) {
  return sequelize.define('menuproduct', {
    menuid: {
      type: DataTypes.INTEGER,
      allowNull: false,
      primaryKey: true,
      references: {
        model: 'null',
        key: 'null'
      }
    },
    productid: {
      type: DataTypes.INTEGER,
      allowNull: false,
      references: {
        model: 'product',
        key: 'productid'
      }
    }
  }, {
    tableName: 'menuproduct'
  });
};
