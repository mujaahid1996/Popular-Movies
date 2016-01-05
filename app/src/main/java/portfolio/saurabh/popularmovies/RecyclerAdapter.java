package portfolio.saurabh.popularmovies;

import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.design.widget.AppBarLayout;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.squareup.picasso.Picasso;

import java.util.List;


public class RecyclerAdapter extends RecyclerView.Adapter<PosterViewHolder> {
    public static final String TAG = RecyclerAdapter.class.getName();
    public static final String POSTER_BASE_URL = "https://image.tmdb.org/t/p/w185";
    public static final String BACKDROP_BASE_URL = "http://image.tmdb.org/t/p/w500";
    LayoutInflater inflater;
    Context context;
    List<MovieData> movieDataList;

    public RecyclerAdapter(Context context, List<MovieData> movieDataList) {
        this.context = context;
        inflater = LayoutInflater.from(context);
        this.movieDataList = movieDataList;
    }

    @Override
    public PosterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new PosterViewHolder(inflater.inflate(R.layout.recycler_item, parent, false));
    }

    @Override
    public void onBindViewHolder(final PosterViewHolder holder, int position) {
        final MovieData movie = movieDataList.get(position);
        String poster_url = movie.posterurl;
        if (!(poster_url.isEmpty() || poster_url.equals("null"))) {
            Picasso.with(context).load(POSTER_BASE_URL + poster_url).error(R.drawable.placeholder).into(holder.poster);
        }
        if (!MainActivity.mIsDualPane) {
            holder.root.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, MovieDetail.class);
                    intent.putExtra(MovieDetail.KEY_MOVIE, movie);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        AppBarLayout barLayout = (AppBarLayout) ((AppCompatActivity) context).findViewById(R.id.actionbar);
                        ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation((AppCompatActivity) context, Pair.create((View) holder.poster, "poster"), Pair.create((View) barLayout, "actionbar"));
                        context.startActivity(intent, options.toBundle());
                    } else
                        context.startActivity(intent);
                }
            });
        } else {
            holder.root.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ((FragmentActivity) context).getSupportFragmentManager().beginTransaction()
                            .replace(R.id.movie_detail, DetailsFragment.getInstance(movie))
                            .commit();
                }
            });
        }

    }

    @Override
    public int getItemCount() {
        return movieDataList == null ? 0 : movieDataList.size();
    }

}
