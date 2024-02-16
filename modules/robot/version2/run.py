import argparse
import requests
from flask import request, jsonify

from app import app
from app.system_info import SYSTEM


def send_post_request(args):
    url = f'http://{args.api_host}:{args.api_port}/api/robot'
    print(url)
    data = {
        'secret': args.secret_key,
        'interactive': args.interactive,
        'groups': args.groups,
        'system': SYSTEM,
        'api': f'{args.host}:{args.port}'
    }
    print(data)
    try:
        requests.post(url, json=data)
    except:
        print('Произошла ошибка при отправке POST-запроса')


def get_args():
    parser = argparse.ArgumentParser()
    parser.add_argument('--secret_key', type=str, required=True, help='Secret key for the application')
    parser.add_argument('--port', default=8002, type=int)
    parser.add_argument('--api-port', type=str, required=True)
    parser.add_argument('--api-host', type=str, required=True)
    # parser.add_argument('--groups', type=int, required=True, help='Specify groups in format "1 2 4 9"', nargs='+')
    # parser.add_argument('--interactive', type=bool, required=True, help='Flag for interactive mode (True/False)')
    # parser.add_argument('--host', type=str)
    return parser.parse_args()


@app.before_request
def limit_remote_addr():
    curr_host, curr_port = request.host.split(':')
    print(curr_port, curr_host)
    print(HOST, PORT)
    if curr_host != HOST or curr_port != PORT:
        return jsonify({'message': 'Access denied'}), 403


if __name__ == "__main__":
    args = get_args()
    app.secret_key = args.secret_key
    HOST = args.api_host
    PORT = args.api_port
    # send_post_request(args)
    # app.run(port=args.port, host=args.host)
    app.run(debug=True, port=args.port)
