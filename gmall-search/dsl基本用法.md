```dsl
GET _cluster/health

GET _analyze
{
  "text":"我是一个中国人,喜大普奔,蓝瘦香菇",
  "analyzer":"ik_max_word"
}

##查看索引库情况
GET /_cat/indices?v

##创建索引库
PUT /liuhao

PUT /liuhao2
{
  "settings": {
    "number_of_shards":3,
    "number_of_replicas":2
  }
  
}

##获取索引库
GET /liuhao
GET /liuhao2

##删除索引库
DELETE liuhao


## index：是否以该字段进行搜索默认为true
## store：是否额外存储,默认为false,主要看你是否展示,需要展示就保存在文档列表 其实所有字段都会保存到_source字段中
## text：是否分词,必须是字符串类型，必须以该字段进行检索。就应该使用分词（text）指定分词器


##创建映射 等同于mysql的 table结构
PUT liuhao2/_mapping/goods
{
  "properties": {
    "title": {
      "type": "text",
      "analyzer": "ik_max_word",
      "store": true
    },
    "images": {
        "type": "keyword", 
        "index": "false"
    },
    "price": {
      "type": "long"
    }
  }
}

GET liuhao2/_mapping

## 插入一条数据
POST liuhao2/goods
{
  "title":"小米手机,未发骚而生",
  "images":"https://img12.360buyimg.com/n7/jfs/t1/102862/31/16082/430668/5e78aa16E38d234cb/d05cb19409053b03.jpg",
  "price":"1999"
}

## 指定ID POST {index}/{type}/{id} id相同 就覆盖
POST liuhao2/goods/1
{
  "title":"华为手机,哈哈哈",
  "images":"https://img12.360buyimg.com/n7/jfs/t1/102862/31/16082/430668/5e78aa16E38d234cb/d05cb19409053b03.jpg",
  "price":"1399"
}


GET liuhao2/_search


## 添加没有定义的字段  添加成功 且类型会自动推断 不知道就会添加多种类型
POST liuhao2/goods/12
{
  "title":"华为手机，不是发烧友",
  "images":"https://img12.360buyimg.com/n7/jfs/t1/102862/31/16082/430668/5e78aa16E38d234cb/d05cb19409053b03.jpg",
  "price":"3399",
  "stock": 100,
  "attr":{
    "category":"手机",
    "brand":"华为"
  }
}

## 非覆盖 单字段更新 必须用doc包裹

POST liuhao2/goods/1/_update
{
  "doc":{
    "price":1399
  }
}

##删除数据
DELETE liuhao2/goods/gIpgJnEBN9hsiKPI7sad


##批量导入

POST /atguigu/goods/_bulk
{"index":{"_id":1}}
{ "title":"小米手机", "images":"http://image.jd.com/12479122.jpg", "price":1999, "stock": 200, "attr": { "category": "手机", "brand": "小米" } }
{"index":{"_id":2}}
{"title":"超米手机", "images":"http://image.jd.com/12479122.jpg", "price":2999, "stock": 300, "attr": { "category": "手机", "brand": "小米" } }
{"index":{"_id":3}}
{ "title":"小米电视", "images":"http://image.jd.com/12479122.jpg", "price":3999, "stock": 400, "attr": { "category": "电视", "brand": "小米" } }
{"index":{"_id":4}}
{ "title":"小米笔记本", "images":"http://image.jd.com/12479122.jpg", "price":4999, "stock": 200, "attr": { "category": "笔记本", "brand": "小米" } }
{"index":{"_id":5}}
{ "title":"华为手机", "images":"http://image.jd.com/12479122.jpg", "price":3999, "stock": 400, "attr": { "category": "手机", "brand": "华为" } }
{"index":{"_id":6}}
{ "title":"华为笔记本", "images":"http://image.jd.com/12479122.jpg", "price":5999, "stock": 200, "attr": { "category": "笔记本", "brand": "华为" } }
{"index":{"_id":7}}
{ "title":"荣耀手机", "images":"http://image.jd.com/12479122.jpg", "price":2999, "stock": 300, "attr": { "category": "手机", "brand": "华为" } }
{"index":{"_id":8}}
{ "title":"oppo手机", "images":"http://image.jd.com/12479122.jpg", "price":2799, "stock": 400, "attr": { "category": "手机", "brand": "oppo" } }
{"index":{"_id":9}}
{ "title":"vivo手机", "images":"http://image.jd.com/12479122.jpg", "price":2699, "stock": 300, "attr": { "category": "手机", "brand": "vivo" } }
{"index":{"_id":10}}
{ "title":"华为nova手机", "images":"http://image.jd.com/12479122.jpg", "price":2999, "stock": 300, "attr": { "category": "手机", "brand": "华为" } }


##查询 
##这是查询所有的完整版
GET /atguigu/_search
{
  "query": {
    "match_all": {}
  }
}


## match 根据字段匹配 分词匹配都会出来
GET /atguigu/_search
{
  "query": {
    "match": {
      "title": "小米手机"
    }
  }
}


## and关系匹配 比如 小米手机  分成 小米和手机   这里就是同时满足这两个条件
GET /atguigu/_search
{
  "query": {
    "match": {
      "title":{
        "query": "小米手机",
        "operator": "and"
      }
    }
  }
}
## 短句匹配 条件不分词 效率低 慎用 
GET /atguigu/_search
{
  "query": {
    "match_phrase": {
      "title": "手机"
    }
  }
}

## 组合匹配 多字段匹配
GET /atguigu/_search
{
  "query": {
    "multi_match": {
      "query": "华为 ",
      "fields": ["title","attr.brand.keyword"]
    }
  }
}

##词条查询 value就是一个词条  这里华为手机 没有这种词条 所以没发查询到结果
##适合不能分词 数据查询 比如：数字，日期
GET /atguigu/_search
{
  "query": {
    "term":{
      "title": {
        "value": "华为手机"
      }
    }
  }
}

GET /atguigu/_search
{
  "query": {
    "terms": {
      "price": [
        "2999", 
        "4999"
      ]
    }
  }
}


## range 范围查询
GET /atguigu/_search
{
  "query": {
    "range":{
      "price":{
        "gte": 3000,
        "lte": 5000
      }
    }
  }
}

#模糊查询  可以纠错 最大错误数2 
GET /atguigu/_search
{
   "query": {
     "fuzzy": {
       "title": { 
         "value": "appe",
         "fuzziness":2
       }
     }
   }
}

## 组合查询 交集 并集 非  影响得分

##交集
GET /atguigu/_search
{
  "query": {
    "bool": {
      "must": [
        {
          "range": {
            "price": {
              "gte": 1999,
              "lte": 3999
            }
          }
        },
        {
          "range": {
            "price": {
              "gte": 2999,
              "lte": 4999
            }
          }
        }
      ]
    }
  }
}

## 并集
GET /atguigu/_search
{
  "query": {
    "bool": {
      "should": [
        {
          "range": {
            "price": {
              "gte": 1999,
              "lte": 3999
            }
          }
        },
        {
          "range": {
            "price": {
              "gte": 2999,
              "lte": 4999
            }
          }
        }
      ]
    }
  }
}


##评分测试
GET atguigu/_search
{
  "query": {
    "bool": {
      "must": [
        {"match": {
          "title": "手机"
        }}
      ]
    }
  }
}

##评分改变
GET atguigu/_search
{
  "query": {
    "bool": {
      "must": [
        {"match": {
          "title": "手机"
        }},
        {
          "range": {
            "price": {
              "gte": 1000,
              "lte": 4000
            }
          }
        },
        {
          "term": {
            "attr.brand.keyword": {
              "value": "华为"
            }
          }
        }
      ]
    }
  }
}

##评分不变化 用fliter

GET atguigu/_search
{
  "query": {
    "bool": {
      "must": [
        {"match": {
          "title": "手机"
        }},
        {
          "range": {
            "price": {
              "gte": 1000,
              "lte": 4000
            }
          }
        }
      ],
      "filter": {
        "term": {
          "attr.brand.keyword": "华为"
        }
      }
    }
  }
}


##排序 分页 from 就是offset
GET atguigu/_search
{
  "query": {
    "match_all": {}
  },
  "sort": [
    {
      "price": {
        "order": "desc"
      }
    },{
      "_id": {
        "order": "desc"
      }
    }
  ],
  "from": 0,
  "size": 2
}

##高亮 必须有查询条件 没有条件如何高亮 _source结果过滤 返回只展示的字段节约带宽
GET atguigu/_search
{
  "query": {
    "match": {
      "title": "小米手机"
    }
  },
  "highlight": {
    "fields": {"title":{}},
    "pre_tags": "<em>",
    "post_tags": "</em>"
  },
  "_source": ["title","price"]
}






###聚合


###桶 类似于 group by  度量类似于 函数 如max(..)
GET atguigu/_search
{
  "size": 0, 
  "aggs": {
      "brandAgg": {
        "terms":{
          "field":"attr.brand.keyword"
        },
        "aggs": {
          "priceAvg": {
            "avg": {
              "field": "price"
            }
          },
          "categroyAgg":{
            "terms": {
              "field": "attr.category.keyword"  
            }
          }
        }
      }
  }
}













```


