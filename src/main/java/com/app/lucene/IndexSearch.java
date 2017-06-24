package com.app.lucene;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.*;
import org.apache.lucene.store.FSDirectory;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.stream.Stream;

/**
 * lucene 索引搜索
 * Created by yangzhao on 17/2/19.
 */
public class IndexSearch {

    private FSDirectory dictionary;

    private DirectoryReader directoryReader;

    private IndexSearcher indexSearcher;

    private QueryParser queryParser;

    public IndexSearcher getIndexSearcher() {
        return indexSearcher;
    }

    public IndexSearch(String indexPath, Analyzer analyzer, String field){
        try {
            dictionary = FSDirectory.open(Paths.get(indexPath));
            directoryReader = DirectoryReader.open(dictionary);
            indexSearcher = new IndexSearcher(directoryReader);
            queryParser = new QueryParser(field,analyzer);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public TopDocs search(String content){
        try {
            Query query = queryParser.parse(content);
            TopDocs topDocs = indexSearcher.search(query, 10);
            return topDocs;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void main(String[]args){
        IndexSearch indexSearch = new IndexSearch("/Users/yangzhao/Desktop/开发工作空间/lucene/lucene_study",new StandardAnalyzer(),"content");
        TopDocs topDocs = indexSearch.search("出生");
        ScoreDoc[] scoreDocs = topDocs.scoreDocs;
        IndexSearcher indexSearcher = indexSearch.getIndexSearcher();
        Stream.of(scoreDocs).forEach(scoreDoc -> {
            try {
                Document document = indexSearcher.doc(scoreDoc.doc);
                String name = document.get("name");
                String content = document.get("content");
                String age = document.get("age");
                System.out.println(name+"----"+content+"----"+age);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } );
    }
}
