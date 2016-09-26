package com.android.msahakyan.fma.fragment;

import android.graphics.Paint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.msahakyan.fma.R;
import com.android.msahakyan.fma.adapter.ItemListAdapter;
import com.android.msahakyan.fma.app.FmaApplication;
import com.android.msahakyan.fma.model.Artist;
import com.android.msahakyan.fma.model.Page;
import com.android.msahakyan.fma.network.NetworkRequestListener;
import com.android.msahakyan.fma.network.NetworkManager;
import com.android.msahakyan.fma.util.AppUtils;
import com.android.msahakyan.fma.util.Item;
import com.android.msahakyan.fma.view.FadeInNetworkImageView;

import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import timber.log.Timber;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ArtistDetailFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ArtistDetailFragment extends BaseItemDetailFragment<Artist> {

    private static final String KEY_ITEM_URL = "KEY_ITEM_URL";

    @Bind(R.id.list_view)
    RecyclerView mListView;
    @Bind(R.id.artist_image)
    FadeInNetworkImageView mArtistImageView;
    @Bind(R.id.artist_name)
    TextView mArtistName;
    @Bind(R.id.artist_bio)
    TextView mArtistBio;
    @Bind(R.id.artist_creation_date)
    TextView mCreationDate;
    @Bind(R.id.artist_followers)
    TextView mFollowers;
    @Bind(R.id.artist_comments)
    TextView mComments;
    @Bind(R.id.artist_location)
    TextView mLocation;
    @Bind(R.id.label_albums)
    TextView mAlbumsLabel;

    private NetworkRequestListener<Page<Item>> mNetworkRequestListener;
    private ItemListAdapter mAdapter;

    public ArtistDetailFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param artist The artist instance.
     * @return A new instance of fragment ArtistDetailFragment.
     */
    public static ArtistDetailFragment newInstance(Artist artist) {
        ArtistDetailFragment fragment = new ArtistDetailFragment();
        Bundle args = new Bundle();
        args.putParcelable(KEY_ITEM_PARCEL, artist);
        fragment.setArguments(args);

        return fragment;
    }

    public static ArtistDetailFragment newInstance(String artistUrl) {
        ArtistDetailFragment fragment = new ArtistDetailFragment();
        Bundle args = new Bundle();
        args.putString(KEY_ITEM_URL, artistUrl);
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
                    Timber.d("Received response for artist's albums: " + response.getItems().size());
                    showExtrasView(response);
                }
            }

            @Override
            public void onError(int statusCode, String errorMessage) {
                Timber.w(errorMessage);
                showErrorView();
            }
        };

        createAlbumsAdapter();
    }

    private void showExtrasView(Page<Item> response) {
        if (mAdapter != null) {
            mAdapter.clear();
            List<Item> albums = response.getItems();
            mAdapter.addAll(albums);
            updateAlbumCount(albums.size());
        }
    }

    private void updateAlbumCount(int size) {
        mAlbumsLabel.setText(getString(R.string.label_albums) + " (" + size + ")");
    }

    private void setLayoutManager() {
        GridLayoutManager layoutManager = new GridLayoutManager(mActivity, 2);
        mListView.setLayoutManager(layoutManager);
        mListView.setAdapter(mAdapter);
    }

    private void createAlbumsAdapter() {
        mAdapter = new ItemListAdapter(mActivity, new ArrayList<>());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_artist_detail, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        showContentView();
        setLayoutManager();
    }

    @Override
    protected void showBasicView() {
        mArtistImageView.setImageUrl(mItem.getImage(), FmaApplication.getInstance().getImageLoader());
        mArtistName.setText(mItem.getName());
        if (mItem.getBio() != null) {
            mArtistBio.setVisibility(View.VISIBLE);
            mArtistBio.setPaintFlags(mArtistBio.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
            mArtistBio.setText(getString(R.string.artist_bio));
            mArtistBio.setTextColor(ContextCompat.getColor(mActivity, R.color.colorAccent));
            mArtistBio.setOnClickListener(v ->
                AppUtils.showCustomDialog(mActivity, mItem.getBio()));
        }
        mCreationDate.setText(getString(R.string.artist_creation_date, AppUtils.getCreationDateOnly(mItem.getCreationDate())));
        mFollowers.setText(getString(R.string.artist_favourites, mItem.getFavouritesCount()));
        mComments.setText(getString(R.string.artist_comments, mItem.getCommentsCount()));
        if (mItem.getLocation() != null) {
            mLocation.setText(getString(R.string.artist_location, mItem.getLocation()));
        }
    }

    @Override
    public void refresh() {
        super.refresh();
        new NetworkManager().getAlbumsByArtistName(mNetworkRequestListener, mItem.getName());
    }
}
