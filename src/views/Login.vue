<template>
  <div class="login" v-title data-title="登录">
    <div class="me-login-box">
      <!-- 左边表单区域 -->
      <div class="me-login-form">
        <!-- 名称区域 -->
        <div>
          <img src="../assets/img/logo.png" style="width: 20%" />
          <h1>云上博客</h1>
        </div>

        <!-- 登录主体区 -->
        <div class="me-login-main">
          <el-form ref="userForm" :model="userForm" :rules="rules">
            <el-form-item prop="account">
              <el-input
                placeholder="用户名"
                v-model="userForm.account"
              ></el-input>
            </el-form-item>

            <el-form-item prop="password">
              <el-input
                placeholder="密码"
                type="password"
                v-model="userForm.password"
              ></el-input>
            </el-form-item>

            <el-form-item size="small">
              <el-button
                round
                type="primary"
                @click.native.prevent="login('userForm')"
                class="me-login-btn"
                >登录</el-button
              >
            </el-form-item>

            <el-form-item size="small" style="text-align: center">
              没有账户?
              <router-link to="/register">
                <el-button style="font-size: 14px" type="text"
                  >立即注册</el-button
                >
              </router-link>
            </el-form-item>
          </el-form>
        </div>
      </div>

      <!-- 右边轮播图区域 -->
      <div class="me-login-carousel">
        <el-carousel height="579px" interval="3000">
          <el-carousel-item v-for="item in carouselIMG" :key="item.src">
            <img :src="item" />
          </el-carousel-item>
        </el-carousel>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: "Login",
  data() {
    return {
      userForm: {
        account: "",
        password: "",
      },
      rules: {
        account: [
          { required: true, message: "请输入用户名", trigger: "blur" },
          { max: 10, message: "不能大于10个字符", trigger: "blur" },
        ],
        password: [
          { required: true, message: "请输入密码", trigger: "blur" },
          { max: 10, message: "不能大于10个字符", trigger: "blur" },
        ],
      },
      carouselIMG: [
        require("../assets/img/1.jpg"),
        require("../assets/img/2.jpg"),
        require("../assets/img/3.jpg"),
        require("../assets/img/4.jpg"),
      ],
    };
  },
  methods: {
    login(formName) {
      this.$refs[formName].validate((valid) => {
        if (valid) {
          this.$store
            .dispatch("login", this.userForm)
            .then(() => {
              this.$router.push("/");
            })
            .catch((error) => {
              if (error !== "error") {
                this.$message({
                  message: error,
                  type: "error",
                  showClose: true,
                });
              }
            });
        } else {
          return false;
        }
      });
    },
  },
};
</script>

<style lang="less" scoped>
.login {
  min-width: 100%;
  min-height: 100%;
  background-image: url("../assets/img/bg.jpg");
  background-repeat: no-repeat;
  background-size: cover;

}

.me-login-box {
  display: flex;
  position: absolute;
  top: 10%;
  left: 20%;
  width: 60%;
  height: 80%;
  background-color: #fff;
  border-radius: 20px;
  overflow: hidden;

  .me-login-form {
    width: 40%;
    padding: 20px 10px;
    h1 {
      display: inline-block;
      font-size: 24px;
      vertical-align: middle;
    }
    img {
      vertical-align: middle;
    }
  }

  .me-login-main {
    margin-top: 30%;
    margin-right: 12%;
    margin-left: 10%;
    .me-login-btn {
      width: 60%;
      font-size: 16px;
      margin-left: 20%;
    }
  }

  .me-login-carousel {
    width: 60%;
    img {
      width: 100%;
      vertical-align: middle;
    }
  }
}
</style>
