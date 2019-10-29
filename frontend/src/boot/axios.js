import Vue from 'vue'
import axios from 'axios'

Vue.prototype.$axios = axios
axios.defaults.timeout = 10000
