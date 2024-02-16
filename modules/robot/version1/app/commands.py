import pyautogui
import pyperclip
import time

from version1.app.utils import prepare_int, prepare_string, prepare_float, prepare_key, prepare_value, check_variable
from version1.app.command import Command
from version1.app import main_state


WIDTH, HEIGHT = pyautogui.size()


class PressCommand(Command):
    def __init__(self, next_command, args):
        super().__init__(next_command)
        if len(args) != 1:
            raise ValueError("wrong arguments' number for press")
        self.key = prepare_key(args[0])

    def __call__(self):
        pyautogui.press(self.key())


class ScrollCommand(Command):
    def __init__(self, next_command, args):
        super().__init__(next_command)
        if len(args) != 2 and (args[0] != 'up' or args[0] != 'down'):
            raise ValueError("inappropriate arguments for scroll")
        self.x = prepare_int(args[1])
        self.mult = 1 if args[0] == 'up' else -1

    def __call__(self):
        pyautogui.scroll(self.x() * self.mult)


class WriteCommand(Command):
    def __init__(self, next_command, args):
        super().__init__(next_command)
        if len(args) != 1:
            raise ValueError("inappropriate arguments' number for write")
        self.text = prepare_string(args[0])

    def __call__(self):
        pyautogui.write(self.text())


class ClickCommand(Command):
    def __init__(self, next_command, args):
        super().__init__(next_command)
        self.x = None
        self.y = None
        self.button = "left"
        if len(args) == 1 and (args[0] == "left" or args[0] == "right"):
            self.button = args[0]
        elif len(args) == 2:
            self.x = prepare_float(args[0])
            self.y = prepare_float(args[1])
        elif len(args) == 3 and (args[0] == "left" or args[0] == "right"):
            self.button = args[0]
            self.x = prepare_float(args[1])
            self.y = prepare_float(args[2])
        elif len(args) != 0:
            raise ValueError("inappropriate arguments for click")

    def __call__(self):
        if self.x is None:
            pyautogui.click(button=self.button)
        else:
            pyautogui.click(x=int(self.x() * WIDTH), y=int(self.y() * HEIGHT), button=self.button)


class MoveCommand(Command):
    def __init__(self, next_command, args):
        super().__init__(next_command)
        if len(args) != 2:
            raise ValueError("wrong arguments' number for move")
        self.x = prepare_float(args[0])
        self.y = prepare_float(args[1])

    def __call__(self):
        pyautogui.moveTo(int(self.x() * WIDTH), int(self.y() * HEIGHT))


class HotkeyCommand(Command):
    def __init__(self, next_command, args):
        super().__init__(next_command)
        if len(args) == 0:
            raise ValueError("missing arguments for hotkey")
        self.keys = list(map(lambda x: prepare_key(x), args))

    def __call__(self):
        pyautogui.hotkey(*map(lambda x: x(), self.keys))


class AssignmentCommand(Command):
    def __init__(self, next_command, args):
        super().__init__(next_command)
        if len(args) != 3:
            raise ValueError("wrong arguments for assignment operator")
        check_variable(args[0])
        self.variable = args[0]
        self.value = prepare_value(args[-1])

    def __call__(self):
        main_state.init_variable(self.variable, self.value())


class OutputCommand(Command):
    def __init__(self, next_command, args):
        super().__init__(next_command)
        if len(args) == 0:
            raise ValueError("missing arguments for output")
        if len(args) != 1 and len(args) != 2:
            raise ValueError("wrong arguments' number for output")
        self.value = prepare_value(args[1]) if len(args) == 2 else lambda: None
        self.name = args[0]
        check_variable(self.name)

    def __call__(self):
        main_state.add_result(self.name, self.value())


class SleepCommand(Command):
    def __init__(self, next_command, args):
        super().__init__(next_command)
        if len(args) != 1:
            raise ValueError("wrong arguments' number for sleep")
        self.ms = prepare_int(args[0])

    def __call__(self):
        time.sleep(self.ms() / 1000)


class PutCommand(Command):
    def __init__(self, next_command, args):
        super().__init__(next_command)
        if len(args) != 2:
            raise ValueError("wrong arguments' number for put")
        self.variable = args[1]
        check_variable(self.variable)

    def __call__(self):
        main_state.init_variable(self.variable, pyperclip.paste().splitlines()[-1])


class HoldingCommand(Command):
    def __init__(self, next_command, args, block):
        super().__init__(next_command)

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
            self.keys.append(prepare_key(args[i]))

    def __call__(self):
        if self.button is not None and self.x is not None:
            pyautogui.mouseDown(
                button=self.button, x=int(self.x() * WIDTH), y=int(self.y() * HEIGHT)
            )
        elif self.button is not None:
            pyautogui.mouseDown(button=self.button)
        for key in self.keys:
            pyautogui.keyDown(key())
        self.interpreter.execute()
        if self.button is not None:
            pyautogui.mouseUp(button=self.button)
        for key in self.keys:
            pyautogui.keyUp(key())


class FunctionCallCommand(Command):
    def __init__(self, next_command, args):
        super().__init__(next_command)
        if len(args) != 1:
            raise ValueError("wrong arguments' number for do")
        self.command = lambda: main_state.get_func(args[0])

    def __call__(self):
        self.command().execute()
