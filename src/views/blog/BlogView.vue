<template>
  <div>
    <el-container class="me-view-container">
      <el-main>
        <div class="me-view-card">
          <!-- 文章标题 -->
          <h1 class="me-view-title">{{ article.title }}</h1>

          <!-- 文章简介 -->
          <div class="me-view-author">
            <a @click="toInfo(article.author)">
              <img class="me-view-picture" :src="article.author.avatar" />
              <span>{{ article.author }}</span>
            </a>
            <el-button
              v-if="!isFollow"
              icon="el-icon-plus"
              round
              type="primary"
              size="mini"
              @click="isFollow = !isFollow"
              >关注</el-button
            >
            <el-button
              v-else
              icon="el-icon-close"
              type="warning"
              round
              size="mini"
              @click="isFollow = !isFollow"
              >取关</el-button
            >

            <div class="me-view-info">
              <span>{{ article.createDate | format }}</span>
              <span>阅读 {{ article.viewCounts }}</span>
              <span>评论 {{ article.commentCounts }}</span>
            </div>
            <div class="me-author-tools">
              <el-button
                v-if="this.article.author == this.$store.state.account"
                @click="editArticle()"
                size="mini"
                round
                type="primary"
                icon="el-icon-edit"
                >编辑</el-button
              >
              <el-button
                v-if="this.article.author == this.$store.state.account"
                @click="confirmDelete()"
                size="mini"
                round
                type="danger"
                icon="el-icon-delete"
              >
                删除
              </el-button>

              <el-button
                v-else-if="!isFavorite"
                size="small"
                type="primary"
                @click="isFavorite = !isFavorite"
                round
                icon="el-icon-star-off"
                style="margin-right: 40px"
              >
                收藏文章
              </el-button>

              <el-button
                v-else
                size="small"
                @click="isFavorite = !isFavorite"
                round
                icon="el-icon-star-on"
                style="margin-right: 40px; color: #5fb878"
              >
                取消收藏
              </el-button>
            </div>
          </div>

          <!-- 文章主体 -->
          <div class="me-view-content" id="content">
            <markdown-editor :editor="article.editor"></markdown-editor>
          </div>

          <div class="me-view-end">
            <el-alert
              title="感谢阅读！"
              type="success"
              description="感谢您对本作者的支持，感谢您对云上博客的支持！"
              center
            >
            </el-alert>
          </div>

          <div></div>

          <div class="me-view-tag">
            标签：
            <el-button
              @click="tagOrCategory('tag', t.id)"
              size="mini"
              type="primary"
              v-for="t in article.tags"
              :key="t.id"
              round
              plain
              >{{ t.tagName }}</el-button
            >
          </div>

          <div class="me-view-tag">
            文章分类：
            <el-button
              @click="tagOrCategory('category', article.category.id)"
              size="mini"
              type="primary"
              round
              plain
              >{{ article.category.categoryName }}</el-button
            >
          </div>

          <div class="me-view-comment">
            <div class="me-view-comment-write">
              <el-row :gutter="20">
                <el-col :span="2">
                  <a class="">
                    <img class="me-view-picture" :src="avatar" />
                  </a>
                </el-col>
                <el-col :span="22">
                  <el-input
                    type="textarea"
                    :autosize="{ minRows: 2 }"
                    placeholder="你的评论..."
                    class="me-view-comment-text"
                    v-model="comment.content"
                    resize="none"
                  >
                  </el-input>
                </el-col>
              </el-row>

              <el-row :gutter="20">
                <el-col :span="2" :offset="22" class="commentBt">
                  <el-button type="primary" @click="publishComment()"
                    >评论</el-button
                  >
                </el-col>
              </el-row>
            </div>

            <div class="me-view-comment-title">
              <span>{{ article.commentCounts }} 条评论</span>
            </div>

            <commment-item
              v-for="(c, index) in comments"
              :comment="c"
              :articleId="article.id"
              :index="index"
              :rootCommentCounts="comments.length"
              @commentCountsIncrement="commentCountsIncrement"
              :key="c.id"
            >
            </commment-item>
          </div>
        </div>
      </el-main>
    </el-container>
  </div>
</template>

<script>
import MarkdownEditor from "@/components/markdown/MarkdownEditor";
import CommmentItem from "@/components/comment/CommentItem";
import { viewArticle, deleteArticleById } from "@/api/article";
import { getCommentsByArticle, publishComment } from "@/api/comment";

import default_avatar from "@/assets/img/default_avatar.png";

