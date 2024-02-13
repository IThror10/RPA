import pyautogui
import pyperclip
import numpy as np
import cv2
import time

from .utils import prepare_int, prepare_string, prepare_float, check_key, get_image_center, prepare_value, check_variable, get_binary_file
from .command import Command
from . import main_state
from .system_info import SYSTEM


WIDTH, HEIGHT = pyautogui.size()


class PressCommand(Command):
    def __init__(self, next_command, line, args):
        super().__init__(next_command, line)
        if len(args) != 1:
            raise ValueError("wrong arguments' number for press")
        self.key = prepare_string(args[0])

    def __call__(self):
        key = self.key()
        check_key(key, self.line)
        try:
            pyautogui.press(key)
        except Exception:
            raise ValueError(f"error while executing line {self.line}")


class ScrollCommand(Command):
    def __init__(self, next_command, line, args):
        super().__init__(next_command, line)
        if len(args) != 2 and (args[0] != 'up' or args[0] != 'down'):
            raise ValueError("inappropriate arguments for scroll")
        self.x = prepare_int(args[1])
        self.mult = 1 if args[0] == 'up' else -1

    def __call__(self):
        try:
            pyautogui.scroll(self.x() * self.mult)
        except Exception:
            raise ValueError(f"error while executing line {self.line}")


class WriteCommand(Command):
    def __init__(self, next_command, line, args):
        super().__init__(next_command, line)
        if len(args) != 1:
            raise ValueError("inappropriate arguments' number for write")
        self.text = prepare_string(args[0])

    def __call__(self):
        try:
            pyautogui.write(self.text())
        except KeyError as e:
            raise ValueError(f"line {self.line}: {e.args[0]}")
        except:
            raise ValueError(f"error while executing line {self.line}")


class ClickCommand(Command):
    def __init__(self, next_command, line, args):
        super().__init__(next_command, line)
        self.x = None
        self.y = None
        self.image_name = None
        self.text_name = None
        self.button = "left"
        if (len(args) == 2 or len(args) == 3) and args[0] == "image":
            self.image_name = prepare_string(args[-1])
            self.button = "left" if len(args) == 2 else args[1]
        elif (len(args) == 2 or len(args) == 3) and args[0] == "text":
            self.text_name = prepare_string(args[-1])
            self.button = "left" if len(args) == 2 else args[1]
        elif len(args) == 2:
            self.x = prepare_float(args[0])
            self.y = prepare_float(args[1])
        elif (len(args) == 3 or len(args) == 1) and (args[0] == "left" or args[0] == "right"):
            self.button = args[0]
            self.x = None if len(args) == 1 else prepare_float(args[1])
            self.y = None if len(args) == 1 else prepare_float(args[2])
        elif len(args) != 0:
            raise ValueError("inappropriate arguments for click")

    def __call__(self):
        if self.image_name:
            image = main_state.get_image(self.image_name())
            x, y = get_image_center(image)
            pyautogui.click(x=x, y=y, button=self.button)
        elif self.text_name is not None:
            text = self.text_name()
            x_min, y_min, x_max, y_max = main_state.get_word_position(text)
            print(x_min, y_min, x_max, y_max)
            print((x_max + x_min) / 2, (y_max + y_min) / 2)
            pyautogui.click(x=((x_max + x_min) / 2) * WIDTH, y=((y_max + y_min) / 2) * HEIGHT, button=self.button)
        elif self.x is None:
            pyautogui.click(button=self.button)
        else:
            pyautogui.click(x=int(self.x() * WIDTH), y=int(self.y() * HEIGHT), button=self.button)


class MoveCommand(Command):
    def __init__(self, next_command, line, args):
        super().__init__(next_command, line)
        if len(args) != 2:
            raise ValueError("wrong arguments' number for move")
        self.image_name = None
        self.text = None
        if args[0] == 'image':
            self.image_name = prepare_string(args[1])
        elif args[0] == 'text':
            self.text = prepare_string(args[1])
        else:
            self.x = prepare_float(args[0])
            self.y = prepare_float(args[1])

    def __call__(self):
        if self.image_name is not None:
            image_array = main_state.get_image(self.image_name())
            x, y = get_image_center(image_array)
            pyautogui.moveTo(x=x, y=y)
        elif self.text is not None:
            text = self.text()
            x_min, y_min, x_max, y_max = main_state.get_word_position(text)
            print(x_min, y_min, x_max, y_max)
            print((x_max + x_min) / 2, (y_max + y_min) / 2)
            pyautogui.moveTo(x=((x_max + x_min) / 2) * WIDTH, y=((y_max + y_min) / 2) * HEIGHT)
        else:
            pyautogui.moveTo(self.x() * WIDTH, self.y() * HEIGHT)


class HotkeyCommand(Command):
    def __init__(self, next_command, line, args):
        super().__init__(next_command, line)
        if len(args) == 0:
            raise ValueError("missing arguments for hotkey")
        self.keys = list(map(lambda x: prepare_string(x), args))

    def __call__(self):
        # pyautogui.hotkey(*map(lambda x: x(), self.keys))
        for key in self.keys:
            curr = key()
            check_key(curr, self.line)
            pyautogui.keyDown(curr)
        for key in self.keys:
            pyautogui.keyUp(key())


class AssignmentCommand(Command):
    def __init__(self, next_command, line, args):
        super().__init__(next_command, line)
        if len(args) != 3:
            raise ValueError("wrong arguments for assignment operator")
        check_variable(args[0])
        self.variable = args[0]
        self.value = prepare_value(args[-1])

    def __call__(self):
        main_state.init_variable(self.variable, self.value())


