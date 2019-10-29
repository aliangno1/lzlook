import axios from 'axios'

const Api = {
  SEARCH: '/lzlook/novel/search',
  NOVEL: '/lzlook/novel/novel',
  CHAPTER: '/lzlook/novel/chapter'
}

async function get (path, params) {
  if (params) {
    path += '?'
    for (let k of Object.keys(params)) {
      path += k + '=' + params[k] + '&'
    }
  }
  return axios.get(path)
}

export default {

  /**
   * 搜索接口
   */
  search: async params => get(Api.SEARCH, params),

  /**
  * 小说内容查询接口
  */
  novel: async (params) => get(Api.NOVEL, params),

  /**
   * 章节内容查询接口
  */
  chapter: async (params) => get(Api.CHAPTER, params)

}
