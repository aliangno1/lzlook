<template>
  <q-page class="flex flex-center">
    <q-form @submit="search(keyword)" class="q-gutter-md">
      <div class="column">
        <div class="col self-center">
          <q-input
            outlined
            dense
            v-model="keyword"
            placeholder="请输入小说名"
          ></q-input>
        </div>
        <div class="col self-center q-mt-md">
          <q-btn type="submit" label="Lz Search" color="black" no-caps></q-btn>
        </div>
      </div>
    </q-form>
  </q-page>
</template>

<script>
export default {
  name: 'PageIndex',
  data () {
    return {
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
  }
}
</script>
