package com.company.SearchEngine;

import com.company.BTree.IBTree;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class SearchEngine implements ISearchEngine {

    private final List<String> documentsID;
    private final IBTree<String, String> documents;

    public SearchEngine(IBTree<String, String> documents) {
        documentsID = new ArrayList<>();
        this.documents = documents;
    }

    @Override
    public void indexWebPage(String filePath) {

        try {

            NodeList docs = this.getDocuments(filePath);

            for (int i = 0; docs != null && i < docs.getLength(); i++){
                Element doc = (Element) docs.item(i);
                String documentID = doc.getAttribute("id");
                String documentText = doc.getTextContent();
                this.documentsID.add(documentID);
                this.documents.insert(documentID, documentText);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void indexDirectory(String directoryPath) {

        File[] xmlFiles = new File(directoryPath).listFiles();
        if (xmlFiles != null) {
            for (File xmlFile : xmlFiles) {
                this.indexWebPage(xmlFile.getPath());
            }
        }

    }

    @Override
    public void deleteWebPage(String filePath) {

        NodeList docs = this.getDocuments(filePath);

        for (int i = 0; docs != null && i < docs.getLength(); i++){
            Element doc = (Element) docs.item(i);
            String documentID = doc.getAttribute("id");
            for (int j = 0; j < this.documentsID.size(); j++){
                if (this.documentsID.get(j).equals(documentID))
                {
                    this.documentsID.remove(j);
                    this.documents.delete(documentID);
                    j--;

                }
            }

        }

    }

    @Override
    public List<ISearchResult> searchByWordWithRanking(String word) {
        List<ISearchResult> results = new ArrayList<>();
        for (String s : this.documentsID) {
            int counts = getNumberOfRepetitions(this.documents.search(s), word);
            if (counts > 0) {
                SearchResult result = new SearchResult();
                result.setId(s);
                result.setRank(counts);
                results.add(result);
            }
        }
        return results;
    }

    @Override
    public List<ISearchResult> searchByMultipleWordWithRanking(String sentence) {
        List<ISearchResult> results = new ArrayList<>();
        for (String s : this.documentsID) {
            String [] words = sentence.split("\n");
            int minCounts = 100000;
            for (String word : words) {
                minCounts = Math.min(getNumberOfRepetitions(this.documents.search(s), word), minCounts);
                if (minCounts == 0) break;
            }
            if (minCounts > 0){
                SearchResult result = new SearchResult();
                result.setId(s);
                result.setRank(minCounts);
                results.add(result);
            }
        }
        return results;
    }

    private NodeList getDocuments(String filePath) {
        try {

            File xmlFile = new File(filePath);
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.parse(xmlFile);

            return document.getElementsByTagName("doc");

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private int getNumberOfRepetitions(String text, String word){
        int counts = 0;

        String[] words = text.split("[ \n]");

        for (String s : words) {
            if (word.equalsIgnoreCase(s))
                counts++;
        }
        return counts;
    }
}
