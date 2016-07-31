package com.practical.experience.recyclerviewchallenge.parser;

import android.util.Log;

import com.practical.experience.recyclerviewchallenge.ImageItem;
import com.practical.experience.recyclerviewchallenge.common.IParser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class JsonParser implements IParser<ImageItem> {
    private static final String TAG = JsonParser.class.getSimpleName();
    @Override
    public List<ImageItem> parse(String text) {
        List<ImageItem> list = new ArrayList<>();
        try {
            JSONObject jsonBody = new JSONObject(text);
            parseItems(list, jsonBody);

            Log.d(TAG, list.toString());
        }
        catch (JSONException je) {
            Log.e(TAG, "Failed to parse JSON", je);
        }
        catch (IOException ioe) {
            Log.e(TAG, "Failed to fetch items", ioe);
        }
        catch (Exception e) {
            Log.e(TAG, "Failed to fetch", e);
        }

        return list;
    }

    private void parseItems(List<ImageItem> items, JSONObject jsonBody)
            throws IOException, JSONException {
        JSONObject photosJsonObject = jsonBody.getJSONObject("photos");
        JSONArray photoJsonArray = photosJsonObject.getJSONArray("photo");

        for (int i = 0; i < photoJsonArray.length(); ++i) {
            JSONObject photoJsonObject = photoJsonArray.getJSONObject(i);

            if (!photoJsonObject.has("url_l"))
                continue;

            ImageItem item = new ImageItem();

            item.setId(photoJsonObject.getString("id"));
            item.setCaption(photoJsonObject.getString("title"));
            item.setUrl(photoJsonObject.getString("url_l"));

            items.add(item);
        }
    }
}
