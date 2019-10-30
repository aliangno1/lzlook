<template>
  <q-page padding class="bg-amber-1">
    <q-card class="bg-orange-2" id="top">
      <q-card-section>
        <div class="text-center">
          <div class="text-h5 q-mb-lg q-mt-sm">
            {{ chapter.name }}
          </div>
        </div>
        <div id="content" v-html="chapter.content" class="q-mx-md text-subtitle1"></div>
        <div class="row q-mt-md">
          <div class="col text-center">
            <q-btn @click="toChapter(chapter.previous)" label="上一章"></q-btn>
          </div>
          <div class="col text-center">
            <q-btn @click="toChapter(chapter.next)" label="下一章"></q-btn>
          </div>
        </div>
      </q-card-section>
    </q-card>
    <!-- <div class="q-pa-sm q-mx-sm"></div> -->
  </q-page>
</template>

<script>
import { mapState } from 'vuex'
export default {
  // name: 'PageName',
  computed: {
    ...mapState('lzlook', [
      'chapter'
    ])
  },
  methods: {
    async toChapter (url) {
      await this.$store.dispatch('lzlook/chapter', { url })
      window.scroll(0, 0)
    }
  },
  created () {
    this.$store.commit('lzlook/update', { showSearchHeader: true })
  }
}
</script>
<style scoped>
</style>
