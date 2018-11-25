package com.tanziMahbub.lucene;

import java.io.IOException;
import java.nio.file.Paths;
import org.apache.lucene.document.Document;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.store.FSDirectory;


public class SearchEngine {
	
	private IndexSearcher searcher = null;
    private QueryParser parser = null;
    //private static final String INDEX_DIR = "C:\\Users\\Mahbubul Alam\\workspace\\Lucene\\index-directory";
    String INDEX_DIR = null;
    /** Creates a new instance of SearchEngine */
    public SearchEngine(String indexDirectory) throws IOException {
    	
    	INDEX_DIR = indexDirectory;
        searcher = new IndexSearcher(DirectoryReader.open(FSDirectory.open(Paths.get(INDEX_DIR))));
        parser = new QueryParser("content", new StandardAnalyzer());
    }
    
    public TopDocs performSearch(String queryString, int n)
    throws IOException, ParseException {
        Query query = parser.parse(queryString);        
        return searcher.search(query, n);
    }

    public Document getDocument(int docId)
    throws IOException {
        return searcher.doc(docId);
    }

}
