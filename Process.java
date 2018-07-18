import java.util.*;
import java.io.*;

public class Process {
    private Map<String, Dictionary> dictionaryMap = new TreeMap<>();
    private List<PostingList> postingLists = new ArrayList<>();
    private Map<Integer, Map> mapOfTokensMap = new Hashtable<>();


public void doProcess(List<File> filePaths, List<Document> documentList) {

        StopWords stopWords = new StopWords();
        stopWords.createStopWordsList();
        for (int i = 0; i < filePaths.size(); i++) {
            Map tokensMap;
            Document document = new Document();
            try {
                tokensMap = Tokenizer.Tokenize(filePaths.get(i).toString(), stopWords);
                document.initialize(filePaths.get(i));
           
                documentList.add(document); 
                createDictionary(document, tokensMap);
                mapOfTokensMap.put(document.getDocNumber(), tokensMap);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        generateOffset();

        createPostingList();

    }


 private void createPostingList() {
        PostingList postingList;
        Dictionary dictionary;

        for (String term : dictionaryMap.keySet()) {
            dictionary = dictionaryMap.get(term);
            for (int i = 0; i < dictionary.getDocumentFrequency(); i++) {
                postingList = new PostingList();
                postingList.setDocId(dictionary.getDocId(i));
                Map tokensMap = mapOfTokensMap.get(dictionary.getDocId(i));
                int count = (int) tokensMap.get(term);
                postingList.setTermFrequency(count);
                postingLists.add(postingList);
            }
        }
    }



private void createDictionary(Document document, Map<String, Integer> tokensMap) {

        Dictionary dictionary;

        for (String term : tokensMap.keySet()) {
            if (dictionaryMap.containsKey(term))
                dictionary = dictionaryMap.get(term);
            else
                dictionary = new Dictionary(term);

            dictionary.updateDictionary(tokensMap.get(term), document);
            dictionaryMap.put(term, dictionary);
        }
    }



 private void generateOffset() {
        Dictionary dictionary;
        for (String term : dictionaryMap.keySet()) {
            dictionary = dictionaryMap.get(term);
            dictionary.updateOffset();
        }
    }


public void writeDocumentTable(List<Document> documentList, String filePath) throws IOException {

        BufferedWriter documentWriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(filePath)));

        for (int i = 0; i < documentList.size(); i++) {
            Document document = documentList.get(i);
            documentWriter.write(document.getDocNumber() + "\t" + document.getDocPath() +  "\t" + document.getHeadline() + "\t" + document.getReview() + "\t" + document.getSnippet() + "\t" + document.getRate() +  "\n");
            documentWriter.flush();
        }

        documentWriter.close();
    }


 public void writeDictionary(String dictionaryPath) throws IOException {

BufferedWriter dictionaryWriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(dictionaryPath)));

        for (String term : dictionaryMap.keySet()) {
            Dictionary dictionary = dictionaryMap.get(term);
            dictionaryWriter.write(dictionary.getTerm() + "," +
                dictionary.getDocumentFrequency() + "," + dictionary.getOffset() + "\n");
            dictionaryWriter.flush();
        }
        dictionaryWriter.close();
    }



public void writePostingFile(String filePath) throws IOException {
        BufferedWriter postingWriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(filePath)));

        for (PostingList postingList : postingLists) {
            postingWriter.write(postingList.getDocId() + "," + postingList.getTermFrequency() + "\n");
            postingWriter.flush();
        }

        postingWriter.close();
    }

   
}


