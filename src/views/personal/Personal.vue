<template>
  <div>
    <el-container class="me-personal-main">
      <!-- 个人信息处 -->
      <el-header>
        <div class="me-person-up">
          <!-- 头像区域 -->
          <div class="me-avatar-box">
            <img :src="login.avatar" />
          </div>
          <!-- 简介区 -->
          <div class="me-person-info">
            <div class="me-person-info-1">
              <span>{{ login.account }}</span>
              <el-button
                v-if="!isFollow"
                icon="el-icon-plus"
                class="me-follow-btn"
                type="primary"
                size="small"
                round
                @click="isFollow = !isFollow"
                >关注</el-button
              >
              <el-button
                v-else
                icon="el-icon-close"
                class="me-follow-btn"
                type="warning"
                size="small"
                round
                @click="isFollow = !isFollow"
                >取关</el-button
              >
              <el-button
                size="mini"
                round
                class="me-edit-btn"
                icon="el-icon-document"
                @click="dialogVisible = true"
                >编辑资料</el-button
              >
            </div>
            <div class="me-person-info-2">
              <ul>
                <li>{{}}文章</li>
                |
                <li>{{}}被收藏</li>
                |
                <li>{{}}粉丝</li>
                |
                <li>{{}}关注</li>
              </ul>
            </div>
          </div>
        </div>
        <div class="me-person-down">
          个性签名：“Freedom is the oxygen of soul💚”
        </div>
      </el-header>

      <!-- 主体 -->
      <el-container>
        <el-aside>
          <div class="">
            <card-article
              class="me-recommend"
              cardHeader="推荐文章"
              :articles="hotArticles"
            ></card-article>
          </div>
        </el-aside>
        <el-main class="me-articles me-area">
          <article-scroll-page></article-scroll-page>
        </el-main>
      </el-container>

      <!-- 编辑资料 -->
      <el-dialog :visible.sync="dialogVisible" width="50%">
        <!-- 个人信息 -->
        <el-card class="me-person-card">
          <el-form ref="form" :model="form" label-width="200px" size="small">
            <!-- 头像修改-待接口 -->
            <el-form-item style="text-align: center" label-width="0">
              <el-upload
                class="avatar-uploader"
                action="http://localhost:8888/upload"
                :show-file-list="false"
                :on-success="handleAvatarSuccess"
              >
                <img :src="login.avatar" v-bind="form.avatar" />
              </el-upload>
              <span>点击头像即可修改头像</span>
            </el-form-item>

            <el-form-item label="用户名" style="margin-right: 150px">
              <el-input v-model="form.account" disabled></el-input>
            </el-form-item>
            <el-form-item label="昵称" style="margin-right: 150px">
              <el-input
                v-model="form.nickName"
                :disabled="!isUpdate"
              ></el-input>
            </el-form-item>
          </el-form>
          <div style="text-align: center">
            <el-button
              v-if="!isUpdate"
              type="primary"
              @click="isUpdate = !isUpdate"
              >修改个人信息</el-button
            >
            <template v-else-if="isUpdate">
              <el-button type="primary" @click="update('form')">保存</el-button>
              <el-button @click="isUpdate = !isUpdate">取消</el-button>
            </template>
            <br />
            <el-button type="text" @click="changePwd('ruleForm')"
              >修改密码？</el-button
            >
          </div>
        </el-card>

        <!-- 修改密码对话框 -->
        <el-dialog
          title="修改密码"
          :visible.sync="updatePasswordDialog"
          width="400px"
          append-to-body
        >
          <el-form
            :model="ruleForm"
            status-icon
            :rules="rules"
            ref="ruleForm"
            label-width="80px"
            class="demo-ruleForm"
            style="margin-right: 30px"
          >
            <el-form-item label="新密码" prop="pass">
              <el-input
                type="password"
                v-model="ruleForm.pass"
                autocomplete="off"
              ></el-input>
            </el-form-item>
            <el-form-item label="确认密码" prop="checkPass">
              <el-input
                type="password"
                v-model="ruleForm.checkPass"
                autocomplete="off"
              ></el-input>
            </el-form-item>
          </el-form>
          <div slot="footer" class="dialog-footer">
            <el-button @click="updatePasswordDialog = false">取 消</el-button>
            <el-button type="primary" @click="updatePassword('ruleForm')"
              >确 定</el-button
            >
          </div>
        </el-dialog>
      </el-dialog>
    </el-container>
  </div>
</template>


