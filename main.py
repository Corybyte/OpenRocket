from flask import Flask, request

import Pods_cg_helper
import Pods_cp_helper
import Pods_moi_helper
import Transition_cp_helper
import body_tube_moi_helper
import demo
import inner_component_cg_helper
import inner_component_moi_helper
import inner_tube_cg_helper
import inner_tube_moi_helper
import launchlug_cg_helper
import launchlug_cp_helper
import launchlug_moi_helper
import massComponent_cg_helper
import massComponent_moi_helper
import parachute_cg_helper
import parachute_moi_helper
import rail_button_cg_helper
import rail_button_cp_helper
import rail_button_moi_helper
import shockCord_cg_helper
import shockCord_moi_helper
import stage_cg_helper
import stage_cp_helper
import stage_moi_helper
import streamer_cg_helper
import streamer_moi_helper
import transition_cg_helper
import transition_moi_helper
import tube_fine_set_cg_helper
import tube_fine_set_cp_helper
import tube_fine_set_moi_helper
import whole_cg_helper
import whole_cp_helper
import whole_moi_helper
from calculateFunction import myFunction
from calculatePoint import myPoint
from common_helper import equals
import os
import nose_cone_cg_helper
import nose_cone_cp_helper
import nose_cone_moi_helper
import finset_cg_helper
import finset_cp_helper
import finset_moi_helper
import body_tube_cg_helper
import body_tube_cp_helper
import traceback

app = Flask(__name__)


def check(result, answer, dir):
    if equals(result, answer):
        with open(os.path.join(dir, "result.txt"), 'w') as f:
            f.write("Yes")
    else:
        with open(os.path.join(dir, "result.txt"), 'w') as f:
            f.write("No")


###NoseCone
@app.route('/NoseCone/calculateCG', methods=['POST'])
def calculateNoseConeCG():
    app.logger.info(f"{request.json}")
    try:
        cg = nose_cone_cg_helper.calculateCG(request.json)
        error_file_path = "/data/workspace/myshixun/step1/error.txt"
        if os.path.exists(error_file_path):
            os.remove(error_file_path)
        check(cg, request.json['answer'], os.path.join(os.getcwd(), "step1"))
        return {"code": 200, "msg": "ok", "result": cg}
    except Exception:
        with open(os.path.join("step1", "error.txt"), 'w') as f:
            f.write( traceback.format_exc())
        return {"code": 500, "msg": "error", "result": traceback.format_exc()}


@app.route('/Demo/demo', methods=['POST'])
def testDemo():
    app.logger.info(f"{request.json}")
    try:
        print(request.json)
        # 自己的逻辑，返回值进行结果校验
        cg = demo.calculate(request.json)
        #下面的一般不用动
        error_file_path = "/data/workspace/myshixun/step1/error.txt"
        if os.path.exists(error_file_path):
            os.remove(error_file_path)
        #check(cg, request.json['answer'], os.path.join(os.getcwd(), "step1"))
        return {"code": 200, "msg": "ok", "result": cg}
    #异常处理
    except Exception:
        with open(os.path.join("step1", "error.txt"), 'w') as f:
            f.write( traceback.format_exc())
        return {"code": 500, "msg": "error", "result": traceback.format_exc()}



@app.route('/NoseCone/calculateCP', methods=['POST'])
def calculateNoseConeCP():
    app.logger.info(f"{request.json}")
    try:
        cp = nose_cone_cp_helper.calculateCP(request.json)
        error_file_path = "/data/workspace/myshixun/step2/error.txt"
        if os.path.exists(error_file_path):
            os.remove(error_file_path)
        check(cp, request.json['answer'], os.path.join(os.getcwd(), "step2"))
        return {"code": 200, "msg": "ok", "result": cp}
    except Exception:
        with open(os.path.join("step2", "error.txt"), 'w') as f:
            f.write( traceback.format_exc())
        return {"code": 500, "msg": "error", "result": traceback.format_exc()}


