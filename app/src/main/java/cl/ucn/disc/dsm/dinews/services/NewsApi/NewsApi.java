package cl.ucn.disc.dsm.dinews.services.NewsApi;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Query;

/**
 * Interface de acceso a los endpoints the NewsApi.org
 *
 * @author Diego Urrutia-Astorga.
 */
interface NewsApi {

    /**
     * The URL
     */
    String BASE_URL = "https://newsapi.org/v2/";

    /**
     * The API Key
     */
    String API_KEY = "1be22468d7a04dcf9b70d2d6439c30e2";

    /**
     * https://newsapi.org/docs/endpoints/top-headlines
     *
     * @param category to use as filter.
     * @param pageSize the number of results to get.
     * @return the call of {@link NewsApiResult}.
     */
    @Headers({"X-Api-Key: " + API_KEY})
    @GET("top-headlines")
    Call<NewsApiResult> getTopHeadlines(@Query("category") final String category, @Query("pageSize") final int pageSize);

    /**
     * https://newsapi.org/docs/endpoints/everything
     *
     * @return the call of {@link NewsApiResult}.
     */
    @Headers({"X-Api-Key: " + API_KEY})
    // Change the list of sources.
    @GET("everything?sources=ars-technica,wired,hacker-news,recode")
    Call<NewsApiResult> getEverything(@Query("pageSize") final int pageSize);

}

