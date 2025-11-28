// 认证路由
const express = require('express');
const router = express.Router();
const User = require('../models/user.js');
const { generateToken } = require('../utils/jwt.js');

/**
 * 登录接口
 * @route POST /login
 * @param {Object} req - Express请求对象
 * @param {Object} res - Express响应对象
 */
router.post('/login', async (req, res) => {
  try {
    // 获取form表单提交的用户名和密码
    const { account, password } = req.body;
    
    // 验证输入
    if (!account || !password) {
      return res.status(400).json({
        code: 400,
        message: '用户名和密码不能为空',
        data: null
      });
    }
    
    // 查找用户
    const user = await User.findByUsername(account);
    if (!user) {
      return res.status(401).json({
        code: 401,
        message: '用户名或密码错误',
        data: null
      });
    }
    
    // 验证密码
    const isPasswordValid = await user.verifyPassword(password);
    if (!isPasswordValid) {
      return res.status(401).json({
        code: 401,
        message: '用户名或密码错误',
        data: null
      });
    }
    
    // 生成JWT token
    const tokenPayload = {
      id: user.id,
      username: user.username,
      email: user.email
    };
    const token = generateToken(tokenPayload);
    
    // 返回用户信息和token
    res.status(200).json({
      code: 200,
      message: 'success',
      success: true,
      data: {
        success: true,
        data: token
      }
    });
  } catch (error) {
    console.error('登录失败:', error);
    res.status(500).json({
      code: 500,
      message: '登录失败，请稍后重试',
      data: null,
      error: process.env.NODE_ENV === 'development' ? error.message : undefined
    });
  }
});

/**
 * 注册接口（可选，用于测试）
 * @route POST /register
 */
router.post('/register', async (req, res) => {
  try {
    const { username, email, password } = req.body;
    
    // 验证输入
    if (!username || !email || !password) {
      return res.status(400).json({
        status: 'error',
        message: '用户名、邮箱和密码不能为空'
      });
    }
    
    // 检查用户名是否已存在
    const existingUser = await User.findByUsername(username);
    if (existingUser) {
      return res.status(400).json({
        status: 'error',
        message: '用户名已存在'
      });
    }
    
    // 检查邮箱是否已存在
    const existingEmail = await User.findByEmail(email);
    if (existingEmail) {
      return res.status(400).json({
        status: 'error',
        message: '邮箱已被注册'
      });
    }
    
    // 创建新用户
    const newUser = await User.create(username, email, password);
    
    res.status(201).json({
      status: 'success',
      message: '注册成功',
      data: {
        user: newUser.toSafeObject()
      }
    });
  } catch (error) {
    console.error('注册失败:', error);
    res.status(500).json({
      status: 'error',
      message: '注册失败，请稍后重试',
      error: process.env.NODE_ENV === 'development' ? error.message : undefined
    });
  }
});

module.exports = router;