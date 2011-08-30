<%@ LANGUAGE = JScript %>

<%
Response.Write(main());

function main() {
	var boardType = Request.QueryString("boardType");
	var timeTaken = Request.QueryString("timeTaken");
	var name = Request.QueryString("name");
	var dateRecorded = Request.QueryString("dateRecorded");

	if (boardType == "" || timeTaken == "" || name == "" || dateRecorded == "") {
	   return;
	}

	//read top scores from XML file
	//NOTE: DOES NOT HANDLE CONCURRENT ACCESSES TO THE FILE YET
	filename = "top_scores.xml";

	var xml = Server.CreateObject("Microsoft.XMLDOM");
	xml.async = false;
	xml.load(Server.MapPath(".") + "\\top_scores.xml");

	var numScoresRecorded = xml.selectSingleNode("topScores/numberScoresRecorded").text;
	var rank = -1;
	var scoresOnBoard = xml.selectNodes("topScores/" + boardType + "/score");
	var scoreBefore = null;

	var notFound = true;
	for (var i = 0; i < scoresOnBoard.length && notFound; i++) {
		var currentScore = scoresOnBoard[i].selectSingleNode("timeTaken").text;
		if (parseInt(timeTaken) < parseInt(currentScore)) {
			rank = i;
			notFound = false;
		}
		scoreBefore = scoresOnBoard[i];
	}

	var lastScore = false;
	if (notFound && rank == -1) {
		if (i < numScoresRecorded - 1) {
			rank = i + 1;
			notFound = false;
			lastScore = true;
		}
		else if (i <= numScoresRecorded) {
			rank = i;
			notFound = false;
			lastScore = true;
		}
	}

	if (rank > numScoresRecorded || rank == -1 || (scoresOnBoard.length != 0 && notFound)) {
	   return;
	}

	var newScore = xml.createElement("score");
	var timeTakenNode = xml.createElement("timeTaken");
	timeTakenNode.text = timeTaken;
	var nameNode = xml.createElement("name");
	nameNode.text = name;
	var dateRecordedNode = xml.createElement("dateRecorded");
	dateRecordedNode.text = dateRecorded;
	newScore.appendChild(timeTakenNode);
	newScore.appendChild(nameNode);
	newScore.appendChild(dateRecordedNode);

	var board = xml.selectSingleNode("topScores/" + boardType);
	if (rank == 0 && scoresOnBoard.length == 0) {
		board.appendChild(newScore);
	} else if (rank == board.childNodes.length || lastScore) {
		board.appendChild(newScore);
	}else {
		board.insertBefore(newScore, scoreBefore);
	}

	xml.save (Server.mappath(".") + "\\top_scores.xml")

	return xml.xml;
}

%>