@app.route('/NoseCone/calculateMOI', methods=['POST'])
def calculateNoseConeMOI():
    app.logger.info(f"{request.json}")
    try:
        moi = nose_cone_moi_helper.calculateMOI(request.json)
        error_file_path = "/data/workspace/myshixun/step3/error.txt"
        if os.path.exists(error_file_path):
            os.remove(error_file_path)
        check(moi, request.json['answer'], os.path.join(os.getcwd(), "step3"))
        return {"code": 200, "msg": "ok", "result": moi}
    except Exception:
        with open(os.path.join("step3", "error.txt"), 'w') as f:
            f.write( traceback.format_exc())
        return {"code": 500, "msg": "error", "result": traceback.format_exc()}


####BodyTube
@app.route('/BodyTube/calculateCG', methods=['POST'])
def calculateBodyTubeCG():
    app.logger.info(f"{request.json}")
    try:
        cg = body_tube_cg_helper.calculateCG(request.json)
        error_file_path = "/data/workspace/myshixun/step4/error.txt"
        if os.path.exists(error_file_path):
            os.remove(error_file_path)
        check(cg, request.json['answer'], os.path.join(os.getcwd(), "step4"))
        return {"code": 200, "msg": "ok", "result": cg}
    except Exception:
        with open(os.path.join("step4", "error.txt"), 'w') as f:
            f.write( traceback.format_exc())
        return {"code": 500, "msg": "error", "result": traceback.format_exc()}


@app.route('/BodyTube/calculateCP', methods=['POST'])
def calculateBodyTubeCP():
    app.logger.info(f"{request.json}")
    try:
        cp = body_tube_cp_helper.calculateCP(request.json)
        error_file_path = "/data/workspace/myshixun/step5/error.txt"
        if os.path.exists(error_file_path):
            os.remove(error_file_path)
        check(cp, request.json['answer'], os.path.join(os.getcwd(), "step5"))
        return {"code": 200, "msg": "ok", "result": cp}
    except Exception:
        with open(os.path.join("step5", "error.txt"), 'w') as f:
            f.write( traceback.format_exc())
        return {"code": 500, "msg": "error", "result": traceback.format_exc()}


@app.route('/BodyTube/calculateMOI', methods=['POST'])
def calculateBodyTubeMOI():
    app.logger.info(f"{request.json}")
    try:
        moi = body_tube_moi_helper.calculate_moi(request.json)
        error_file_path = "/data/workspace/myshixun/step6/error.txt"
        if os.path.exists(error_file_path):
            os.remove(error_file_path)
        check(moi, request.json['answer'], os.path.join(os.getcwd(), "step6"))
        return {"code": 200, "msg": "ok", "result": moi}
    except Exception:
        with open(os.path.join("step6", "error.txt"), 'w') as f:
            f.write( traceback.format_exc())
        return {"code": 500, "msg": "error", "result": traceback.format_exc()}


# Finset
@app.route('/FinSet/calculateCG', methods=['POST'])
def calculateFinSetCG():
    app.logger.info(f"{request.json}")
    try:
        cg = finset_cg_helper.calculateCG(request.json)
        error_file_path = "/data/workspace/myshixun/step7/error.txt"
        if os.path.exists(error_file_path):
            os.remove(error_file_path)
        check(cg, request.json['answer'], os.path.join(os.getcwd(), "step7"))
        return {"code": 200, "msg": "ok", "result": cg}
    except Exception:
        with open(os.path.join("step7", "error.txt"), 'w') as f:
            f.write( traceback.format_exc())
        return {"code": 500, "msg": "error", "result": traceback.format_exc()}


@app.route('/FinSet/calculateCP', methods=['POST'])
def calculateFinSetCP():
    app.logger.info(f"{request.json}")
    try:
        cp = finset_cp_helper.calculateCP(request.json)
        error_file_path = "/data/workspace/myshixun/step8/error.txt"
        if os.path.exists(error_file_path):
            os.remove(error_file_path)
        check(cp, request.json['answer'], os.path.join(os.getcwd(), "step8"))
        return {"code": 200, "msg": "ok", "result": cp}
    except Exception:
        with open(os.path.join("step8", "error.txt"), 'w') as f:
            f.write( traceback.format_exc())
        return {"code": 500, "msg": "error", "result": traceback.format_exc()}


