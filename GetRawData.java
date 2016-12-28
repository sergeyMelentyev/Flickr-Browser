package com.example.atmen.flickrbrowser;

import android.os.AsyncTask;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

enum DownLoadStatus { IDLE, PROCESSING, NOT_INITIALIZED, FAILED_OR_EMPTY, OK }

class GetRawData extends AsyncTask<String, Void, String> {
    DownLoadStatus mDownLoadStatus;
    private final OnDownLoadComplete mCallBack;

    interface OnDownLoadComplete {
        void onDownLoadComplete(String data, DownLoadStatus status);
    }

    GetRawData(OnDownLoadComplete callBack) {
        this.mDownLoadStatus = DownLoadStatus.IDLE;
        this.mCallBack = callBack;
    }

    void runInSameThread(String s) {
        if (mCallBack != null)
            mCallBack.onDownLoadComplete(doInBackground(s), mDownLoadStatus);
    }

    @Override
    protected void onPostExecute(String s) {
        if (mCallBack != null) {
            mCallBack.onDownLoadComplete(s, mDownLoadStatus);
        }
    }

    @Override
    protected String doInBackground(String... strings) {
        HttpURLConnection connection = null;
        BufferedReader reader = null;
        if (strings == null) {
            mDownLoadStatus = DownLoadStatus.NOT_INITIALIZED;
            return null;
        }
        try {
            mDownLoadStatus = DownLoadStatus.PROCESSING;
            URL url = new URL(strings[0]);

            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.connect();

            int response = connection.getResponseCode();
            StringBuilder result = new StringBuilder();
            reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));

            for (String line = reader.readLine(); line != null; line = reader.readLine()){
                result.append(line).append("\n");
            }

            mDownLoadStatus = DownLoadStatus.OK;
            return result.toString();
        } catch (MalformedURLException e) {
            // error logic here
        } catch (IOException e) {
            // error logic here
        } catch (SecurityException e) {
            // error logic here
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    // error logic here
                }
            }
        }
        mDownLoadStatus = DownLoadStatus.FAILED_OR_EMPTY;
        return null;
    }
}
