from flask import Flask, request

import Transition_cp_helper
import body_tube_moi_helper
import transition_cg_helper
import transition_moi_helper
import tube_fine_set_cg_helper
import tube_fine_set_cp_helper
import tube_fine_set_moi_helper
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
        check(cg, request.json['answer'], os.path.join(os.getcwd(), "step1"))
        return {"code": 200, "msg": "ok", "result": cg}
    except Exception:
        return {"code": 500, "msg": "error", "result": traceback.format_exc()}

@app.route('/NoseCone/calculateCP', methods=['POST'])
def calculateNoseConeCP():
    app.logger.info(f"{request.json}")
    try:
        cp = nose_cone_cp_helper.calculateCP(request.json)
        check(cp, request.json['answer'], os.path.join(os.getcwd(), "step2"))
        return {"code": 200, "msg": "ok", "result": cp}
    except Exception:
        return {"code": 500, "msg": "error", "result": traceback.format_exc()}

@app.route('/NoseCone/calculateMOI', methods=['POST'])
def calculateNoseConeMOI():
    app.logger.info(f"{request.json}")
    try:
        moi = nose_cone_moi_helper.calculateMOI(request.json)
        check(moi, request.json['answer'], os.path.join(os.getcwd(), "step3"))
        return {"code": 200, "msg": "ok", "result": moi}
    except Exception:
        return {"code": 500, "msg": "error", "result": traceback.format_exc()}

####BodyTube
@app.route('/BodyTube/calculateCG', methods=['POST'])
def calculateBodyTubeCG():
    app.logger.info(f"{request.json}")
    try:
        cg = body_tube_cg_helper.calculateCG(request.json)
        check(cg, request.json['answer'], os.path.join(os.getcwd(), "step4"))
        return {"code": 200, "msg": "ok", "result": cg}
    except Exception:
        return {"code": 500, "msg": "error", "result": traceback.format_exc()}
@app.route('/BodyTube/calculateCP', methods=['POST'])
def calculateBodyTubeCP():
    app.logger.info(f"{request.json}")
    try:
        cp = body_tube_cp_helper.calculateCP(request.json)
        check(cp, request.json['answer'], os.path.join(os.getcwd(), "step5"))
        return {"code": 200, "msg": "ok", "result": cp}
    except Exception:
        return {"code": 500, "msg": "error", "result": traceback.format_exc()}
@app.route('/BodyTube/calculateMOI', methods=['POST'])
def calculateBodyTubeMOI():
    app.logger.info(f"{request.json}")
    try:
        moi = body_tube_moi_helper.calculate_moi(request.json)
        check(moi, request.json['answer'], os.path.join(os.getcwd(), "step6"))
        return {"code": 200, "msg": "ok", "result": moi}
    except Exception:
        return {"code": 500, "msg": "error", "result": traceback.format_exc()}

# Finset
@app.route('/FinSet/calculateCG', methods=['POST'])
def calculateFinSetCG():
    app.logger.info(f"{request.json}")
    try:
        cg = finset_cg_helper.calculateCG(request.json)
        check(cg, request.json['answer'], os.path.join(os.getcwd(), "step7"))
        return {"code": 200, "msg": "ok", "result": cg}
    except Exception:
        return {"code": 500, "msg": "error", "result": traceback.format_exc()}

@app.route('/FinSet/calculateCP', methods=['POST'])
def calculateFinSetCP():
    app.logger.info(f"{request.json}")
    try:
        cp = finset_cp_helper.calculateCP(request.json)
        check(cp, request.json['answer'], os.path.join(os.getcwd(), "step8"))
        return {"code": 200, "msg": "ok", "result": cp}
    except Exception:
        return {"code": 500, "msg": "error", "result": traceback.format_exc()}

@app.route('/FinSet/calculateMOI', methods=['POST'])
def calculateFinSetMOI():
    app.logger.info(f"{request.json}")
    try:
        moi = finset_moi_helper.calculate_moi(request.json)
        check(moi, request.json['answer'], os.path.join(os.getcwd(), "step9"))
        return {"code": 200, "msg": "ok", "result": moi}
    except Exception:
        return {"code": 500, "msg": "error", "result": traceback.format_exc()}

# Transition
@app.route('/Transition/calculateCG', methods=['POST'])
def calculateTransitionCG():
    app.logger.info(f"{request.json}")
    try:
        cg = transition_cg_helper.calculateCG(request.json)
        check(cg, request.json['answer'], os.path.join(os.getcwd(), "step10"))
        return {"code": 200, "msg": "ok", "result": cg}
    except Exception:
        return {"code": 500, "msg": "error", "result": traceback.format_exc()}
@app.route('/Transition/calculateCP', methods=['POST'])
def calculateTransitionCP():
    app.logger.info(f"{request.json}")
    try:
        cp = Transition_cp_helper.calculateCP(request.json)
        print(type(cp))
        print(cp)
        check(cp, request.json['answer'], os.path.join(os.getcwd(), "step11"))
        return {"code": 200, "msg": "ok", "result": cp}
    except Exception:
        return {"code": 500, "msg": "error", "result": traceback.format_exc()}
@app.route('/Transition/calculateMOI', methods=['POST'])
def calculateTransitionMOI():
    app.logger.info(f"{request.json}")
    try:
        moi = transition_moi_helper.calculateMOI(request.json)
        check(moi, request.json['answer'], os.path.join(os.getcwd(), "step12"))
        return {"code": 200, "msg": "ok", "result": moi}
    except Exception:
        return {"code": 500, "msg": "error", "result": traceback.format_exc()}


@app.route('/TubeFinSet/calculateCG', methods=['POST'])
def calculateTubeFinSetCG():
    app.logger.info(f"{request.json}")
    try:
        cg = tube_fine_set_cg_helper.calculateCG(request.json)
        check(cg, request.json['answer'], os.path.join(os.getcwd(), "step13"))
        return {"code": 200, "msg": "ok", "result": cg}
    except Exception:
        return {"code": 500, "msg": "error", "result": traceback.format_exc()}

@app.route('/TubeFinSet/calculateCP', methods=['POST'])
def calculateTubeFinSetCP():
    app.logger.info(f"{request.json}")
    try:
        cp = tube_fine_set_cp_helper.calculateCP(request.json)
        check(cp, request.json['answer'], os.path.join(os.getcwd(), "step14"))
        return {"code": 200, "msg": "ok", "result": cp}
    except Exception:
        return {"code": 500, "msg": "error", "result": traceback.format_exc()}


@app.route('/TubeFinSet/calculateMOI', methods=['POST'])
def calculateTubeFinSetMOI():
    app.logger.info(f"{request.json}")
    try:
        moi = tube_fine_set_moi_helper.calculateMOI(request.json)
        check(moi, request.json['answer'], os.path.join(os.getcwd(), "step15"))
        return {"code": 200, "msg": "ok", "result": moi}
    except Exception:
        return {"code": 500, "msg": "error", "result": traceback.format_exc()}


if __name__ == '__main__':
    app.run(host="127.0.0.1",port=8080, debug=True)