package com.huawei.fusionchargeapp.model.apis;

import com.corelibs.api.RequestBodyCreator;
import com.huawei.fusionchargeapp.constants.Urls;
import com.huawei.fusionchargeapp.model.beans.BaseData;

import okhttp3.RequestBody;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import rx.Observable;

/**
 * Created by zhangwei on 2018/5/4.
 */

public interface UploadImagesApi {

/*
    @POST(Urls.MODIFY_USER_INFO)
    @FormUrlEncoded
    Observable<BaseData> uploadUserPhoto(@Field("img") RequestBody body);*/

    /**
     *  完整的请求：请求方式（GET/POST）、请求头（语言、编码、连接方式等）、请求体（和请求体之间空一行） 请求的数据
     *
     * 1、multipart/form-data的基础方法是post，也就是说是由post方法来组合实现的
       2、multipart/form-data与post方法的不同之处：请求头，请求体。
       3、multipart/form-data的请求头必须包含一个特殊的头信息：Content-Type，
       且其值也必须规定为multipart/form-data，同时还需要规定一个内容分割符用于分割请求体中的多个post的内容，
        如文件内容和文本内容自然需要分割开来，不然接收方就无法正常解析和还原这个文件了。

    请求格式类型:
     @FormUrlEncoded	表示请求发送编码表单数据，每个键值对需要使用@Field注解
     @Multipart	表示请求发送multipart数据，需要配合使用@Part  传递文件用
     @Streaming	表示响应用字节流的形式返回.如果没使用该注解,默认会把数据全部载入到内存中.该注解在在下载大文件的特别有用

     请求体类型：
     @Body	多用于post请求发送非表单数据,比如想要以post方式传递json格式数据
     @Filed	多用于post请求中表单字段,Filed和FieldMap需要FormUrlEncoded结合使用
     @FiledMap	和@Filed作用一致，用于不确定表单参数
     @Part	用于表单字段,Part和PartMap与Multipart注解结合使用,适合文件上传的情况
     @PartMap	用于表单字段,默认接受的类型是Map
     */
    @POST(Urls.MODIFY_IMG)
    @Multipart  //表示请求发送multipart数据，需要配合使用@Part
    Observable<BaseData> upload(@Part("type") RequestBody type, @Part("file"+ RequestBodyCreator.MULTIPART_HACK) RequestBody file);
}
