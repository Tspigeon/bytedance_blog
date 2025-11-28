<template>
  <div class="me-view-comment-item">
    <!-- 评论主用户 -->
    <div class="me-view-comment-author">
      <a>
        <!-- <img class="me-view-picture" :src="comment.author.avatar" /> -->
        <span class="me-view-avatar"
          ><img class="me-view-picture" src="../../assets/img/bg.jpg"
        /></span>
        <span class="me-view-nickname">{{ comment.author.nickname }}</span>
      </a>
      <div class="me-view-meta">
        <span>{{ comment.createDate | format }}</span>
        <span>{{ rootCommentCounts - index }}楼</span>
      </div>
    </div>
    <!-- 评论主体 -->
    <div class="me-view-comment-main">
      <p class="me-view-comment-content">{{ comment.content }}</p>
      <div class="me-view-comment-tools">
        <a class="me-view-comment-tool" style="margin-left: 5px"
          ><i class="el-icon-caret-top"></i>12</a
        >
        <a
          class="me-view-comment-tool"
          @click="showComment(-1, comment.author)"
        >
          <i class="el-icon-edit-outline"></i>&nbsp; 评论
        </a>

        <el-button
          size="small"
          v-if="comment.author.nickname == login.account"
          icon="el-icon-delete"
          type="text"
          style="color: #f56c6c"
          @click="confirmDelete(comment.id)"
          >删除</el-button
        >
      </div>

      <div class="me-reply-list">
        <div class="me-reply-item" v-for="c in comment.childrens" :key="c.id">
          <div style="font-size: 14px">
            <span class="me-reply-user"
              >{{ c.author.nickname }}:&nbsp;&nbsp;</span
            >

            <span v-if="c.level == 2" class="me-reply-user"
              >@{{ c.toUser.nickname }}
            </span>

            <span style="font-size: 13px">{{ c.content }}</span>
          </div>
          <div class="me-view-meta">
            <span style="padding-right: 10px">{{ c.createDate | format }}</span>
            <a
              class="me-view-comment-tool"
              @click="showComment(c.id, c.author)"
            >
              <i class="el-icon-edit-outline"></i>&nbsp;回复
            </a>
          </div>
        </div>

        <div class="me-view-comment-write" v-show="commentShow">
          <template>
            <el-input
              v-model="reply.content"
              type="input"
              style="width: 90%"
              :placeholder="placeholder"
              class="me-view-comment-text"
              resize="none"
            >
            </el-input>

            <el-button
              style="margin-left: 8px"
              @click="publishComment()"
              type="text"
              >评论</el-button
            >
          </template>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import { publishComment, deleteCommentById } from "@/api/comment";

export default {
  name: "CommentItem",
  props: {
    articleId: Number,
    comment: Object,
    index: Number,
    rootCommentCounts: Number,
  },
  data() {
    return {
      placeholder: "你的评论...",
      commentShow: false,
      commentShowIndex: "",
      reply: this.getEmptyReply(),
      login: this.$store.state,
    };
  },
  methods: {
    showComment(commentShowIndex, toUser) {
      this.reply = this.getEmptyReply();

      if (this.commentShowIndex !== commentShowIndex) {
        if (toUser) {
          this.placeholder = `@${toUser.nickname} `;
          this.reply.toUserId = toUser.id;
        } else {
          this.placeholder = "你的评论...";
        }
        this.commentShow = true;
        this.commentShowIndex = commentShowIndex;
      } else {
        this.commentShow = false;
        this.commentShowIndex = "";
      }
    },
    //添加评论
    publishComment() {
      if (!this.reply.content) {
        return;
      }
      publishComment(this.reply, this.$store.state.token)
        .then((data) => {
          if (data.success) {
            this.$message({
              type: "success",
              message: "评论成功",
              showClose: true,
            });
            if (!this.comment.childrens) {
              this.comment.childrens = [];
            }
            this.comment.childrens.unshift(data.data);
            this.$emit("commentCountsIncrement");
            this.showComment(this.commentShowIndex);
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
      setTimeout(() => {
        this.$router.go(0);
      }, 2000);
    },
    // 确认删除
    confirmDelete(id) {
      this.$confirm("确定删除该评论？", "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning",
      })
        .then(() => {
          this.deleteComment(id);
          setTimeout(() => {
            this.$router.go(0);
          }, 500);
        })
        .catch(() => {
          this.$message({
            type: "info",
            message: "已取消删除",
          });
        });
    },
    //删除评论
    deleteComment(id) {
      deleteCommentById(id)
        .then((data) => {
          if (data.success) {
            this.$message({
              type: "success",
              message: "删除评论成功",
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

    getEmptyReply() {
      return {
        articleId: this.articleId,
        parent: this.comment.id,
        toUserId: "",
        content: "",
      };
    },
  },
};
</script>


<style lang="less" scoped>
.me-view-comment-item {
  margin-top: 20px;
  padding-bottom: 16px;
  border-bottom: 1px solid #f0f0f0;
  background: #f8f8f8;
  border-radius: 10px;
  box-shadow: 0 0 6px rgba(0, 0, 0, 0.12);

  .me-view-comment-author {
    display: flex;
    padding: 10px 10px;
    vertical-align: middle;
    a {
      display: flex;
      .me-view-avatar {
        display: flex;
        margin: 0 5px;
        height: 30px;
        width: 30px;
        overflow: hidden;
        border-radius: 10px;
      }

      .me-view-nickname {
        line-height: 30px;
        font-size: 14px;
        color: #888;
      }
      .me-view-nickname:hover {
        color: #5fb878;
      }
    }

    .me-view-meta {
      line-height: 30px;
      margin: 0 0 0 auto;
      color: #c9c9c9;
      font-size: 12px;
      span {
        padding-right: 10px;
      }
    }
  }

  .me-view-comment-main {
    padding-left: 10px;
    .me-view-comment-content {
      font-size: 13px;
      line-height: 1.5;
    }
    .me-view-comment-tools {
      margin-top: 4px;
      margin-bottom: 10px;
    }
    .me-view-comment-tool {
      font-size: 13px;
      color: #a6a6a6;
      padding-right: 20px;
      padding-left: 10px;
    }
    .me-view-comment-tool:hover {
      color: #5fb878;
    }
    .me-view-meta {
      font-size: 13px;
      color: #a6a6a6;
    }
  }
}

.me-reply-list {
  padding-left: 16px;
  border-left: 4px solid #c5cac3;
  .me-reply-item {
    padding: 8px 0;
    border-bottom: 1px solid #f0f0f0;
  }
  .me-reply-user {
    color: #78b6f7;
  }
}
</style>
