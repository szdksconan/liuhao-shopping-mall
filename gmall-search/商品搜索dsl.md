### 搜索核心dsl
```dsl
GET /goods/_search
{
  "query":{
  "bool":{
    "must":[
      {
        "match":
        {
          "title":{
            "query":"手机",
            "operator":"and"
          }
        }
      }
    ],
    "filter": [
      {
        "terms":{
          "brandId":["5"]
        }
      },
      {
        "terms":{
          "categoryId":["225"]
        }
      },
      {
        "bool": {
          "must":[
            {
            "nested":{
              "path":"attrs",
              "query":{
                "bool":{
                  "must":[
                    {
                      "term":{
                        "attrs.attrId":36
                      }
                    },{
                      "terms":{
                        "attrs.attrValue":["晓龙855"]
                      }
                    }
                  ]
                }
              }
            }
            }
          ]
        }
      }
  ]
  }
  },
  "sort": [
    {
      "skuId": {
        "order": "desc"
      }
    }
  ],
  "aggs": {
    "brandIdAgg": {
      "terms": {
        "field": "brandId"
      },
      "aggs":{
        "brandNameAgg":{
          "terms": {
            "field": "brandName" 
          }
        }
      }
    },
    "categoryIdAggs":{
      "terms": {
        "field": "categoryId"
      }
    },
    "attrAgg":{
      "nested":{
        "path": "attrs"
      },
      "aggs":{
        "attrIdAgg":{
          "terms": {
            "field": "attrs.attrId"
          },
           "aggs":{
          "attrNameAgg":{
            "terms": {
              "field": "attrs.attrName"
            }
          },
          "attrValueAgg":{
            "terms": {
              "field": "attrs.attrValue"
            }
          }
        }
        }
      }
    }
  },
   ,"_source": ["title","brandName","skuId","price"]
}

```