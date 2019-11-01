import api from '../../api/lzlook'
import { LoadingBar } from 'quasar'
import { storage } from '../../util/storage'

export async function search ({ commit, state }) {
  const keyword = state.keyword
  LoadingBar.start()
  try {
    const { data: { list } } = await api.search({ keyword })
    storage.setItem('list', JSON.stringify(list))
    commit('update', { list })
  } finally {
    LoadingBar.stop()
  }
}

export async function novel ({ commit, state }, payload) {
  const { url } = payload
  LoadingBar.start()
  try {
    const { data: { entity } } = await api.novel({ url })
    storage.setItem('novel', JSON.stringify(entity))
    commit('update', { novel: entity })
  } catch (e) {
  } finally {
    LoadingBar.stop()
  }
}

export async function chapter ({ commit, state }, payload) {
  const { url } = payload
  LoadingBar.start()
  try {
    const { data: { entity } } = await api.chapter({ url })
    storage.setItem('chapter', JSON.stringify(entity))
    commit('update', { chapter: entity })
  } catch (e) {
  } finally {
    LoadingBar.stop()
  }
}
