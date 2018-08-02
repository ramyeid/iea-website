import argparse

parser = argparse.ArgumentParser()
parser.add_argument("destination_file")
args = parser.parse_args()
destination_file = args.destination_file

def sleep(delay):
    with open(destination_file, "a") as file:
        file.write("sleep:{}\n".format(delay))
