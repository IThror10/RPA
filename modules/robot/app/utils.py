import re

from . import main_state


KEYS = ['shift', 'cntrl', 'left', 'right', 'esc', 'alt']


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
