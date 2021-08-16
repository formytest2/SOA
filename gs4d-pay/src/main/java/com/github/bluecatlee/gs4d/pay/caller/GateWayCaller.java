package com.github.bluecatlee.gs4d.pay.caller;

import com.alibaba.fastjson.JSONObject;
import com.github.bluecatlee.gs4d.common.utils.JsonUtil;
import com.github.bluecatlee.gs4d.pay.bean.BaseResponse;
import com.github.bluecatlee.gs4d.pay.bean.BusinessCallbackResponse;
import com.github.bluecatlee.gs4d.pay.bean.SendVo;
import com.github.bluecatlee.gs4d.pay.constant.RespEnum;
import com.github.bluecatlee.gs4d.pay.entity.PayOrderInfo;
import lombok.extern.slf4j.Slf4j;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.logging.HttpLoggingInterceptor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import retrofit2.Retrofit;
import retrofit2.converter.scalars.ScalarsConverterFactory;

import javax.annotation.PostConstruct;
import java.util.concurrent.TimeUnit;

/**
 * 调用业务回调方法demo (走http网关)
 */
//@Service
@Slf4j
@Deprecated
public class GateWayCaller implements CallbackCaller {

    GatewayApi gatewayApi;

    @Value("${gateway.appKey:}")
    String appKey;

    @Value("${gateway.host:}")
    String host;

    @PostConstruct
    public void buildRetrofitCaller() {
        Retrofit.Builder builder = new Retrofit.Builder()
                .addConverterFactory(ScalarsConverterFactory.create())
                .baseUrl(host + "/apiTools/");
        if (log.isDebugEnabled()) {
            HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
            interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            OkHttpClient client = new OkHttpClient.Builder().addInterceptor(interceptor).build();
            builder.client(client);
        }
        Retrofit build = builder.build();
        gatewayApi = build.create(GatewayApi.class);
    }

    @Override
    public BaseResponse call(PayOrderInfo orderInfo, String businessCallback) throws Exception {
    	BaseResponse respones = null;
    	String channel =  orderInfo.getChannel();
        respones = businessCallback(orderInfo, businessCallback);

    	return respones;
    }

//    private BaseResponse presalCall(PayOrderInfo orderInfo, String businessCallback) throws Exception {
//        SendVo sendVo = new SendVo();
//        sendVo.setTenantNumId(orderInfo.getTenantNumId());
//        sendVo.setDataSign(0L + orderInfo.getDataSign());
//        sendVo.setTmlNumId(orderInfo.getOutTradeNo());
//        sendVo.setTransactionId(orderInfo.getTransactionId());
//        sendVo.setTotalFee(orderInfo.getTotalFee());
//        SerializeConfig serializeConfig = new SerializeConfig();
//        serializeConfig.setPropertyNamingStrategy(PropertyNamingStrategy.SnakeCase);
//        String str = JSON.toJSONString(sendVo, serializeConfig);
//        String encode = URLEncoder.encode(str, "UTF-8");
//        log.debug("origin param :{}", str);
//        log.debug("encoded param :{}", encode);
//        Response<String> execute = gatewayApi.callMethod(businessCallback, appKey, encode, "N").execute();
//        log.info("result {}", execute.isSuccessful());
//        BaseResponse response = new BaseResponse();
//        if (execute.isSuccessful()) {
//            log.info(execute.body());
//            JSONObject object = JSON.parseObject(execute.body());
//            if (object.getString("code") == "0") {
//                response.setCode(RespEnum.SUCCESS.getStatus());
//            }
//            response.setResBody(execute.body());
//        } else {
//            log.info("error: code:{}, body:{}", execute.code(), execute.errorBody().string());
//        }
//        return response;
//    }
    
    private BaseResponse businessCallback(PayOrderInfo orderInfo, String businessCallback) throws Exception {
    	//TODO 业务回调
              
        SendVo sendVo = new SendVo();
        sendVo.setTenantNumId(orderInfo.getTenantNumId());
        sendVo.setDataSign(0L + orderInfo.getDataSign());
        sendVo.setOutTradeNo(orderInfo.getOutTradeNo());
        sendVo.setTransactionId(orderInfo.getTransactionId());
        sendVo.setTotalFee(orderInfo.getTotalFee());
        sendVo.setTradeType(String.valueOf(orderInfo.getTradeType()));
        sendVo.setCode(RespEnum.SUCCESS.getStatus());
       
        String reqJson = JsonUtil.toJson(sendVo);
        
        return httpPost(reqJson, businessCallback);
    }
        
    private BaseResponse httpPost(String requestBody, String url) {
		okhttp3.Response okhttp3Response = null;
        BaseResponse response = new BaseResponse();

		try {
			HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
			interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

			OkHttpClient client = new OkHttpClient().newBuilder().connectTimeout(40, TimeUnit.SECONDS)
					.readTimeout(60, TimeUnit.SECONDS).writeTimeout(60, TimeUnit.SECONDS).addInterceptor(interceptor)
					.build();
			
			MediaType jsonType = MediaType.parse("application/json; charset=utf-8");

			okhttp3.RequestBody body = okhttp3.RequestBody.create(jsonType, requestBody);
			Request request = new Request.Builder().url(url).post(body)
					.build();

			okhttp3Response = client.newCall(request).execute();
			String responseContent = okhttp3Response.body().string();

			log.info(" httpPost  url = {}  requestbody = {}   responsebody = {} ", url, requestBody, responseContent);
			
			if (okhttp3Response.isSuccessful()) {
	        	BusinessCallbackResponse businessResponse =  JSONObject.parseObject(responseContent,BusinessCallbackResponse.class);
	        	if("0".equals(businessResponse.getCode()) || "00".equals(businessResponse.getCode())) {
	                response.setCode(RespEnum.SUCCESS.getStatus());
	                response.setMessage(RespEnum.SUCCESS.getMsg());   
	        	} else {
	                response.setCode(RespEnum.ERROR_992.getStatus()); 
	                response.setMessage(RespEnum.ERROR_992.getMsg());
	        	}
			} else {
                response.setCode(RespEnum.ERROR_992.getStatus()); 
                response.setMessage(RespEnum.ERROR_992.getMsg());
			}
			


		} catch (Exception e) {
			log.error("business call back url = {} ,request = {} ",url, requestBody, e);
            response.setCode(RespEnum.ERROR_992.getStatus()); 
            response.setMessage(RespEnum.ERROR_992.getMsg());
		} finally {
			if (okhttp3Response != null) {
				okhttp3Response.close();
			}
		}
		
		return response;
    }
}
