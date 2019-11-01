import api from '../../api/lzlook'
import { LoadingBar } from 'quasar'
import { storage } from '../../util/storage'

export async function search ({ commit, state }) {
  const keyword = state.keyword
  LoadingBar.start()
  const { data: { list } } = await api.search({ keyword })
  LoadingBar.stop()
  storage.setItem('list', JSON.stringify(list))
  commit('update', { list })
}

export async function novel ({ commit, state }, payload) {
  const { url } = payload
  LoadingBar.start()
  const { data: { entity } } = await api.novel({ url })
  LoadingBar.stop()
  storage.setItem('novel', JSON.stringify(entity))
  commit('update', { novel: entity })
}

export async function chapter ({ commit, state }, payload) {
  const { url } = payload
  LoadingBar.start()
  const { data: { entity } } = await api.chapter({ url })
  LoadingBar.stop()
  storage.setItem('chapter', JSON.stringify(entity))
  commit('update', { chapter: entity })
}
