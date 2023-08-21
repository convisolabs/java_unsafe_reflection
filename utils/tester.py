import json
import sys
import glob
import os
import subprocess
import socket
import threading
import time
import logging
import datetime


class Tester:
    def __init__(self, folder, port):
        self.folder = folder
        self.port = port
        self.current_class = ''
        time_s = datetime.datetime.now().strftime("%d-%m-%Y_%Hh%Mm%Ss")
        logfile = f'/tmp/tester.{time_s}.log'
        logging.basicConfig(filename=logfile, level=logging.INFO)
        print(logfile)


    def start(self):
        self.start_server()
        self.all_classes = self.class_names()
        total = len(self.all_classes)
        for idx, c in enumerate(self.all_classes):
            self.current_class = c
            s = f'[{idx+1} / {total}] Testing {self.current_class}'
            print(s)
            logging.info(s)
            self.run_tests(self.current_class)


    def class_names(self):
        temp_arr = []
        pattern = os.path.join(self.folder, '*.json')
        for f in glob.glob(pattern):
            class_names = self.extract_class_names(f)
            if class_names:
                temp_arr.append(class_names)
        flattened = [element for sublist in temp_arr for element in sublist]
        return sorted(flattened)


    def extract_class_names(self, filename):
        l = set()
        json_data = json.loads(open(filename).read())
        if json_data:
            for trace in json_data:
                keys = list(trace[0].keys())
                if '<init>' in keys[0]:
                    class_name = keys[0].split('.<init>')[0]
                    l.add(class_name)
        return list(l)


    def start_server(self):
        server = threading.Thread(target=self.do_start_server, args=(None,))
        server.start()


    def do_start_server(self, args):
        print(f"Starting server at 127.0.0.1:{self.port}")
        server = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
        server.bind(('127.0.0.1', int(self.port)))
        server.listen(5)
        while True:
            client, addr = server.accept()
            logging.info(f'{self.current_class} -------------------> CONNECTED')
            client.close()


    def run_tests(self, class_name):
        self.run_cmd(['./run.sh', class_name, f"/tmp/{class_name}.write"])
        self.run_cmd(['./run.sh', class_name, f"$(uname>/tmp/{class_name}.cmd)"])
        self.run_cmd(['./run.sh', class_name, "file:///etc/issue"])
        self.run_cmd(['./run.sh', class_name, "/etc/issue"])

        schemes = ['http', 'ftp', 'ldap', 'ssh', 'redis']
        for scheme in schemes:
            self.run_cmd(['./run.sh', class_name, f"{scheme}://127.0.0.1:{self.port}/?x={class_name}"])

        schemes = [
            'jdbc:mysql', 'jdbc:postgresql', 'jdbc:sqlserver',
            'jdbc:oracle', 'jdbc:sqlite', 'jdbc:h2',
            'mongodb://', 'mongodb+srv://'
        ]
        for scheme in schemes:
            self.run_cmd(['./run.sh', class_name, f"{scheme}://127.0.0.1:{self.port}"])


    def run_cmd(self, cmd):
        cmd_s = ' '.join(cmd)
        print(cmd_s)
        logging.info(cmd_s)
        with subprocess.Popen(cmd, stdout=subprocess.PIPE, stderr=subprocess.PIPE, shell=False) as process:
            try:
                stdout, stderr = process.communicate(timeout=5)
                logging.info(stdout.decode())
                logging.info(stderr.decode())
            except subprocess.TimeoutExpired:
                logging.info("-------------------> TIMED OUT")
                process.kill()
                stdout, stderr = process.communicate()
                logging.info(stdout.decode())
                logging.info(stderr.decode())
            except Exception:
                process.kill()
                process.wait()


def main():
    if len(sys.argv) < 3:
        print("usage: %s <folder_with_jsons> <server_port>" % sys.argv[0])
        sys.exit(1)
    tester = Tester(sys.argv[1], sys.argv[2])
    tester.start()

if __name__ == '__main__':
    main()

