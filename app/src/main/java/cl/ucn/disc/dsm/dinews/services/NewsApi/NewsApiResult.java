package cl.ucn.disc.dsm.dinews.services.NewsApi;
import java.util.ArrayList;
import java.util.List;

public class NewsApiResult {
    public String status;
    public long totalResults;
    public List<Article> articles = new ArrayList<>();

}
