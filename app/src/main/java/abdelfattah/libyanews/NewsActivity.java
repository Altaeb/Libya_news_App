package abdelfattah.libyanews;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.navigation.NavigationView;

import java.util.Objects;

import abdelfattah.libyanews.fragment.CultureFragment;
import abdelfattah.libyanews.fragment.EducationFragment;
import abdelfattah.libyanews.fragment.FashionFragment;
import abdelfattah.libyanews.fragment.HomeFragment;
import abdelfattah.libyanews.fragment.LifeStyleFragment;
import abdelfattah.libyanews.fragment.PoliticsFragment;
import abdelfattah.libyanews.fragment.SportsFragment;
import abdelfattah.libyanews.fragment.TechnologyFragment;

public class NewsActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

// --Commented out by Inspection START (10/22/2019 10:58 PM):
//    /* Tag for log messages */
//    private static String LOG_TAG = NewsActivity.class.getName ();
// --Commented out by Inspection STOP (10/22/2019 10:58 PM)

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate ( savedInstanceState );
        setContentView ( R.layout.activity_news );

        Toolbar toolbar = findViewById ( R.id.default_toolbar );
        setSupportActionBar ( toolbar );

        // Setup the navigation drawer
        DrawerLayout drawer = findViewById ( R.id.drawer_layout );
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle (
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close );
        drawer.addDrawerListener ( toggle );
        toggle.syncState ();

        NavigationView navigationView = findViewById ( R.id.nav_view );
        navigationView.setNavigationItemSelectedListener ( this );

        // Start the first Menu item in Navigation Drawer and highlight it
        onNavigationItemSelected ( navigationView.getMenu ().getItem ( 0 ).setChecked ( true ) );
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId ();
        switch (id) {
            case R.id.nav_home:
                getSupportFragmentManager ().beginTransaction ()
                        .replace ( R.id.content_body, new HomeFragment () )
                        .commit ();
                break;
            case R.id.nav_culture:
                getSupportFragmentManager ().beginTransaction ()
                        .replace ( R.id.content_body, new CultureFragment () )
                        .commit ();
                break;
            case R.id.nav_education:
                getSupportFragmentManager ().beginTransaction ()
                        .replace ( R.id.content_body, new EducationFragment () )
                        .commit ();
                break;
            case R.id.nav_fashion:
                getSupportFragmentManager ().beginTransaction ()
                        .replace ( R.id.content_body, new FashionFragment () )
                        .commit ();
                break;
            case R.id.nav_life_style:
                getSupportFragmentManager ().beginTransaction ()
                        .replace ( R.id.content_body, new LifeStyleFragment () )
                        .commit ();
                break;
            case R.id.nav_politics:
                getSupportFragmentManager ().beginTransaction ()
                        .replace ( R.id.content_body, new PoliticsFragment () )
                        .commit ();
                break;
            case R.id.nav_sports:
                getSupportFragmentManager ().beginTransaction ()
                        .replace ( R.id.content_body, new SportsFragment () )
                        .commit ();
                break;
            case R.id.nav_technology:
                getSupportFragmentManager ().beginTransaction ()
                        .replace ( R.id.content_body, new TechnologyFragment () )
                        .commit ();
                break;
            case R.id.nav_settings:
                Intent settingActivityIntent = new Intent ( NewsActivity.this, SettingsActivity.class );
                startActivity ( settingActivityIntent );
                break;
        }

        // When user selects an item from navigation drawer, close it
        DrawerLayout drawer = findViewById ( R.id.drawer_layout );
        drawer.closeDrawer ( GravityCompat.START );
        return true;
    }

    @Override
    public void onBackPressed() {
        // When user presses the back button and navigation drawer is open, close the drawer first
        DrawerLayout drawer = findViewById ( R.id.drawer_layout );
        if (drawer.isDrawerOpen ( GravityCompat.START )) {
            drawer.closeDrawer ( GravityCompat.START );
        } else {
            super.onBackPressed ();
        }
    }

    // Change the title of action bar as per fragment
    public void setActionBarTitle(String title) {
        Objects.requireNonNull ( getSupportActionBar () ).setTitle ( title );
    }
}