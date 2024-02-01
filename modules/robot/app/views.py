from flask import jsonify, Blueprint, request

from . import main_state
from .interpreter import Interpreter


main_blueprint = Blueprint('main', __name__)


@main_blueprint.route('/version', methods=['GET'])
def version():
    return jsonify({'current': '0.1', 'from': '0.1'})


@main_blueprint.route('/language')
def language():
    path = '../doc.md'
    with open(path, 'rb') as f:
        file_data = f.read()
    file_data_base64 = file_data.encode('base64')

    return jsonify({'text': file_data_base64})


@main_blueprint.route('/session/clear', methods=['POST'])
def clear():
    main_state.clear()
    # print(main_state.get_output())
    return jsonify({"message": "success"})


@main_blueprint.route('/session/execute', methods=['POST'])
def execute():
    code = request.get_json()['code']
    try:
        interpreter = Interpreter(code)
    except ValueError as e:
        return jsonify({"message": "syntax error", "error": e.args}), 400
    try:
        interpreter.execute()
    except:
        return jsonify({"message": "runtime error"}), 400
    # print(main_state.get_output())
    # print(main_state.variables)
    output = main_state.get_output()
    return jsonify({"message": "success", "output": output})
