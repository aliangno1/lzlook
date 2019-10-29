<template>
  <q-page padding class="bg-orange-1">
    <div class="text-center">
      <div class="text-h5 q-my-md">
        {{ novel.name }}
      </div>
    </div>
    <div class="q-pa-sm">
      <div v-for="(chapter, index) in novel.chapters" :key="index">
        <div>
          <span class="q-ml-md" @click="toChapter(chapter.url)">
            <a href="javascript:void(0)">
              {{ chapter.name }}
            </a>
          </span>
        </div>

        <q-separator class="q-my-xs"></q-separator>
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
      'novel'
    ])
  },
  methods: {
    async toChapter (url) {
      await this.$store.dispatch('lzlook/chapter', { url })
      this.$router.push('chapter')
    }
  }
}
</script>
