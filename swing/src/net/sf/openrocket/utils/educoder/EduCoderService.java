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

    /***
     * 计算头锥组件转动惯量
     * @param request request
     * @return result
     */
    @POST("NoseCone/calculateMOI")
    Call<Result> calculateMOI(@Body NoseConeMOIRequest request);

    /**
     * 计算头锥组件压心位置
     *
     * @param request request
     * @return result
     */
    @POST("NoseCone/calculateCP")
    Call<Result> calculateCP(@Body NoseConeCpRequest request);



    /**
     * 计算箭体组件重心位置
     *
     * @param request request
     * @return result
     */
    @POST("BodyTube/calculateCG")
    Call<Result> calculateCG(@Body BodyTubeCgRequest request);

    /**
     * 计算箭体组件压心位置
     *
     * @param request request
     * @return result
     */
    @POST("BodyTube/calculateCP")
    Call<Result> calculateCP(@Body BodyTubeCpRequest request);

    /**
     * 计算箭体组件转动惯量
     *
     * @param request request
     * @return result
     */
    @POST("BodyTube/calculateMOI")
    Call<Result> calculateMOI(@Body BodyTubeMOIRequest request);

    /**
     * 计算尾翼组件重心位置
     *
     * @param request request
     * @return result
     */
    @POST("FinSet/calculateCG")
    Call<Result> calculateCG(@Body FinSetCgRequest request);
    /**
     * 计算尾翼组件压心位置
     *
     * @param request request
     * @return result
     */
    @POST("FinSet/calculateCP")
    Call<Result> calculateCP(@Body FinSetCpRequest request);

    /**
     * 计算尾翼组件转动惯量
     *
     * @param request request
     * @return result
     */
    @POST("FinSet/calculateMOI")
    Call<Result> calculateMOI(@Body FinSetMOIRequest request);

    /**
     * 计算级间段组件压心位置
     *
     * @param request request
     * @return result
     */
    @POST("Transition/calculateCG")
    Call<Result> calculateCG(@Body TransitionCgRequest request);
    @POST("Transition/calculateCP")
    Call<Result> calculateCP(@Body TransitionCpRequest request);
    @POST("Transition/calculateMOI")
    Call<Result> calculateMOI(@Body TransitionMOIRequest request);



}
