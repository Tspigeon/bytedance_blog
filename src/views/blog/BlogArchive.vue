<template>
  <div>
    <el-container>
      <el-main class="me-articles">
        <div class="me-month-title">
          <h1>{{ currentArchive }}</h1>
        </div>
        <article-scroll-page v-bind="article"></article-scroll-page>
      </el-main>

      <el-aside class="me-area">
        <el-button
          type="primary"
          style="display: block; margin: 30px auto"
          @click="all()"
        >
          全部
        </el-button>
        <span>月份签 ：</span>
        <ul class="me-month-list">
          <li
            v-for="a in archives"
            :key="a.year + a.month"
            class="me-month-item"
          >
            <el-badge :value="a.count">
              <el-button @click="changeArchive(a.year, a.month)"
                >{{ a.year + "年" + a.month + "月" }}
              </el-button>
            </el-badge>
          </li>
        </ul>
      </el-aside>
    </el-container>
  </div>
</template>

<script>
import ArticleScrollPage from "@/views/common/ArticleScrollPage";
import { listArchives } from "@/api/article";

export default {
  name: "BlogArchive",
  components: {
    ArticleScrollPage,
  },
  created() {
    this.listArchives();
  },
  watch: {
    $route() {
      if (this.$route.params.year && this.$route.params.month) {
        this.article.query.year = this.$route.params.year;
        this.article.query.month = this.$route.params.month;
      } else {
        this.$router.go(0);
      }
    },
  },
  data() {
    return {
      article: {
        query: {
          month: this.$route.params.month,
          year: this.$route.params.year,
        },
      },
      archives: [],
    };
  },
  computed: {
    title() {
      return this.currentArchive + " - 文章归档";
    },
    currentArchive() {
      if (this.article.query.year && this.article.query.month) {
        return `${this.article.query.year}年${this.article.query.month}月`;
      }
      return "全部";
    },
  },
  methods: {
    changeArchive(year, month) {
      this.$router.push({ path: `/archives/${year}/${month}` });
    },
    listArchives() {
      listArchives()
        .then((data) => {
          this.archives = data.data;
        })
        .catch((error) => {
          this.$message({
            type: "error",
            message: "文章归档加载失败!",
            showClose: true,
          });
        });
    },
    all() {
      this.currentArchive = "全部";
      this.$router.push("/archives");
    },
  },
};
</script>

<style lang="less" scoped>
.el-container {
  width: 800px;
  margin-right: 150px;
}

.el-aside {
  padding: 20px 0 50px 0px;
  position: fixed;
  right: 300px;
  width: 150px !important;
  background-color: #fff;
  border-radius: 8px;
  box-shadow: 0 3px 5px hsla(0, 0%, 7%, 0.1), 0 0 0 1px hsla(6%, 6%, 8%, 0.05);
  span {
    margin: 0 24px;
    font-family: "Franklin Gothic Medium", "Arial Narrow", Arial, sans-serif;
    color: #5fb878;
  }
}

.el-main {
  padding-left: 50px;
  line-height: 16px;
}

.me-month-list {
  margin-top: 10px;
  margin-bottom: 10px;
  text-align: center;
  list-style-type: none;
}

.me-month-item {
  margin-top: 18px;
  padding: 4px;
  font-size: 18px;
  color: #5fb878;
}

.me-order-list {
  float: right;
}

.me-month-title {
  margin-left: 4px;
  margin-bottom: 12px;
  text-align: center;
  h1 {
    font-family: "Franklin Gothic Medium", "Arial Narrow", Arial, sans-serif;
    color: #5fb878;
  }
}
</style>
