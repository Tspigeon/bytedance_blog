// 数据库表结构初始化脚本
const db = require('../utils/db.js');
const bcrypt = require('bcryptjs');

/**
 * 初始化数据库表结构
 */
async function initializeDatabase() {
  const connection = null;
  
  try {
    console.log('开始初始化数据库...');
    
    // 获取数据库连接
    const connection = await db.getConnection();
    
    // 1. 创建users表
    console.log('创建users表...');
    const createUsersTable = `
      CREATE TABLE IF NOT EXISTS users (
        id INT AUTO_INCREMENT PRIMARY KEY,
        username VARCHAR(50) NOT NULL UNIQUE,
        email VARCHAR(100) NOT NULL UNIQUE,
        password VARCHAR(255) NOT NULL,
        created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
        updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
        INDEX idx_username (username),
        INDEX idx_email (email)
      ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
    `;
    
    await connection.query(createUsersTable);
    console.log('users表创建成功');
    
    // 2. 检查是否需要创建测试用户
    const [existingUsers] = await connection.query('SELECT COUNT(*) as count FROM users');
    if (existingUsers[0].count === 0) {
      console.log('创建测试用户...');
      
      // 创建一个测试用户
      const testUsername = 'testuser';
      const testEmail = 'test@example.com';
      const testPassword = 'testpassword'; // 明文密码，实际会被加密
      
      // 加密密码
      const hashedPassword = await bcrypt.hash(testPassword, 10);
      
      // 插入测试用户
      await connection.query(
        'INSERT INTO users (username, email, password) VALUES (?, ?, ?)',
        [testUsername, testEmail, hashedPassword]
      );
      
      console.log(`测试用户创建成功！用户名: ${testUsername}, 密码: ${testPassword}`);
    }
    
    console.log('数据库初始化完成！');
    
  } catch (error) {
    console.error('数据库初始化失败:', error);
    throw error;
  } finally {
    // 释放连接
    if (connection) {
      try {
        await connection.release();
      } catch (err) {
        console.error('释放数据库连接失败:', err);
      }
    }
  }
}

// 执行初始化
initializeDatabase()
  .then(() => {
    console.log('数据库初始化脚本执行成功');
    process.exit(0);
  })
  .catch((error) => {
    console.error('数据库初始化脚本执行失败:', error);
    process.exit(1);
  });