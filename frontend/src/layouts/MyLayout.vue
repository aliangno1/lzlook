<template>
  <q-layout view="lHh Lpr lff" class="shadow-2 rounded-borders">
    <q-header elevated reveal class="bg-cyan-8">
      <q-toolbar>
        <q-btn flat @click="drawer = !drawer" round dense icon="menu" />
        <q-space></q-space>
        <q-form @submit="search(keyword)">
          <q-input
            dark
            dense
            standout
            v-model="keyword"
            input-class="text-left"
            class="q-ml-md"
            @focus="isShowSearchHistory = true"
            @blur="isShowSearchHistory = false"
          >
            <template v-slot:append>
              <q-icon v-if="keyword === ''" name="search" />
              <q-icon
                v-else
                name="clear"
                class="cursor-pointer"
                @click="keyword = ''"
              />
            </template>
          </q-input>
          <q-list
            bordered
            v-show="isShowSearchHistory && searchRecords.length > 0"
            style="position:fixed;width:200px"
            class="q-mt-sm q-ml-md "
          >
            <div
              v-for="(item, index) in searchRecords"
              :key="index"
              @click="searchHistory(item)"
            >
              <q-item
                dense
                clickable
                class="q-pa-none q-pl-sm text-black bg-white"
              >
                <q-item-section>
                  <q-item-label>{{ item }}</q-item-label>
                </q-item-section>
              </q-item>
            </div>
          </q-list>
        </q-form>
      </q-toolbar>
    </q-header>

    <q-footer>
      <!-- <q-toolbar-title>Footer</q-toolbar-title> -->
      <div id="footer" class="bg-grey-3 full-width" v-show="isShowFooter">
        <div class="row full-height">
          <div class="col text-left text-body2 self-center q-px-md text-black">
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
    </q-footer>

    <q-drawer v-model="drawer" :width="200" :breakpoint="400">
      <q-scroll-area
        style="height: calc(100% - 150px); margin-top: 150px; border-right: 1px solid #ddd"
      >
        <q-list padding class="text-black">
          <q-item clickable v-ripple @click="to('/')">
            <q-item-section>
              首页
            </q-item-section>
          </q-item>

          <q-item clickable v-ripple @click="to('/results')">
            <q-item-section>
              搜索结果
            </q-item-section>
          </q-item>

          <q-item clickable v-ripple @click="to('/novel')">
            <q-item-section>
              章节目录
            </q-item-section>
          </q-item>

          <q-item clickable v-ripple @click="to('/chapter')">
            <q-item-section>
              当前章节
            </q-item-section>
          </q-item>
          <q-separator></q-separator>
          <q-item clickable v-ripple>
            <q-item-section>
              退出
            </q-item-section>
          </q-item>
        </q-list>
      </q-scroll-area>

      <q-img
        class="absolute-top"
        src="https://cdn.quasar.dev/img/material.png"
        style="height: 150px"
        @click="toLogin()"
      >
        <div class="absolute-bottom bg-transparent">
          <q-avatar size="56px" class="q-mb-sm">
            <img :src="isLogin ? user.avatar : avatar" />
          </q-avatar>

          <div v-if="isLogin" class="text-weight-bold">{{ user.name }}</div>
          <div v-else>
            点击头像登录
          </div>
          <!-- <div>@rstoenescu</div> -->
        </div>
      </q-img>
    </q-drawer>
    <!-- <header-component></header-component> -->
    <q-page-container>
      <router-view />
    </q-page-container>
    <!-- <footer-component></footer-component> -->
  </q-layout>
</template>

<script>
// import HeaderComponent from '../components/HeaderComponent'
// import FooterComponent from '../components/FooterComponent'
import { storage } from '../util/storage'
import { mapState } from 'vuex'
export default {
  data () {
    return {
      drawer: false,
      avatar: '../statics/avatar/user.png',
      isShowDisclaimer: false,
      isShowSearchHistory: false
    }
  },
  computed: {
    keyword: {
      get () {
        return this.$store.state.lzlook.keyword
      },
      set (value) {
        this.$store.commit('lzlook/update', { keyword: value })
      }
    },
    searchRecords: function () {
      const records = this.$store.state.lzlook.searchRecords.slice()
      return records.filter(item => item.indexOf(this.keyword) === 0).reverse()
    },
    ...mapState('lzlook', ['user', 'isLogin', 'isShowFooter'])
  },
  methods: {
    async search (keyword) {
      if (!keyword) {
        return
      }
      await this.$store.dispatch('lzlook/search')
      this.isShowSearchHistory = false
      if (this.$route.path !== '/results') {
        this.$router.push('results')
      }
    },
    async searchHistory (item) {
      this.$store.commit('lzlook/update', { keyword: item })
      console.log(item)
      await this.search(item)
    },
    to (path) {
      if (this.$route.path !== path) {
        this.$router.push(path)
      }
    },
    toLogin () {
      if (this.isLogin) {
        return
      }
      this.$router.push('/login')
    },
    showDisclaimer () {
      this.isShowDisclaimer = true
    },
    showHistory () {
      this.isShowSearchHistory = true
    }
  },
  // components: { HeaderComponent, FooterComponent },
  created () {
    const listStr = storage.getItem('list')
    const novelStr = storage.getItem('novel')
    const chapterStr = storage.getItem('chapter')
    const searchRecordsStr = storage.getItem('searchRecords')
    if (listStr) {
      this.$store.commit('lzlook/update', {
        list: listStr ? JSON.parse(listStr) : [],
        novel: novelStr ? JSON.parse(novelStr) : {},
        chapter: novelStr ? JSON.parse(chapterStr) : {},
        searchRecords: searchRecordsStr ? JSON.parse(searchRecordsStr) : []
      })
    }
  }
}
</script>

<style>
a:link {
  text-decoration: none;
  color: blue;
}
a:active {
  text-decoration: underline;
}
a:hover {
  text-decoration: underline;
  color: red;
}
a:visited {
  text-decoration: none;
  color: blue;
}
#footer {
  position: fixed;
  bottom: 0%;
  height: 45px;
}
</style>
