package com.kosratdahmad.downloadfiles;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Streaming;

/**
 * Created by kosrat on 12/9/16.
 */

public interface RetrofitInterface {

    @GET("{url}")
    @Streaming
    Call<ResponseBody> downloadFile(@Path("url") String user);
}
