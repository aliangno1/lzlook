<template>
  <q-layout view="lHr lpr lFr">
    <header-component></header-component>
    <q-page-container>
      <router-view />
    </q-page-container>
    <footer-component></footer-component>
  </q-layout>
</template>

<script>
import HeaderComponent from '../components/HeaderComponent'
import FooterComponent from '../components/FooterComponent'
import { storage } from '../util/storage'
export default {
  data () {
    return {
    }
  },
  components: { HeaderComponent, FooterComponent },
  created () {
    const listStr = storage.getItem('list')
    const novelStr = storage.getItem('novel')
    const chapterStr = storage.getItem('chapter')
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
  color: blue;
}
</style>
