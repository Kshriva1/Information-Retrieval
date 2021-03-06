import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Programpart2 {

    private static List<Document> documentList = new ArrayList<>();

   

    public static void main(String[] args) {
        String DOCUMENT_TABLE = args[3];
         String DICTIONARY = args[1];
         String POSTING = args[2];

        String filePath = args[0];
        File dir = new File(filePath);

        List<File> filePaths = new ArrayList<>();

        Set<String> fileNames = new HashSet<>();
        getFiles(dir, fileNames, filePaths);

        Process processor = new Process();
        processor.doProcess(filePaths, documentList);

        try {
            processor.writeDocumentTable(documentList, DOCUMENT_TABLE);
            processor.writeDictionary(DICTIONARY);
            processor.writePostingFile(POSTING);
        } catch (IOException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
    }



 private static void getFiles(File file, Set<String> fileNames, List<File> filePaths) {
        if(file.isDirectory()) {
            File[] subFiles = file.listFiles();
            for (File f : subFiles)
                getFiles(f, fileNames, filePaths); 
        } else {
            if(!"".equalsIgnoreCase(file.getName()) && !fileNames.contains(file.getName()))
                filePaths.add(file);
        }
    }
}



