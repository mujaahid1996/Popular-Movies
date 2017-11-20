package portfolio.saurabh.popularmovies.ui.main.favorite;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import javax.inject.Inject;

import portfolio.saurabh.popularmovies.MovieApplication;
import portfolio.saurabh.popularmovies.R;
import portfolio.saurabh.popularmovies.data.Movie;
import portfolio.saurabh.popularmovies.database.MyDatabaseHelper;
import portfolio.saurabh.popularmovies.di.component.ApplicationComponent;
import portfolio.saurabh.popularmovies.di.component.DaggerUiComponent;
import portfolio.saurabh.popularmovies.di.component.UiComponent;
import portfolio.saurabh.popularmovies.di.module.UiModule;
import portfolio.saurabh.popularmovies.ui.detail.DetailsFragment;
import portfolio.saurabh.popularmovies.ui.main.MainActivity;


public class FavoritesFragment extends Fragment {
    RecyclerView recyclerView;
    @Inject
    MyDatabaseHelper myDatabaseHelper;

    @Inject
    ListAdapter mAdapter;

    private LiveData<List<Movie>> movieLiveData;

    ApplicationComponent getAppComponent() {
        return ((MovieApplication) getActivity().getApplicationContext()).getComponent();
    }

    UiComponent getComponent() {
        return DaggerUiComponent.builder()
                .applicationComponent(getAppComponent())
                .uiModule(new UiModule(getContext()))
                .build();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.favorites_list, container, false);
        getComponent().inject(this);
        recyclerView = layout.findViewById(R.id.recycler_view);
        DisplayMetrics metrics = getResources().getDisplayMetrics();
        int width = (int) (metrics.widthPixels / metrics.density);
        //For Tabs
        boolean isTablet = getResources().getBoolean(R.bool.isTablet);
        boolean isLandscape = getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE;
        width = isTablet && isLandscape ? (width / 2) : width;
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), width / 140));
        recyclerView.setAdapter(mAdapter);
        movieLiveData = myDatabaseHelper.movieModel().getAllFavoriteMovies();
        movieLiveData.observe(this, new Observer<List<Movie>>() {
            @Override
            public void onChanged(@Nullable List<Movie> movies) {
                mAdapter.setData(movies);

                if (MainActivity.mIsDualPane) {

                    if (getActivity() != null && movies != null && !movies.isEmpty()) {
                        final Movie movie = movies.get(0);
                        getActivity()
                                .getSupportFragmentManager()
                                .beginTransaction()
                                .replace(R.id.movie_detail, DetailsFragment.getInstance(movie))
                                .commit();
                    }
                }
            }
        });

        return layout;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        movieLiveData.removeObservers(this);
    }
}