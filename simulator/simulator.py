import time
from random import randrange
from itertools import cycle

normal_log_messages = [
    "normal poruka 1",
    "normal poruka 2",
    "normal poruka 3",
    "normal poruka 4",
    "normal poruka 5",
]

attack_log_messages = [
    "attack poruka 1",
    "attack poruka 2",
    "attack poruka 3",
    "attack poruka 4",
    "attack poruka 5",
]

class State:
    def handle(self):
        pass


class NormalState(State):
    def handle(self):
        print("--- State: Normal ---")

        for i in range(randrange(10)):
            with open("simulator_log.txt", "a") as log:
                msg = normal_log_messages[randrange(len(normal_log_messages))]
                print(msg)
                log.write(msg + '\n')

            time.sleep(1)

class AttackState(State):
    def handle(self):
        print("--- State: Attack ---")

        for i in range(randrange(10)):
            with open("simulator_log.txt", "a") as log:
                msg = attack_log_messages[randrange(len(attack_log_messages))]
                print(msg)
                log.write(msg + '\n')

            time.sleep(1)


def simulate(states):
    # Beskonacna petlja koja prolazi kroz sva stanja
    for state in cycle(states):
        state.handle()
        #time.sleep(5)


if __name__ == '__main__':
    states = [
        NormalState(),
        NormalState(),
        AttackState(),
        NormalState(),
        AttackState(),
        AttackState(),
        NormalState()
    ]
    
    simulate(states)
