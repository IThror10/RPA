from PIL import Image
import numpy as np
import pyautogui
import cv2
import easyocr
import base64
import io
import time

from .system_info import SYSTEM


class State:
    def __init__(self):
        self.variables = {}
        self.output = {}
        self.funcs ={}
        self.images = {}
        self.windows = []
        self.window_words = {}
        self.window_screen = {}
        self.reader = easyocr.Reader(['en', 'ru'])

    def init_variable(self, name, value):
        if isinstance(value, str) and value.startswith('"') and value.endswith('"'):
            value = value[1:-1]
        self.variables[name] = value

    def init_func(self, name, command):
        if name in self.funcs:
            raise ValueError(f"Redefinition of function {name}")
        self.funcs[name] = command

    def get_word_position(self, word):
        def check(x):
            if min_col <= x[0][0][0] <= max_col \
                    and min_col <= x[0][1][0] <= max_col\
                    and min_row <= x[0][0][1] <= max_row \
                    and min_row <= x[0][2][1] <= max_row:
                return False
            return True

        window = 'DEFAULT_WINDOW' if len(self.windows) == 0 else self.windows[-1]
        screenshot = cv2.cvtColor(np.array(pyautogui.screenshot()), cv2.COLOR_BGR2GRAY)
        height, width = screenshot.shape
        if window not in self.window_words:
            self.window_words[window] = self.reader.readtext(screenshot)
            self.window_screen[window] = screenshot
        else:
            different_indices = np.where(screenshot != self.window_screen[window])
            if len(different_indices[0]) != 0:
                self.window_screen[window] = screenshot
                min_row = np.min(different_indices[0])
                max_row = np.max(different_indices[0])
                min_col = np.min(different_indices[1])
                max_col = np.max(different_indices[1])
                self.window_words[window] = list(filter(lambda x: check(x), self.window_words[window]))
                new_words = self.reader.readtext(screenshot[min_row:max_row + 1, min_col:max_col + 1])
                self.window_words[window].extend(new_words)

        results = sorted(filter(lambda x: word in x[1], self.window_words[window]), key=lambda x: len(x[1]))
        print(results)
        if len(results) == 0:
            raise ValueError(f"cannot find word {word} on the screen")
        return \
            results[0][0][0][0] / width, \
            results[0][0][0][1] / height, \
            results[0][0][2][0] / width, \
            results[0][0][2][1] / height


    def get_func(self, name):
        return self.funcs[name]

    def clear_funcs(self):
        self.funcs = {}

    def add_result(self, name, value=None):
        if value is None:
            self.output[name] = self.variables[name]
        else:
            self.output[name] = value

    def get_variable(self, name):
        try:
            return self.variables[name]
        except KeyError:
            raise KeyError(f"unknown variable: {name}")

    def clear(self):
        self.variables = {}
        self.output = {}
        self.images = {}

    def get_output(self):
        return self.output

    def add_image(self, name, value):
        image_data = base64.b64decode(value)
        image_stream = io.BytesIO(image_data)
        self.images[name] = np.array(Image.open(image_stream))

    def get_image(self, name):
        try:
            return self.images[name]
        except KeyError:
            raise ValueError(f"unknown image: {name}")

    def clear_images(self):
        self.images = {}

    def open_window(self, name):
        self.windows.append(name)

    def close_all_windows(self):
        if SYSTEM == "Darwin":
            for _ in range(len(self.windows)):
                pyautogui.keyDown('command')
                pyautogui.keyDown('q')
                pyautogui.keyUp('q')
                pyautogui.keyUp('command')
                time.sleep(10)
        elif SYSTEM == "Windows":
            with pyautogui.hold("alt"):
                for _ in range(len(self.windows)):
                    pyautogui.press('f4')
                    time.sleep(10)
        self.windows = []

    def switch_to_window(self, window):
        try:
            index = self.windows.index(window)
        except ValueError:
            raise ValueError(f"unknown window: {window}")
        key = 'cmd' if SYSTEM == "Darwin" else 'alt'
        with pyautogui.hold(key):
            time.sleep(0.5)
            for i in range(len(self.windows) - index - 1):
                pyautogui.press('tab')
        self.windows.pop(index)
        self.windows.append(window)
