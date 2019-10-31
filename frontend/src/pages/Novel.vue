<template>
  <q-page class="bg-amber-1 q-mb-xl q-px-sm q-pt-xs">
    <div class="q-pb-xs q-gutter-sm" v-if="isShowTips">
      <q-banner
        inline-actions
        rounded
        class="bg-amber-3 q-pa-xs"
        style="font-size:0.8em"
      >
        注意！页面内容来自<span
          ><a :href="novel.source">{{ novel.source }}</a></span
        >，本站不储存任何内容，为了更好的阅读体验进行在线解析，若有广告出现，请及时反馈。若您觉得侵犯了您的利益，请通知我们进行删除，然后访问
        <span><a :href="novel.source">原网页</a></span>
        <template v-slot:action>
          <q-btn
            flat
            rounded
            icon="close"
            class="q-pa-sm"
            @click="isShowTips = false"
          />
        </template>
      </q-banner>
    </div>
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
    ]),
    isShowTips: {
      get () {
        return this.$store.state.lzlook.isShowTips
      },
      set (value) {
        this.$store.commit('lzlook/update', { isShowTips: value })
      }
    }
  },
  components: { ScrollComponent },
  methods: {
    async toChapter (url) {
      await this.$store.dispatch('lzlook/chapter', { url })
      this.$router.push('chapter')
    }
  },
  created () {
    this.$store.commit('lzlook/update', { isShowSearchHeader: true })
  }
}
</script>
