from mq_communication import RabbitMq

class Stats:
    def __init__(self):
        self.rabbit_mq = RabbitMq(self)
        self.count = 0

    def receive_msg(self, message):
        if message != "gata":
            self.count = self.count + 1
        else:
            print(f"Pentru aceasta licitatie s-au trimis {self.count} mesaje")
            with open("logs.txt", "a") as logFile:
                logFile.write(f"Pentru aceasta licitatie s-au trimis {self.count} mesaje")
            exit(0)
        # print(f"am primit mesajul: {message}")


if __name__ == '__main__':
    print('A pornit Stats.py')
    stats = Stats()
    while True:
        stats.rabbit_mq.receive_message()
