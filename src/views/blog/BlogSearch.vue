<template>
  <div>
      <h2>搜索结果</h2>
    <div class="me-articles">
      <article-scroll-page v-bind="article"></article-scroll-page>
    </div>
  </div>
</template>

<script>
import ArticleScrollPage from "@/views/common/ArticleScrollPage";
import { searchArticle } from "@/api/article";

export default {
  name: "BlogSearch",
  components: {
    ArticleScrollPage,
  },
  data(){
    return{
      article:'',
      search:this.$route.query.search,
    }
  },
  mounted(){
    this.load()
  },
  methods:{
    load(){
      console.log(this.$route)
      searchArticle(this.search).then((res) => {
        if (res.success) {
          console.log(res)
        }else{
          console.log(res)
        }

      });
    }
  }
};
</script>

<style scoped>
h2{
    text-align: center;
    margin-bottom: 10px;
    font-family:'Times New Roman', Times, serif;
}
.me-articles {
  margin: 0 100px;
}
</style>