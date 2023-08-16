import json
import sys

class Printer:
    def print(self, s, level=0): 
        print(self.format(s, level))

    def print_title(self, s, level=0):
        print(self.format(f'[**] {s}', level))

    def print_ok(self, s, level=0):
        print(self.format(f'[OK] {s}', level))

    def print_error(self, s, level=0):
        print(self.format(f'[!!] {s}', level))

    def format(self, s, level=0):
        pad = ""
        if level > 0: pad = "    "*level
        return f"{pad}{s}"

def main():
    if len(sys.argv) < 2:
        print("usage: %s <result.json>" % sys.argv[0])
        sys.exit(1)
      
    json_data = json.loads(open(sys.argv[1]).read())
    printer = Printer()
    sep = "-"* 100
    output = ""
    for i, trace in enumerate(json_data):
        output += printer.format(f"Trace {i}", 2) + "\n"
        last_location = None
        for step in trace:
            for location, code in step.items():
                if last_location != location:
                    output += printer.format(f"at {location}:", 3) + "\n"
                    last_location = location
                output += printer.format(f"{code}", 4) + "\n"
        output += printer.format(sep, 2) + "\n"
    print(output)        
        

if __name__ == '__main__':
    main()
