import re

from . import main_state
from .utils import get_screenshot, get_binary_image
from .commands import *


command_to_class = {
    "press": PressCommand,
    "scroll": ScrollCommand,
    "write": WriteCommand,
    "click": ClickCommand,
    "move": MoveCommand,
    "hotkey": HotkeyCommand,
    "output": OutputCommand,
    "sleep": SleepCommand,
    "put": PutCommand,
    "do": FunctionCallCommand,
    "open": OpenCommand,
    "switch": SwitchCommand
}


class Interpreter:
    def __init__(self, code: str, line=0):
        commands = code.split('\n')[::-1]
        n = len(commands)
        prev = None
        curr = None
        self.has_error = False
        self.command = prev
        if len(code) == 0:
            return
        block = []
        errors = []
        else_command = Interpreter('')
        n = len(commands)
        for i in range(n):
            args = re.findall(r'[^" ]+|"[^"]*"', commands[i])
            args = list(filter(lambda x: len(x) > 0, args))

            if len(args) == 0 or args[0].strip().startswith('#'):
                continue

            if args[0].startswith("\t"):
                block.append(' '.join(args)[1:])
                continue
            if args[0].lower() == 'def':
                if len(args) != 2:
                    errors.append(f'Line {line + n - i}: error in function declaration')
                try:
                    command = Interpreter('\n'.join(block[::-1]), line=n-i)
                    main_state.init_func(args[1], command)
                except ValueError as e:
                    errors.append(e.args[0])
                block = []
                continue
            if args[0].lower() == "else":
                if len(args) != 1:
                    errors.append(f'Line {line + n - i}: error in else statement syntax')
                try:
                    else_command = Interpreter('\n'.join(block[::-1]), line=n-i)
                except ValueError as e:
                    errors.append(e.args[0])
                block = []
                continue
            try:
                if args[0].lower() == 'holding':
                    command = Interpreter('\n'.join(block[::-1]), line=n-i)
                    curr = HoldingCommand(prev, line + n - i, ' '.join(args[1:]).split(', '), command)
                    block = []
                elif args[0].lower() == 'if':
                    if_command = Interpreter('\n'.join(block[::-1]), line=n-i)
                    curr = IfCommand(prev, line + n - i, args[1:], if_command, else_command)
                    block = []
                elif len(block) != 0:
                    raise ValueError("the code block does not belong to any control construct")
                elif len(args) > 1 and args[1] == "<-":
                    curr = AssignmentCommand(prev, line + n - i, args)
                elif args[0].lower() not in command_to_class:
                    raise ValueError(f"Line {line + n - i}: unknown command {args[0]}")
                else:
                    curr = command_to_class[args[0].lower()](prev, line + n - i, args[1:])
                prev = curr
            except ValueError as e:
                errors.append(e.args[0])

        if len(errors) > 0:
            self.has_error = True
            main_state.clear_funcs()
            raise ValueError('\n'.join(errors[::-1]))
        self.command = prev

    def execute(self, video_flag=False):
        video = []
        if not self.has_error:
            curr = self.command
            while curr is not None:
                curr()
                print(curr)
                if video_flag:
                    width, height, image = get_screenshot()
                    video.append({
                        "width": width,
                        "height": height,
                        "image": get_binary_image(image),
                    })
                print(curr)
                curr = curr.next_command
        main_state.close_all_windows()
        return video
