<template>
  <div id="scroll" class="column">
    <div class="col q-mb-xs">
      <q-btn class="bg-white q-px-sm q-py-xs" @click="up" size="sm" icon="fas fa-chevron-up">
      </q-btn>
      <q-separator></q-separator>
    </div>
    <div class="col">
      <q-btn class="bg-white q-px-sm q-py-xs" @click="down" size="sm" icon="fas fa-chevron-down">
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
  right: 2%;
  width: 35px;
}
</style>