class OutputCommand(Command):
    def __init__(self, next_command, line, args):
        super().__init__(next_command, line)
        if len(args) == 0:
            raise ValueError("missing arguments for output")
        if args[0] == "file":
            if len(args) != 3:
                raise ValueError("wrong arguments' number for output")
            self.value = lambda: get_binary_file(prepare_string(args[-1])())
            self.name = args[1]
        else:
            if len(args) != 1 and len(args) != 2:
                raise ValueError("wrong arguments' number for output")
            self.value = prepare_value(args[1]) if len(args) == 2 else lambda: None
            self.name = args[0]
        check_variable(self.name)

    def __call__(self):
        main_state.add_result(self.name, self.value())


class SleepCommand(Command):
    def __init__(self, next_command, line, args):
        super().__init__(next_command, line)
        if len(args) != 1:
            raise ValueError("wrong arguments' number for sleep")
        self.ms = prepare_int(args[0])

    def __call__(self):
        time.sleep(self.ms() / 1000)


class PutCommand(Command):
    def __init__(self, next_command, line, args):
        super().__init__(next_command, line)
        if len(args) != 2:
            raise ValueError("wrong arguments' number for put")
        self.variable = args[1]
        check_variable(self.variable)

    def __call__(self):
        main_state.init_variable(self.variable, pyperclip.paste().splitlines()[-1])


class HoldingCommand(Command):
    def __init__(self, next_command, line, args, block):
        super().__init__(next_command, line)

        if args[-1].lower() != 'do':
            raise ValueError("Missing do in holding construction")
        if len(args) < 2:
            raise ValueError("missing arguments for holding")

        self.button = None
        self.x, self.y = None, None
        self.interpreter = block
        self.keys = []
        start = 0

        if args[0].startswith('click'):
            self.button = "left"
            start = 1
            click_args = args[0].split(' ')
            i = 1
            if click_args[i].lower() == 'right' or click_args[i].lower() == 'left':
                self.button = click_args[i].lower()
                i += 1
            if i < len(click_args):
                self.x = prepare_float(click_args[i])
                self.y = prepare_float(click_args[i + 1])
            if len(click_args) > 4:
                raise ValueError("too many arguments for click")

        for i in range(start, len(args) - 1):
            self.keys.append(prepare_string(args[i]))

    def __call__(self):
        if self.button is not None and self.x is not None:
            pyautogui.mouseDown(
                button=self.button, x=int(self.x() * WIDTH), y=int(self.y() * HEIGHT)
            )
        elif self.button is not None:
            pyautogui.mouseDown(button=self.button)
        for key in self.keys:
            curr = key()
            check_key(curr, self.line)
            pyautogui.keyDown(curr)
        self.interpreter.execute()
        if self.button is not None:
            pyautogui.mouseUp(button=self.button)
        for key in self.keys:
            pyautogui.keyUp(key())


class FunctionCallCommand(Command):
    def __init__(self, next_command, line, args):
        super().__init__(next_command, line)
        if len(args) != 1:
            raise ValueError("wrong arguments' number for do")
        self.command = lambda: main_state.get_func(args[0])

    def __call__(self):
        self.command().execute()


class OpenCommand(Command):
    def __init__(self, next_command, line, args):
        super().__init__(next_command, line)
        if len(args) != 1:
            raise ValueError("wrong arguments' number for open")
        self.name = prepare_string(args[0])

    def __call__(self):
        if SYSTEM == 'Darwin':
            pyautogui.keyDown('command')
            pyautogui.keyDown('space')
            pyautogui.keyUp('space')
            pyautogui.keyUp('command')
        elif SYSTEM == 'Windows':
            pyautogui.press('win')

        window = self.name()
        pyautogui.write(window)
        pyautogui.press('enter')
        time.sleep(10)
        main_state.open_window(window)


class IfCommand(Command):
    def __init__(self, next_command, line, args, if_command, else_command):
        super().__init__(next_command, line)
        if len(args) != 1:
            raise ValueError("error in if statement syntax")
        self.if_command = if_command
        self.else_command = else_command
        self.condition = lambda: bool(main_state.get_variable(args[0]))

    def __call__(self):
        if self.condition():
            self.if_command.execute()
        else:
            self.else_command.execute()


class SwitchCommand(Command):
    def __init__(self, next_command, line, args):
        super().__init__(next_command, line)
        if len(args) != 1:
            raise ValueError("wrong arguments' number for switch")
        self.name = prepare_string(args[0])

    def __call__(self):
        main_state.switch_to_window(self.name())


class ImageExistsCommand(Command):
    def __init__(self, next_command, line, args):
        super().__init__(next_command, line)
        if len(args) != 1:
            raise ValueError("wrong arguments' number for image_exists")
        self.name = prepare_string(args[0])
        self.result = None

    def __call__(self):
        try:
            image = main_state.get_image(self.name())
        except KeyError as e:
            raise ValueError(f"Line {self.line}: {e.args[0]}")
        except:
            raise ValueError(f"error while executing line {self.line}")
        height, width, _ = image.shape
        screenshot = np.array(pyautogui.screenshot())
        result = cv2.matchTemplate(screenshot, image, cv2.TM_CCOEFF_NORMED)
        min_val, max_val, min_loc, top_left = cv2.minMaxLoc(result)
        self.result = max_val > 0.1
