<template>
  <div class="me-index-container">
    <div class="me-index-1">
      <h1>
        <strong> 为什么 </strong>
        选择云上？
      </h1>
      <p>
        打造一个多人经验分享博客平台，在这里你可以记录、分享自己的学习经验和笔记
      </p>
    </div>

    <div class="me-index-2">
      <div class="index-2-1">
        <h2>分享经验</h2>
        <p>
          这里有各学科知识笔记、学习经验分享、工作面试经验交流，无论是老师还是在校学生都能在云上找到想要的。
        </p>
      </div>
      <div class="index-2-2">
        <img src="../assets/img/1.jpg" />
      </div>
    </div>

    <div class="me-index-3">
      <div class="index-3-1">
        <img src="../assets/img/2.jpg" />
      </div>
      <div class="index-3-2">
        <h2>学习交友</h2>
        <p>
          这里你可以找到志同道合的学习伙伴，讨论学习与经验，寻找属于自己的伯乐。
        </p>
      </div>
    </div>

    
    
  </div>
</template>

<script>
import anime from "animejs";

export default {
  data() {
    return {};
  },
  mounted() {
    /**
     * 等到整个视图都渲染完毕
     */
    this.firstAnimate();
    this.$nextTick(function () {
      window.addEventListener("scroll", this.handlerTop);
    });
  },
  methods: {
    handlerTop() {
      let top = document.body.scrollTop || document.documentElement.scrollTop;
      if (top === 0) {
        this.firstAnimate();
      } else if (top > 180&&top<560) {
        this.secondAnimate();
      }else if(top > 560){
        this.thirdAnimate();
      }
    },
    // 入场动画
    firstAnimate() {
      anime({
        targets: ".me-index-1",
        translateX: [-20, 0],
        opacity: [0, 1],
        easing: "linear",
        duration: 500,
        delay: 300
      });
    },
    secondAnimate() {
      anime({
        targets: ".me-index-2",
        translateY: -100,
        duration: 5000,
      });
    },
    thirdAnimate(){
      anime({
        targets: ".me-index-3",
        translateY: -100,
        duration: 5000,
      });
    },
  },
};
</script>

<style lang="less" scoped>
.me-index-container {
  margin: auto;
  background: #f3f3f3;
  .me-index-1 {
    padding: 15% 10% 20% 10%;
    width: 80%;
    text-align: center;
    h1 {
      font-size: 32px;
      strong {
        color: rgb(157, 164, 175);
      }
    }
  }
  .me-index-2 {
    display: flex;
    margin: 50px 50px;
    height: 350px;
    background: #fff;
    border-radius: 20px;
    .index-2-1 {
      margin-left: 50px;
      padding-top: 50px;
      width: 35%;
      p {
        padding-top: 50px;
      }
    }
    .index-2-2 {
      display: flex;
      margin: auto 50px;
      width: 65%;
      height: 80%;
      overflow: hidden;
      img {
        width: 100%;
        height: 100%;
        object-fit: cover;
      }
    }
  }

  .me-index-3 {
    display: flex;
    margin: 50px 50px;
    height: 400px;
    background: #fff;
    border-radius: 20px;
    .index-3-1 {
      display: flex;
      margin: auto 50px;
      width: 65%;
      height: 80%;
      overflow: hidden;
      img {
        width: 100%;
        height: 100%;
        object-fit: cover;
      }
    }
    .index-3-2 {
      margin-left: 20px;
      padding-top: 100px;
      width: 35%;
      p {
        padding-top: 50px;
      }
    }
  }
}
</style>