@app.route('/FinSet/calculateMOI', methods=['POST'])
def calculateFinSetMOI():
    app.logger.info(f"{request.json}")
    try:
        moi = finset_moi_helper.calculate_moi(request.json)
        error_file_path = "/data/workspace/myshixun/step9/error.txt"
        if os.path.exists(error_file_path):
            os.remove(error_file_path)
        check(moi, request.json['answer'], os.path.join(os.getcwd(), "step9"))
        return {"code": 200, "msg": "ok", "result": moi}
    except Exception:
        with open(os.path.join("step9", "error.txt"), 'w') as f:
            f.write( traceback.format_exc())
        return {"code": 500, "msg": "error", "result": traceback.format_exc()}


# Transition
@app.route('/Transition/calculateCG', methods=['POST'])
def calculateTransitionCG():
    app.logger.info(f"{request.json}")
    try:
        cg = transition_cg_helper.calculateCG(request.json)
        error_file_path = "/data/workspace/myshixun/step10/error.txt"
        if os.path.exists(error_file_path):
            os.remove(error_file_path)
        check(cg, request.json['answer'], os.path.join(os.getcwd(), "step10"))
        return {"code": 200, "msg": "ok", "result": cg}
    except Exception:
        with open(os.path.join("step10", "error.txt"), 'w') as f:
            f.write( traceback.format_exc())
        return {"code": 500, "msg": "error", "result": traceback.format_exc()}


@app.route('/Transition/calculateCP', methods=['POST'])
def calculateTransitionCP():
    app.logger.info(f"{request.json}")
    try:
        cp = Transition_cp_helper.calculateCP(request.json)
        error_file_path = "/data/workspace/myshixun/step11/error.txt"
        if os.path.exists(error_file_path):
            os.remove(error_file_path)
        check(cp, request.json['answer'], os.path.join(os.getcwd(), "step11"))
        return {"code": 200, "msg": "ok", "result": cp}
    except Exception:
        with open(os.path.join("step11", "error.txt"), 'w') as f:
            f.write( traceback.format_exc())
        return {"code": 500, "msg": "error", "result": traceback.format_exc()}


@app.route('/Transition/calculateMOI', methods=['POST'])
def calculateTransitionMOI():
    app.logger.info(f"{request.json}")
    try:
        moi = transition_moi_helper.calculateMOI(request.json)
        error_file_path = "/data/workspace/myshixun/step12/error.txt"
        if os.path.exists(error_file_path):
            os.remove(error_file_path)
        check(moi, request.json['answer'], os.path.join(os.getcwd(), "step12"))
        return {"code": 200, "msg": "ok", "result": moi}
    except Exception:
        with open(os.path.join("step12", "error.txt"), 'w') as f:
            f.write( traceback.format_exc())
        return {"code": 500, "msg": "error", "result": traceback.format_exc()}


@app.route('/TubeFinSet/calculateCG', methods=['POST'])
def calculateTubeFinSetCG():
    app.logger.info(f"{request.json}")
    try:
        cg = tube_fine_set_cg_helper.calculateCG(request.json)
        error_file_path = "/data/workspace/myshixun/step13/error.txt"
        if os.path.exists(error_file_path):
            os.remove(error_file_path)
        check(cg, request.json['answer'], os.path.join(os.getcwd(), "step13"))
        return {"code": 200, "msg": "ok", "result": cg}
    except Exception:
        with open(os.path.join("step13", "error.txt"), 'w') as f:
            f.write( traceback.format_exc())
        return {"code": 500, "msg": "error", "result": traceback.format_exc()}


@app.route('/TubeFinSet/calculateCP', methods=['POST'])
def calculateTubeFinSetCP():
    app.logger.info(f"{request.json}")
    try:
        cp = tube_fine_set_cp_helper.calculateCP(request.json)
        error_file_path = "/data/workspace/myshixun/step14/error.txt"
        if os.path.exists(error_file_path):
            os.remove(error_file_path)
        check(cp, request.json['answer'], os.path.join(os.getcwd(), "step14"))
        return {"code": 200, "msg": "ok", "result": cp}
    except Exception:
        with open(os.path.join("step14", "error.txt"), 'w') as f:
            f.write( traceback.format_exc())
        return {"code": 500, "msg": "error", "result": traceback.format_exc()}


