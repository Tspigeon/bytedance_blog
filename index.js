const express = require('express');
const cors = require('cors');
const app = express();
const PORT = process.env.PORT || 3000;

// 导入数据库连接工具
const db = require('./utils/db.js');

// 配置cors中间件
app.use(cors({
  origin: '*', // 允许所有来源的跨域请求
  methods: ['GET', 'POST', 'PUT', 'DELETE', 'OPTIONS'],
  allowedHeaders: ['Content-Type', 'Authorization'],
  credentials: true
}));

// 解析JSON请求体
app.use(express.json());

// 解析form表单请求体
app.use(express.urlencoded({ extended: true })); // extended: true 使用qs库解析复杂对象

// 根路由
app.get('/', (req, res) => {
  res.json({
    message: '欢迎使用博客后端API',
    status: 'success'
  });
});

// 健康检查路由
app.get('/health', (req, res) => {
  res.json({
    status: 'ok',
    timestamp: new Date().toISOString()
  });
});

// 注册认证路由
const authRoutes = require('./routes/auth.js');
app.use(authRoutes); // 直接挂载，使登录接口为 /login

// 错误处理中间件
app.use((err, req, res, next) => {
  console.error(err.stack);
  res.status(500).json({
    message: '服务器内部错误',
    error: process.env.NODE_ENV === 'development' ? err.message : undefined
  });
});

// 启动服务器
async function startServer() {
  try {
    // 先测试数据库连接
    await db.testConnection();
    
    // 启动Express服务器
    app.listen(PORT, () => {
      console.log(`服务器运行在 http://localhost:${PORT}`);
    });
  } catch (error) {
    console.error('服务器启动失败:', error);
    process.exit(1);
  }
}

// 执行启动函数
startServer();

module.exports = app; // 用于测试