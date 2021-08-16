package com.github.bluecatlee.gs4d.pay.caller;

import retrofit2.Call;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface GatewayApi {

    @POST("execute")
    Call<String> callMethod(@Query(value = "method") String methodName,
                            @Query("app_key") String appKey,
                            @Query(value = "params", encoded = true) String params,
                            @Query("convert_flag") String convertFlag
    );

}