@app.route('/TubeFinSet/calculateMOI', methods=['POST'])
def calculateTubeFinSetMOI():
    app.logger.info(f"{request.json}")
    try:
        moi = tube_fine_set_moi_helper.calculateMOI(request.json)
        error_file_path = "/data/workspace/myshixun/step15/error.txt"
        if os.path.exists(error_file_path):
            os.remove(error_file_path)
        check(moi, request.json['answer'], os.path.join(os.getcwd(), "step15"))
        return {"code": 200, "msg": "ok", "result": moi}
    except Exception:
        with open(os.path.join("step15", "error.txt"), 'w') as f:
            f.write( traceback.format_exc())
        return {"code": 500, "msg": "error", "result": traceback.format_exc()}


@app.route('/LaunchLug/calculateCG', methods=['POST'])
def calculateLaunchLugCG():
    app.logger.info(f"{request.json}")
    try:
        cg = launchlug_cg_helper.calculateCG(request.json)
        error_file_path = "/data/workspace/myshixun/step16/error.txt"
        if os.path.exists(error_file_path):
            os.remove(error_file_path)
        check(cg, request.json['answer'], os.path.join(os.getcwd(), "step16"))
        return {"code": 200, "msg": "ok", "result": cg}
    except Exception:
        with open(os.path.join("step16", "error.txt"), 'w') as f:
            f.write( traceback.format_exc())
        return {"code": 500, "msg": "error", "result": traceback.format_exc()}


@app.route('/LaunchLug/calculateCP', methods=['POST'])
def calculateLaunchLugCP():
    app.logger.info(f"{request.json}")
    try:
        cp = launchlug_cp_helper.calculateCP(request.json)
        error_file_path = "/data/workspace/myshixun/step17/error.txt"
        if os.path.exists(error_file_path):
            os.remove(error_file_path)
        check(cp, request.json['answer'], os.path.join(os.getcwd(), "step17"))
        return {"code": 200, "msg": "ok", "result": cp}
    except Exception:
        with open(os.path.join("step17", "error.txt"), 'w') as f:
            f.write( traceback.format_exc())
        return {"code": 500, "msg": "error", "result": traceback.format_exc()}


@app.route('/LaunchLug/calculateMOI', methods=['POST'])
def calculateLaunchLugMOI():
    app.logger.info(f"{request.json}")
    try:
        moi = launchlug_moi_helper.calculateMOI(request.json)
        error_file_path = "/data/workspace/myshixun/step18/error.txt"
        if os.path.exists(error_file_path):
            os.remove(error_file_path)
        check(moi, request.json['answer'], os.path.join(os.getcwd(), "step18"))
        return {"code": 200, "msg": "ok", "result": moi}
    except Exception:
        with open(os.path.join("step18", "error.txt"), 'w') as f:
            f.write( traceback.format_exc())
        return {"code": 500, "msg": "error", "result": traceback.format_exc()}


@app.route('/RailButton/calculateCG', methods=['POST'])
def calculateRailButtonCG():
    app.logger.info(f"{request.json}")
    try:
        cg = rail_button_cg_helper.calculateCG(request.json)
        error_file_path = "/data/workspace/myshixun/step19/error.txt"
        if os.path.exists(error_file_path):
            os.remove(error_file_path)
        check(cg, request.json['answer'], os.path.join(os.getcwd(), "step19"))
        return {"code": 200, "msg": "ok", "result": cg}
    except Exception:
        with open(os.path.join("step19", "error.txt"), 'w') as f:
            f.write( traceback.format_exc())
        return {"code": 500, "msg": "error", "result": traceback.format_exc()}


@app.route('/RailButton/calculateCP', methods=['POST'])
def calculateRailButtonCP():
    app.logger.info(f"{request.json}")
    try:
        cp = rail_button_cp_helper.calculateCP(request.json)
        error_file_path = "/data/workspace/myshixun/step20/error.txt"
        if os.path.exists(error_file_path):
            os.remove(error_file_path)
        check(cp, request.json['answer'], os.path.join(os.getcwd(), "step20"))
        return {"code": 200, "msg": "ok", "result": cp}
    except Exception:
        with open(os.path.join("step20", "error.txt"), 'w') as f:
            f.write( traceback.format_exc())
        return {"code": 500, "msg": "error", "result": traceback.format_exc()}


