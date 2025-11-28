<template>
  <div>
      <el-main>
        <div class="me-ct-title">
          <template v-if="this.$route.params.type === 'tag'">
            <img class="me-ct-picture" :src="ct.avatar?ct.avatar:defaultAvatar"/>
            <h3 class="me-ct-name">{{ct.tagName}}</h3>
          </template>

          <template v-else>
            <img class="me-ct-picture" :src="ct.avatar?ct.avatar:defaultAvatar"/>
            <h3 class="me-ct-name">{{ct.categoryName}}</h3>
            <p>{{ct.description}}</p>
          </template>

          <span class="me-ct-meta">{{ct.articles}} 文章</span>
        </div>

        <div class="me-articles me-area">
          <article-scroll-page v-bind="article"></article-scroll-page>
        </div>

      </el-main>
  </div>
</template>

<script>
  import ArticleScrollPage from '@/views/common/ArticleScrollPage'
  import {getTagDetail} from '@/api/tag'
  import {getCategoryDetail} from '@/api/category'
  import defaultAvatar from '@/assets/img/logo.png'


  export default {
    name: 'BlogCategoryTag',
    created() {
      this.getCategoryOrTagAndArticles()
    },
    watch: {
      '$route': 'getCategoryOrTagAndArticles'
    },
    data() {
      return {
        defaultAvatar: defaultAvatar,
        ct: {},
        article: {
          query: {
            tagId: '',
            categoryId: ''
          }
        },
      }
    },
    computed: {
      title() {
        if(this.$route.params.type === 'tag'){
          return `${this.ct.tagName} - 标签`
        }
        return `${this.ct.categoryName} - 文章分类`
      }
    },
    methods: {
      getCategoryOrTagAndArticles() {
        let id = this.$route.params.id
        let type = this.$route.params.type
        if ('tag' === type) {
          this.getTagDetail(id)
          this.article.query.tagId = id
        } else {
          this.getCategoryDetail(id)
          this.article.query.categoryId = id
        }

      },
      getCategoryDetail(id) {
        getCategoryDetail(id).then(data => {
          this.ct = data.data
        }).catch(error => {
          if (error !== 'error') {
            this.$message({type: 'error', message: '文章分类加载失败', showClose: true})
          }
        })
      },
      getTagDetail(id) {
        getTagDetail(id).then(data => {
          this.ct = data.data
        }).catch(error => {
          if (error !== 'error') {
            this.$message({type: 'error', message: '标签加载失败', showClose: true})
          }
        })
      }
    },
    components: {
      ArticleScrollPage
    }
  }
</script>

<style>
  .me-ct-title {
    text-align: center;
    height: 150px;
    padding: 20px;
  }

  .me-ct-picture {
    width: 60px;
    height: 60px;
  }

  .me-ct-name {
    font-size: 28px;
  }

  .me-ct-meta {
    font-size: 12px;
    color: #969696;
  }

  .me-articles {
    width: 70%;
    margin: 30px auto;
  }

</style>
