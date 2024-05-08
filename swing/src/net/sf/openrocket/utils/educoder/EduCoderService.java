package net.sf.openrocket.utils.educoder;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface EduCoderService {
    /**
     * 计算头锥组件重心位置
     *
     * @param request request
     * @return result
     */
    @POST("NoseCone/calculateCG")
    Call<Result> calculateCG(@Body NoseConeCgRequest request);

    /**
     * 计算尾翼组件重心位置
     *
     * @param request request
     * @return result
     */
    @POST("FinSet/calculateCG")
    Call<Result> calculateCG(@Body FinSetCgRequest request);

    /**
     * 计算头锥组件压心位置
     *
     * @param request request
     * @return result
     */
    @POST("NoseCone/calculateCP")
    Call<Result> calculateCP(@Body NoseConeCpRequest request);
}