@app.route('/RailButton/calculateMOI', methods=['POST'])
def calculateRailButtonMOI():
    app.logger.info(f"{request.json}")
    try:
        moi = rail_button_moi_helper.calculateMOI(request.json)
        error_file_path = "/data/workspace/myshixun/step21/error.txt"
        if os.path.exists(error_file_path):
            os.remove(error_file_path)
        check(moi, request.json['answer'], os.path.join(os.getcwd(), "step21"))
        return {"code": 200, "msg": "ok", "result": moi}
    except Exception:
        with open(os.path.join("step21", "error.txt"), 'w') as f:
            f.write( traceback.format_exc())
        return {"code": 500, "msg": "error", "result": traceback.format_exc()}


@app.route('/InnerTube/calculateCG', methods=['POST'])
def calculateInnerTubeCG():
    app.logger.info(f"{request.json}")
    try:
        cg = inner_tube_cg_helper.calculateCG(request.json)
        error_file_path = "/data/workspace/myshixun/step22/error.txt"
        if os.path.exists(error_file_path):
            os.remove(error_file_path)
        check(cg, request.json['answer'], os.path.join(os.getcwd(), "step22"))
        return {"code": 200, "msg": "ok", "result": cg}
    except Exception:
        with open(os.path.join("step22", "error.txt"), 'w') as f:
            f.write( traceback.format_exc())
        return {"code": 500, "msg": "error", "result": traceback.format_exc()}


@app.route('/InnerTube/calculateMOI', methods=['POST'])
def calculateInnerTubeMOI():
    app.logger.info(f"{request.json}")
    try:
        moi = inner_tube_moi_helper.calculateMOI(request.json)
        error_file_path = "/data/workspace/myshixun/step24/error.txt"
        if os.path.exists(error_file_path):
            os.remove(error_file_path)
        check(moi, request.json['answer'], os.path.join(os.getcwd(), "step24"))
        return {"code": 200, "msg": "ok", "result": moi}
    except Exception:
        with open(os.path.join("step24", "error.txt"), 'w') as f:
            f.write( traceback.format_exc())
        return {"code": 500, "msg": "error", "result": traceback.format_exc()}


@app.route('/InnerComponent/calculateCG', methods=['POST'])
def calculateInnerComponentCG():
    app.logger.info(f"{request.json}")
    try:
        cg = inner_component_cg_helper.calculateCG(request.json)
        error_file_path = "/data/workspace/myshixun/step25/error.txt"
        if os.path.exists(error_file_path):
            os.remove(error_file_path)
        check(cg, request.json['answer'], os.path.join(os.getcwd(), "step25"))
        return {"code": 200, "msg": "ok", "result": cg}
    except Exception:
        with open(os.path.join("step25", "error.txt"), 'w') as f:
            f.write( traceback.format_exc())
        return {"code": 500, "msg": "error", "result": traceback.format_exc()}


@app.route('/InnerComponent/calculateMOI', methods=['POST'])
def calculateInnerComponentMOI():
    app.logger.info(f"{request.json}")
    try:
        moi = inner_component_moi_helper.calculateMOI(request.json)
        error_file_path = "/data/workspace/myshixun/step27/error.txt"
        if os.path.exists(error_file_path):
            os.remove(error_file_path)
        check(moi, request.json['answer'], os.path.join(os.getcwd(), "step27"))
        return {"code": 200, "msg": "ok", "result": moi}
    except Exception:
        with open(os.path.join("step27", "error.txt"), 'w') as f:
            f.write( traceback.format_exc())
        return {"code": 500, "msg": "error", "result": traceback.format_exc()}


@app.route('/Parachute/calculateCG', methods=['POST'])
def calculateParachuteCG():
    app.logger.info(f"{request.json}")
    try:
        cg = parachute_cg_helper.calculateCG(request.json)
        error_file_path = "/data/workspace/myshixun/step28/error.txt"
        if os.path.exists(error_file_path):
            os.remove(error_file_path)
        check(cg, request.json['answer'], os.path.join(os.getcwd(), "step28"))
        return {"code": 200, "msg": "ok", "result": cg}
    except Exception:
        with open(os.path.join("step28", "error.txt"), 'w') as f:
            f.write( traceback.format_exc())
        return {"code": 500, "msg": "error", "result": traceback.format_exc()}


