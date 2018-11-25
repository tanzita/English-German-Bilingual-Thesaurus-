package com.tanziMahbub.lucene;

import java.io.IOException;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.io.File;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

public class Indexer {

	//private static final String INDEX_DIR = "C:\\Users\\Mahbubul Alam\\workspace\\Lucene\\index-directory";
	String INDEX_DIR = null;

	ResultSet rs = null;

	/** Creates a new instance of Indexer */
	public Indexer(String indexDirectory) {
		
		INDEX_DIR = indexDirectory;
	}

	private IndexWriter indexWriter = null;

	public IndexWriter getIndexWriter(boolean create) throws IOException {

		if (create == true) {

			if (indexWriter == null) {

				Directory indexDir = FSDirectory.open(Paths.get(INDEX_DIR));

				IndexWriterConfig config = new IndexWriterConfig(new StandardAnalyzer());

				indexWriter = new IndexWriter(indexDir, config);
			}
		}
		return indexWriter;
	}

	public void closeIndexWriter() throws IOException {

		if (indexWriter != null) {

			indexWriter.close();
		}
	}

	public void indexDictionary(int id, String engWord, String gerWord) throws IOException {

		IndexWriter writer = getIndexWriter(false);

		Document doc = new Document();
		doc.add(new StringField("id", Integer.toString(id), Field.Store.YES));
		doc.add(new StringField("eng", engWord, Field.Store.YES));
		doc.add(new StringField("ger", gerWord, Field.Store.YES));
		String fullSearchableText = engWord + " " + gerWord;
		doc.add(new TextField("content", fullSearchableText, Field.Store.NO));

		writer.addDocument(doc);
		
		
	}

	public void buildIndexes() throws IOException, ClassNotFoundException {

		// check whether index directory is empty or not
		int flag = 0;
		File file = new File(INDEX_DIR);

        //
        // Check to see if the object represent a directory.
        //
        if (file.isDirectory()) {
            //
            // Get list of file in the directory. When its length is not zero
            // the folder is not empty.
            //
            String[] files = file.list();

            if (files.length > 0) {
                //System.out.println("The " + file.getPath() + " is not empty!");
            }
            else{
            	//System.out.println("The " + file.getPath() + " is empty!");
            	flag = 1;
            }
        }

		if (flag == 1) {
			// load the sqlite-JDBC driver using the current class loader
			Class.forName("org.sqlite.JDBC");

			Connection connection = null;
			try {
				
				//File f1 = new File("dict.db");
				//String path = f1.getAbsolutePath();
				
				//System.out.println("jdbc:sqlite:"+path);
				
				/*System.out.println("jdbc:sqlite:C:\\Users\\Mahbubul Alam\\workspace\\Lucene\\src\\com\\anondo\\lucene\\dict.db");
				System.out.println("jdbc:sqlite:"+path);
				
				// create a database connection
				connection = DriverManager.getConnection(
						"jdbc:sqlite:C:\\Users\\Mahbubul Alam\\workspace\\Lucene\\src\\com\\anondo\\lucene\\dict.db");*/
				
				//connection = DriverManager.getConnection("jdbc:sqlite:"+path);
				connection = DriverManager.getConnection("jdbc:sqlite::resource:dict.db");

				Statement statement = connection.createStatement();
				statement.setQueryTimeout(30); // set timeout to 30 sec.

				rs = statement.executeQuery("select * from EngToGer");


				getIndexWriter(true);

				while (rs.next()) {
					indexDictionary(rs.getInt("id"), rs.getString("english"), rs.getString("german"));
				} System.out.println("done now you can search");
			} catch (SQLException e) {
				// if the error message is "out of memory",
				// it probably means no database file is found
				System.err.println(e.getMessage());
			} finally {
				try {
					if (connection != null)
						connection.close();
				} catch (SQLException e) {
					// connection close failed.
					System.err.println(e);
				}
			}

			// Don't forget to close the index writer when done
			closeIndexWriter();
		} else {

			getIndexWriter(true);

			// Don't forget to close the index writer when done
			closeIndexWriter();
		}
	}

}