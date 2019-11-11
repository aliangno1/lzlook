<template>
  <q-page padding>
    <div class="q-mt-md">
      <q-splitter v-model="splitterModel">
        <template v-slot:before>
          <q-tabs v-model="tab" vertical class="text-teal">
            <q-tab name="info" icon="account_circle" label="个人信息" />
            <q-tab name="bookshelf" icon="menu_book" label="我的书架" />
            <q-tab name="bookmark" icon="bookmark" label="我的书签" />
            <q-tab name="history" icon="history" label="浏览记录" />
            <q-tab name="logout" icon="fas fa-sign-in-alt" label="登出" />
          </q-tabs>
        </template>

        <template v-slot:after>
          <q-tab-panels
            v-model="tab"
            animated
            transition-prev="jump-up"
            transition-next="jump-up"
          >
            <q-tab-panel name="info">
              <div class="text-h4 q-mb-md">个人信息</div>
            </q-tab-panel>

            <q-tab-panel name="bookshelf">
              <div class="text-h4 q-mb-md">我的书架</div>
            </q-tab-panel>

            <q-tab-panel name="bookmark">
              <div class="text-h4 q-mb-md">我的书签</div>
            </q-tab-panel>
            <q-tab-panel name="history">
              <div class="text-h4 q-mb-md">浏览历史</div>
            </q-tab-panel>
          </q-tab-panels>
        </template>
      </q-splitter>
    </div>
  </q-page>
</template>

<script>
export default {
  name: 'PageName',
  data () {
    return {
      tab: 'info',
      splitterModel: 20
    }
  },
  created () {
    this.$store.commit('lzlook/update', { isShowSearchHeader: true })
  }

}
</script>
