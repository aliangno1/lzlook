<template>
  <q-page class="full-height">
    <a href="https://github.com/aliangno1/lzlook">
    <img
        width="149"
        height="149"
        src="https://github.blog/wp-content/uploads/2008/12/forkme_left_orange_ff7600.png?resize=149%2C149"
        class="attachment-full size-full"
        alt="Fork me on GitHub"
        data-recalc-dims="1"
    /></a>
    <q-form
      @submit="search(keyword)"
      style="top:40%;position:fixed;"
      class="full-width"
    >
      <div class="row justify-center" id="form">
        <q-input
          outlined
          dense
          v-model="keyword"
          placeholder="请输入完整小说名"
          class="col-sm-6"
          style="max-width:600px"
        ></q-input>
      </div>

      <div class="column" id="input">
        <div class="col self-center row"></div>
        <div class="col self-center q-mt-md">
          <q-btn type="submit" label="Lz Search" color="black" no-caps></q-btn>
        </div>
      </div>
    </q-form>
    <footer-component></footer-component>
  </q-page>
</template>

<script>
import FooterComponent from '../components/FooterComponent'
export default {
  name: 'PageIndex',
  data () {
    return {
    }
  },
  components: { FooterComponent },
  computed: {
    keyword: {
      get () {
        return this.$store.state.lzlook.keyword
      },
      set (value) {
        this.$store.commit('lzlook/update', { keyword: value })
      }
    }
  },
  methods: {
    async search (keyword) {
      if (!keyword) {
        return
      }
      await this.$store.dispatch('lzlook/search')
      this.$router.push('results')
    }
  },
  created () {
    this.$store.commit('lzlook/update', { isShowSearchHeader: false })
  }
}
</script>
<style scoped>
</style>
