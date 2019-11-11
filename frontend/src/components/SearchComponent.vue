<template>
  <div>
    <q-form @submit="search(keyword)">
      <q-input
        v-model="keyword"
        dense
        outlined
        standout
        bg-color="white"
        type="search"
        clear-icon="search"
      >
        <template v-slot:append>
          <q-icon name="search" />
        </template>
      </q-input>
    </q-form>
  </div>
</template>

<script>
export default {
  // name: 'ComponentName',
  data () {
    return {}
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
      if (this.$route.path !== '/results') {
        this.$router.push('results')
      }
    }
  }
}
</script>
