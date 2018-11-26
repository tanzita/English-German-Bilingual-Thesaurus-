# English-German Bilingual Thesaurus using Lucene

## Project Members
  Tanzia Haque Tanzi [tanzia.haque.tanzi@gmail.com]
  
  Mahbub Ul Alam [mahbub.ul.alam.anondo@gmail.com]

## Description

In this project we built a English-German Bilingual Thesaurus. We used sqlite to create the database. We used data from dict.cc and obeyed their copyright rules. We then used Lucene to index the data. For indexing we used StandardAnalyzer. We then created document based on German word and English word and finally based on the query the searching is performed.

We made sure that indexing is done only once to perform the search operation more speedy. To make the system user friendly we created a Graphical User Interface where User can provide the input and view the output.

We have sorted the output according to the hit count of individual result.

## Work distribution

The work scope of the two project members are described as follows,

* Data collection, preprocessing and database creation: Mahbub Ul Alam and Tanzia Haque Tanzi
* Lucene Indexing and query creation: Tanzia Haque Tanzi and Mahbub Ul Alam
* Graphical User Interface Creation: Tanzia Haque Tanzi

## Conclusion

We have created this project as a indexing based search project. So in traditional sense it is not like a dictionary but more of a most important words/phrase collection of a given word/phrase.
