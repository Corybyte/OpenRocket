from flask import Flask, request
from common_helper import equals
import os
import nose_cone_cg_helper
import nose_cone_cp_helper
import finset_cg_helper
import finset_cp_helper
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

@app.route('/NoseCone/calculateCG', methods=['POST'])
def calculateNoseConeCG():
    app.logger.info(f"{request.json}")
    try:
        cg = nose_cone_cg_helper.calculateCG(request.json)
        check(cg, request.json['answer'], os.path.join(os.getcwd(), "step1"))
        return {"code": 200, "msg": "ok", "result": cg}
    except Exception:
        return {"code": 500, "msg": "error", "result": traceback.format_exc()}


@app.route('/FinSet/calculateCG', methods=['POST'])
def calculateFinSetCG():
    app.logger.info(f"{request.json}")
    try:
        cg = finset_cg_helper.calculateCG(request.json)
        check(cg, request.json['answer'], os.path.join(os.getcwd(), "step3"))
        return {"code": 200, "msg": "ok", "result": cg}
    except Exception:
        return {"code": 500, "msg": "error", "result": traceback.format_exc()}

@app.route('/BodyTube/calculateCG', methods=['POST'])
def calculateBodyTubeCG():
    app.logger.info(f"{request.json}")
    try:
        cg = body_tube_cg_helper.calculateCG(request.json)
        check(cg, request.json['answer'], os.path.join(os.getcwd(), "step5"))
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

@app.route('/FinSet/calculateCP', methods=['POST'])
def calculateFinSetCP():
    app.logger.info(f"{request.json}")
    try:
        cp = finset_cp_helper.calculateCP(request.json)
        check(cp, request.json['answer'], os.path.join(os.getcwd(), "step4"))
        return {"code": 200, "msg": "ok", "result": cp}
    except Exception:
        return {"code": 500, "msg": "error", "result": traceback.format_exc()}

@app.route('/BodyTube/calculateCP', methods=['POST'])
def calculateBodyTubeCP():
    app.logger.info(f"{request.json}")
    try:
        cp = body_tube_cp_helper.calculateCP(request.json)
        check(cp, request.json['answer'], os.path.join(os.getcwd(), "step6"))
        return {"code": 200, "msg": "ok", "result": cp}
    except Exception:
        return {"code": 500, "msg": "error", "result": traceback.format_exc()}

if __name__ == '__main__':
    app.run(port=8080, debug=True)