@app.route('/Parachute/calculateMOI', methods=['POST'])
def calculateParachuteMOI():
    app.logger.info(f"{request.json}")
    try:
        moi = parachute_moi_helper.calculateMOI(request.json)
        error_file_path = "/data/workspace/myshixun/step30/error.txt"
        if os.path.exists(error_file_path):
            os.remove(error_file_path)
        check(moi, request.json['answer'], os.path.join(os.getcwd(), "step30"))
        return {"code": 200, "msg": "ok", "result": moi}
    except Exception:
        with open(os.path.join("step30", "error.txt"), 'w') as f:
            f.write( traceback.format_exc())
        return {"code": 500, "msg": "error", "result": traceback.format_exc()}


@app.route('/ShockCord/calculateCG', methods=['POST'])
def calculateShockCordCG():
    app.logger.info(f"{request.json}")
    try:
        cg = shockCord_cg_helper.calculateCG(request.json)
        error_file_path = "/data/workspace/myshixun/step31/error.txt"
        if os.path.exists(error_file_path):
            os.remove(error_file_path)
        check(cg, request.json['answer'], os.path.join(os.getcwd(), "step31"))
        return {"code": 200, "msg": "ok", "result": cg}
    except Exception:
        with open(os.path.join("step31", "error.txt"), 'w') as f:
            f.write( traceback.format_exc())
        return {"code": 500, "msg": "error", "result": traceback.format_exc()}


@app.route('/ShockCord/calculateMOI', methods=['POST'])
def calculateShockCordMOI():
    app.logger.info(f"{request.json}")
    try:
        moi = shockCord_moi_helper.calculateMOI(request.json)
        error_file_path = "/data/workspace/myshixun/step33/error.txt"
        if os.path.exists(error_file_path):
            os.remove(error_file_path)
        check(moi, request.json['answer'], os.path.join(os.getcwd(), "step33"))
        return {"code": 200, "msg": "ok", "result": moi}
    except Exception:
        with open(os.path.join("step33", "error.txt"), 'w') as f:
            f.write( traceback.format_exc())
        return {"code": 500, "msg": "error", "result": traceback.format_exc()}


@app.route('/MassComponent/calculateCG', methods=['POST'])
def calculateMassComponentCG():
    app.logger.info(f"{request.json}")
    try:
        cg = massComponent_cg_helper.calculateCG(request.json)
        error_file_path = "/data/workspace/myshixun/step34/error.txt"
        if os.path.exists(error_file_path):
            os.remove(error_file_path)
        check(cg, request.json['answer'], os.path.join(os.getcwd(), "step34"))
        return {"code": 200, "msg": "ok", "result": cg}
    except Exception:
        with open(os.path.join("step34", "error.txt"), 'w') as f:
            f.write( traceback.format_exc())
        return {"code": 500, "msg": "error", "result": traceback.format_exc()}


@app.route('/MassComponent/calculateMOI', methods=['POST'])
def calculateMassComponentMOI():
    app.logger.info(f"{request.json}")
    try:
        moi = massComponent_moi_helper.calculateMOI(request.json)
        error_file_path = "/data/workspace/myshixun/step36/error.txt"
        if os.path.exists(error_file_path):
            os.remove(error_file_path)
        check(moi, request.json['answer'], os.path.join(os.getcwd(), "step36"))
        return {"code": 200, "msg": "ok", "result": moi}
    except Exception:
        with open(os.path.join("step36", "error.txt"), 'w') as f:
            f.write( traceback.format_exc())
        return {"code": 500, "msg": "error", "result": traceback.format_exc()}


@app.route('/Streamer/calculateCG', methods=['POST'])
def calculateStreamerCG():
    app.logger.info(f"{request.json}")
    try:
        cg = streamer_cg_helper.calculateCG(request.json)
        error_file_path = "/data/workspace/myshixun/step37/error.txt"
        if os.path.exists(error_file_path):
            os.remove(error_file_path)
        check(cg, request.json['answer'], os.path.join(os.getcwd(), "step37"))
        return {"code": 200, "msg": "ok", "result": cg}
    except Exception:
        with open(os.path.join("step37", "error.txt"), 'w') as f:
            f.write( traceback.format_exc())
        return {"code": 500, "msg": "error", "result": traceback.format_exc()}


