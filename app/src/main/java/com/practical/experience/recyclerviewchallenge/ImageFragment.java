package com.practical.experience.recyclerviewchallenge;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.practical.experience.recyclerviewchallenge.common.BaseApplication;
import com.practical.experience.recyclerviewchallenge.common.IListenerCallback;
import com.practical.experience.recyclerviewchallenge.parser.HttpRequest;

import java.util.ArrayList;
import java.util.List;

public class ImageFragment extends Fragment {
    private static final String TAG = ImageFragment.class.getSimpleName();
    private static final String API_KEY = "f974a8374ea8aa4fb5f0f6b683dd772b";

    private RecyclerView mImageRecyclerView;
    private List<ImageItem> mItems = new ArrayList<>();

    private void setupAdapter() {
        if (isAdded()) {
            mImageRecyclerView.setAdapter(new ImageAdapter(mItems));
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            String url = Uri.parse("https://api.flickr.com/services/rest/")
                        .buildUpon()
                        .appendQueryParameter("method", "flickr.photos.getRecent")
                        .appendQueryParameter("api_key", API_KEY)
                        .appendQueryParameter("format", "json")
                        .appendQueryParameter("nojsoncallback", "1")
                        .appendQueryParameter("extras", "url_l")
                        .build().toString();
            HttpRequest request = new HttpRequest(url);
            IListenerCallback<ImageItem> callback = new IListenerCallback<ImageItem>() {
                @Override
                public void onComplete(List<ImageItem> responseContent) {
                    mItems = responseContent;
                    setupAdapter();
                }

                @Override
                public void onError(String errorData) {
                    Toast.makeText(getContext(), errorData, Toast.LENGTH_SHORT).show();
                }
            };

            request.execute(callback);
        } catch (Exception e) {
            Toast.makeText(getContext(), "Some error occurred.", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View currentView = inflater.inflate(R.layout.fragment_recycler_view, container, false);

        mImageRecyclerView = (RecyclerView) currentView.findViewById(R.id.image_recycler_view);
        mImageRecyclerView.setHasFixedSize(true);
        mImageRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));

        setupAdapter();

        return currentView;
    }

    @Override
    public void onResume() {
        super.onResume();

    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        BaseApplication.getInstance().getRequestQueue().cancelAll(this);
    }

    private class ImageHolder extends RecyclerView.ViewHolder {
        NetworkImageView mImageView;
        TextView mTextView;
        private ImageLoader mImageLoader = BaseApplication.getInstance().getImageLoader();

        public void bind(ImageItem item) {
            mImageLoader.get(item.getUrl(), ImageLoader.getImageListener(mImageView, R.drawable.background, android.R.drawable.ic_dialog_alert));
            mImageView.setImageUrl(item.getUrl(), mImageLoader);
            mTextView.setText(item.getCaption());
        }

        public ImageHolder(View itemView) {
            super(itemView);

            mImageView = (NetworkImageView) itemView.findViewById(R.id.item_image_view);
            mTextView = (TextView) itemView.findViewById(R.id.item_title);
        }
    }

    private class ImageAdapter extends RecyclerView.Adapter<ImageHolder> {
        private List<ImageItem> mItems;

        public ImageAdapter(List<ImageItem> items) {
            mItems = items;
        }

        @Override
        public ImageHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater li = LayoutInflater.from(getActivity());
            View v = li.inflate(R.layout.item_recycler_view, parent, false);

            return new ImageHolder(v);
        }

        @Override
        public void onBindViewHolder(ImageHolder holder, int position) {
            ImageItem item = mItems.get(position);

            if (null != item)
                holder.bind(item);
        }

        @Override
        public int getItemCount() {
            return mItems.size();
        }
    }
}
