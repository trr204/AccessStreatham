const express = require("express");
const app = express();
const bodyParser = require("body-parser");
const jwt = require("jwt-simple");
const uid = "test";
const secretKey = "imagoose";
const sql = require("mssql");
const config = {
	user: 'questionable',
	password: 'temporary',
	server: 'localhost',
	database: 'AccessStreatham'
}

app.use(bodyParser.json());

app.get('/version',async(req,res) => {
	try {
		console.log("version connection established")
		const tkn = req.headers["x-auth"];
		const tempUid = jwt.decode(tkn, secretKey);
		sql.close();
		let version = {}
		sql.connect(config, function (err) {
			if (err) console.log(err);
			const request = new sql.Request();
			request.query("select * from version_number", function(err, recordset) {
				if (err) console.log(err);
				version = recordset.recordset[0];
				res.status(200).json(version).end();
			});
		});

	} catch(exn) {
		res.status(401).end();
	}
});

app.get('/connectiontest',async(req,res) => {
	console.log("connection established")
	res.status(200).json({'result':'Connection established'}).end();
});

app.get('/graphdata', async(req, res) => {
	try {
		console.log("graphdata connection established")
		const tkn = req.headers["x-auth"];
		const tempUid = jwt.decode(tkn, secretKey);
		sql.close();

		sql.connect(config, function (err) {
			if (err) console.log(err);
			const request = new sql.Request();
			let returndata = {};
			request.query("select * from vertex", function(err, vertexset) {
				if (err) console.log(err);	
				returndata.vertices = vertexset.recordset;
				request.query("select * from edge where startvertexid is not null and endvertexid is not null", function(err, edgeset) {
					if (err) console.log(err);
					returndata.edges = edgeset.recordset;
					request.query("select * from edge_vertex_association where vertexid is not null", function(err, joinset) {
						if (err) console.log(err);
						returndata.joins = joinset.recordset;
						res.status(200).json(returndata).end();
					});
				});	
			});	
		});
	} catch (exn) {
		console.log(exn);
		res.status(401).end();
	}
});

app.get('/incident/list', async(req,res) => {
	try {
		console.log("incident list conection established");
		const tkn = req.headers["x-auth"];
		const tempUid = jwt.decode(tkn, secretKey);
		sql.close();

		sql.connect(config, function(err) {
			if (err) console.log(err);
			const request = new sql.Request();
			request.query("select * from incident_report where active = 1", function(err, recordset) {
				if (err) console.log(err);
				let returndata = {};
				returndata.incidents = recordset.recordset;
				res.status(200).json(returndata).end();
			});
		});
	} catch (exn) {
		console.log(exn);
		res.status(500).end();
	}
});

app.post('/incident/report', async(req, res) => {
	try {
		console.log("incident report connection established");
		const tkn = req.headers["x-auth"];
		const tempUid = jwt.decode(tkn, secretKey);
		sql.close();
		sql.connect(config, function (err) {
			if (err) console.log(err);
			const request = new sql.Request();
			const vertexId = req.body.vertexId;
			request.query("update incident_report set active = 0 where vertexId = " + vertexId, function(err, recordset) {
				if (err) console.log(err);
				const description = req.body.incidentDescription;
				const time = req.body.incidentReportTime;
				console.log("insert for incident report");
				const queryString = "insert into incident_report(vertexId, description, reportedAt, active) values (" + vertexId+", '" + description + "', '" + time + "', 1)";
				request.query(queryString, function(err, recordser) {
					if (err) console.log(err);
					res.status(204).json().end();
				});
			});
		});
	} catch (exn) {
		console.log(exn);
		res.status(500).end();
	}
});

app.delete('/incident/remove/:vertexId', async(req, res) => {
	try {
		console.log("incident report removal connection established");
		const tkn = req.headers["x-auth"];
		const tempUid = jwt.decode(tkn, secretKey);
		sql.close();
		sql.connect(config, function (err) {
			if (err) console.log(err);
			const vertexId = req.params.vertexId;
			const request = new sql.Request();
			request.query("update incident_report set active = 0 where vertexId = " + vertexId, function(err, recordset) {
				if (err) console.log(err);
				res.status(204).json().end();
			});
		});
	} catch (exn) {
		console.log(exn);
		res.status(500).end();
	}

});

app.get('/token', async(req, res) => {
	try {
		console.log("token connection established")
		const tkn = jwt.encode(uid, secretKey);
		res.status(200).json(tkn).end();
	} catch (exn) {
		console.log(exn);
		res.status(500).end();
	}
});

app.listen(3000, () => {
	console.log("listening on port 3000...");
});