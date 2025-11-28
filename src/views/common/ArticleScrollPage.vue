<template>
  <scroll-page
    :loading="loading"
    :offset="offset"
    :no-data="noData"
    v-on:load="load"
  >
    <article-item v-for="a in articles" :key="a.id" v-bind="a"></article-item>
  </scroll-page>
</template>

<script>
import anime from "animejs";
import ArticleItem from "@/components/article/ArticleItem";
import ScrollPage from "@/components/scrollpage";
import { getArticles } from "@/api/article";

export default {
  name: "ArticleScrollPage",
  props: {
    offset: {
      type: Number,
      default: 100,
    },
    page: {
      type: Object,
      default() {
        return {};
      },
    },
    query: {
      type: Object,
      default() {
        return {};
      },
    },
  },
  watch: {
    query: {
      handler() {
        this.noData = false;
        this.articles = [];
        this.innerPage.pageNumber = 1;
        this.load();
      },
      deep: true,
    },
    page: {
      handler() {
        this.noData = false;
        this.articles = [];
        this.innerPage = this.page;
        this.load();
      },
      deep: true,
    },
  },
  created() {
    this.load();
  },
  data() {
    return {
      loading: false,
      noData: false,
      innerPage: {
        pageSize: 10,
        pageNumber: 1
      },
      articles: [],
    };
  },
  methods: {
    load() {
      this.getArticles();
    },
    // 列表入场动画
    listAnimate() {
      anime({
        targets: ".me-articles .me-area",
        translateX: [-20, 0],
        opacity: [0, 1],
        easing: "linear",
        duration: 150,
        delay: anime.stagger(150), // 每个元素的延迟增加150毫秒。
      });
    },
    view(id) {
      this.$router.push({ path: `/view/${id}` });
    },
    getArticles() {
      this.loading = true;

      getArticles(this.query, this.innerPage)
        .then((data) => {
          let newArticles = data.data;
          if (newArticles && newArticles.length > 0) {
            this.innerPage.pageNumber += 1;
            this.articles = this.articles.concat(newArticles);
            this.$nextTick(() => {
              this.listAnimate(); //展示动画
            });
          } else {
            this.noData = true;
          }
        })
        .catch((error) => {
          if (error !== "error") {
            this.$message({
              type: "error",
              message: "文章加载失败!",
              showClose: true,
            });
          }
        })
        .finally(() => {
          this.loading = false;
        });
    },
  },
  components: {
    "article-item": ArticleItem,
    "scroll-page": ScrollPage,
  },
};
</script>

<style scoped>
.el-card {
  border-radius: 10px;
}

.el-card:not(:first-child) {
  margin-top: 20px;
}
</style>
