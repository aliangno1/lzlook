<template>
  <div id="footer" class="bg-grey-3 full-width" v-show="isShowFooter">
    <div class="row full-height">
      <div class="col text-right text-body2 self-center q-pr-md">
        <span @click="showDisclaimer">免责声明</span>
      </div>
    </div>
    <q-dialog v-model="isShowDisclaimer">
      <q-card style="width: 700px; max-width: 80vw;">
        <q-card-section>
          <div class="text-subtitle1">免责声明</div>
        </q-card-section>

        <q-card-section>
          本站是一个开源小说搜索引擎，提供且仅提供第三方搜索引擎提供的小说检索结果。本站只负责展示搜索结果，展示内容与本站无关。
        </q-card-section>

        <q-card-actions align="right" class="bg-white text-teal">
          <q-btn flat label="OK" v-close-popup />
        </q-card-actions>
      </q-card>
    </q-dialog>
  </div>
</template>

<script>
import { mapState } from 'vuex'
export default {
  // name: 'ComponentName',
  data () {
    return {
      isShowDisclaimer: false
    }
  },
  computed: {
    ...mapState('lzlook', [
      'isShowFooter'
    ])
  },
  methods: {
    showDisclaimer () {
      this.isShowDisclaimer = true
    }
  }
}
</script>
<style scoped>
#footer {
  position: fixed;
  bottom: 0%;
  height: 45px;
}
</style>
