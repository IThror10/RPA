import pyautogui
import re

from version1.app import main_state


KEYS = ['shift', 'cntrl', 'left', 'right', 'esc', 'alt', 'enter', 'up', 'down', 'space', 'backspace']

NAMES = ["press", "scroll", "write", "click", "move", "hotkey", "output", "sleep", "put", "do"]


def is_literal(string: str) -> bool:
    return string.startswith('"') and string.endswith('"')


def prepare_int(x: str):
    if not x.isdigit():
        return lambda: int(main_state.get_variable(x))
    return lambda: int(x)


def prepare_string(x: str):
    if x.startswith('"') and x.endswith('"'):
        return lambda: x[1:-1]
    return lambda: main_state.get_variable(x)


def prepare_value(x: str):
    if x.startswith('"') and x.endswith('"'):
        return lambda: x[1:-1]
    if x.isdigit():
        return lambda: x
    return lambda: main_state.get_variable(x)


def prepare_float(x: str):
    pattern = r'^[-+]?[0-9]*\.?[0-9]+([eE][-+]?[0-9]+)?$'
    if not bool(re.match(pattern, x)):
        return lambda: float(main_state.get_variable(x))
    return lambda: float(x)


def prepare_key(key: str):
    if key in KEYS:
        return lambda: key
    if key.startswith('"') and key.endswith('"'):
        return lambda: key[1]
    else:
        return lambda: main_state.get_variable(key)


def get_screenshot():
    screenshot = pyautogui.screenshot()
    width, height = screenshot.size
    return width, height, screenshot


def check_variable(variable: str):
    if is_literal(variable):
        raise ValueError("string cannot be variable name")
    if not variable[0].isalpha() or variable in NAMES:
        raise ValueError("wrong argument name")
