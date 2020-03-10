package cl.ucn.disc.dsm.dinews.activities;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import cl.ucn.disc.dsm.dinews.R;
import cl.ucn.disc.dsm.dinews.databinding.ActivityMainBinding;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The Main Launcher Activity.
 *
 * @author Diego Barraza Moreno.
 */

public class MainActivity extends AppCompatActivity {

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
            });
        }

    }
}