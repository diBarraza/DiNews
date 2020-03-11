package cl.ucn.disc.dsm.dinews.services;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;

import cl.ucn.disc.dsm.dinews.model.Noticia;
import cl.ucn.disc.dsm.dinews.services.NewsApi.NewsApiNoticiaService;
import cl.ucn.disc.dsm.dinews.services.NewsApi.NewsApiResult;

import static cl.ucn.disc.dsm.dinews.services.NoticiaServiceTest.log;

public class NewsApiNoticiaServiceTest {



    /**
     * Test {@link NoticiaService#getNoticias(int)} with NewsAPI.org
     */
    @Test
    public void testGetNoticiasNewsApi() {

        final int size = 20;

        log.debug("Testing the NewsApiNoticiaService, requesting {} News.", size);

        // The noticia service
        final NewsApiNoticiaService noticiaService = new NewsApiNoticiaService();

        // The List of Noticia.
        final List<Noticia> noticias = noticiaService.getNoticias(size);

        Assertions.assertNotNull(noticias);
        Assertions.assertEquals(noticias.size(), size, "Error de tamanio");

        for (final Noticia noticia : noticias) {
            log.debug("Noticia: {}.", noticia);
        }

        log.debug("Done.");

    }
}
