from flask import jsonify, Blueprint, request

from version1.app import main_state
from version1.app.interpreter import Interpreter
from version1.app.utils import get_screenshot


main_blueprint = Blueprint('main', __name__)


@main_blueprint.route('/version', methods=['GET'])
def version():
    return jsonify({'current': '0.1', 'from': '0.1'})


@main_blueprint.route('/language')
def language():
    path = '../doc.md'
    with open(path, 'rb') as f:
        data = f.read()
    return jsonify({'text': str(data)})


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
        main_state.init_variable(variable['name'], variable['value'])
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
    return jsonify({"width": width, "height": height, "image": image})
