# lzlook 设计书
### 结构设计
- 页面部分
  1. 搜索页
  2. 搜索结果页
  3. 小说总览页
  4. 章节内容页
- 后端部分
  1. 异步获取各网站检索结果
  2. 获取小说数据（不同源采用不同方法）
  3. 获取小说章节数据（不同源采用不同方法）
### api
https://api.lzlook.com
/v1/search

```json
{
  "list": [
    {
      "source": "https://www.x23us.com",
      "novelName": "明日之劫",
      "url": "https://www.x23us.com/html/73/73425/"
    }
  ]
}
```
https://api.lzlook.com
/v1/chapter

```json
{
  "novel": "明日之劫",
  "source": "https://www.x23us.com",
  "author": "废土",
  "updateTime": "2019-04-02 07:59:51",
  "latestChapter": "最新章节链接",
  "chapters": [
    {
      "name": "章节名称",
      "url": "章节链接"
    }
  ]
}
```
https://api.lzlook.com
/v1/content

```json
{
  "name": "章节名",
  "content": "章节内容",
  "previous": "上一章链接",
  "next": "下一章链接"
}
```

