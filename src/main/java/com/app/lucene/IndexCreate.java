package com.app.lucene;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.analysis.synonym.SolrSynonymParser;
import org.apache.lucene.document.*;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.Term;
import org.apache.lucene.store.FSDirectory;

import java.io.IOException;
import java.nio.file.Paths;

/**
 * lucene 索引创建
 * Created by yangzhao on 17/2/19.
 */
public class IndexCreate {

    private IndexWriterConfig indexWriterConfig;

    private FSDirectory dictionary;

    private IndexWriter indexWriter;

    public IndexCreate(String indexPath){
        indexWriterConfig = new IndexWriterConfig();
        indexWriterConfig.setOpenMode(IndexWriterConfig.OpenMode.CREATE_OR_APPEND);
        try {
            dictionary = FSDirectory.open(Paths.get(indexPath));
            indexWriter = new IndexWriter(dictionary,indexWriterConfig);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public IndexCreate(String indexPath,Analyzer analyzer){
        indexWriterConfig = new IndexWriterConfig(analyzer);
        indexWriterConfig.setOpenMode(IndexWriterConfig.OpenMode.CREATE_OR_APPEND);
        try {
            dictionary = FSDirectory.open(Paths.get(indexPath));
            indexWriter = new IndexWriter(dictionary,indexWriterConfig);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void addDocument(Document document){
        try {
            indexWriter.addDocument(document);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 修改索引
     * @param term 词元的名称
     * @param value 词元的值
     * @param document
     */
    public void updateIndex(String term,String value,Document document){
        try {
            indexWriter.updateDocument(new Term(term,value),document);
            indexWriter.commit();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void deleteIndex(String fieldName,String value){
        try {
            indexWriter.deleteDocuments(new Term(fieldName,value));
            indexWriter.forceMergeDeletes();;
            indexWriter.commit();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public long commit(){
        try {
            return indexWriter.commit();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return -1;
    }

    public void closeResource(){
        try {
            if (dictionary!=null) dictionary.close();
            if (indexWriter!=null) indexWriter.close();
        }catch (Exception exception){
            exception.printStackTrace();
        }

    }

    public static void main(String[]args){
        IndexCreate indexCreate = new IndexCreate("/Users/yangzhao/Desktop/开发工作空间/lucene/lucene_study");
//        Document document = new Document();
//        document.add(new StringField("name","杨钊", Field.Store.YES));
//        document.add(new TextField("content","杨钊出生于1993年，来自山东烟台", Field.Store.YES));
//        document.add(new StringField("age","25", Field.Store.YES));
//        Document document2 = new Document();
//        document2.add(new StringField("name","吴淑君", Field.Store.YES));
//        document2.add(new TextField("content","吴淑君出生于1992年，来自浙江杭州", Field.Store.YES));
//        document2.add(new StringField("age","26", Field.Store.YES));
//        indexCreate.addDocument(document);
//        indexCreate.addDocument(document2);
//        long commit = indexCreate.commit();
//        System.out.println(commit);
        indexCreate.deleteIndex("name","吴淑君");
//        indexCreate.updateIndex("name","杨钊",document2);
    }
}
