<template>
  <el-card>
    <h3 style="text-align:center; color:#5FB878; ">欢迎您！</h3><br>
    <p class="me-author-name">{{user.nickName}}</p>
    <div style="padding: 18px 15px 0 0; text-align: center;">
      <span><i class="el-icon-location-outline"></i> {{city}}</span>
    </div>
  </el-card>

</template>

<script>
  export default {
    name: 'CardMe',
    data() {
      return {
        city:''
      }
    },
    mounted(){
      if(this.$store.state.account.length != 0){
        AMap.plugin("AMap.Geolocation", () => {
        var geolocation = new AMap.Geolocation({
          // 是否使用高精度定位，默认：true
          enableHighAccuracy: false,
          // 设置定位超时时间，默认：无穷大
          timeout: 10000,
        });
 
          geolocation.getCityInfo((status, res) => {   //只能获取当前用户所在城市和城市的经纬度
            if (status == "complete") {
                // console.log("res",res)
                this.city=res.city;
            }else{
           this.city='未查询到所在城市';
           }
           })
      })}
    },
    computed: {
      user() {
        let login = this.$store.state.account.length != 0
        let avatar = this.$store.state.avatar
        let nickName = this.$store.state.name
        return {
          login, avatar,nickName
        }
      },
    },
  }
</script>

<style scoped>
  .me-author-name {
    text-align: center;
    font-size: 22px;
    border-bottom: 1px solid #5FB878;
  }
</style>
