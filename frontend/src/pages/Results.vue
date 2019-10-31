<template>
  <q-page class="q-mb-xl">
    <div class="q-mt-md q-mx-md" style="white-space:nowrap; overflow:hidden;">
      <div v-for="(item, index) in list" :key="index" class="q-mb-md">
        <a
          v-if="item.parsed"
          href="javascript:void(0)"
          @click="novel(item.url)"
          >{{ item.title }}</a
        >
        <a v-else :href="item.url" @click="novel(item.url)">{{ item.title }}</a>
        <div class="text-caption">
          <span class="text-teal q-mr-sm">{{ item.source }}</span>
          <a :href="item.url">查看源网址</a>
        </div>
        <div class="row">
          <div class="col text-left">
            <q-badge color="primary " v-if="item.recommend">推荐源</q-badge>
          </div>
          <div class="col text-right">
            <q-badge color="primary" v-if="item.parsed">已解析</q-badge>
            <q-badge color="red-12" v-else>未解析</q-badge>
          </div>
        </div>
      </div>
    </div>
  </q-page>
</template>

<script>

import { mapState } from 'vuex'
export default {
  // name: 'PageName',

  computed: {
    ...mapState('lzlook', [
      'list'
    ])
  },
  methods: {
    async novel (url) {
      await this.$store.dispatch('lzlook/novel', { url })
      this.$store.commit('lzlook/update', { isShowTips: true })
      this.$router.push('novel')
    }
  },
  created () {
    this.$store.commit('lzlook/update', { isShowSearchHeader: true })
  }
}
</script>
