// 数据库连接工具
const mysql = require('mysql2/promise');
const dbConfig = require('../config/db.config.js');

// 创建连接池
const pool = mysql.createPool(dbConfig);

// 测试连接
async function testConnection() {
  try {
    const connection = await pool.getConnection();
    console.log('数据库连接成功！');
    connection.release();
    return true;
  } catch (error) {
    console.error('数据库连接失败:', error.message);
    return false;
  }
}

// 查询方法封装
async function query(sql, params) {
  try {
    const [rows] = await pool.execute(sql, params);
    return rows;
  } catch (error) {
    console.error('数据库查询错误:', error.message);
    throw error;
  }
}

module.exports = {
  pool,
  testConnection,
  query
};