## 快速开始


### 定义index 和 mapping 也就是对应mysql这种的数据库和表结构

```dsl

@Document(indexName = "user",type = "info",shards = 3,replicas = 2)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {
    @Id
    private Long id;
    @Field(type = FieldType.Text,analyzer = "ik_max_word")
    private String name;
    @Field(type = FieldType.Integer)
    private Integer age;
    @Field(type = FieldType.Keyword,index = false)
    private String password;


}




```

JTA 标准命名的接口可以直接使用实现了简单查询接口


也可以@query注解用DSL实现复杂的查询

==真.自定义方法:构建自定义查询器SearchQueryBuilder==

```dsl 



//JTA 标准定义  一定要指定具体的泛型类型
public interface UserRepository extends ElasticsearchRepository<User,Long>{

    List<User> findByAgeBetween(Integer age1,Integer age2);
    
    
    @Query(" {\n" +
            "    \"range\": {\n" +
            "      \"age\": {\n" +
            "        \"gte\": \"?0\",\n" +
            "        \"lte\": \"?1\"\n" +
            "      }\n" +
            "    }\n" +
            "  }")
    List<User> findByDslQuery(Integer age1,Integer age2);



}




@SpringBootTest
class LiuhaoElasticsearchApplicationTests {

	@Autowired
	private ElasticsearchRestTemplate elasticsearchRestTemplate;

	@Autowired
	private UserRepository userRepository;

	@Test
	void contextLoads() {

		//创建库
		elasticsearchRestTemplate.createIndex(User.class);

		//创建映射
		elasticsearchRestTemplate.putMapping(User.class);
	}

	@Test
	void addUser(){
		userRepository.save(new User(1l,"刘浩是一个傻逼",19,"12345"));
	}

	@Test
	void addUserBat(){
		List<User> userList = Arrays.asList(
		new User(1l,"刘浩是一个傻逼",19,"12345"),
		new User(2l,"李倩，麻辣隔壁，喜大普奔",33,"12345"),
		new User(3l,"风车车，重庆扛霸子",22,"12345"),
		new User(4l,"习大大，牛逼",56,"12345"),
		new User(5l,"特朗普，好孩子",78,"12345"),
		new User(6l,"李宇春，真男人",30,"12345")
		);

		userRepository.saveAll(userList);

	}

	@Test
	void finUser(){
		//userRepository.findAll().forEach(t-> System.out.println(t.getId()+" "+t.getName()));
		//userRepository.findByAgeBetween(18,30).forEach(t-> System.out.println(t));
		userRepository.findByDslQuery(20,40).forEach(t-> System.out.println(t));
	}
	
	
	//自定义查询构建器
	@Test
	void findUserByQueryBulid(){
		NativeSearchQueryBuilder queryBuilder = new NativeSearchQueryBuilder();
		//构建查询
		queryBuilder.withQuery(QueryBuilders.matchQuery("name","喜大普奔"));
		//分页
		queryBuilder.withPageable(PageRequest.of(0,2));
		//排序
		queryBuilder.withSort(SortBuilders.fieldSort("age").order(SortOrder.DESC));
		queryBuilder.withHighlightBuilder(new HighlightBuilder().field("name").preTags("<em>").postTags("</em>"));
		Page<User> page =  userRepository.search(queryBuilder.build());
		System.out.println("元素数量："+page.getTotalElements());
		System.out.println("页数："+page.getTotalPages());
		page.getContent().forEach(t-> System.out.println(t));
	}



}

```




