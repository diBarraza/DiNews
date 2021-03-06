package cl.ucn.disc.dsm.dinews.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.Toast;

import cl.ucn.disc.dsm.dinews.R;
import cl.ucn.disc.dsm.dinews.activities.adapters.NoticiaAdapter;
import cl.ucn.disc.dsm.dinews.databinding.ActivityMainBinding;

import cl.ucn.disc.dsm.dinews.model.Noticia;
import cl.ucn.disc.dsm.dinews.services.NewsApi.NewsApiNoticiaService;
import cl.ucn.disc.dsm.dinews.services.NoticiaService;

import org.apache.commons.lang3.time.StopWatch;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * The Main Launcher Activity.
 *
 * @author Diego Barraza Moreno.
 */

public class MainActivity extends AppCompatActivity {

    /**
     * The Adapter
     */
    private NoticiaAdapter noticiaAdapter;

    /**
     * The NoticiaService
     */
    private NewsApiNoticiaService newsApiNoticiaService;
    /**
     * The Logger
     */
    private static final Logger log = LoggerFactory.getLogger(MainActivity.class);

    /**
     * @param savedInstanceState to use.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Inflate the layout
        ActivityMainBinding binding = ActivityMainBinding.inflate(getLayoutInflater());

        // Assign to the main view.
        setContentView(binding.getRoot());

        // Set the toolbar
        {
            this.setSupportActionBar(binding.toolbar);
        }

        // The refresh
        {
            binding.swlRefresh.setOnRefreshListener(() -> {
                log.debug("Refreshing ..");

                // Execute in background ..
                AsyncTask.execute(() -> {

                    // How much time do we need?
                    final StopWatch stopWatch = StopWatch.createStarted();

                    try {

                        // 1. Get the List from NewsApi (in background)
                        final List<Noticia> noticias = this.newsApiNoticiaService.getNoticias(50);

                        // (in UI)
                        this.runOnUiThread(() -> {

                            // 2. Set in the adapter (
                            this.noticiaAdapter.setNoticias(noticias);

                            // 3. Show a Toast!
                            Toast.makeText(this, "Done: " + stopWatch, Toast.LENGTH_SHORT).show();

                        });

                    } catch (Exception ex) {

                        log.error("Error", ex);

                        // (in UI)
                        this.runOnUiThread(() -> {

                            // Build the message
                            final StringBuffer sb = new StringBuffer("Error: ");
                            sb.append(ex.getMessage());
                            if (ex.getCause() != null) {
                                sb.append(", ");
                                sb.append(ex.getCause().getMessage());
                            }

                            // 3. Show the Toast!
                            Toast.makeText(this, sb.toString(), Toast.LENGTH_LONG).show();

                        });

                    } finally {

                        // 4. Hide the spinning circle
                        binding.swlRefresh.setRefreshing(false);

                    }

                });

            });
        }

        // The Adapter + RecyclerView
        {
            // The Adapter
            this.noticiaAdapter = new NoticiaAdapter();

            // The Adapter
            binding.rvNoticias.setAdapter(this.noticiaAdapter);

            // The layout (ListView)
            binding.rvNoticias.setLayoutManager(new LinearLayoutManager(this));

            // The separator (line)
            binding.rvNoticias.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        }

        // The NoticiaService
        this.newsApiNoticiaService = new NewsApiNoticiaService();

    }
}