export default {
  name: "BlogView",
  data() {
    return {
      article: {
        id: "",
        title: "",
        commentCounts: 0,
        viewCounts: 0,
        summary: "",
        author: {},
        tags: [],
        category: {},
        createDate: "",
        editor: {
          value: "",
          toolbarsFlag: false,
          subfield: false,
          defaultOpen: "preview",
        },
      },
      isFavorite: true,
      isFollow: false,
      comments: [],
      comment: {
        article: {},
        content: "",
      },
    };
  },

  created() {
    this.getArticle();
  },

  computed: {
    avatar() {
      let avatar = this.$store.state.avatar;

      if (avatar != null) {
        return avatar;
      }
      return default_avatar;
    },
    title() {
      return `${this.article.title} - 文章`;
    },
  },

  watch: {
    $route: "getArticle",
  },

  methods: {
    //标签分类跳转
    tagOrCategory(type, id) {
      this.$router.push({ path: `/${type}/${id}` });
    },

    //编辑文章
    editArticle() {
      this.$router.push({ path: `/write/${this.article.id}` });
    },
    //文章初始化
    getArticle() {
      viewArticle(this.$route.params.id)
        .then((data) => {
          Object.assign(this.article, data.data);
          this.article.editor.value = data.data.body.content;

          this.getCommentsByArticle();
        })
        .catch((error) => {
          if (error !== "error") {
            this.$message({
              type: "error",
              message: "文章加载失败",
              showClose: true,
            });
          }
        });
    },

    //上传评论
    publishComment() {
      if (!this.comment.content) {
        return;
      }
      this.comment.article.id = this.article.id;
      let parms = { articleId: this.article.id, content: this.comment.content };
      publishComment(parms, this.$store.state.token)
        .then((data) => {
          if (data.success) {
            this.comment.content = "";
            this.comments.unshift(data.data);
            this.commentCountsIncrement();
            this.getCommentsByArticle();
            this.$message({
              type: "success",
              message: "评论成功",
              showClose: true,
            });
          } else {
            this.$message({
              type: "error",
              message: data.msg,
              showClose: true,
            });
          }
        })
        .catch((error) => {
          if (error !== "error") {
            this.$message({
              type: "error",
              message: "评论失败",
              showClose: true,
            });
          }
        });
    },

    //加载评论
    getCommentsByArticle() {
      getCommentsByArticle(this.article.id)
        .then((data) => {
          if (data.success) {
            this.comments = data.data;
            // console.log(this.comments)
          } else {
            this.$message({
              type: "error",
              message: "评论加载失败",
              showClose: true,
            });
          }
        })
        .catch((error) => {
          if (error !== "error") {
            this.$message({
              type: "error",
              message: "评论加载失败",
              showClose: true,
            });
          }
        });
    },

    commentCountsIncrement() {
      this.article.commentCounts += 1;
    },

    // 确认删除
    confirmDelete() {
      this.$confirm("确定删除该文章？", "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning",
      })
        .then(() => {
          this.deleteArticle();
          setTimeout(() => {
            this.$router.push("/all");
          }, 500);
        })
        .catch(() => {
          this.$message({
            type: "info",
            message: "已取消删除",
          });
        });
    },
    //删除文章
    deleteArticle() {
      deleteArticleById(this.article.id)
        .then((data) => {
          if (data.success) {
            this.$message({
              type: "success",
              message: "删除成功",
              showClose: true,
            });
          } else {
            this.$message({
              type: "error",
              message: data.msg,
              showClose: true,
            });
          }
        })
        .catch((error) => {
          if (error !== "error") {
            this.$message({
              type: "error",
              message: "删除失败",
              showClose: true,
            });
          }
        });
    },
  },
  components: {
    "markdown-editor": MarkdownEditor,
    CommmentItem,
  },
  //组件内的守卫 调整body的背景色
  // beforeRouteEnter(to, from, next) {
  //   window.document.body.style.backgroundColor = "#fff";
  //   next();
  // },
  // beforeRouteLeave(to, from, next) {
  //   window.document.body.style.backgroundColor = "#f5f5f5";
  //   next();
  // },
};
</script>

<style lang="less" scoped>
.me-view-container {
  width: 800px;
  margin: 0 auto;
}

.el-main {
  overflow: hidden;
  .me-view-title {
    font-size: 38px;
    font-weight: 800;
    line-height: 1.3;
    font-family: "Gill Sans", "Gill Sans MT", Calibri, "Trebuchet MS",
      sans-serif;
  }

  .me-view-author {
    position: relative;
    margin: 20px 0;
    vertical-align: middle;
    .me-view-info {
      position: absolute;
      right: 200px;
      bottom: -5px;
      font-size: 14px;
      color: #969696;
    }
    .me-author-tools {
      position: absolute;
      right: 10px;
      bottom: -10px;
    }
  }

  .me-view-end {
    margin-top: 20px;
  }
}

.me-view-picture {
  width: 40px;
  height: 40px;
  border: 1px solid #ddd;
  border-radius: 50%;
  vertical-align: middle;
  background-color: #5fb878;
}

.me-view-tag {
  margin-top: 20px;
  padding-left: 6px;
  border-left: 4px solid #c5cac3;
}

.me-view-tag-item {
  margin: 0 4px;
}

.me-view-comment {
  margin-top: 60px;
  .me-view-comment-title {
    font-weight: 600;
    border-bottom: 1px solid #f0f0f0;
    padding-bottom: 20px;
  }

  .me-view-comment-write {
    margin-top: 20px;
  }

  .me-view-comment-text {
    font-size: 16px;
  }

  .commentBt {
    margin-left: 690px !important;
    margin-top: 10px;
  }
}
</style>

