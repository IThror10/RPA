class State:
    def __init__(self):
        self.variables = {}
        self.output = {}
        self.funcs ={}

    def init_variable(self, name, value):
        if isinstance(value, str) and value.startswith('"') and value.endswith('"'):
            value = value[1:-1]
        self.variables[name] = value

    def init_func(self, name, command):
        if name in self.funcs:
            raise ValueError(f"Redefinition of function {name}")
        self.funcs[name] = command

    def get_func(self, name):
        return self.funcs[name]

    def clear_funcs(self):
        self.funcs = {}

    def add_result(self, name, value=None):
        if value is None:
            self.output[name] = self.variables[name]
        elif value.startswith('"') and value.endswith('"'):
            self.output[name] = value[1:-1]
        elif value.isdigit():
            self.output[name] = value
        else:
            self.output[name] = self.variables[value]

    def get_variable(self, name):
        return self.variables[name]

    def clear(self):
        self.variables = {}
        self.output = {}

    def get_output(self):
        return self.output
