package cl.ucn.disc.dsm.dinews.services.NewsApi;

import android.os.Build;

import com.squareup.okhttp.OkHttpClient;

import net.openhft.hashing.LongHashFunction;

import org.apache.commons.lang3.builder.Builder;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.threeten.bp.ZonedDateTime;
import org.threeten.bp.format.DateTimeParseException;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import javax.xml.transform.Transformer;

import cl.ucn.disc.dsm.dinews.model.Noticia;
import okhttp3.Request;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.HttpException;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static cl.ucn.disc.dsm.dinews.MainApplication.log;

public class NewsApiNoticiaService {

    private static Response<String> response;
    /**
     * The NewsAPI
     */
    public final NewsApi newsApi;

    /**
     * The Constructor.
     */
    public NewsApiNoticiaService() {
        // Logging with slf4j
        final HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor(log::debug)
                .setLevel(HttpLoggingInterceptor.Level.BODY);

        // Web Client
        final OkHttpClient httpClient = new OkHttpClient().clone();

        // https://futurestud.io/tutorials/retrofit-getting-started-and-android-client
        this.newsApi = new Retrofit.Builder()
                // The main URL
                .baseUrl(NewsApi.BASE_URL)
                // JSON to POJO
                .addConverterFactory(GsonConverterFactory.create())
                // Validate the interface
                .validateEagerly(true)
                // Build the Retrofit ..
                .build()
                // .. get the NewsApi.
                .create(NewsApi.class);
        /*
        * // Web Client
    final OkHttpClient httpClient = new Builder()
        .connectTimeout(5, TimeUnit.SECONDS)
        .writeTimeout(5, TimeUnit.SECONDS)
        .readTimeout(5, TimeUnit.SECONDS)
        .callTimeout(10, TimeUnit.SECONDS)
        .addNetworkInterceptor(loggingInterceptor)
        .build();
        * */





    }

    /**
     * Get the Noticias from the Call.
     *
     * @param theCall to use.
     * @return the {@link List} of {@link Noticia}.
     */
    private static List<Noticia> getNoticiasFromCall(final Call<NewsApiResult> theCall) throws NewsAPIException {

        try {

            // Get the result from the call
            final Response<NewsApiResult> response = theCall.execute();

            // UnSuccessful !
            if (!response.isSuccessful()) {

                // Error!
                throw new NewsAPIException(
                        "Can't get the NewsResult, code: " + response.code(),
                        new HttpException(response)
                );

            }

            final NewsApiResult theResult = response.body();

            // No body
            if (theResult == null) {
                throw new NewsAPIException("NewsResult was null", new HttpException(response));
            }

            // No articles
            if (theResult.articles == null) {
                throw new NewsAPIException("Articles in NewsResult was null", new HttpException(response));
            }
            // Article to Noticia (transformer)
            return theResult.articles.stream()
                    .map(NewsApiNoticiaService::transform)
                    .collect(Collectors.toList());

        } catch (final IOException | NewsAPIException ex) {
            throw new NewsAPIException("Can't get the NewsResult", ex);
        }

    }
    /**
     * Article to Noticia.
     *
     * @param article to transform
     * @return the Noticia.
     */
    public static Noticia transform(final Article article) {

        // Nullity
        if (article == null) {
            throw new NewsApiNoticiaService.NewsAPIException("Article was null", new HttpException(response));
        }

        // The host
        final String host = getHost(article.url);

        // Si el articulo es null ..
        if (article.title == null) {

            log.warn("Article without title: {}", toString(article));

            // .. y el contenido es null, lanzar exception!
            if (article.description == null) {
                throw new NewsApiNoticiaService.NewsAPIException("Article without title and description", new HttpException(response));
            }


            article.title = "Information unavailable";
        }

        // FIXED: En caso de no haber una fuente.
        if (article.source == null) {
            article.source = new Source();

            if (host != null) {
                article.source.name = host;
            } else {
                article.source.name = "No Source*";
                log.warn("Article without source: {}", toString(article));
            }
        }

        // FIXED: Si el articulo no tiene author
        if (article.author == null) {

            if (host != null) {
                article.author = host;
            } else {
                article.author = "No Author*";
                log.warn("Article without author: {}", toString(article));
            }
        }

        // The date.
        final ZonedDateTime publishedAt = parseZonedDateTime(article.publishedAt)
                .withZoneSameInstant(Noticia.ZONE_ID);

        // The unique id (computed from hash)
        final Long theId = LongHashFunction.xx()
                .hashChars(article.title + article.source.name);

        // The Noticia.
        return new Noticia(
                theId,
                article.title,
                article.source.name,
                article.author,
                article.url,
                article.urlToImage,
                article.description,
                article.content,
                publishedAt
        );

    }

    /**
     * Convierte una fecha de {@link String} a una {@link ZonedDateTime}.
     *
     * @param fecha to parse.
     * @return the fecha.
     * @throws cl.ucn.disc.dsm.dinews.services.NewsApi.NewsApiNoticiaService.NewsAPIException en caso de no lograr
     *                                                                                         convertir la fecha.
     */
    private static ZonedDateTime parseZonedDateTime(final String fecha) {

        // Na' que hacer si la fecha no existe
        if (fecha == null) {
            throw new NewsApiNoticiaService.NewsAPIException("Can't parse null fecha", new HttpException(response));
        }

        try {
            // Tratar de convertir la fecha ..
            return ZonedDateTime.parse(fecha);
        } catch (DateTimeParseException ex) {

            // Mensaje de debug
            log.error("Can't parse date: ->{}<-. Error: ", fecha, ex);

            // Anido la DateTimeParseException en una NoticiaTransformerException.
            throw new NewsApiNoticiaService.NewsAPIException("Can't parse date: " + fecha, ex);
        }
    }
    /**
     * Get the host part of one url.
     *
     * @param url to use.
     * @return the host part (without the www)
     */
    private static String getHost(final String url) {

        try {

            final URI uri = new URI(url);
            final String hostname = uri.getHost();

            // to provide faultproof result, check if not null then return only hostname, without www.
            if (hostname != null) {
                return hostname.startsWith("www.") ? hostname.substring(4) : hostname;
            }

            return null;

        } catch (final URISyntaxException | NullPointerException ex) {
            return null;
        }
    }
    /**
     * Transforma en String un objeto t mostrando sus atributos.
     *
     * @param t   to convert.
     * @param <T> type of t.
     * @return the object in string format.
     */
    public static <T> String toString(final T t) {
        return ReflectionToStringBuilder.toString(t, ToStringStyle.MULTI_LINE_STYLE);
    }

    /**
     * The Exception.
     */
    public static final class NewsAPIException extends RuntimeException {

        public NewsAPIException(final String message) {
            super(message);
        }

        public NewsAPIException(final String message, final Throwable cause) {
            super(message, cause);
        }

    }
    /**
     * Get the Noticias from the Call.
     *
     * @param pageSize how many.
     * @return the {@link List} of {@link Noticia}.
     */

    public List<Noticia> getNoticias(int pageSize) {

        // the Call
        final Call<NewsApiResult> theCall = this.newsApi.getEverything(pageSize);

        // Process the Call.
        return getNoticiasFromCall(theCall);
    }
}
