package br.com.alisonfrancisco.moviestowatch.api;

import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class TBMD {

    private OkHttpClient client = null;
    private String apiKey = null;
    private String url = null;

    public TBMD(String pApiKey) {
        client = new OkHttpClient();
        apiKey = pApiKey;
    }

    public Call search(String pQuery){
        Response response = null;

        //montagem Search URL
        url = "https://api.themoviedb.org/3/search/movie?page=1&query=" + pQuery +"&api_key="+apiKey;

        Request request = new Request.Builder().url(url).get().build();

        OkHttpClient client = new OkHttpClient();

        return client.newCall(request);
  }
}
