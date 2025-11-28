<template>
  <div>
      <el-main>
        <el-tabs v-model="activeName">
          <!-- 文章分类 -->
          <el-tab-pane label="文章分类" name="category">
            <ul class="me-allct-items">
              <li
                v-for="c in categorys"
                @click="view(c.id)"
                :key="c.id"
                class="me-allct-item"
              >
                <div class="me-allct-content">
                  <a class="me-allct-info">
                    <img
                      class="me-allct-img"
                      :src="c.avatar ? c.avatar : defaultAvatar"
                    />
                    <h4 class="me-allct-name">{{ c.categoryName }}</h4>
                    <p class="me-allct-description">{{ c.description }}</p>
                  </a>

                  <div class="me-allct-meta">
                    <span>{{ c.articles }} 文章</span>
                  </div>
                </div>
              </li>
            </ul>
          </el-tab-pane>
          <!-- 文章标签 -->
          <el-tab-pane label="文章标签" name="tag">
            <ul class="me-allct-items">
              <li
                v-for="t in tags"
                @click="view(t.id)"
                :key="t.id"
                class="me-allct-item"
              >
                <div class="me-allct-content">
                  <a class="me-allct-info">
                    <img
                      class="me-allct-img"
                      :src="t.avatar ? t.avatar : defaultAvatar"
                    />
                    <h4 class="me-allct-name">{{ t.tagName }}</h4>
                  </a>

                  <div class="me-allct-meta">
                    <span>{{ t.articles }} 文章</span>
                  </div>
                </div>
              </li>
            </ul>
          </el-tab-pane>
        </el-tabs>
      </el-main>
  </div>
</template>

<script>
import defaultAvatar from "@/assets/img/logo.png";
import { getAllCategorysDetail } from "@/api/category";
import { getAllTagsDetail } from "@/api/tag";

export default {
  name: "BlogAllCategoryTag",
  created() {
    this.getCategorys();
    this.getTags();
  },
  data() {
    return {
      defaultAvatar: defaultAvatar,
      categorys: [],
      tags: [],
      currentActiveName: "category",
    };
  },
  computed: {
    activeName: {
      get() {
        return (this.currentActiveName = this.$route.params.type);
      },
      set(newValue) {
        this.currentActiveName = newValue;
      },
    },
    categoryTagTitle() {
      if (this.currentActiveName == "category") {
        return "文章分类";
      }
      return "标签";
    },
  },
  methods: {
    view(id) {
      this.$router.push({ path: `/${this.currentActiveName}/${id}` });
    },
    getCategorys() {
      getAllCategorysDetail()
        .then((data) => {
          this.categorys = data.data;
        })
        .catch((error) => {
          if (error !== "error") {
            this.$message({
              type: "error",
              message: "文章分类加载失败",
              showClose: true,
            });
          }
        });
    },
    getTags() {
      getAllTagsDetail()
        .then((data) => {
          this.tags = data.data;
        })
        .catch((error) => {
          if (error !== "error") {
            this.$message({
              type: "error",
              message: "标签加载失败",
              showClose: true,
            });
          }
        });
    },
  },
};
</script>

<style scoped>
.el-main {
  padding: 0 60px;
}

.me-allct-items {
  padding-top: 2rem;
}

.me-allct-item {
  width: 25%;
  display: inline-block;
  margin-bottom: 2.4rem;
  padding: 0 0.7rem;
  box-sizing: border-box;
}

.me-allct-content {
  display: inline-block;
  width: 100%;
  background-color: #f9f9f9;
  border-radius: 20px;
  text-align: center;
  box-shadow: 0 0 6px rgba(0, 0, 0, 0.12);
  padding: 20px 0;
}

.me-allct-info {
  cursor: pointer;
}

.me-allct-img {
  margin: -40px 0 10px;
  border-radius: 60px;
  box-shadow: 0 0 6px rgba(0, 0, 0, 0.15);
  width: 60px;
  height: 60px;
  vertical-align: middle;
}

.me-allct-name {
  font-size: 21px;
  font-weight: 150;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
  margin-top: 4px;
}

.me-allct-description {
  min-height: 50px;
  font-size: 13px;
  line-height: 25px;
}

.me-allct-meta {
  font-size: 12px;
  color: #969696;
}
</style>
