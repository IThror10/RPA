from .commands import *
from . import main_state

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
    "do": FunctionCallCommand
}


class Interpreter:
    def __init__(self, code: str, line=0):
        commands = code.split('\n')[::-1]
        prev = None
        curr = None
        block = []
        errors = []
        n = len(commands)
        for i in range(n):
            args = commands[i].split(' ')
            args = list(filter(lambda x: len(x) > 0, args))

            if len(args) == 0 or args[0].strip().startswith('#'):
                continue

            if args[0].startswith("\t"):
                if i == 0 or commands[i - 1].strip(' ').startswith("\t"):
                    block.append(' '.join(args)[1:])
                else:
                    block = [' '.join(args)[1:]]
                continue
            if args[0].lower() == 'def':
                if len(args) != 2:
                    errors.append(f'Line {line + n - i}: error in function declaration')
                try:
                    command = Interpreter('\n'.join(block[::-1]), line=n-i)
                    main_state.init_func(args[1], command)
                except ValueError as e:
                    errors.append(e.args[0])
                continue
            try:
                if args[0].lower() == 'holding':
                    try:
                        command = Interpreter('\n'.join(block[::-1]), line=n-i)
                        curr = HoldingCommand(prev, ' '.join(args[1:]).split(', '), command)
                    except ValueError as e:
                        errors.append(e.args[0])
                elif len(args) > 1 and args[1] == "<-":
                    curr = AssignmentCommand(prev, args)
                elif args[0].lower() not in command_to_class:
                    raise ValueError(f"Unknown command {args[0]}")
                else:
                    curr = command_to_class[args[0].lower()](prev, args[1:])
                prev = curr
            except ValueError as e:
                errors.append(f'Line {line + n - i}: {e.args[0]}')

        if len(errors) > 0:
            self.has_error = True
            raise ValueError('\n'.join(errors[::-1]))
        self.has_error = False
        self.command = prev

    def execute(self):
        if not self.has_error:
            curr = self.command
            while curr is not None:
                curr()
                curr = curr.next_command
            main_state.clear_funcs()
