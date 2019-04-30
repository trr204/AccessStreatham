import xml.etree.ElementTree as parse
import sys
import os



if __name__ == "__main__":

	edgefilename = sys.argv[1]
	vertexfilename = sys.argv[2]
	nodes = []
	edgeroot = parse.parse(edgefilename)
	vertexroot = parse.parse(vertexfilename)
	edgedoc = edgeroot.getroot()
	vertexdoc = vertexroot.getroot()
	if os.path.exists("functions.txt"):
		os.remove("functions.txt")
	fo = open("functions.txt",'x') #text file to output SQL commands to

	if os.path.exists("coordinates.txt"):
		os.remove("coordinates.txt")
	co = open("coordinates.txt",'x') #text file to output JSON objects to
	co.write('{"objects":[{"locations":[')

	for e in edgeroot.iterfind('way'):
		valid = False
		for t in e.iterfind('tag'):
			if t.attrib["k"] == "highway":
				valid = True
		if valid == True:
			for n in e.iterfind('nd'):
				nodes.append(n.attrib["ref"])


	# for v in root.iterfind('node'):
	# 	if v.attrib["id"] in nodes:
	# 		fo.write('populateVertexData(db, "'+v.attrib["id"]+'", "'+v.attrib["lat"]+'", "'+v.attrib["lon"]+'");\n') #extract node data from xml
	nodeCount = 0
	for v in vertexroot.iterfind('node'):
			if v.attrib["id"] in nodes:
				nodeCount += 1
				fo.write('INSERT INTO Vertex (OsmId, Latitude, Longitude) VALUES (\''+v.attrib["id"]+'\', '+v.attrib["lat"]+', '+v.attrib["lon"]+')\n') #extract node data from xml
				if (nodeCount%100 == 1):
					co.write('{"latitude":' + v.attrib["lat"] + ',"longitude":' + v.attrib["lon"] + '}\n' )
				else:
					co.write(',{"latitude":' + v.attrib["lat"] + ',"longitude":' + v.attrib["lon"] + '}\n' )
				if (nodeCount%100 == 0):
					co.write(']},\n{"locations":[')
	co.write(']}]}')
	for e in edgeroot.iterfind('way'):
		valid = False
		stairs = 0
		for t in e.iterfind('tag'):
			if t.attrib["k"] == "highway":
				valid = True
				if t.attrib["v"] == "steps":
					stairs = 1
		if valid == True:
			i = 0
			vertex1 = ''
			vertex2 = ''
			j = 0
			edge_vertex_join = []		
			for n in e.iterfind('nd'):
				j+=1
			for n in e.iterfind('nd'):
				if i == 0:
					vertex1 = n.attrib["ref"]
				if i == (j-1):
					vertex2 = n.attrib["ref"]
				edge_vertex_join.append('INSERT INTO Edge_Vertex_Association (EdgeId, VertexId, VertexPos) VALUES ((SELECT EdgeId FROM Edge WHERE OsmId = \''+e.attrib["id"]+'\'), (SELECT VertexId FROM Vertex WHERE OsmId = \''+n.attrib["ref"]+'\'),'+str(i)+')\n') #extract vertex-edge association data
				i+=1
			fo.write('INSERT INTO Edge (OsmId, StartVertexId, EndVertexId, Stairs) VALUES (\''+e.attrib["id"]+'\',(SELECT VertexId FROM Vertex WHERE OsmId = \''+vertex1+'\'), (SELECT VertexId FROM Vertex WHERE OsmId = \''+vertex2+'\'), '+str(stairs)+')\n') #extract edge data
			for j in edge_vertex_join:
				fo.write(j);