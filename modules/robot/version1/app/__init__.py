from flask import Flask
import os

from version1.app.state import State

main_state = State()

from version1.app.views import main_blueprint  # noqa


app = Flask(__name__)
app.register_blueprint(main_blueprint)
app.secret_key = os.urandom(16).hex()
