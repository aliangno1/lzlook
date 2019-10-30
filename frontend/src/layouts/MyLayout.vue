<template>
  <q-layout view="lHr lpr lFr">
    <header-component></header-component>
    <q-page-container>
      <router-view />
    </q-page-container>
  </q-layout>
</template>

<script>
import HeaderComponent from '../components/HeaderComponent'
export default {
  data () {
    return {
    }
  },
  components: { HeaderComponent },
  created () {
    const listStr = localStorage.getItem('list')
    const novelStr = localStorage.getItem('novel')
    const chapterStr = localStorage.getItem('chapter')
    if (listStr) {
      this.$store.commit('lzlook/update', {
        list: JSON.parse(listStr),
        novel: JSON.parse(novelStr),
        chapter: JSON.parse(chapterStr)
      })
    }
  }
}
</script>

<style >
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
  color: purple;
}
</style>
