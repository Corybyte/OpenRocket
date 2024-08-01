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



    /**
     * 计算管状翼组件重心位置
     *
     * @param request request
     * @return result
     */
    @POST("TubeFinSet/calculateCG")
    Call<Result> calculateCG(@Body TubeFinsetCGRequest request);
    /**
     * 计算管状翼组件压心位置
     *
     * @param request request
     * @return result
     */
    @POST("TubeFinSet/calculateCP")
    Call<Result> calculateCP(@Body TubeFinSetCpRequest request);

    /**
     * 计算管状翼组件转动惯量
     *
     * @param request request
     * @return result
     */
    @POST("TubeFinSet/calculateMOI")
    Call<Result> calculateMOI(@Body TubeFinSetMOIRequest request);

    /**
     * 计算发射套柄组件重心位置
     *
     * @param request request
     * @return result
     */
    @POST("LaunchLug/calculateCG")
    Call<Result> calculateCG(@Body LaunchLugCgRequest request);


    /**
     * 计算发射套柄组件重心位置
     *
     * @param request request
     * @return result
     */
    @POST("LaunchLug/calculateCP")
    Call<Result> calculateCP(@Body LaunchLugCpRequest request);


    /**
     * 计算发射套柄组件转动惯量
     *
     * @param request request
     * @return result
     */
    @POST("LaunchLug/calculateMOI")
    Call<Result> calculateMOI(@Body LaunchLugMOIRequest request);


    /**
     * 计算RailButton组件重心位置
     *
     * @param request request
     * @return result
     */
    @POST("RailButton/calculateCG")
    Call<Result> calculateCG(@Body RailButtonCgRequest request);

    /**
     * 计算RailButton组件压心位置
     *
     * @param request request
     * @return result
     */
    @POST("RailButton/calculateCP")
    Call<Result> calculateCP(@Body RailButtonCpRequest request);



    /**
     * 计算RailButton组件转动惯量
     *
     * @param request request
     * @return result
     */
    @POST("RailButton/calculateMOI")
    Call<Result> calculateMOI(@Body RailButtonMOIRequest request);


    /**
     * 计算内筒组件重心位置
     *
     * @param request request
     * @return result
     */
    @POST("InnerTube/calculateCG")
    Call<Result> calculateCG(@Body InnerTubeCgRequest request);


    /**
     * 计算连接器、中心环、隔板、发动机组件转动惯量
     *
     * @param request request
     * @return result
     */
    @POST("InnerTube/calculateMOI")
    Call<Result> calculateMOI(@Body InnerTubeMOIRequest request);


    /**
     * 计算连接器、中心环、隔板、发动机组件重心位置
     *
     * @param request request
     * @return result
     */
    @POST("InnerComponent/calculateCG")
    Call<Result> calculateCG(@Body InnerComponentCgRequest request);



    /**
     * 计算连接器、中心环、隔板、发动机组件转动惯量
     *
     * @param request request
     * @return result
     */
    @POST("InnerComponent/calculateMOI")
    Call<Result> calculateMOI(@Body InnerComponentMOIRequest request);


    /**
     * 计算Stage组件重心位置
     *
     * @param request request
     * @return result
     */
    @POST("Stage/calculateCG")
    Call<Result> calculateCG(@Body StageCgRequest request);


    /**
     * 计算Stage组件压心位置
     *
     * @param request request
     * @return result
     */
    @POST("Stage/calculateCP")
    Call<Result> calculateCP(@Body StageCpRequest request);



    /**
     * 计算Stage组件转动惯量
     *
     * @param request request
     * @return result
     */
    @POST("Stage/calculateMOI")
    Call<Result> calculateMOI(@Body StageMOIRequest request);




    /**
     * 计算Pods组件重心位置
     *
     * @param request request
     * @return result
     */
    @POST("Pods/calculateCG")
    Call<Result> calculateCG(@Body PodsCgRequest request);


    /**
     * 计算Pods组件压心位置
     *
     * @param request request
     * @return result
     */
    @POST("Pods/calculateCP")
    Call<Result> calculateCP(@Body PodsCpRequest request);



    /**
     * 计算Pods组件转动惯量
     *
     * @param request request
     * @return result
     */
    @POST("Pods/calculateMOI")
    Call<Result> calculateMOI(@Body PodsMOIRequest request);


    /**
     * 计算降落伞组件重心位置
     *
     * @param request request
     * @return result
     */
    @POST("Parachute/calculateCG")
    Call<Result> calculateCG(@Body ParachuteCgRequest request);





    /**
     * 计算降落伞组件转动惯量
     *
     * @param request request
     * @return result
     */
    @POST("Parachute/calculateMOI")
    Call<Result> calculateMOI(@Body ParachuteMOIRequest request);


    /**
     * 计算飘带组件重心位置
     *
     * @param request request
     * @return result
     */
    @POST("Streamer/calculateCG")
    Call<Result> calculateCG(@Body StreamerCgRequest request);





    /**
     * 计算飘带组件转动惯量
     *
     * @param request request
     * @return result
     */
    @POST("Streamer/calculateMOI")
    Call<Result> calculateMOI(@Body StreamerMOIRequest request);


    /**
     * 计算减震索组件重心位置
     *
     * @param request request
     * @return result
     */
    @POST("ShockCord/calculateCG")
    Call<Result> calculateCG(@Body ShockCordCgRequest request);





    /**
     * 计算减震索组件转动惯量
     *
     * @param request request
     * @return result
     */
    @POST("ShockCord/calculateMOI")
    Call<Result> calculateMOI(@Body ShockCordMOIRequest request);


    /**
     * 计算MassComponent组件重心位置
     *
     * @param request request
     * @return result
     */
    @POST("MassComponent/calculateCG")
    Call<Result> calculateCG(@Body MassComponentCgRequest request);


    /**
     * 计算MassComponent组件转动惯量
     *
     * @param request request
     * @return result
     */
    @POST("MassComponent/calculateMOI")
    Call<Result> calculateMOI(@Body MassComponentMOIRequest request);


    /**
     * 计算总体组件重心位置
     *
     * @param request request
     * @return result
     */
    @POST("Whole/calculateCG")
    Call<Result> calculateCG(@Body WholeCgRequest request);

    /**
     * 计算总体组件压心位置
     *
     * @param request request
     * @return result
     */
    @POST("Whole/calculateCP")
    Call<Result> calculateCP(@Body WholeCpRequest request);


    /**
     * 计算总体组件转动惯量位置
     *
     * @param request request
     * @return result
     */
    @POST("Whole/calculateMOI")
    Call<Result> calculateMOI(@Body WholeMOIRequest request);

}
