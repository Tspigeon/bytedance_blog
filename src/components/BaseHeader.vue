<template>
  <el-header class="me-area">
    <!-- logo区域 -->
    <el-col :span="1" class="me-header-logo">
      <router-link to="/">
        <img src="../assets/img/logo.png" />
      </router-link>
    </el-col>

    <!-- 主体导航栏 -->
    <el-col :span="8">
      <el-menu
        :router="true"
        menu-trigger="hover"
        active-text-color="#5FB878"
        :default-active="activeIndex"
        mode="horizontal"
        background-color="#fff"
        text-color="#000"
      >
        <el-menu-item index="/">首页</el-menu-item>
        <el-submenu>
          <template slot="title">笔记中心</template>
          <el-menu-item style="display:block" index="/all">所有笔记</el-menu-item>
          <el-menu-item index="/category/all">笔记分类</el-menu-item>
          <el-menu-item index="/archives">笔记归档</el-menu-item>
        </el-submenu>
        <el-menu-item index="/write">发布中心</el-menu-item>
        <el-menu-item>
          <a href="https://www.baidu.com">疑问解决？</a>
        </el-menu-item>
      </el-menu>
    </el-col>

    <!-- 搜索框 -->
    <el-col :span="6" style="margin-top: 10px">
      <el-input
        class="me-search-input"
        v-model="search"
        placeholder="请输入内容"
        prefix-icon="el-icon-search"
        @focus="focusAnimate"
      >
      </el-input>
      <el-button round type="primary" @click="toSearch()">
        搜索
      </el-button>
    </el-col>

    <!-- 个人导航栏 -->
    <el-col :span="3" :offset="1">
      <el-menu
        :router="true"
        menu-trigger="click"
        mode="horizontal"
        active-text-color="#5FB878"
      >
        <template v-if="!user.login">
          <el-menu-item index="/login">
            <el-button type="text">登录</el-button>
          </el-menu-item>
          <el-menu-item index="/register">
            <el-button type="text">注册</el-button>
          </el-menu-item>
        </template>

        <template v-else>
          <el-submenu class="me-header-user" index>
            <template slot="title">
              <img
                class="me-header-avatar"
                :src="user.avatar"
              />&nbsp;&nbsp;&nbsp;{{ user.nickName }}
            </template>
            <el-menu-item index @click="personal" style="color:#666"
              ><i class="el-icon-document"></i>个人</el-menu-item
            >
            <el-menu-item index @click="logout" style="color:#F56C6C"
              ><i class="el-icon-back"></i>退出</el-menu-item
            >
          </el-submenu>
        </template>
      </el-menu>
    </el-col>
  </el-header>
</template>

<script>
import anime from "animejs";
import { searchArticle } from "@/api/article";
export default {
  name: "BaseHeader",
  props: {
    activeIndex: String,
    simple: {
      type: Boolean,
      default: false,
    },
  },
  data() {
    return {
      search: "",
      articles: [],
    };
  },
  computed: {
    user() {
      let login = this.$store.state.account.length != 0;
      let avatar = this.$store.state.avatar;
      let nickName = this.$store.state.name;
      return {
        login,
        avatar,
        nickName,
      };
    },
  },
  methods: {
    logout() {
      this.$store
        .dispatch("logout")
        .then(() => {
          this.$router.push({ path: "/" });
        })
        .catch((error) => {
          if (error !== "error") {
            this.$message({ message: error, type: "error", showClose: true });
          }
        });
    },
    personal() {
      this.$router.push("/personal/document");
    },
    // querySearchAsync(queryString, cb) {
    //   searchArticle(this.search).then((res) => {
    //     if (res.success) {
    //       var results = [];
    //       for (const item of res.data) {
    //         results.push({
    //           id: item.id,
    //           value: item.title,
    //         });
    //       }
    //       cb(results);
    //     }
    //   });
    // },
    toSearch() {
      console.log("搜索请求" + this.search);
      this.$router.push({
        path: "/search",
        query:{
          search: this.search,
        }
      });
    },

    // 入场动画
    focusAnimate() {
      anime({
        targets: ".me-search-input",
        width: "65%", // -> from '28px' to '100%',
        direction: 1000,
        easing: "spring(1, 80, 10, 0)",
      });
    },
  },
};
</script>

<style lang="less" scoped>
.me-area {
  position: fixed;
  z-index: 1024;
  min-width: 100%;
  background: #fff;
  box-shadow: 0 4px 5px hsla(0, 0%, 7%, 0.1), 0 0 0 1px hsla(0, 0%, 7%, 0.2);
  border: 0;

  .me-header-logo {
    margin: 5px 65px;
    margin-top: 10px;
    font-size: 24px;
    img {
      width: 120%;
    }
  }

  .me-search-input {
    width: 20%;
  }

  .me-header-avatar {
    width: 36px;
    height: 36px;
    border-radius: 50%;
    vertical-align: middle;
    background-color: #5fb878;
  }
}
</style>