==嵌套查询==
==场景：多个子属性的值匹配 不出现穿插==

```dsl
//比如数据格式 查询attrs下面的满足条件 要满足attrId=36 且 attrValue = 晓龙855 就需要嵌套查询了

{
        "_index" : "goods",
        "_type" : "info",
        "_id" : "6",
        "_score" : 1.0,
        "_source" : {
          "skuId" : 6,
          "pic" : null,
          "title" : "刘浩S10手机 8g,白色,256",
          "price" : 998.0,
          "sale" : 0,
          "store" : null,
          "createTime" : 1585297771000,
          "brandId" : 5,
          "brandName" : "刘浩品牌",
          "categoryId" : 225,
          "categoryName" : "手机",
          "attrs" : [
            {
              "attrId" : 25,
              "attrName" : "大萨达222",
              "attrValue" : "23231"
            },
            {
              "attrId" : 36,
              "attrName" : "CPU型号",
              "attrValue" : "晓龙855"
            },
            {
              "attrId" : 33,
              "attrName" : "电池",
              "attrValue" : "4000"
            },
            {
              "attrId" : 34,
              "attrName" : "屏幕",
              "attrValue" : "4"
            }
          ]
        }
      },
      {
        "_index" : "goods",
        "_type" : "info",
        "_id" : "7",
        "_score" : 1.0,
        "_source" : {
          "skuId" : 7,
          "pic" : null,
          "title" : "刘浩S10手机 8g,白色,256",
          "price" : 998.0,
          "sale" : 0,
          "store" : null,
          "createTime" : 1585297791000,
          "brandId" : 5,
          "brandName" : "刘浩品牌",
          "categoryId" : 225,
          "categoryName" : "手机",
          "attrs" : [
            {
              "attrId" : 25,
              "attrName" : "大萨达222",
              "attrValue" : "23231"
            },
            {
              "attrId" : 36,
              "attrName" : "CPU型号",
              "attrValue" : "晓龙855"
            },
            {
              "attrId" : 33,
              "attrName" : "电池",
              "attrValue" : "4000"
            },
            {
              "attrId" : 34,
              "attrName" : "屏幕",
              "attrValue" : "4"
            }
          ]
        }
      },






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
                        "attrs.attrValue":["骁龙855"]
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
}
}






```


