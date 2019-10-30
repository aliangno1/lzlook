<template>
  <div id="scroll" class="column">
    <div class="col">
      <q-btn class="bg-white" @click="up">
        <i class="fas fa-chevron-up"></i>
      </q-btn>
      <q-separator></q-separator>
    </div>
    <div class="col">
      <q-btn class="bg-white" @click="down">
        <i class="fas fa-chevron-down"></i>
      </q-btn>
    </div>
  </div>
</template>

<script>
export default {
  // name: 'ComponentName',
  data () {
    return {
    }
  },
  methods: {
    up () {
      window.scrollTo(0, 0)
    },
    down () {
      window.scrollTo(0, document.body.scrollHeight)
    }
  }
}
</script>

<style>
#scroll {
  position: fixed;
  bottom: 10%;
  right: 3%;
  width: 45px;
}
</style>
