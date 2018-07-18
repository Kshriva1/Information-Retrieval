import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

public class Programpart3 {


    private static Map<String, Dictionary> dictionaryMap;
    private static List<Document> documentList;
    private static Map<Integer, PostingList> postingsList;

    

    public static void main(String[] args) {



     String dictionaryFile = args[0];
     String postingsFile = args[1];
     String docsTableFile = args[2];
     String resultFile = args[3];



        Scanner sc = new Scanner(System.in);
        String query;
        Tokenizer tokenizer = new Tokenizer();
        StopWords stopWords = new StopWords();
        BufferedWriter bw = null;

        try {
            
            dictionaryMap = createDictionaryMap(dictionaryFile);
            documentList = createDocumentList(docsTableFile);
            postingsList = createPostingList(postingsFile);
            bw = new BufferedWriter(new FileWriter(resultFile));

        } catch (IOException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }

        stopWords.createStopWordsList();


        while (true) {
            System.out.println();
            System.out.println("Enter query or EXIT to exit");
            query = sc.nextLine();

            
            if (query.equalsIgnoreCase("EXIT")) {
                System.out.println("EXIT!");
                sc.close();
                System.exit(1);
            }

        
            String[] queryTokens = tokenizer.TokenizeQuery(query, stopWords);
            List<Ranking> rankingList = getQueryRanking(queryTokens);
            Collections.sort(rankingList);
            writeResult(query, bw, rankingList);
        }
    }

    private static void writeResult(String query, BufferedWriter bw, List<Ranking> rankingList) {

        StringBuilder content = new StringBuilder();
        int counter = 0;

        if (rankingList.isEmpty())
            content.append("NO RESULTS").append("\n");

        else {
            for (Ranking ranking : rankingList) {
              

                content.append(query).append("\n");
                content.append(ranking.getDocumentPath()).append("\n");
                content.append(ranking.getHeadline()).append("\n");
                content.append(ranking.getReview()).append("\n");
                content.append(ranking.getSnippet()).append("\n");
                content.append(ranking.getRate()).append("\n\n");
            }
        }

        try {
            bw.write(content.toString());
            bw.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private static List<Document> createDocumentList(String docsTableFile) throws IOException {
        List<Document> list = new ArrayList<>();

        BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(docsTableFile)));

        Document document;
        String line;
        String[] tokens;

        while ((line = reader.readLine()) != null) {
            tokens = line.split("\t");
            document = new Document();
            document.setDocNumber(Integer.parseInt(tokens[0]));
            document.setHeadline(tokens[2]);
            document.setReview(tokens[3]);
            document.setRate(tokens[5]);
            document.setSnippet(tokens[4]);
            document.setDocPath(tokens[1]);

            list.add(document);
        }

        return list;
    }

    private static Map<Integer, PostingList> createPostingList(String postingsFile) throws IOException {
        Map<Integer, PostingList> list = new Hashtable<>();

        BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(postingsFile)));

        PostingList postingList;
        String line;
        String[] tokens;
        int index = 0;

        while ((line = reader.readLine()) != null) {
            tokens = line.split(",");
            postingList = new PostingList();
            postingList.setDocId(Integer.parseInt(tokens[0]));
            postingList.setTermFrequency(Integer.parseInt(tokens[1]));

            list.put(index++, postingList);
        }

        return list;
    }

    private static Map<String, Dictionary> createDictionaryMap(String dictionaryFile) throws IOException {
        Map<String, Dictionary> map = new TreeMap<>();

        BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(dictionaryFile)));

        Dictionary dictionary;
        String[] tokens;
        String line;

        while ((line = reader.readLine()) != null) {
            tokens = line.split(",");
            dictionary = new Dictionary();
            dictionary.setTerm(tokens[0]);
           
            dictionary.setDocumentFrequency(Integer.parseInt(tokens[1]));
            dictionary.setOffset(Integer.parseInt(tokens[2]));

            map.put(tokens[0], dictionary);
        }

        return map;
    }

   

private static List<Ranking> getQueryRanking(String[] queryTokens) {
        List<Ranking> list = new ArrayList<>();
        Set<Integer> docIdSet = new TreeSet<>();
        Document document = new Document();

        for (String term : queryTokens) {
            Dictionary dictionary = dictionaryMap.get(term);
            if (dictionary == null) {
                System.out.println("NO RESULTS");
                continue;
            }

            List<PostingList> postings = getListOfPostingList(dictionary);
            for (PostingList posting : postings) {
                docIdSet.add(posting.getDocId());
            }
        }
                
              for (Integer docId : docIdSet) {
           
            document = getDocumentByDocId(docId);

            
       

            Ranking ranking = new Ranking();
            ranking.setHeadline(document.getHeadline());
            ranking.setDocumentPath(document.getDocPath());
            ranking.setReview(document.getReview());
            ranking.setRate(document.getRate());
            ranking.setSnippet(document.getSnippet());

            list.add(ranking);

         }
        
            
        return list;
    }


     private static Document getDocumentByDocId(Integer docId) {
        for (Document document : documentList) {
            if (document.getDocNumber() == docId)
                return document;
        }

        return null;
}


    
    private static List<PostingList> getListOfPostingList(Dictionary dictionary) {
        List<PostingList> list = new ArrayList<>();

        int offset = dictionary.getOffset();
        int df = dictionary.getDocumentFrequency();

        for (int i = offset; i < (offset + df); i++) {
            list.add(postingsList.get(i));
        }

        return list;
    }

    
}
