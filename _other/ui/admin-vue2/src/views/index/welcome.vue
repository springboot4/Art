<template>
  <!--  <a-card :bordered="false" style="display: flex;justify-content:center;height: 100%" >
    &lt;!&ndash;    <div style="margin:100px auto;">
      <img src="~@/assets/welcome.png" class="logo" alt="logo">
    </div>&ndash;&gt;
    <div style="margin:100px auto; width: auto;height: auto">-->
  <div style="display: flex;justify-content:center;height: 100%" >
    <iframe
      :src="htmlUrl"
      ref="iframe"
      id="bdIframe"
      frameborder="0"
      scrolling="no"></iframe>
  </div>

<!--    </div>
  </a-card>-->
</template>

<script>
  export default {
    name: 'Welcome',
    data () {
      return {
        htmlUrl: 'static/2.html'
      }
    },
    mounted () {
      /**
       * iframe-宽高自适应显示
       */
      const oIframe = document.getElementById('bdIframe')
      const deviceWidth = document.documentElement.clientWidth
      const deviceHeight = document.documentElement.clientHeight
      oIframe.style.width = (Number(deviceWidth) - 220) + 'px' // 数字是页面布局宽度差值
      oIframe.style.height = (Number(deviceHeight) - 120) + 'px' // 数字是页面布局高度差
    }
  }
</script>

<style lang="less" scoped>

</style>
