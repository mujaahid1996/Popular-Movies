package portfolio.saurabh.popularmovies;

import android.content.Context;
import android.net.Uri;


public class UriBuilder {
    public static final String BASE_URL = "https://api.themoviedb.org/3/discover/movie";
    static final String sortParam = "sort_by";
    static final String keyParam = "api_key";
    static final String HIGHEST_RATED="vote_average.desc";
    static final String MOST_POPULAR="popularity.desc";
    Uri.Builder builder;

    public UriBuilder(Context context) {
        String key = context.getString(R.string.api_key);
        builder = Uri.parse(BASE_URL).buildUpon();
        builder.appendQueryParameter(keyParam, key);
    }

    public void setSortValue(String value) {
        builder.appendQueryParameter(sortParam, value);
    }

    @Override
    public String toString() {
        return builder.toString();
    }
}