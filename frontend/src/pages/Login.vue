<template>
  <q-page padding>
    <div class="row justify-around">
      <q-card flat class="col-xs-12 col-sm-10" style="max-width:600px">
        <q-form @submit="login()">
          <q-card-section>
            <div class="text-h6 text-right">
              登录/注册
            </div>
          </q-card-section>
          <q-card-section>
            <q-input
              label="账号"
              v-model="name"
              :rules="[val => !!val || '账号不能为空']"
              lazy-rules
              autocomplete
            ></q-input>
            <q-input
              label="密码"
              v-model="password"
              :type="isPwd ? 'password' : 'text'"
              :rules="[val => !!val || '密码不能为空']"
              lazy-rules
              autocomplete
            >
              <template v-slot:append>
                <q-icon
                  :name="isPwd ? 'visibility_off' : 'visibility'"
                  class="cursor-pointer"
                  @click="isPwd = !isPwd"
                />
              </template>
            </q-input>
          </q-card-section>
          <q-card-actions align="around">
            <q-btn label="登陆" flat color="primary" type="submit"></q-btn>
            <q-btn label="注册" flat color="primary"></q-btn>
          </q-card-actions>
        </q-form>
      </q-card>
    </div>
  </q-page>
</template>

<script>
import Response from '../constants/response'
export default {
  name: 'Login',
  data () {
    return {
      name: '',
      password: '',
      isPwd: true
    }
  },
  created () {
    this.$store.commit('lzlook/update', { isShowSearchHeader: false })
  },
  methods: {
    async login () {
      const response = await this.$store.dispatch('lzlook/login', { name: this.name, password: this.password })
      if (response.code === Response.SUCCESS_CODE) {
        this.$router.push('/')
      } else {
        alert(response.msg)
      }
    }
  }
}
</script>
