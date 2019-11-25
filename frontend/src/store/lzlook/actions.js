import api from '../../api/lzlook'
import { LoadingBar } from 'quasar'
import { storage } from '../../util/storage'
import Response from '../../constants/response'

export async function search ({ commit, state }) {
  const keyword = state.keyword
  LoadingBar.start()
  try {
    const { data: { list } } = await api.search({ keyword })
    storage.setItem('list', JSON.stringify(list))
    // const searchRecords = state.searchRecords
    // searchRecords.push(keyword)
    // if (searchRecords.size > 3) {
    //   searchRecords.remove(searchRecords[0])
    // }
    commit('updateSearchRecords', { keyword })
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

export async function login ({ commit }, payload) {
  const { name, password } = payload
  const { data } = await api.login({ name, password })
  if (data.code === Response.SUCCESS_CODE) {
    const { entity } = data
    commit('update', { user: entity, isLogin: true })
  }
  return data
}
