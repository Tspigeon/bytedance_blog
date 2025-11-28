<template>
  <div class="register" v-title data-title="注册">
    <!--<video preload="auto" class="me-video-player" autoplay="autoplay" loop="loop">
          <source src="../../static/vedio/sea.mp4" type="video/mp4">
      </video>-->

    <div class="me-login-box">
      <h1>欢迎注册</h1>

      <el-form ref="userForm" :model="userForm" :rules="rules">
        <el-form-item prop="account">
          <el-input placeholder="用户名" v-model="userForm.account"> </el-input>
        </el-form-item>

        <el-form-item prop="nickname">
          <el-input placeholder="昵称" v-model="userForm.nickname"></el-input>
        </el-form-item>

        <el-form-item prop="password">
          <el-input
            placeholder="密码"
            type="password"
            v-model="userForm.password"
            autocomplete="off"
          ></el-input>
        </el-form-item>

        <el-form-item prop="confirm">
          <el-input
            placeholder="确认密码"
            type="password"
            v-model="userForm.confirm"
            autocomplete="off"
          ></el-input>
        </el-form-item>

        <el-form-item>
          <el-button
            round
            type="primary"
            @click.native.prevent="register('userForm')"
            class="me-login-button"
            >注册</el-button
          >
        </el-form-item>

        <el-form-item style="text-align: center">
          已有账户？
          <router-link to="/login">
            <el-button style="font-size: 14px" type="text">去登录</el-button>
          </router-link>
        </el-form-item>
      </el-form>
    </div>
  </div>
</template>

<script>
export default {
  name: "Register",
  data() {
    var validatePass = (rule, value, callback) => {
      if (value === "") {
        callback(new Error("请输入密码"));
      } else {
        if (this.userForm.confirm !== "") {
          this.$refs.userForm.validateField("confirm");
        }
        callback();
      }
    };
    var validatePass2 = (rule, value, callback) => {
      if (value === "") {
        callback(new Error("请再次输入密码"));
      } else if (value !== this.userForm.password) {
        callback(new Error("两次输入密码不一致!"));
      } else {
        callback();
      }
    };
    return {
      userForm: {
        account: "",
        nickname: "",
        password: "",
        confirm: "",
      },
      rules: {
        account: [
          { required: true, message: "请输入用户名", trigger: "blur" },
          { min: 2, max: 10, message: "请输入2-10个字符", trigger: "blur" },
        ],
        nickname: [
          { required: true, message: "请输入昵称", trigger: "blur" },
          { max: 10, message: "不能多于10个字符", trigger: "blur" },
        ],
        password: [
          { validator: validatePass, trigger: "blur" },
          { min: 6, message: "不能少于6个字符", trigger: "blur" },
        ],
        confirm: [
          { validator: validatePass2, trigger: "blur" },
          { min: 6, message: "不能少于6个字符", trigger: "blur" },
        ],
      },
      changeAgainFlag: 1,
    };
  },
  methods: {
    register(formName) {
      this.$refs[formName].validate((valid) => {
        // 当输入框有内容再发送请求
        if (valid) {
          this.$store
            .dispatch("register", this.userForm)
            .then(() => {
              this.$message({
                message: "注册成功！",
                type: "success",
                showClose: true,
              });
              this.$router.push({ path: "/" });
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
.register {
  width: 100%;
  height: 100%;
  background-image: url("../assets/img/bg.jpg") !important;
  background-size: cover;
}

.me-login-box {
  position: absolute;
  top: 20%;
  left: 35%;
  width: 30%;
  height: 60%;
  background-color: #fff;
  border-radius: 20px;
  padding-top: 2%;
  .el-form{
    width: 80%;
    padding: 0 10%;
  }

  h1 {
    text-align: center;
    font-size: 24px;
    margin-bottom: 20px;
    vertical-align: middle;
  }
  .me-login-button {
    width: 60%;
    font-size: 16px;
    margin-left: 20%;
  }
}
</style>
