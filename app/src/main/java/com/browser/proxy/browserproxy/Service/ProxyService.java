package com.browser.proxy.browserproxy.Service;

import com.browser.proxy.browserproxy.utils.ProxyResponse;
import retrofit2.Call;
import retrofit2.http.GET;

public interface ProxyService {

    @GET("proxy.json")
    Call<ProxyResponse> getListProxy();
}
