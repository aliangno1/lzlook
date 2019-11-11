<template>
  <div class="bg-grey-3 q-pt-sm" v-show="isShowSearchHeader">
    <q-item>
      <q-item-section side class="q-pa-none">
        <q-btn
          icon="img:statics/icons/app-icon.png"
          flat
          class="q-px-xs"
          to="/"
          color="black"
        >
          <!-- <i class="fas fa-home"></i> -->
        </q-btn>
      </q-item-section>
      <q-item-section class="q-pl-sm">
        <q-item-label>
          <div style="max-width:200px">
            <search-component></search-component>
          </div>
        </q-item-label>
      </q-item-section>
      <q-item-section side>
        <q-item-label class="q-pl-xl">
          <q-btn
            label="登录"
            color="primary"
            @click="toLogin"
            v-if="!isLogin"
          ></q-btn>
          <q-avatar v-if="isLogin" @click="to('/user')">
            <img :src="user.avatar" />
          </q-avatar>
        </q-item-label>
      </q-item-section>
    </q-item>
  </div>
</template>

<script>
import SearchComponent from './SearchComponent'
import { mapState } from 'vuex'
export default {
  // name: 'ComponentName',
  data () {
    return {}
  },
  computed: {
    ...mapState('lzlook', [
      'isShowSearchHeader',
      'isLogin',
      'user'
    ])
  },
  components: {
    SearchComponent
  },
  methods: {
    toLogin () {
      this.$router.push('/login')
    },
    to (path) {
      if (this.$route.path !== path) {
        this.$router.push(path)
      }
    }
  }
}
</script>
