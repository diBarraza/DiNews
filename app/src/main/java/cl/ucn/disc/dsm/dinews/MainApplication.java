package cl.ucn.disc.dsm.dinews;
import android.app.Application;

import androidx.appcompat.app.AppCompatDelegate;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.jakewharton.threetenabp.AndroidThreeTen;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The Main Application
 *
 * @author Diego Barraza Moreno.
 */
public class MainApplication extends Application {
    /**
     * The Logger.
     */
    public static final Logger log = LoggerFactory.getLogger(MainApplication.class);

    /**
     * Called when the application is starting, before any activity, service, or receiver objects (excluding content
     * providers) have been created.
     */
    @Override
    public void onCreate() {
        super.onCreate();
        AndroidThreeTen.init(this);
        // Day and Night support
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_AUTO_BATTERY);
        // Facebook fresco
        Fresco.initialize(this);


        log.debug("Initializing: Done.");

    }

}
