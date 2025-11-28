<template>
  <div id="app">
    <router-view/>
    <go-top></go-top>
  </div>
</template>

<script>
import GoTop from '@/components/gotop/GoTop'
export default {
  name: 'App',
  components: { GoTop }
}
</script>

<style>
</style>
