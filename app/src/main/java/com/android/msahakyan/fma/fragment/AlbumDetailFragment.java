package com.android.msahakyan.fma.fragment;

import android.graphics.Paint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.msahakyan.fma.R;
import com.android.msahakyan.fma.adapter.ItemClickListener;
import com.android.msahakyan.fma.adapter.ItemListAdapter;
import com.android.msahakyan.fma.application.FmaApplication;
import com.android.msahakyan.fma.model.Album;
import com.android.msahakyan.fma.model.Artist;
import com.android.msahakyan.fma.model.Page;
import com.android.msahakyan.fma.network.FmaApiService;
import com.android.msahakyan.fma.network.NetworkRequestListener;
import com.android.msahakyan.fma.util.AppUtils;
import com.android.msahakyan.fma.util.Item;
import com.android.msahakyan.fma.view.FadeInNetworkImageView;
import com.android.volley.toolbox.ImageLoader;

import java.net.HttpURLConnection;
import java.util.ArrayList;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.OnClick;
import timber.log.Timber;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AlbumDetailFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AlbumDetailFragment extends BaseItemDetailFragment<Album> implements ItemClickListener<Item> {

    @Inject
    FmaApiService fmaApiService;
    @Inject
    ImageLoader imageLoader;

    @Bind(R.id.list_view)
    RecyclerView mListView;
    @Bind(R.id.album_image)
    FadeInNetworkImageView mAlbumImageView;
    @Bind(R.id.item_title)
    TextView mTitleView;
    @Bind(R.id.artist_name)
    TextView mArtistName;
    @Bind(R.id.artist_image)
    FadeInNetworkImageView mArtistImage;
    @Bind(R.id.artist_creation_date)
    TextView mArtistCreationDate;
    @Bind(R.id.artist_info_container)
    RelativeLayout mArtistInfoContainer;


    private NetworkRequestListener<Page<Item>> mNetworkRequestListener;
    private ItemListAdapter adapter;
    private int mPage;
    private Artist mArtist;

    public AlbumDetailFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param album Album instance.
     * @return A new instance of fragment AlbumDetailFragment.
     */
    public static AlbumDetailFragment newInstance(Album album) {
        AlbumDetailFragment fragment = new AlbumDetailFragment();
        Bundle args = new Bundle();
        args.putParcelable(KEY_ITEM_PARCEL, album);
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mNetworkRequestListener = new NetworkRequestListener<Page<Item>>() {
            @Override
            public void onSuccess(@Nullable Page<Item> response, int statusCode) {
                if (response != null && statusCode == HttpURLConnection.HTTP_OK) {
                    Timber.d("Received response for tracks: " + response.getItems().size());
                    showExtrasView(response);
                }
            }

            @Override
            public void onError(int statusCode, String errorMessage) {
                Timber.w(errorMessage);
                showErrorView();
            }
        };

        createTracksAdapter();
    }

    private void showExtrasView(Page<Item> response) {
        if (adapter != null) {
            adapter.clear();
            adapter.addAll(response.getItems());
        }
    }

    private void setLayoutManager() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(activity);
        mListView.setLayoutManager(layoutManager);
        mListView.setAdapter(adapter);
    }

    private void createTracksAdapter() {
        mPage = 1;
        adapter = new ItemListAdapter(activity, new ArrayList<>(), this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_album_detail, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        showContentView();
        setLayoutManager();
    }

    @Override
    public void onResume() {
        super.onResume();
        loadArtistInfo();
    }

    private void loadArtistInfo() {
        fmaApiService.getArtistByName(new NetworkRequestListener<Page<Item>>() {
            @Override
            public void onSuccess(@Nullable Page<Item> response, int statusCode) {
                if (statusCode == HttpURLConnection.HTTP_OK && response != null) {
                    mArtist = (Artist) response.getItems().get(0);
                    // Setting artist image when details info is loaded
                    if (mArtistImage != null) {
                        mArtistImage.setErrorImageResId(R.drawable.artist_icon);
                        mArtistImage.setImageUrl(mArtist.getImage(), imageLoader);
                    }
                    if (mArtistCreationDate != null) {
                        mArtistCreationDate.setText(AppUtils.getCreationDateOnly(mArtist.getCreationDate()));
                    }
                }
            }

            @Override
            public void onError(int statusCode, String errorMessage) {
                Timber.w("Error when loading artist info [statusCode: " + statusCode +
                    ", errorMessage: " + errorMessage);

            }
        }, item.getArtistName());
    }

    @Override
    protected void showBasicView() {
        mAlbumImageView.setImageUrl(item.getImageFile(), imageLoader);
        mTitleView.setText(item.getTitle());
        mArtistName.setText(item.getArtistName());
        mArtistImage.setImageResource(R.drawable.artist_icon);

        boolean isDetailsAvailable = !TextUtils.isEmpty(item.getInformation());

        if (isDetailsAvailable) {
            mTitleView.setPaintFlags(mTitleView.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
            mTitleView.setTextColor(ContextCompat.getColor(activity, R.color.colorAccent));
            mTitleView.setOnClickListener(v ->
                AppUtils.showCustomDialog(activity, item.getInformation()));
        }
    }

    @Override
    public void refresh() {
        super.refresh();
        fmaApiService.getTracksByAlbumId(mNetworkRequestListener, item.getId(), mPage);
    }

    @OnClick(R.id.artist_info_container)
    public void onArtistInfoClick(View v) {
        navigationManager.showArtistDetailFragment(mArtist);
    }

    @Override
    public void onActivityCreated(@android.support.annotation.Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        FmaApplication.get(activity).getApplicationComponent().inject(this);
        ActionBar actionBar = activity.getSupportActionBar();
        if (actionBar != null) {
            actionBar.show();
        }
    }

    @Override
    public void onItemClicked(Item item, RecyclerView.ViewHolder holder) {
        navigationManager.showTrackPlayFragment(adapter.getItems(), holder.getAdapterPosition());
    }
}
