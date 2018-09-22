package com.example.mnnit.mnnitdean;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.widget.ImageView;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class imageLoadTask extends AsyncTask<Void,Void, Bitmap> {
    private String url;
    private ImageView imageView;
    public imageLoadTask(String url,ImageView imageView){
        this.url=url;
        this.imageView=imageView;
    }

    @Override
    protected Bitmap doInBackground(Void... params) {
        try {
            URL urlconnection=new URL(url);
            HttpURLConnection connection=(HttpURLConnection)urlconnection
                    .openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream inputStream=connection.getInputStream();
            Bitmap myBitmap= BitmapFactory.decodeStream(inputStream);
            return myBitmap;
        }catch (Exception e)
        {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(Bitmap result) {
        super.onPostExecute(result);
        imageView.setImageBitmap(result);
    }
}
