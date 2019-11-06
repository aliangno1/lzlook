<template>
  <div class="bg-grey-3 q-pt-sm" v-show="isShowSearchHeader">
    <q-item>
      <q-item-section side class="q-pa-none">
        <q-btn icon="img:statics/icons/app-icon.png" flat class="q-px-xs" to="/" color="black">
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
          <q-btn label="登录" color="primary"></q-btn>
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
      'isShowSearchHeader'
    ])
  },
  components: {
    SearchComponent
  }
}
</script>
