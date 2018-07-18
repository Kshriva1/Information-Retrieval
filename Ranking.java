
public class Ranking implements Comparable<Ranking> {

	
	private String headline;
	
	private String documentPath;
	
	private String snippet;
	
	private String reviewer;
	
	private String rate;
	
	
	
	public String getHeadline()
	{
		return headline;
	}
	
	
	public String getDocumentPath() 
	  {
        return documentPath;
      }
	
	public void setHeadline(String headline)
	{
		this.headline = headline;
	}
	
	public void setDocumentPath(String documentPath)
	{
		this.documentPath = documentPath;
	}
	
	
	public String getSnippet()
	{
		return snippet;
	}
	
	
	 public void setSnippet(String snippet) {
	        this.snippet = snippet;
	}
	
	
	 public String getReview() {
	        return reviewer;
	    }

	    public void setReview(String reviewer) {
	        this.reviewer = reviewer;
	}
	    
	    public String getRate() {
	        return rate;
	    }

	    public void setRate(String rate) {
	        this.rate = rate;
	}
	    
	    @Override
	    public int compareTo(Ranking ranking) {
	        return this.documentPath.compareTo(ranking.documentPath);
	} 
           
	    
	     
	    
}

