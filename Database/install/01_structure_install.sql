-- Structure Install for AccessStreatham --

CREATE TABLE Vertex (
	VertexId INTEGER PRIMARY KEY IDENTITY(1,1),
	OsmId VARCHAR(MAX),
	Latitude FLOAT,
	Longitude FLOAT,
	Elevation INTEGER,
	Label VARCHAR(MAX)
)

CREATE TABLE Edge (
	EdgeId INTEGER PRIMARY KEY IDENTITY(1,1),
	OsmId VARCHAR(MAX),
	StartVertexId INTEGER,
	EndVertexId INTEGER,
	FOREIGN KEY (StartVertexId) REFERENCES Vertex(VertexId),
	FOREIGN KEY (EndVertexId) REFERENCES Vertex(VertexId),
	Stairs BIT
)

CREATE TABLE Edge_Vertex_Association (
	AssociationId INTEGER PRIMARY KEY IDENTITY(1,1),
	EdgeId INTEGER,
	VertexId INTEGER,
	VertexPos INTEGER,
	FOREIGN KEY (EdgeId) REFERENCES Edge(EdgeId),
	FOREIGN KEY (VertexId) REFERENCES Vertex(VertexId)
)

CREATE TABLE Incident_Report (
	ReportId INTEGER PRIMARY KEY IDENTITY(1,1),
	VertexId INTEGER,
	Description VARCHAR(MAX),
	ReportedAt VARCHAR(MAX),
	Active BIT,
	FOREIGN KEY (VertexId) REFERENCES Vertex(VertexId)
)

CREATE TABLE Version_Number (
	VersionNum INTEGER
)