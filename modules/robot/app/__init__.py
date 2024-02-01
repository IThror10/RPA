from flask import Flask
import os

from .state import State

main_state = State()

from .views import main_blueprint  # noqa


app = Flask(__name__)
app.register_blueprint(main_blueprint)
app.secret_key = os.urandom(16).hex()