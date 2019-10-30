<template>
  <q-page padding class="bg-amber-1">
    <q-card class="bg-orange-2" id="top">
      <q-card-section class="q-pa-sm">
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
      </q-card-section>
    </q-card>
    <scroll-component> </scroll-component>
  </q-page>
</template>

<script>
import ScrollComponent from '../components/ScrollComponent'
import { mapState } from 'vuex'
export default {
  // name: 'PageName',
  computed: {
    ...mapState('lzlook', [
      'novel'
    ])
  },
  components: { ScrollComponent },
  methods: {
    async toChapter (url) {
      await this.$store.dispatch('lzlook/chapter', { url })
      this.$router.push('chapter')
    }
  },
  created () {
    this.$store.commit('lzlook/update', { showSearchHeader: true })
  }
}
</script>
