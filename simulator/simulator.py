import time
from datetime import datetime
from random import randrange
from itertools import cycle
from config import LOG_FILE_PATH
from config import LOG_SOURCE

# TODO: Napisati smislenije poruke
normal_log_messages = [
    "INFO - normal poruka 1",
    "INFO - normal poruka 2",
    "INFO - normal poruka 3",
    "INFO - normal poruka 4",
    "INFO - normal poruka 5",
    "SUCCESS - normal poruka 1",
    "SUCCESS - normal poruka 2",
    "SUCCESS - normal poruka 3",
    "SUCCESS - normal poruka 4",
    "SUCCESS - normal poruka 5",
    "INFO - 1234567890"
]

# TODO: Napisati smislenije poruke
attack_log_messages = [
    "ERROR - attack poruka 1",
    "ERROR - attack poruka 2",
    "ERROR - attack poruka 3",
    "ERROR - attack poruka 4",
    "ERROR - attack poruka 5",
    "WARN - failed login, username: username",
    "WARN - failed login, username: username",
    "WARN - attack poruka 3",
    "WARN - attack poruka 4",
    "WARN - failed login, username: username2",
]

class State:
    def handle(self):
        pass

def write_to_log_file(msg):
    with open(LOG_FILE_PATH, "a") as log:
        log.write(msg + '\n')


class NormalState(State):
    def handle(self):
        print("--- State: Normal ---")

        for i in range(randrange(10)):
            msg = normal_log_messages[randrange(len(normal_log_messages))]
            msg = str(datetime.now()) + " - " + LOG_SOURCE + " - " + msg + " - " + str(i % 7 + 1)
            print(msg)
            write_to_log_file(msg)

            time.sleep(1)

class AttackState(State):
    def handle(self):
        print("--- State: Attack ---")

        for i in range(randrange(10)):
            msg = attack_log_messages[randrange(len(attack_log_messages))]
            msg = str(datetime.now()) + " - " + LOG_SOURCE + " - " + msg + " - " + str(i % 7 + 1)
            print(msg)
            write_to_log_file(msg)

            time.sleep(1)


def simulate(states):
    print('== Simulation started ==')

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
    
    print('Log file path: ' + LOG_FILE_PATH)

    simulate(states)