@app.route('/Streamer/calculateMOI', methods=['POST'])
def calculateStreamerMOI():
    app.logger.info(f"{request.json}")
    try:
        moi = streamer_moi_helper.calculateMOI(request.json)
        error_file_path = "/data/workspace/myshixun/step39/error.txt"
        if os.path.exists(error_file_path):
            os.remove(error_file_path)
        check(moi, request.json['answer'], os.path.join(os.getcwd(), "step39"))
        return {"code": 200, "msg": "ok", "result": moi}
    except Exception:
        with open(os.path.join("step39", "error.txt"), 'w') as f:
            f.write( traceback.format_exc())
        return {"code": 500, "msg": "error", "result": traceback.format_exc()}


@app.route('/Stage/calculateCG', methods=['POST'])
def calculateStageCG():
    app.logger.info(f"{request.json}")
    try:
        cg = stage_cg_helper.calculateCG(request.json)
        error_file_path = "/data/workspace/myshixun/step49/error.txt"
        if os.path.exists(error_file_path):
            os.remove(error_file_path)
        check(cg, request.json['answer'], os.path.join(os.getcwd(), "step49"))
        return {"code": 200, "msg": "ok", "result": cg}
    except Exception:
        with open(os.path.join("step49", "error.txt"), 'w') as f:
            f.write( traceback.format_exc())
        return {"code": 500, "msg": "error", "result": traceback.format_exc()}


@app.route('/Stage/calculateCP', methods=['POST'])
def calculateStageCP():
    app.logger.info(f"{request.json}")
    try:
        cp = stage_cp_helper.calculateCP(request.json)
        error_file_path = "/data/workspace/myshixun/step50/error.txt"
        if os.path.exists(error_file_path):
            os.remove(error_file_path)
        check(cp, request.json['answer'], os.path.join(os.getcwd(), "step50"))
        return {"code": 200, "msg": "ok", "result": cp}
    except Exception:
        with open(os.path.join("step50", "error.txt"), 'w') as f:
            f.write( traceback.format_exc())
        return {"code": 500, "msg": "error", "result": traceback.format_exc()}


@app.route('/Stage/calculateMOI', methods=['POST'])
def calculateStageMOI():
    app.logger.info(f"{request.json}")
    try:
        moi = stage_moi_helper.calculateMOI(request.json)
        error_file_path = "/data/workspace/myshixun/step51/error.txt"
        if os.path.exists(error_file_path):
            os.remove(error_file_path)
        check(moi, request.json['answer'], os.path.join(os.getcwd(), "step51"))
        return {"code": 200, "msg": "ok", "result": moi}
    except Exception:
        with open(os.path.join("step51", "error.txt"), 'w') as f:
            f.write( traceback.format_exc())
        return {"code": 500, "msg": "error", "result": traceback.format_exc()}


@app.route('/Pods/calculateCG', methods=['POST'])
def calculatePodsCG():
    app.logger.info(f"{request.json}")
    try:
        cg = Pods_cg_helper.calculateCG(request.json)
        error_file_path = "/data/workspace/myshixun/step52/error.txt"
        if os.path.exists(error_file_path):
            os.remove(error_file_path)
        check(cg, request.json['answer'], os.path.join(os.getcwd(), "step52"))
        return {"code": 200, "msg": "ok", "result": cg}
    except Exception:
        with open(os.path.join("step52", "error.txt"), 'w') as f:
            f.write( traceback.format_exc())
        return {"code": 500, "msg": "error", "result": traceback.format_exc()}


@app.route('/Pods/calculateCP', methods=['POST'])
def calculatePodsCP():
    app.logger.info(f"{request.json}")
    try:
        cp = Pods_cp_helper.calculateCP(request.json)
        error_file_path = "/data/workspace/myshixun/step53/error.txt"
        if os.path.exists(error_file_path):
            os.remove(error_file_path)
        check(cp, request.json['answer'], os.path.join(os.getcwd(), "step53"))
        return {"code": 200, "msg": "ok", "result": cp}
    except Exception:
        with open(os.path.join("step53", "error.txt"), 'w') as f:
            f.write( traceback.format_exc())
        return {"code": 500, "msg": "error", "result": traceback.format_exc()}