<script>
import { userModify } from "../../api/login";
import CardArticle from "@/components/card/CardArticle";
import ArticleScrollPage from "@/views/common/ArticleScrollPage";
import { getHotArtices } from "@/api/article";

export default {
  data() {
    var validatePass = (rule, value, callback) => {
      if (value === "") {
        callback(new Error("请输入密码"));
      } else {
        if (this.ruleForm.checkPass !== "") {
          this.$refs.ruleForm.validateField("checkPass");
        }
        callback();
      }
    };
    var validatePass2 = (rule, value, callback) => {
      if (value === "") {
        callback(new Error("请再次输入密码"));
      } else if (value !== this.ruleForm.pass) {
        callback(new Error("两次输入密码不一致!"));
      } else {
        callback();
      }
    };
    return {
      login: this.$store.state,
      myArticles: [],
      hotArticles: [],
      dialogVisible: false,
      form: {
        account: "",
        nickName: "",
      },
      ruleForm: {
        pass: "",
        checkPass: "",
      },
      isUpdate: false,
      isFollow: false,
      updatePasswordDialog: false,
      rules: {
        pass: [{ validator: validatePass, trigger: "blur" }],
        checkPass: [{ validator: validatePass2, trigger: "blur" }],
      },
    };
  },
  components: {
    "card-article": CardArticle,
    ArticleScrollPage,
  },
  mounted() {
    this.init();
  },
  methods: {
    // 获取最热文章列表
    getHotArtices() {
      getHotArtices()
        .then((data) => {
          this.hotArticles = data.data;
        })
        .catch((error) => {
          if (error !== "error") {
            this.$message({
              type: "error",
              message: "最热文章加载失败!",
              showClose: true,
            });
          }
        });
    },

    // 更新用户密码信息
    changePwd(formName) {
      this.updatePasswordDialog = true;
      // 每次刷新表单
      this.$nextTick(() => {
        this.$refs[formName].resetFields();
      });
    },

    updatePassword(formName) {
      this.$refs[formName].validate((valid) => {
        if (valid) {
          let data = {
            id: this.login.id,
            password: this.ruleForm.pass,
          };
          // console.log(this.login);
          userModify(data)
            .then((data) => {
              this.$message.success("修改密码成功！");
              this.updatePasswordDialog = false;
            })
            .catch((error) => {
              if (error !== "error") this.$message("修改失败");
            });
        } else {
          this.$message.error("请输入内容！");
          return false;
        }
      });
    },

    init() {
      this.form.account = this.login.account;
      this.form.nickName = this.login.name;
      this.getHotArtices();
    },
  },
};
</script>

<style lang="less" scoped>
.me-personal-main {
  margin: 30px 50px;
  .el-header {
    background-color: #fff;
    height: 120px !important;
    margin-bottom: 30px;
    .me-person-up {
      position: relative;
      .me-avatar-box {
        position: absolute;
        left: 15px;
        top: -30px;
        height: 80px;
        width: 80px;
        border-radius: 50%;
        padding: 5px;
        border: 1px solid #eee;
        box-shadow: 0 0 10px #ddd;
        background-color: #f6f6f6;
        img {
          width: 100%;
          height: 100%;
          border-radius: 50%;
        }
      }
      .me-person-info {
        position: absolute;
        left: 130px;
        .me-person-info-1 {
          font-size: 26px;
          padding-top: 8px;
          color: #555;
          font-family: "Gill Sans", "Gill Sans MT", Calibri, "Trebuchet MS",
            sans-serif;
          .me-follow-btn {
            position: absolute;
            top:6px;
            right: 0px;
          }
          .me-edit-btn {
            position: absolute;
            top: 20px;
            right: -170%;
          }
        }
        .me-person-info-2 {
          padding-top: 5px;
          li {
            color: #888;
            font-family: "Gill Sans", "Gill Sans MT", Calibri, "Trebuchet MS",
              sans-serif;
            display: inline;
            padding: 0 8px;
          }
        }
      }
    }
    .me-person-down {
      padding-top: 80px;
      color: #666;
    }
  }
  .el-aside {
    background: url("../../assets/img/mybg.jpg");
    background-size: cover;
    padding: 30px 30px;
    .me-recommend {
      position: fixed;
      width: 241px;
      height: 251px;
      left: 201px;
      top: 280px;
      background-color: #fff;
      padding: 0px 30px;
    }
  }
  .me-area {
    margin-left: 3%;
    background-color: #fff;
  }
}

.el-dialog {
  .el-upload {
    img {
      height: 80px;
      width: 80px;
      border-radius: 50%;
    }
  }
}
</style>