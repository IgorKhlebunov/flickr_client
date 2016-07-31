package com.practical.experience.recyclerviewchallenge.parser;

import android.os.AsyncTask;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.practical.experience.recyclerviewchallenge.ImageItem;
import com.practical.experience.recyclerviewchallenge.common.BaseApplication;
import com.practical.experience.recyclerviewchallenge.common.IListenerCallback;
import com.practical.experience.recyclerviewchallenge.common.IParser;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.List;

public class HttpRequest {
    private String mUrl;
    private IParser<ImageItem> mParser = new JsonParser();
    private IListenerCallback<ImageItem> mCallback;

    public HttpRequest(String url) {
        mUrl = url;
    }

    public void execute(IListenerCallback<ImageItem> callback) {
        mCallback = callback;
        BaseApplication.getInstance().addToRequestQueue(createRequest());
    }

    protected Request<String> createRequest() {
        return new StringRequest(
                Request.Method.GET,
                mUrl,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        HTMLParseAsyncTask task = new HTMLParseAsyncTask();
                        task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        StringWriter errors = new StringWriter();
                        error.printStackTrace(new PrintWriter(errors));
                        mCallback.onError(error.toString());
                    }
                });
    }

    public class HTMLParseAsyncTask extends AsyncTask<String, Void, Void> {

        private List<ImageItem> mItems;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(String... params) {
            if (null != mParser)
                mItems = mParser.parse(params[0]);

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            try {
                mCallback.onComplete(mItems);
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


}
