from flask import jsonify, Blueprint, request

from . import main_state
from .interpreter import Interpreter
from .utils import get_screenshot, get_binary_image, get_binary_file, is_float


main_blueprint = Blueprint('main', __name__)


@main_blueprint.route('/version', methods=['GET'])
def version():
    return jsonify({'current': '0.2', 'from': '0.2'})


@main_blueprint.route('/language')
def language():
    return jsonify({'text': get_binary_file('doc.md')})


@main_blueprint.route('/session/clear', methods=['POST'])
def clear():
    main_state.clear()
    main_state.clear_funcs()
    return jsonify({"message": "success"})


@main_blueprint.route('/session/execute', methods=['POST'])
def execute():
    request_args = request.get_json()
    code = request_args['code']
    input_variables = request_args['input']
    video_flag = request_args['videoFlag']
    for variable in input_variables:
        if variable["type"].lower() == 'image':
            try:
                main_state.add_image(variable['name'], variable['value'])
            except:
                return jsonify({
                    "message": "input error",
                    "error": f"error while processing image {variable['name']}"
                }), 400
        elif variable["type"] == 'integer':
            if isinstance(variable["value"], int):
                main_state.add_variable(variable["name"], variable["name"])
            else:
                return jsonify({
                    "message": "input error",
                    "error": f"wrong value for integer type in variable {variable['name']}"
                }), 400
        elif variable['type'] == 'bool':
            if isinstance(variable['value'], bool):
                main_state.add_variable(variable["name"], variable["value"])
            else:
                return jsonify({
                    "message": "input error",
                    "error": f"wrong value for boolean type in variable {variable['name']}"
                }), 400
        elif variable["type"] == "float":
            if is_float(variable["value"]) or isinstance(variable["value"], int):
                main_state.add_variable(variable["name"], variable["value"])
            else:
                return jsonify({
                    "message": "input error",
                    "error": f"wrong value for float type in variable {variable['name']}"
                }), 400
        else:
            return jsonify({
                "message": "input error",
                "error": f"unknown type {variable['type']}"
            }), 400
    try:
        interpreter = Interpreter(code)
    except ValueError as e:
        return jsonify({"message": "syntax error", "error": e.args}), 400
    try:
        video = interpreter.execute(video_flag=video_flag)
    except RuntimeError:
        return jsonify({"message": "runtime error"}), 400
    output = main_state.get_output()
    main_state.clear_funcs()
    return jsonify({"message": "success", "output": output, "video": video})


@main_blueprint.route('/check')
def check():
    code = request.get_json()['code']
    try:
        Interpreter(code)
    except ValueError as e:
        return jsonify({"message": "error", "error": e.args}), 400
    return jsonify({"message": "ok"})


@main_blueprint.route('/screenshot')
def screenshot():
    width, height, image = get_screenshot()
    return jsonify({"width": width, "height": height, "image": get_binary_image(image)})
