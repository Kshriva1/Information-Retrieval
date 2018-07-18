import java.io.*;

public class Document {

    private static int globalDocNumber = 0;

    private int docNumber;

    private String headline;

    private String reviewer;

    private String snippet;

    private String rate;
     
    private String docPath;

   

    public Document() {
        docNumber = ++globalDocNumber;
    }

    public void initialize(File path) throws IOException {

       docPath = path.getAbsolutePath();
        

        BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(path)));

        String line;
        StringBuilder builder = new StringBuilder();
        String snippetString = "";
        int SNIPPET_LENGTH = 50;
        int count1=0,count2=0,count3;

        StringBuilder builder1 = new StringBuilder();
        String reviewString = "";
        int REVIEW_LENGTH = 15;
        
       
        while ((line = br.readLine()) != null) {
            if (line.contains("<HTML>")) {
                 headline = br.readLine().replaceAll(",", " ").trim();
            }
                if(line.contains("Capsule review") || line.contains("capsule review") || line.contains("capsule Review") || line.contains("Capsule Review"))
                {
                 builder = builder.append(line.replaceAll("\t", "")).append(" ");
                }
                else
                {
                  if(line.contains("film") || line.contains("Film"))
                  {
                    builder = builder.append(line.replaceAll("\t", "")).append(" ");
                  }
                }


             

            if(line.contains("film review") || line.contains("Film review") || line.contains("film Review") || line.contains("Film Review") || line.contains("Screenplay by") || line.contains("commentary") || line.contains("Film commentary") || line.contains("(Directed") || line.contains(" Evelyn C. Leeper") || line.contains("Great Unknown") || line.contains("Lesser-Known Films") || line.contains("(1972-written and directed by"))  {
               
                         builder1 = builder1.append(line.replaceAll("\t", "")).append(" ");     
            
             }
          

           if (line.contains("-4 to +4")) {
             if(line.contains("0") || line.contains("-1") || line.contains("-2") || line.contains("-3") || line.contains("-4"))
              {
                rate = "Negative";
              }
                if(line.contains("0") || line.contains("1") || line.contains("2") || line.contains("3") || line.contains("4"))
                  {
                 rate = "Positive";
                  }
               }
             
                      
                 
            
           else if(line.contains("Capsule review") || line.contains("capsule review") || line.contains("capsule Review") || line.contains("Capsule Review"))
             {
               
                   if(line.contains("outstanding") || line.contains("best") || line.contains("exciting"))
                      count1++;       
                   
                   if(line.contains("dull") || line.contains("boring") || line.contains("dissapointing") || line.contains("failure"))
                      count2++;
               
                  count3 = count1 - count2;
                  if(count3 > 0)
                    rate = "Positive";
                 else
                    rate = "Negative";
             }
           
             else if(line.contains("rate") && line.contains(headline))
             {
               
                  if(line.contains("outstanding") || line.contains("best") || line.contains("exciting"))
                       rate = "Positive";
                     if(line.contains("dull") || line.contains("boring") || line.contains("dissapointing") || line.contains("failure"))
                        rate = "Negative";
              } 
             
         
           
       
         } 

        String [] arr = builder.toString().trim().split(" ");

        for(int i = 0; i < Math.min(SNIPPET_LENGTH,arr.length) ; i++)
            snippetString = snippetString + " " + arr[i].replaceAll(",", "");

        this.snippet = snippetString;

         String [] arr1 = builder1.toString().trim().split(" ");

        for(int j = 0; j < Math.min(REVIEW_LENGTH,arr1.length) ; j++)
            reviewString = reviewString + " " + arr1[j].replaceAll(",", "");

        this.reviewer = reviewString;     
        
    }

    public String getHeadline() {
        return headline;
    }

    public String getSnippet() {
        return snippet;
    }

    public String getReview() {
        return reviewer;
    }

    public String getRate() {
        return rate;
    }
    
    public String getDocPath() {
        return docPath;
}

    
    public int getDocNumber() {
        return docNumber;
    }


    public static void setGlobalDocNumber(int globalDocNumber) {
        Document.globalDocNumber = globalDocNumber;
         }

    public void setDocNumber(int docNumber)
    {
      this.docNumber = docNumber;
    }
   
    public void setHeadline(String headline)
    {
       this.headline = headline;
     
    }
  
    public void setSnippet(String snippet)
    {
       this.snippet = snippet;
    }

    public void setDocPath(String docPath)
    {
      this.docPath = docPath;
    }

    public void setReview(String reviewer)
    {
      this.reviewer = reviewer;
    }
  
    public void setRate(String rate)
     {
       this.rate = rate;
     }

    
}