@app.route('/Pods/calculateMOI', methods=['POST'])
def calculatePodsMOI():
    app.logger.info(f"{request.json}")
    try:
        moi = Pods_moi_helper.calculateMOI(request.json)
        error_file_path = "/data/workspace/myshixun/step54/error.txt"
        if os.path.exists(error_file_path):
            os.remove(error_file_path)
        check(moi, request.json['answer'], os.path.join(os.getcwd(), "step54"))
        return {"code": 200, "msg": "ok", "result": moi}
    except Exception:
        with open(os.path.join("step54", "error.txt"), 'w') as f:
            f.write( traceback.format_exc())
        return {"code": 500, "msg": "error", "result": traceback.format_exc()}

@app.route('/Whole/calculateCG', methods=['POST'])
def calculateWholeCG():
    app.logger.info(f"{request.json}")
    try:
        cg = whole_cg_helper.calculateCG(request.json)
        error_file_path = "/data/workspace/myshixun/step55/error.txt"
        if os.path.exists(error_file_path):
            os.remove(error_file_path)
        check(cg, request.json['answer'], os.path.join(os.getcwd(), "step55"))
        return {"code": 200, "msg": "ok", "result": cg}
    except Exception:
        with open(os.path.join("step55", "error.txt"), 'w') as f:
            f.write( traceback.format_exc())
        return {"code": 500, "msg": "error", "result": traceback.format_exc()}
@app.route('/Whole/calculateCP', methods=['POST'])
def calculateWholeCP():
    app.logger.info(f"{request.json}")
    try:
        cg = whole_cp_helper.calculateCP(request.json)
        error_file_path = "/data/workspace/myshixun/step56/error.txt"
        if os.path.exists(error_file_path):
            os.remove(error_file_path)
        check(cg, request.json['answer'], os.path.join(os.getcwd(), "step56"))
        return {"code": 200, "msg": "ok", "result": cg}
    except Exception:
        with open(os.path.join("step56", "error.txt"), 'w') as f:
            f.write( traceback.format_exc())
        return {"code": 500, "msg": "error", "result": traceback.format_exc()}


@app.route('/Whole/calculateMOI', methods=['POST'])
def calculateWholeMOI():
    app.logger.info(f"{request.json}")
    try:
        cg = whole_moi_helper.calculateMOI(request.json)
        error_file_path = "/data/workspace/myshixun/step57/error.txt"
        if os.path.exists(error_file_path):
            os.remove(error_file_path)
        check(cg, request.json['answer'], os.path.join(os.getcwd(), "step57"))
        return {"code": 200, "msg": "ok", "result": cg}
    except Exception:
        with open(os.path.join("step57", "error.txt"), 'w') as f:
            f.write( traceback.format_exc())
        return {"code": 500, "msg": "error", "result": traceback.format_exc()}

@app.route('/Motor/point', methods=['POST'])
def calculatePoint():
    app.logger.info(f"{request.json}")
    try:
        result = myPoint.point()
        with open(os.path.join("calculatePoint", "result.txt"), 'w') as f:
            f.write("发动机数据点加载成功")
        return {"code": 200, "msg": "ok", "result": result}
    except Exception:
        with open(os.path.join("calculatePoint", "error.txt"), 'w') as f:
            f.write(traceback.format_exc())
        return {"code": 500, "msg": "error", "result": traceback.format_exc()}
@app.route('/Motor/function', methods=['POST'])
def calculateFunction():
    app.logger.info(f"{request.json}")
    try:
        result = myFunction.functions()
        with open(os.path.join("calculateFunction", "result.txt"), 'w') as f:
            f.write("发动机数据点加载成功")
        return {"code": 200, "msg": "ok", "result": result}
    except Exception:
        with open(os.path.join("calculatePoint", "error.txt"), 'w') as f:
            f.write(traceback.format_exc())
        return {"code": 500, "msg": "error", "result": traceback.format_exc()}

if __name__ == '__main__':
    app.run(host="127.0.0.1", port=8080, debug=True)
