import requests
import bs4
import sys
import os
import time

BASE_URL = 'https://libraries.io'
JARS_FOLDER = os.path.abspath(os.path.join(os.path.dirname(__file__), 'jars'))
TOO_MANY_REQUESTS = 429
MAX_ATTEMPT = 10

STATUS_SUCCESS                 = 0
STATUS_LIMIT_REACHED           = 1
STATUS_DOWNLOAD_URL_NOT_FOUND  = 2
STATUS_ALREADY_DOWNLOADED      = 3
STATUS_TRAVERSAL_ATTACK        = 4
STATUS_EXCEPTION               = 5


class Downloader:
    def __init__(self):
        if not os.path.exists(JARS_FOLDER): os.makedirs(JARS_FOLDER)

    def get_req_data(self, url, level=0):
        attempt = 1
        r = requests.get(url)
        while r.status_code == TOO_MANY_REQUESTS:
            if attempt > MAX_ATTEMPT: return None
            #sleep_time = attempt * 30
            sleep_time = 30
            self.print_msg(level, f"Limit Reached - Sleeping for {sleep_time} seconds - {attempt}/{MAX_ATTEMPT}")
            time.sleep(sleep_time)
            attempt += 1
            r = requests.get(url)

        return r


    def print_msg(self, level, msg):
        pad = "    " * level
        print(f"{pad}{msg}")


    def download(self, start, end):
        for i in range(start, end+1):
            print(f"Page {i}/{end}")
            r = self.get_req_data(f"{BASE_URL}/search?languages=Java&order=desc&page={i}&platforms=Maven&sort=rank")
            if r == None:
                self.print_msg(0, "FATAL ERROR")
                sys.exit(1)

            soup = bs4.BeautifulSoup(r.text, features="lxml")
            for proj in soup.find_all("div", {"class": "project"}):
                status = self.process(proj)
                if   status == STATUS_LIMIT_REACHED:            self.print_msg(2, "SKIPPED - LIMIT_REACHED")
                elif status == STATUS_DOWNLOAD_URL_NOT_FOUND:   self.print_msg(2, "SKIPPED - DOWNLOAD_URL_NOT_FOUND")
                elif status == STATUS_ALREADY_DOWNLOADED:       self.print_msg(2, "SKIPPED - ALREADY_DOWNLOADED")
                elif status == STATUS_TRAVERSAL_ATTACK:         self.print_msg(2, "SKIPPED - TRAVERSAL_ATTACK")
                elif status == STATUS_EXCEPTION:                self.print_msg(2, "SKIPPED - STATUS_EXCEPTION")



    def process(self, proj):
        a = proj.find('a', href=True)
        self.print_msg(1, f"Downloading {a.get_text()}")
        project_url = f"{BASE_URL}{a['href']}"
        r = self.get_req_data(project_url, 2)

        if r == None:
            return STATUS_LIMIT_REACHED

        try:
            soup = bs4.BeautifulSoup(r.text, features="lxml")
            links = soup.find("p", {"class": "project-links"})

            jar_url = None

            for a in links.find_all('a', href=True):
                if a.get_text() == "Download":
                    jar_url = a['href']
                    break

            if jar_url == None:
                return STATUS_DOWNLOAD_URL_NOT_FOUND

            path = os.path.join(JARS_FOLDER, os.path.basename(jar_url))
            if os.path.exists(os.path.abspath(path)):
                return STATUS_ALREADY_DOWNLOADED

            if os.path.dirname(os.path.abspath(path)) != JARS_FOLDER:
                return STATUS_TRAVERSAL_ATTACK

            r = self.get_req_data(jar_url, 2)
            if r == None:
                return STATUS_LIMIT_REACHED

            with open(os.path.abspath(path), 'wb') as f:
                f.write(r.content)

        except Exception as e:
            return STATUS_EXCEPTION

        return STATUS_SUCCESS


def main():
    downloader = Downloader()
    downloader.download(1, 2)


if __name__ == "__main__":
    main()
