import { storage } from '../../util/storage'

export function update (state, payload) {
  Object.keys(payload).forEach(key => {
    this._vm.$set(state, key, payload[key])
  })
}

export function updateSearchRecords (state, payload) {
  const { keyword } = payload
  const searchRecords = state.searchRecords
  if (searchRecords.indexOf(keyword) > -1) {
    state.searchRecords = searchRecords.filter(item => item !== keyword)
  }
  state.searchRecords.push(keyword)
  if (state.searchRecords.length > 3) {
    state.searchRecords.shift()
  }
  storage.setItem('searchRecords', JSON.stringify(state.searchRecords))
}
