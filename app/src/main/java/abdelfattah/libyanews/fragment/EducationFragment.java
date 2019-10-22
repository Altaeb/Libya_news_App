package abdelfattah.libyanews.fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.Loader;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import abdelfattah.libyanews.api.Constants;
import abdelfattah.libyanews.helper.FragmentHelper;
import abdelfattah.libyanews.NewsActivity;
import abdelfattah.libyanews.NewsAdapter;
import abdelfattah.libyanews.NewsLoaderFragment;
import abdelfattah.libyanews.object.News;
import abdelfattah.libyanews.object.UserPreference;
import abdelfattah.libyanews.R;

public class EducationFragment extends Fragment
        implements LoaderManager.LoaderCallbacks<List<News>> {

    private static final int NEWS_LOADER_ID = 2;

    private NewsAdapter mNewsAdapter;
    private ProgressBar mProgressBar;
    private TextView mEmptyView;
    private String mCorrectUserQueryApi;
    private SwipeRefreshLayout mSwipeRefreshLayout;

    // Header for list
    private View mListViewHeader;

    public EducationFragment() {
        // Required empty public constructor
    }

    // When user changed the preference from settings and come back to fragment
    // load the news as per news updated user preference
    @Override
    public void onStart() {
        super.onStart ();
        getLatestNews ();
        updateListHeader ( getResources ().getString ( R.string.list_header_title ), false );
        checkNetworkConnectionAndRestartLoader ();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View rootView = inflater.inflate ( R.layout.list, container, false );

        // Set actionbar title
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            ((NewsActivity) Objects.requireNonNull ( getActivity () )).setActionBarTitle ( getString ( R.string.nav_education_title ) );
        }
        // display addition options menu in action bar
        setHasOptionsMenu ( true );
        // Find the progress bar with id progress_bar in list.xml
        mProgressBar = rootView.findViewById ( R.id.progress_bar );
        mProgressBar.setVisibility ( View.GONE );

        // Find the TextView with id empty_view_text_box in list.xml
        mEmptyView = rootView.findViewById ( R.id.empty_view_text_box );
        mEmptyView.setVisibility ( View.GONE );

        // Find the swipe to refresh layout in list.xml
        mSwipeRefreshLayout = rootView.findViewById ( R.id.swipe_to_refresh );
        // Set the color scheme of refresh icon
        mSwipeRefreshLayout.setColorSchemeColors ( getResources ().getColor ( R.color.orange )
                , getResources ().getColor ( R.color.green )
                , getResources ().getColor ( R.color.blue ) );
        mSwipeRefreshLayout.setOnRefreshListener ( new SwipeRefreshLayout.OnRefreshListener () {
            @Override
            public void onRefresh() {
                refreshContent ();
            }
        } );

        // Find the layout for list_header
        mListViewHeader = View.inflate ( getContext (), R.layout.list_header, null );
        updateListHeader ( getResources ().getString ( R.string.list_header_title ), false );

        // Find the ListView with id list in list.xml
        ListView listView = rootView.findViewById ( R.id.list );
        // Attach a header to listView and make it not selectable
        listView.addHeaderView ( mListViewHeader, getResources ().getString ( R.string.list_header_title ), false );
        // Set empty view on ListView in order to display "no data" and "check network connection"
        listView.setEmptyView ( mEmptyView );

        // Create a empty custom adapter and set it on ListView
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            mNewsAdapter = new NewsAdapter ( Objects.requireNonNull ( getContext () ), new ArrayList<News> (), true );
        }
        listView.setAdapter ( mNewsAdapter );

        // Attach a listener on list item to open a link for the news item in web browser
        listView.setOnItemClickListener ( new AdapterView.OnItemClickListener () {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int currentPosition, long l) {
                Intent webIntent = new Intent ( Intent.ACTION_VIEW );
                News currentNewsObject = (News) adapterView.getItemAtPosition ( currentPosition );
                webIntent.setData ( Uri.parse ( currentNewsObject.getWebUrl () ) );
                startActivity ( webIntent );
            }
        } );

        // get the latest news
        getLatestNews ();
        // Check a network connection and initialize a loader
        // We have to initialize a loader in NewsActivity in order to load data when activity
        // restarts, meaning when device orientation changes
        checkNetworkConnectionAndInitLoader ();

        return rootView;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId ();
        if (id == R.id.home_fragment) {
            updateListHeader ( getResources ().getString ( R.string.list_header_title ), false );
            // When user selects the home icon, make the API URL to show some of the latest news
            // for education section
            getLatestNews ();
            // clear out the news and restart the loader
            mNewsAdapter.clear ();
            checkNetworkConnectionAndRestartLoader ();
        }
        return super.onOptionsItemSelected ( item );
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu ( menu, inflater );
        menu.clear ();
        inflater.inflate ( R.menu.fragment_search_menu, menu );
        final MenuItem item = menu.findItem ( R.id.search_fragment );
        SearchView searchView = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            searchView = new SearchView ( Objects.requireNonNull ( ((NewsActivity) Objects.requireNonNull ( getContext () )).getSupportActionBar () ).getThemedContext () );
        }
        item.setShowAsAction ( MenuItem.SHOW_AS_ACTION_ALWAYS );
        item.setActionView ( searchView );
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Objects.requireNonNull ( searchView ).setOnQueryTextListener ( new SearchView.OnQueryTextListener () {
                @Override
                public boolean onQueryTextSubmit(String userQuery) {
                    updateListHeader ( userQuery, true );
                    // Get the user preference
                    UserPreference userPreference = FragmentHelper.getUserPreference ( getContext () );
                    // Generate the url string as per user preference and user submitted query
                    mCorrectUserQueryApi = FragmentHelper.getSectionSearchQueryNews (
                            Constants.SECTION_EDUCATION, userQuery, userPreference );

                    // collapse search view when user submits the query
                    SearchView searchView = (SearchView) item.getActionView ();
                    searchView.onActionViewCollapsed ();

                    // Check the network connection and restart the loader
                    mNewsAdapter.clear ();
                    checkNetworkConnectionAndRestartLoader ();
                    return false;
                }

                @Override
                public boolean onQueryTextChange(String userQuery) {
                    return false;
                }
            } );
        }
    }


    @NonNull
    @Override
    public Loader<List<News>> onCreateLoader(int id, Bundle args) {
        return new NewsLoaderFragment ( getContext (), mCorrectUserQueryApi );
    }

    @Override
    public void onLoadFinished(@NonNull Loader<List<News>> loader, List<News> data) {
        mNewsAdapter.clear ();
        mProgressBar.setVisibility ( View.GONE );
        mSwipeRefreshLayout.setRefreshing ( false );
        mEmptyView.setVisibility ( View.VISIBLE );
        mEmptyView.setText ( getResources ().getString ( R.string.news_activity_no_content ) );

        if (data != null && !data.isEmpty ()) {
            mNewsAdapter.addAll ( data );
        }
    }

    @Override
    public void onLoaderReset(@NonNull Loader<List<News>> loader) {
        mNewsAdapter.clear ();
    }

    /**
     * This method is called to check network connection and initialize the loader
     */
    @SuppressWarnings("deprecation")
    private void checkNetworkConnectionAndInitLoader() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            if (FragmentHelper.isNetworkAvailable ( Objects.requireNonNull ( getContext () ) )) {
                // If the device is connected to network, make progress bar visible and
                // hide empty text view and initialize the loader
                mProgressBar.setVisibility ( View.VISIBLE );
                mEmptyView.setVisibility ( View.GONE );
                getLoaderManager ().initLoader ( NEWS_LOADER_ID, null, this );
            } else {
                // If there is no network, make progress bar invisible and
                // set emptyView TextView text to "Check network connection!"
                mProgressBar.setVisibility ( View.GONE );
                mEmptyView.setText ( getResources ().getString ( R.string.news_activity_check_network_connection ) );
            }
        }
    }

    /**
     * This method is called to check network connection and restart the existing loader
     * to load new batch of data
     */
    @SuppressWarnings("deprecation")
    private void checkNetworkConnectionAndRestartLoader() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            if (FragmentHelper.isNetworkAvailable ( Objects.requireNonNull ( getContext () ) )) {
                // If the device is connected to network, make progress bar visible and
                // hide empty text view and restart the loader
                mProgressBar.setVisibility ( View.VISIBLE );
                mEmptyView.setVisibility ( View.GONE );
                getLoaderManager ().restartLoader ( NEWS_LOADER_ID, null, this );
            } else {
                // If there is no network, make progress bar invisible and
                // set emptyView TextView text to "Check network connection!"
                mProgressBar.setVisibility ( View.GONE );
                mEmptyView.setText ( getResources ().getString ( R.string.news_activity_check_network_connection ) );
            }
        }
    }

    /**
     * This method is called to refresh the content of loader
     */
    private void refreshContent() {
        // Check network connection and restart the loader to refresh news headlines
        checkNetworkConnectionAndRestartLoader ();
        // Set visibility of ProgressBar to GONE when refreshing the content
        mProgressBar.setVisibility ( View.GONE );
    }

    /**
     * This method is used to get the latest news
     */
    private void getLatestNews() {
        // Get the user preferences
        UserPreference userPreference = FragmentHelper.getUserPreference ( getContext () );
        // Make the API URL to show some of the latest news
        // for education section
        mCorrectUserQueryApi = FragmentHelper.getSectionTopHeadlines (
                Constants.SECTION_EDUCATION, userPreference );
    }

    /**
     * This method is used to update text of list header
     *
     * @param query         query submitted by user
     * @param isSearchQuery true or false depending upon search query
     *                      if it's top headline value should be false
     *                      if user performs a search by providing query, it's value should be true
     */
    private void updateListHeader(String query, boolean isSearchQuery) {
        TextView headerText = mListViewHeader.findViewById ( R.id.list_header );
        TextView searchText = mListViewHeader.findViewById ( R.id.list_header_section );
        if (isSearchQuery) {
            headerText.setVisibility ( View.GONE );
            searchText.setText ( query );
        } else {
            headerText.setVisibility ( View.VISIBLE );
            headerText.setText ( getResources ().getString ( R.string.list_header_title ) );
            searchText.setText ( getResources ().getString ( R.string.nav_education_title ) );
        }
    }
}