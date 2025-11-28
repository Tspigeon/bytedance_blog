<template>
  <!--<transition name="el-zoom-in-center">-->
  <transition>
    <div @click="toTop" v-show="topShow" class="me-to-top">
      <i class="el-icon-caret-top"></i>
      <i>回到顶部</i>
    </div>
  </transition>
</template>

<script>
export default {
  name: "GoTop",
  data() {
    return {
      topShow: false,
    };
  },
  methods: {
    toTop() {
      var scrollToptimer = setInterval(function () {
        var top = document.body.scrollTop || document.documentElement.scrollTop;
        var speed = top / 4;
        if (document.body.scrollTop != 0) {
          document.body.scrollTop -= speed;
        } else {
          document.documentElement.scrollTop -= speed;
        }
        if (top == 0) {
          clearInterval(scrollToptimer);
        }
      }, 30);
      this.topShow = false;
    },
    needToTop() {
      let curHeight =
        document.documentElement.scrollTop || document.body.scrollTop;

      if (curHeight > 400) {
        this.topShow = true;
      } else {
        this.topShow = false;
      }
    },
  },
  mounted() {
    /**
     * 等到整个视图都渲染完毕
     */
    this.$nextTick(function () {
      window.addEventListener("scroll", this.needToTop);
    });
  },
};
</script>

<style lang="less">
.me-to-top {
  background-color: #fff;
  position: fixed;
  right: 60px;
  bottom: 100px;
  width: 50px;
  height: 110px;
  border-radius: 30px;
  cursor: pointer;
  transition: 0.3s;
  box-shadow: 0 0 6px rgba(0, 0, 0, 0.12);
  z-index: 5;
  i {
    margin: auto;
    margin-top: 5px;
    color: #5fb878;
    display: block;
    line-height: 40px;
    text-align: center;
    writing-mode: vertical-lr;
    font-style: normal;
  }
}
</style>
