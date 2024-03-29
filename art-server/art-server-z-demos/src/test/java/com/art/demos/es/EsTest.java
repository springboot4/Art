/// *
// * COPYRIGHT (C) 2022 Art AUTHORS(fxzcloud@gmail.com). ALL RIGHTS RESERVED.
// *
// * Licensed under the Apache License, Version 2.0 (the "License");
// * you may not use this file except in compliance with the License.
// * You may obtain a copy of the License at
// *
// * http://www.apache.org/licenses/LICENSE-2.0
// *
// * Unless required by applicable law or agreed to in writing, software
// * distributed under the License is distributed on an "AS IS" BASIS,
// * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// * See the License for the specific language governing permissions and
// * limitations under the License.
// */
//
// package com.art.demos.es;
//
// import co.elastic.clients.elasticsearch.ElasticsearchClient;
// import co.elastic.clients.elasticsearch._types.aggregations.StringTermsBucket;
// import co.elastic.clients.elasticsearch._types.query_dsl.MatchQuery;
// import co.elastic.clients.elasticsearch._types.query_dsl.Query;
// import co.elastic.clients.elasticsearch.core.BulkRequest;
// import co.elastic.clients.elasticsearch.core.IndexRequest;
// import co.elastic.clients.elasticsearch.core.SearchResponse;
// import co.elastic.clients.elasticsearch.core.bulk.BulkOperation;
// import co.elastic.clients.elasticsearch.core.search.Hit;
// import com.art.common.es.utils.EsClientUtil;
// import lombok.extern.slf4j.Slf4j;
// import org.junit.jupiter.api.Test;
// import org.springframework.boot.test.context.SpringBootTest;
//
// import javax.annotation.Resource;
// import java.io.IOException;
// import java.util.HashMap;
// import java.util.LinkedList;
// import java.util.List;
// import java.util.Map;
//
/// **
// * @author Fxz
// * @version 0.0.1
// * @date 2022/5/28 21:53
// * @see <a
/// href="https://www.elastic.co/guide/en/elasticsearch/reference/7.17/docs.html"/>
// */
// @Slf4j
// @SpringBootTest
// public class EsTest {
//
// @Resource
// private ElasticsearchClient elasticsearchClient;
//
// @Test
// public void createIndex() throws IOException {
// elasticsearchClient.indices().create(c -> c.index("test"));
// }
//
// @Test
// public void deleteIndex() throws IOException {
// elasticsearchClient.indices().delete(d -> d.index("text"));
// }
//
// /**
// * @see <a href=
// *
/// "https://www.elastic.co/guide/en/elasticsearch/client/java-api-client/7.17/indexing.html"/>
// */
// @Test
// public void createDocument() throws IOException {
// HashMap<String, String> map = new HashMap<>();
// map.put("name", "fxz");
// map.put("age", "18");
// map.put("des", "emm..");
//
// elasticsearchClient.index(d -> d.index("test").id("1").document(map));
//
// IndexRequest<Map> request = IndexRequest.of(i ->
/// i.index("test").id("2").document(map));
// elasticsearchClient.index(request);
//
// IndexRequest.Builder<Map> indexReqBuilder = new IndexRequest.Builder<>();
// indexReqBuilder.index("test");
// indexReqBuilder.id("3");
// indexReqBuilder.document(map);
// elasticsearchClient.index(indexReqBuilder.build());
// }
//
// @Test
// public void updateDocument() throws IOException {
// HashMap<String, String> map = new HashMap<>();
// map.put("name", "测试null值会不会被更新");
// map.put("age", "");
// map.put("des", null);
// elasticsearchClient.update(u -> u.index("test").id("2").doc(map), HashMap.class);
// }
//
// @Test
// public void deleteDocument() throws IOException {
// // 删除单条
// elasticsearchClient.delete(d -> d.index("test").id("1"));
// // 删除所有
// elasticsearchClient.deleteByQuery(d -> d.index("test").query(q -> q.matchAll(v -> v)));
// }
//
// @Test
// public void getDocument() throws IOException {
// // 查所有 默认返回前10条
// log.info("查所有");
// SearchResponse<Map> search = elasticsearchClient.search(s -> s.index("test").query(q ->
/// q.matchAll(m -> m)),
// Map.class);
// search.hits().hits().forEach(h -> log.info("{}", h.source()));
//
// // 分页 from开始下标从0开始，size为每页记录数
// log.info("分页查");
// search = elasticsearchClient.search(s -> s.index("test").query(q -> q.matchAll(m ->
/// m)).from(0).size(1000),
// Map.class);
// search.hits().hits().forEach(h -> log.info("{}", h.source()));
//
// // 查单条 根据id获取
// log.info("查单条");
// log.info("map:{}", EsClientUtil.getById("test", 2 + "", Map.class));
//
// // 更新单条数据
// log.info("更新单条");
// HashMap<String, String> map = new HashMap<>();
// map.put("name", "测试更新");
// map.put("age", "");
// map.put("des", null);
// map.put("no", "999999999");
// EsClientUtil.updateById("test", "2", map, Map.class);
//
// // 查单条 根据id获取
// log.info("查单条");
// log.info("map:{}", EsClientUtil.getById("test", 2 + "", Map.class));
// }
//
// /**
// * @see <a href=
// *
/// "https://www.elastic.co/guide/en/elasticsearch/client/java-api-client/7.17/searching.html"/>
// */
// @Test
// public void search() throws IOException {
//
// final String searchText = "fxz";
// SearchResponse<Map> response = elasticsearchClient
// .search(s -> s.index("test").query(q -> q.match(t ->
/// t.field("name").query(searchText))), Map.class);
// log.info("命中数量:{}", response.hits().total().value());
// log.info("命中关系:{}", response.hits().total().relation());
// log.info("查询结果:{}", response.hits().hits());
//
// String age = "19";
// Query byName = MatchQuery.of(m -> m.field("name").query(searchText))._toQuery();
// Query byAge = MatchQuery.of(m -> m.field("age").query(age))._toQuery();
// response = elasticsearchClient.search(s -> s.index("test").query(q -> q.bool(b ->
/// b.must(byName).must(byAge))),
// Map.class);
// List<Hit<Map>> hits = response.hits().hits();
// for (Hit<Map> hit : hits) {
// Map map = hit.source();
// log.info("map:{} ", map);
// }
// }
//
// /**
// * @see <a href=
// *
/// "https://www.elastic.co/guide/en/elasticsearch/client/java-api-client/7.17/indexing-bulk.html"/>
// */
// @Test
// public void bulk() throws IOException {
// List<BulkOperation> operations = new LinkedList<>();
//
// for (int i = 1; i <= 1000; i++) {
// String id = String.valueOf(i);
// HashMap<String, String> map = new HashMap<>();
// map.put("name", "fxz" + i);
// map.put("age", "18");
// map.put("des", "emm..");
// map.put("no", i + "");
//
// BulkOperation operation = new BulkOperation.Builder().index(c ->
/// c.index("test").id(id).document(map))
// .build();
// operations.add(operation);
// }
//
// elasticsearchClient.bulk(b -> b.operations(operations));
//
// BulkRequest.Builder br = new BulkRequest.Builder();
//
// for (int i = 1001; i <= 2000; i++) {
// String id = String.valueOf(i);
// HashMap<String, String> map = new HashMap<>();
// map.put("name", "fxz");
// map.put("age", "19");
// map.put("des", "emm..");
// map.put("no", i + "");
//
// br.operations(op -> op.index(idx -> idx.index("test").id(id).document(map)));
// }
//
// elasticsearchClient.bulk(br.build());
// }
//
// /**
// * @see <a href=
// *
/// "https://www.elastic.co/guide/en/elasticsearch/client/java-api-client/7.17/aggregations.html"/>
// */
// @Test
// public void agg() throws IOException {
// String searchText = "fxz";
//
// Query query = MatchQuery.of(m -> m.field("name").query(searchText))._toQuery();
//
// SearchResponse<Void> response = elasticsearchClient.search(b ->
/// b.index("test").size(0).query(query)
// .aggregations("ageAgg", a -> a.terms(t -> t.field("age.keyword"))), Void.class);
//
// List<StringTermsBucket> buckets =
/// response.aggregations().get("ageAgg").sterms().buckets().array();
//
// for (StringTermsBucket bucket : buckets) {
// log.info("There are " + bucket.docCount() + " bikes under " + bucket.key());
// }
// }
//
// }
