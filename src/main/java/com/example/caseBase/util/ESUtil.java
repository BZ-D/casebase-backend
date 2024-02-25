package com.example.caseBase.util;

import com.example.caseBase.PO.Doc;
import com.example.caseBase.dto.QueryBody;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.MultiMatchQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.sort.FieldSortBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.text.ParseException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class ESUtil {

    @Autowired
    RestHighLevelClient client;

    public void addDocument(Doc doc, String indexName) {
        try {
            Map<String, Object> jsonMap = doc.getJsonMap();
            IndexRequest indexRequest = new IndexRequest(indexName)
                    .id(doc.getDocId().toString()).source(jsonMap);
            try {
                IndexResponse indexResponse = client.index(indexRequest, RequestOptions.DEFAULT);
            }catch (IOException ignored){
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Map<Long,String> queryDocument(QueryBody body) throws IOException {

        SearchRequest searchRequest = new SearchRequest();
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();

        BoolQueryBuilder boolQueryBuilder = new BoolQueryBuilder();

        if (body.filter.content != null) {
            MultiMatchQueryBuilder multiMatchQueryBuilder = new MultiMatchQueryBuilder(body.filter.content,
                    "organ", "date", "docNum", "decision", "cause", "basis", "content", "name", "litigants");
            boolQueryBuilder.must(multiMatchQueryBuilder);

        }
        if (body.filter.date != null) {
            Date start = new Date(body.filter.date.startDate);
            Date end = new Date(body.filter.date.endDate == 0 ? new Date().getTime() : body.filter.date.endDate);
            boolQueryBuilder.must(QueryBuilders.rangeQuery("publishDate").from(start).to(end));
        }
        if (body.filter.tag != null) {
            boolQueryBuilder.must(QueryBuilders.termQuery("state", body.filter.tag.equals("未发布") ? 0 : 1));
        }

        searchSourceBuilder.query(boolQueryBuilder);

        if (body.sorter.date != null) {
            searchSourceBuilder.sort(new FieldSortBuilder("publishDate")
                    .order(body.sorter.date.equals("asc") ? SortOrder.ASC : SortOrder.DESC));
        }else{
            searchSourceBuilder.sort(new FieldSortBuilder("publishDate")
                    .order(SortOrder.DESC));
        }

        searchSourceBuilder.size(body.currentPageSize*body.pageNum);
        searchSourceBuilder.from(body.currentPageSize * ((Math.max(body.currentPage, 1)) - 1));

        //高亮
        HighlightBuilder highlightBuilder = new HighlightBuilder();
        HighlightBuilder.Field highlightContent =
                new HighlightBuilder.Field("content");
        highlightContent.highlighterType("unified");
        highlightContent.fragmentSize(140);
        highlightContent.preTags("<font color=\"red\"><strong>");
        highlightContent.postTags("</strong></font>");
        highlightBuilder.field(highlightContent);
        searchSourceBuilder.highlighter(highlightBuilder);

        searchRequest.source(searchSourceBuilder);
        System.out.println(searchSourceBuilder);
        SearchResponse searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);
        SearchHit[] hits = searchResponse.getHits().getHits();
        Map<Long,String> res = new HashMap<>();
        for (SearchHit hit : hits) {
            String highlighted="";
            if(hit.getHighlightFields().get("content")!=null){
                highlighted=String.valueOf(hit.getHighlightFields().get("content").getFragments()[0]);
            }
            res.put(Long.parseLong(hit.getId()),highlighted);
        }
        return res;

    }

    public void deleteDocument(long docId,String index){
        try{
            DeleteRequest deleteRequest=new DeleteRequest(index);
            deleteRequest.id(String.valueOf(docId));
            client.delete(deleteRequest,RequestOptions.DEFAULT);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}