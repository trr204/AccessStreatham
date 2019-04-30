import json
import sys
import os

if __name__ == "__main__":

	jsonfilename = sys.argv[1]
	if os.path.exists("elevationSQL.txt"):
		os.remove("elevationSQL.txt")
	fo = open("elevationSQL.txt",'x') #text file to output SQL commands to

	with open(jsonfilename) as json_file:
		data = json.load(json_file)
		for e in data["objects"]:
			fo.write('UPDATE Vertex SET Elevation = ' + str(e["elevation"]) + ' WHERE Latitude = ' + str(e["latitude"]) + ' AND Longitude = ' + str(e["longitude"])+'\n');