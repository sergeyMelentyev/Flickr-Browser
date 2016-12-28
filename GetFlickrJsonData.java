package com.example.atmen.flickrbrowser;

import android.net.Uri;
import android.os.AsyncTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

class GetFlickrJsonData extends AsyncTask<String, Void, List<Photo>> implements GetRawData.OnDownLoadComplete {
    private List<Photo> mPhotoList = null;
    private String mBaseUrl;
    private String mLanguage;
    private boolean mMatchAll;
    private final OnDataAvailable mCallBack;
    private boolean runningOnSameThread = false;

    interface OnDataAvailable {
        void onDataAvailable(List<Photo> data, DownLoadStatus status);
    }

    GetFlickrJsonData(OnDataAvailable callBack, String baseUrl, String language, boolean matchAll) {
        mCallBack = callBack;
        mBaseUrl = baseUrl;
        mLanguage = language;
        mMatchAll = matchAll;
    }

    void executeOnSameThread(String searchCriteria) {
        runningOnSameThread = true;
        String destinationUri = criteriaUri(searchCriteria, mLanguage, mMatchAll);
        GetRawData getRawData = new GetRawData(this);
        getRawData.execute(destinationUri);
    }

    @Override
    protected List<Photo> doInBackground(String... params) {
        String destinationUri = criteriaUri(params[0], mLanguage, mMatchAll);
        GetRawData getRawData = new GetRawData(this);
        getRawData.runInSameThread(destinationUri);
        return mPhotoList;
    }

    @Override
    protected void onPostExecute(List<Photo> photos) {
        if (mCallBack != null) {
            mCallBack.onDataAvailable(mPhotoList, DownLoadStatus.OK);
        }
    }

    private String criteriaUri (String searchCriteria, String lang, boolean matchAll) {
        return Uri.parse(mBaseUrl).buildUpon()
                .appendQueryParameter("tags", searchCriteria)
                .appendQueryParameter("tagmode", matchAll ? "ALL" : "ANY")
                .appendQueryParameter("lang", lang)
                .appendQueryParameter("format", "json")
                .appendQueryParameter("nojsoncallback", "1")
                .build().toString();
    }

    @Override
    public void onDownLoadComplete(String data, DownLoadStatus status) {
        if (status == DownLoadStatus.OK) {
            mPhotoList = new ArrayList<>();
            try {
                JSONObject jsonData = new JSONObject(data);
                JSONArray itemsArray = jsonData.getJSONArray("items");

                for (int i = 0; i < itemsArray.length(); i++) {
                    JSONObject jsonPhoto = itemsArray.getJSONObject(i);
                    String title = jsonPhoto.getString("title");
                    String author = jsonPhoto.getString("author");
                    String authorId = jsonPhoto.getString("author_id");
                    String tags = jsonPhoto.getString("tags");

                    JSONObject jsonMedia = jsonPhoto.getJSONObject("media");
                    String photoUrl = jsonMedia.getString("m");
                    String link = photoUrl.replaceFirst("_m.", "_b.");

                    Photo photoObject = new Photo(title, author, authorId, link, tags, photoUrl);
                    mPhotoList.add(photoObject);
                }
            } catch (JSONException jsone) {
                jsone.printStackTrace();
                status = DownLoadStatus.FAILED_OR_EMPTY;
            }
        }
        if (runningOnSameThread && mCallBack != null) {
            mCallBack.onDataAvailable(mPhotoList, status);
        }
    }
}
