<template>
  <q-page class="bg-amber-1 q-pb-md">
    <div v-if="!chapter || !chapter.url">
      <q-item>
        <q-item-section>
          <q-item-label class="text-center">
            暂无当前章节
          </q-item-label>
        </q-item-section>
      </q-item>
    </div>
    <div v-else class="q-pa-sm">
      <q-card class="bg-orange-2" id="top">
        <div class="q-pl-sm q-pt-xs">
          <q-btn flat @click="showDrawer" rounded dense icon="fas fa-list-ul" />
        </div>
        <q-card-section>
          <div class="text-center">
            <div class="text-h5 q-mb-lg q-mt-sm">
              {{ chapter.name }}
            </div>
          </div>
          <div
            id="content"
            v-html="chapter.content"
            class="q-mx-md text-subtitle1"
          ></div>
          <div class="row q-mt-md">
            <div class="col text-center">
              <q-btn
                @click="toChapter(chapter.previous)"
                label="上一章"
              ></q-btn>
            </div>
            <div class="col text-center">
              <q-btn @click="next(chapter.next)" label="下一章"></q-btn>
            </div>
          </div>
        </q-card-section>
      </q-card>
      <q-drawer
        v-model="drawer"
        :width="200"
        :breakpoint="500"
        overlay
        bordered
        content-class="bg-grey-2"
      >
        <q-scroll-area class="fit" ref="scrollArea">
          <q-list v-for="(item, index) in novel.chapters" :key="index">
            <q-item clickable v-ripple dense @click="toItemChapter(item.url)">
              <q-item-section>
                <q-item-label
                  caption
                  v-if="item.name === chapter.name"
                  class="text-red"
                >
                  {{ item.name }}
                </q-item-label>
                <q-item-label caption v-else>
                  {{ item.name }}
                </q-item-label>
              </q-item-section>
            </q-item>
            <q-separator v-if="index < novel.chapters.length - 1"></q-separator>
          </q-list>
        </q-scroll-area>
      </q-drawer>
    </div>
  </q-page>
</template>

<script>
import { mapState } from 'vuex'
export default {
  // name: 'PageName',
  data () {
    return {
      drawer: false
    }
  },
  computed: {
    ...mapState('lzlook', ['chapter', 'novel']),
    position () {
      const current = this.novel.chapters.filter(item => {
        return item.name === this.chapter.name
      })[0]
      return (this.novel.chapters.indexOf(current) - 4) * 33
    }
  },
  methods: {
    async toChapter (url) {
      await this.$store.dispatch('lzlook/chapter', { url })
      window.scroll(0, 0)
    },
    async next (url) {
      if (
        this.novel.chapters[this.novel.chapters.length - 1].name ===
        this.chapter.name
      ) {
        await this.$store.dispatch('lzlook/novel', { url: this.novel.source })
        if (
          this.novel.chapters[this.novel.chapters.length - 1].name ===
          this.chapter.name
        ) {
          alert('已是最新章节')
        } else {
          const newChapterIndex = this.novel.chapters.indexOf(this.chapter.name)
          const newChapter = this.novel.chapters[newChapterIndex + 1]
          this.$store.commit('lzlook/update', { chapter: newChapter })
          await this.$store.dispatch('lzlook/chapter', { url: newChapter.url })
        }
      } else {
        await this.$store.dispatch('lzlook/chapter', { url })
      }
      window.scroll(0, 0)
    },
    async toItemChapter (url) {
      if (url !== this.chapter.url) {
        await this.toChapter(url)
      }
      this.drawer = false
    },
    showDrawer () {
      this.drawer = !this.drawer
      this.scroll()
      // this.animateScroll()
    },
    scroll () {
      this.$refs.scrollArea.setScrollPosition(this.position)
    },
    animateScroll () {
      this.$refs.scrollArea.setScrollPosition(this.position, 300)
    }
  },
  created () {
    this.$store.commit('lzlook/update', {
      isShowSearchHeader: true,
      isShowFooter: false
    })
  },
  destroyed () {
    this.$store.commit('lzlook/update', { isShowFooter: true })
  }
}
</script>
<style scoped></style>
