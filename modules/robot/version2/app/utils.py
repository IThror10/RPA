import base64

import numpy as np
import pyautogui
import cv2
import re
import io

from . import main_state
from .system_info import KEYS


NAMES = ["True", "False", "press", "scroll", "write", "click", "move", "hotkey", "output", "sleep",
         "put", "do", "open", "switch"]


def is_string(string: str) -> bool:
    return string.startswith('"') and string.endswith('"')


def is_float(x: str) -> bool:
    pattern = r'^[-+]?[0-9]*\.?[0-9]+([eE][-+]?[0-9]+)?$'
    return bool(re.match(pattern, x))


def prepare_int(x: str):
    if is_string(x):
        raise ValueError("expected int not string")
    if x in ["True", "False"]:
        raise ValueError("expected int not bool")
    if x in NAMES:
        raise ValueError("variable name overrides an operator name")
    if is_float(x):
        return lambda: float(x)
    if not x.isdigit():
        return lambda: int(main_state.get_variable(x))
    return lambda: int(x)


def prepare_string(x: str):
    if x.startswith('"') and x.endswith('"'):
        return lambda: x[1:-1]
    if x in ["True", "False"]:
        raise ValueError("expected string not bool")
    if x in NAMES:
        raise ValueError("variable name overrides an operator name")
    if x.isdigit() or is_float(x):
        return str(x)
    return lambda: main_state.get_variable(x)


def prepare_value(x: str):
    if x.startswith('"') and x.endswith('"'):
        return lambda: x[1:-1]
    if x.isdigit() or is_float(x):
        return lambda: x
    if x == "True" or x == "False":
        return lambda: bool(x)
    return lambda: main_state.get_variable(x)


def prepare_float(x: str):
    if is_string(x):
        raise ValueError("expected float not string")
    if x in ["True", "False"]:
        raise ValueError("expected float not bool")
    if x in NAMES:
        raise ValueError("variable name overrides an operator name")
    if is_float(x) or x.isdigit():
        return lambda: float(x)
    return lambda: float(main_state.get_variable(x))


def check_key(key: str, line: int):
    if key not in KEYS:
        raise ValueError(f"Line {line}: unknown key")


def check_variable(variable: str):
    if is_string(variable):
        raise ValueError("string cannot be variable name")
    if not variable[0].isalpha() or variable in NAMES:
        raise ValueError("wrong argument name")


def get_screenshot():
    screenshot = pyautogui.screenshot()
    width, height = screenshot.size
    return width, height, screenshot


def get_binary_image(image, image_format='PNG'):
    image_stream = io.BytesIO()
    image.save(image_stream, format=image_format)
    binary_image = image_stream.getvalue()
    image_stream.close()
    print(base64.b64encode(binary_image))
    return str(base64.b64encode(binary_image))[2:-1]


def get_image_center(image_array):
    height, width, _ = image_array.shape
    _, _, screen = get_screenshot()
    screen = np.array(screen)
    result = cv2.matchTemplate(screen, image_array, cv2.TM_CCOEFF_NORMED)
    min_val, max_val, min_loc, top_left = cv2.minMaxLoc(result)
    x = (width + 2 * top_left[0]) // 2
    y = (height + 2 * top_left[1]) // 2
    return x, y


def get_binary_file(path: str) -> str:
    with open(path, 'rb') as file:
        binary_data = file.read()
    return str(binary_data)[2:-1]
