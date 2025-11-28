<template>
  <div v-title data-title="云上博客">
    <el-container>
      <el-aside>
        <card-me v-if="isLogin"></card-me>
        <card-tag class="me-card" :tags="hotTags"></card-tag>

        <card-article
          class="me-card"
          cardHeader="最热文章"
          :articles="hotArticles"
        ></card-article>

        <card-archive
          class="me-card"
          cardHeader="文章归档"
          :archives="archives"
        ></card-archive>

        <card-article
          class="me-card"
          cardHeader="最新文章"
          :articles="newArticles"
        ></card-article>
      </el-aside>

      <el-main class="me-articles">
        <article-scroll-page></article-scroll-page>
      </el-main>
    </el-container>
  </div>
</template>

<script>
import anime from "animejs";
import CardMe from "@/components/card/CardMe";
import CardArticle from "@/components/card/CardArticle";
import CardArchive from "@/components/card/CardArchive";
import CardTag from "@/components/card/CardTag";
import ArticleScrollPage from "@/views/common/ArticleScrollPage";

import { getHotArtices, getNewArtices } from "@/api/article";
import { getHotTags } from "@/api/tag";
import { listArchives } from "@/api/article";

export default {
  name: "Index",
  data() {
    return {
      hotTags: [],
      hotArticles: [],
      newArticles: [],
      archives: [],
      isLogin:false
    };
  },
  mounted() {
    this.isLogin = this.$store.state.account.length != 0;
    this.getHotArtices();
    this.getNewArtices();
    this.getHotTags();
    this.listArchives();
    this.Animate();
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
    // 获取最新文章列表
    getNewArtices() {
      getNewArtices()
        .then((data) => {
          this.newArticles = data.data;
        })
        .catch((error) => {
          if (error !== "error") {
            this.$message({
              type: "error",
              message: "最新文章加载失败!",
              showClose: true,
            });
          }
        });
    },
    // 获取最热标签列表
    getHotTags() {
      getHotTags()
        .then((data) => {
          this.hotTags = data.data;
        })
        .catch((error) => {
          if (error !== "error") {
            this.$message({
              type: "error",
              message: "最热标签加载失败!",
              showClose: true,
            });
          }
        });
    },
    // 获取归档文章列表
    listArchives() {
      listArchives()
        .then((data) => {
          this.archives = data.data;
        })
        .catch((error) => {
          if (error !== "error") {
            this.$message({
              type: "error",
              message: "文章归档加载失败!",
              showClose: true,
            });
          }
        });
    },

    // 加载动画
    Animate() {
      anime({
        targets: ".me-card",
        height: "200px",
        duration: 4500,
      });
    },
  },
  components: {
    "card-me": CardMe,
    "card-article": CardArticle,
    "card-tag": CardTag,
    ArticleScrollPage,
    CardArchive,
  },
};
</script>

<style lang="less" scoped>
.el-container {
  width: 100%;
  background: #f3f3f3;
}

.el-aside {
  margin-left: 20px;
  width: 260px;
  .me-card {
    height: 100px;
  }
}

.el-main {
  padding: 0 30px 0 10px;
  line-height: 25px;
}

.el-card {
  border-radius: 20px;
  margin-right: 20px;
}

.el-card:not(:first-child) {
  margin-top: 25px;
}
</style>
