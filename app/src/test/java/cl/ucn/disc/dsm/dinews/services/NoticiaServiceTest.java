/*
 * Copyright 2019-2020 Diego Urrutia Astorga <durrutia@ucn.cl>.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package cl.ucn.disc.dsm.dinews.services;

import cl.ucn.disc.dsm.dinews.model.Noticia;
import cl.ucn.disc.dsm.dinews.services.mockup.MockupNoticiaService;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
public class NoticiaServiceTest {

    /**
     * The Logger.
     */
    private static final Logger log = LoggerFactory.getLogger(NoticiaServiceTest.class);

    /**
     * Test {@link NoticiaService#getNoticias(int)}
     */
    @Test
    public void testGetNoticiasMockup() {

        log.debug("Testing the NoticiaService ..");

        // The noticia service
        final NoticiaService noticiaService = new MockupNoticiaService();

        // The List of Noticia.
        final List<Noticia> noticias = noticiaService.getNoticias(2);

        Assertions.assertNotNull(noticias);
        Assertions.assertEquals(noticias.size(), 2, "Error de tamanio");

        for (final Noticia noticia : noticias) {
            log.debug("Noticia: {}.", noticia);
        }

        log.debug("Done.");
    }

}
