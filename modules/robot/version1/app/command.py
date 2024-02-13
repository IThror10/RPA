from abc import ABC, abstractmethod


class Command(ABC):
    def __init__(self, next_command):
        self.next = next_command

    @property
    def next_command(self):
        return self.next

    @abstractmethod
    def __call__(self):
        pass