==嵌套聚合 难点 根据查询数据构建产品页面的搜索栏==

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


{
  "took" : 38,
  "timed_out" : false,
  "_shards" : {
    "total" : 3,
    "successful" : 3,
    "skipped" : 0,
    "failed" : 0
  },
  "hits" : {
    "total" : 12,
    "max_score" : null,
    "hits" : [
      {
        "_index" : "goods",
        "_type" : "info",
        "_id" : "16",
        "_score" : null,
        "_source" : {
          "skuId" : 16,
          "pic" : null,
          "title" : "刘浩S10手机 8g,白色,128g",
          "price" : 998.0,
          "sale" : 0,
          "store" : null,
          "createTime" : 1585394109000,
          "brandId" : 5,
          "brandName" : "刘浩品牌",
          "categoryId" : 225,
          "categoryName" : "手机",
          "attrs" : [
            {
              "attrId" : 25,
              "attrName" : "大萨达222",
              "attrValue" : "23231"
            },
            {
              "attrId" : 36,
              "attrName" : "CPU型号",
              "attrValue" : "晓龙855"
            },
            {
              "attrId" : 33,
              "attrName" : "电池",
              "attrValue" : "4000"
            },
            {
              "attrId" : 34,
              "attrName" : "屏幕",
              "attrValue" : "4"
            }
          ]
        },
        "sort" : [
          16
        ]
      },
      {
        "_index" : "goods",
        "_type" : "info",
        "_id" : "15",
        "_score" : null,
        "_source" : {
          "skuId" : 15,
          "pic" : null,
          "title" : "刘浩S11手机 8g,白色,256",
          "price" : 998.0,
          "sale" : 0,
          "store" : null,
          "createTime" : 1585394109000,
          "brandId" : 5,
          "brandName" : "刘浩品牌",
          "categoryId" : 225,
          "categoryName" : "手机",
          "attrs" : [
            {
              "attrId" : 25,
              "attrName" : "大萨达222",
              "attrValue" : "23231"
            },
            {
              "attrId" : 36,
              "attrName" : "CPU型号",
              "attrValue" : "晓龙855"
            },
            {
              "attrId" : 33,
              "attrName" : "电池",
              "attrValue" : "4000"
            },
            {
              "attrId" : 34,
              "attrName" : "屏幕",
              "attrValue" : "4"
            }
          ]
        },
        "sort" : [
          15
        ]
      },
      {
        "_index" : "goods",
        "_type" : "info",
        "_id" : "13",
        "_score" : null,
        "_source" : {
          "skuId" : 13,
          "pic" : null,
          "title" : "刘浩S10手机 8g,白色,128g",
          "price" : 998.0,
          "sale" : 0,
          "store" : null,
          "createTime" : 1585393691000,
          "brandId" : 5,
          "brandName" : "刘浩品牌",
          "categoryId" : 225,
          "categoryName" : "手机",
          "attrs" : [
            {
              "attrId" : 25,
              "attrName" : "大萨达222",
              "attrValue" : "23231"
            },
            {
              "attrId" : 36,
              "attrName" : "CPU型号",
              "attrValue" : "晓龙855"
            },
            {
              "attrId" : 33,
              "attrName" : "电池",
              "attrValue" : "4000"
            },
            {
              "attrId" : 34,
              "attrName" : "屏幕",
              "attrValue" : "4"
            }
          ]
        },
        "sort" : [
          13
        ]
      },
      {
        "_index" : "goods",
        "_type" : "info",
        "_id" : "12",
        "_score" : null,
        "_source" : {
          "skuId" : 12,
          "pic" : null,
          "title" : "刘浩S11手机 8g,白色,256",
          "price" : 998.0,
          "sale" : 0,
          "store" : null,
          "createTime" : 1585393691000,
          "brandId" : 5,
          "brandName" : "刘浩品牌",
          "categoryId" : 225,
          "categoryName" : "手机",
          "attrs" : [
            {
              "attrId" : 25,
              "attrName" : "大萨达222",
              "attrValue" : "23231"
            },
            {
              "attrId" : 36,
              "attrName" : "CPU型号",
              "attrValue" : "晓龙855"
            },
            {
              "attrId" : 33,
              "attrName" : "电池",
              "attrValue" : "4000"
            },
            {
              "attrId" : 34,
              "attrName" : "屏幕",
              "attrValue" : "4"
            }
          ]
        },
        "sort" : [
          12
        ]
      },
      {
        "_index" : "goods",
        "_type" : "info",
        "_id" : "11",
        "_score" : null,
        "_source" : {
          "skuId" : 11,
          "pic" : null,
          "title" : "刘浩S10手机 8g,白色,128g",
          "price" : 998.0,
          "sale" : 0,
          "store" : null,
          "createTime" : 1585299740000,
          "brandId" : 5,
          "brandName" : "刘浩品牌",
          "categoryId" : 225,
          "categoryName" : "手机",
          "attrs" : [
            {
              "attrId" : 25,
              "attrName" : "大萨达222",
              "attrValue" : "23231"
            },
            {
              "attrId" : 36,
              "attrName" : "CPU型号",
              "attrValue" : "晓龙855"
            },
            {
              "attrId" : 33,
              "attrName" : "电池",
              "attrValue" : "4000"
            },
            {
              "attrId" : 34,
              "attrName" : "屏幕",
              "attrValue" : "4"
            }
          ]
        },
        "sort" : [
          11
        ]
      },
      {
        "_index" : "goods",
        "_type" : "info",
        "_id" : "10",
        "_score" : null,
        "_source" : {
          "skuId" : 10,
          "pic" : null,
          "title" : "刘浩S10手机 8g,白色,256",
          "price" : 998.0,
          "sale" : 0,
          "store" : null,
          "createTime" : 1585299740000,
          "brandId" : 5,
          "brandName" : "刘浩品牌",
          "categoryId" : 225,
          "categoryName" : "手机",
          "attrs" : [
            {
              "attrId" : 25,
              "attrName" : "大萨达222",
              "attrValue" : "23231"
            },
            {
              "attrId" : 36,
              "attrName" : "CPU型号",
              "attrValue" : "晓龙855"
            },
            {
              "attrId" : 33,
              "attrName" : "电池",
              "attrValue" : "4000"
            },
            {
              "attrId" : 34,
              "attrName" : "屏幕",
              "attrValue" : "4"
            }
          ]
        },
        "sort" : [
          10
        ]
      },
      {
        "_index" : "goods",
        "_type" : "info",
        "_id" : "9",
        "_score" : null,
        "_source" : {
          "skuId" : 9,
          "pic" : null,
          "title" : "刘浩S10手机 8g,白色,128g",
          "price" : 998.0,
          "sale" : 0,
          "store" : null,
          "createTime" : 1585297862000,
          "brandId" : 5,
          "brandName" : "刘浩品牌",
          "categoryId" : 225,
          "categoryName" : "手机",
          "attrs" : [
            {
              "attrId" : 25,
              "attrName" : "大萨达222",
              "attrValue" : "23231"
            },
            {
              "attrId" : 36,
              "attrName" : "CPU型号",
              "attrValue" : "晓龙855"
            },
            {
              "attrId" : 33,
              "attrName" : "电池",
              "attrValue" : "4000"
            },
            {
              "attrId" : 34,
              "attrName" : "屏幕",
              "attrValue" : "4"
            }
          ]
        },
        "sort" : [
          9
        ]
      },
      {
        "_index" : "goods",
        "_type" : "info",
        "_id" : "8",
        "_score" : null,
        "_source" : {
          "skuId" : 8,
          "pic" : null,
          "title" : "刘浩S10手机 8g,白色,256",
          "price" : 998.0,
          "sale" : 0,
          "store" : null,
          "createTime" : 1585297862000,
          "brandId" : 5,
          "brandName" : "刘浩品牌",
          "categoryId" : 225,
          "categoryName" : "手机",
          "attrs" : [
            {
              "attrId" : 25,
              "attrName" : "大萨达222",
              "attrValue" : "23231"
            },
            {
              "attrId" : 36,
              "attrName" : "CPU型号",
              "attrValue" : "晓龙855"
            },
            {
              "attrId" : 33,
              "attrName" : "电池",
              "attrValue" : "4000"
            },
            {
              "attrId" : 34,
              "attrName" : "屏幕",
              "attrValue" : "4"
            }
          ]
        },
        "sort" : [
          8
        ]
      },
      {
        "_index" : "goods",
        "_type" : "info",
        "_id" : "7",
        "_score" : null,
        "_source" : {
          "skuId" : 7,
          "pic" : null,
          "title" : "刘浩S10手机 8g,白色,256",
          "price" : 998.0,
          "sale" : 0,
          "store" : null,
          "createTime" : 1585297791000,
          "brandId" : 5,
          "brandName" : "刘浩品牌",
          "categoryId" : 225,
          "categoryName" : "手机",
          "attrs" : [
            {
              "attrId" : 25,
              "attrName" : "大萨达222",
              "attrValue" : "23231"
            },
            {
              "attrId" : 36,
              "attrName" : "CPU型号",
              "attrValue" : "晓龙855"
            },
            {
              "attrId" : 33,
              "attrName" : "电池",
              "attrValue" : "4000"
            },
            {
              "attrId" : 34,
              "attrName" : "屏幕",
              "attrValue" : "4"
            }
          ]
        },
        "sort" : [
          7
        ]
      },
      {
        "_index" : "goods",
        "_type" : "info",
        "_id" : "6",
        "_score" : null,
        "_source" : {
          "skuId" : 6,
          "pic" : null,
          "title" : "刘浩S10手机 8g,白色,256",
          "price" : 998.0,
          "sale" : 0,
          "store" : null,
          "createTime" : 1585297771000,
          "brandId" : 5,
          "brandName" : "刘浩品牌",
          "categoryId" : 225,
          "categoryName" : "手机",
          "attrs" : [
            {
              "attrId" : 25,
              "attrName" : "大萨达222",
              "attrValue" : "23231"
            },
            {
              "attrId" : 36,
              "attrName" : "CPU型号",
              "attrValue" : "晓龙855"
            },
            {
              "attrId" : 33,
              "attrName" : "电池",
              "attrValue" : "4000"
            },
            {
              "attrId" : 34,
              "attrName" : "屏幕",
              "attrValue" : "4"
            }
          ]
        },
        "sort" : [
          6
        ]
      }
    ]
  },
  "aggregations" : {
    "brandIdAgg" : {
      "doc_count_error_upper_bound" : 0,
      "sum_other_doc_count" : 0,
      "buckets" : [
        {
          "key" : 5,
          "doc_count" : 12,
          "brandNameAgg" : {
            "doc_count_error_upper_bound" : 0,
            "sum_other_doc_count" : 0,
            "buckets" : [
              {
                "key" : "刘浩品牌",
                "doc_count" : 12
              }
            ]
          }
        }
      ]
    },
    "attrAgg" : {
      "doc_count" : 48,
      "attrIdAgg" : {
        "doc_count_error_upper_bound" : 0,
        "sum_other_doc_count" : 0,
        "buckets" : [
          {
            "key" : 25,
            "doc_count" : 12,
            "attrNameAgg" : {
              "doc_count_error_upper_bound" : 0,
              "sum_other_doc_count" : 0,
              "buckets" : [
                {
                  "key" : "大萨达222",
                  "doc_count" : 12
                }
              ]
            },
            "attrValueAgg" : {
              "doc_count_error_upper_bound" : 0,
              "sum_other_doc_count" : 0,
              "buckets" : [
                {
                  "key" : "23231",
                  "doc_count" : 12
                }
              ]
            }
          },
          {
            "key" : 33,
            "doc_count" : 12,
            "attrNameAgg" : {
              "doc_count_error_upper_bound" : 0,
              "sum_other_doc_count" : 0,
              "buckets" : [
                {
                  "key" : "电池",
                  "doc_count" : 12
                }
              ]
            },
            "attrValueAgg" : {
              "doc_count_error_upper_bound" : 0,
              "sum_other_doc_count" : 0,
              "buckets" : [
                {
                  "key" : "4000",
                  "doc_count" : 12
                }
              ]
            }
          },
          {
            "key" : 34,
            "doc_count" : 12,
            "attrNameAgg" : {
              "doc_count_error_upper_bound" : 0,
              "sum_other_doc_count" : 0,
              "buckets" : [
                {
                  "key" : "屏幕",
                  "doc_count" : 12
                }
              ]
            },
            "attrValueAgg" : {
              "doc_count_error_upper_bound" : 0,
              "sum_other_doc_count" : 0,
              "buckets" : [
                {
                  "key" : "4",
                  "doc_count" : 12
                }
              ]
            }
          },
          {
            "key" : 36,
            "doc_count" : 12,
            "attrNameAgg" : {
              "doc_count_error_upper_bound" : 0,
              "sum_other_doc_count" : 0,
              "buckets" : [
                {
                  "key" : "CPU型号",
                  "doc_count" : 12
                }
              ]
            },
            "attrValueAgg" : {
              "doc_count_error_upper_bound" : 0,
              "sum_other_doc_count" : 0,
              "buckets" : [
                {
                  "key" : "晓龙855",
                  "doc_count" : 12
                }
              ]
            }
          }
        ]
      }
    },
    "categoryIdAggs" : {
      "doc_count_error_upper_bound" : 0,
      "sum_other_doc_count" : 0,
      "buckets" : [
        {
          "key" : 225,
          "doc_count" : 12
        }
      ]
    }
  }
}









```