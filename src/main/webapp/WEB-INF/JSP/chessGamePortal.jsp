<!DOCTYPE html>
<html lang="en">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<!-- delete later -->
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1.0, user-scalable=no">
<!-- Bootstrap -->
<link href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.2/css/bootstrap.min.css" rel="stylesheet">
<script src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.1/jquery.min.js"></script>
<!-- delete later{end} -->
<script src="https://cdnjs.cloudflare.com/ajax/libs/stomp.js/2.3.3/stomp.js"></script>
<script src="https://cdnjs.cloudflare.com/ajax/libs/interact.js/1.2.3/interact.min.js"></script>
<script src="../../js/chesspieces.js"></script>
<script src="../../js/chessboard.js"></script>
<title>Chess Game</title>
<style>
    @import url(../../css/General.css);
    #header{
        width:100%;
        padding-bottom: 70px;
    }
    
    #title{
        border-style: solid;
        text-align: center;
        border-radius: 33px;
        background-color: #428bca;
        color: white;
    }
    
    .container{
        width: 100%;
    }
    
    #playerBlock legend{
        background-color: #428bca;
        border-radius: 10px 10px;
        padding-left: 20px;
        color: white;
    }
    
    #playerBlock span{
        padding-left: 20px;
    }
    
    #game-info{
        height: 52vh;
    }
    
    #game-info>div{
        height: inherit;
    }
    
    #gameInfoPanel{
        overflow: auto;
    }
    
    #messages {
        margin-top: 20px;
        height: 20vh;
    }
    
    #messages>div{
        height: inherit;
    }
        
    
    #chessboard .inner {
        margin-top: 10%;
    }
/** 
  *   Player's Name boxes 
  *   Duplicate in ChessAplicationPage.html
  *   Todo remove
  */    
     .player-name {
        font-family: "Raleway";
        font-size: 2em;
        margin-top:60px;
        margin-bottom: 30px;
        overflow:hidden;
        height:50px;
        line-height: 50px;
        padding-bottom:60px;
        min-width: 470px;
    }
    
    .player-name .title {
        font-family: "Orbitron";
        border-style: double;
        border-width: 1px;
        background-color: antiquewhite;
        padding:16px 30px 12px 15px;
        border-radius: 15px 0px 0px 15px;
        background-color: #2886d5;    
        box-shadow: 5px 5px 8px #000000;
    }
    
    .player-name .name {
        border-style: double;
        border-left-style: none;
        border-width: 1px;
        padding:12px 30px 12px 15px;
        background-color: #ffffff;
        box-shadow: 5px 5px 8px #000000;
    }
    
</style>
<script>
$(document).ready(function(){
var sourceId="";
var destId="";
var startOfMove=true;
interact('.draggable')
  .draggable({
    // enable inertial throwing
    inertia: true,
    // keep the element within the area of it's parent
    restrict: {
      restriction: '#layer1',
      endOnly: true,
      elementRect: { top: 0, left: 0, bottom: 1, right: 1 }
    },

    // call this function on every dragmove event
    onmove: function (event) {
      var target = event.target,
          // keep the dragged position in the data-x/data-y attributes
          x = (parseFloat(target.getAttribute('data-x')) || 0) + event.dx,
          y = (parseFloat(target.getAttribute('data-y')) || 0) + event.dy;

      var correctY=target.transform.baseVal[0].matrix.f;
      var correctX=target.transform.baseVal[0].matrix.e;
      // translate the element
      target.style.webkitTransform =
      target.style.transform =
        'translate(' + (x+correctX) + 'px, ' + (y+correctY) + 'px)';

      // update the posiion attributes
      target.setAttribute('data-x', x);
      target.setAttribute('data-y', y);
    },
    // call this function on every dragend event
  });


interact('.dropzone').dropzone({
    accept: '.chesspiece',
    overlap: 0.1,
    ondropactivate: function (event) {
    // add active dropzone feedback
    event.target.classList.add('drop-active');
  },
  ondragenter: function (event) {
    var draggableElement = event.relatedTarget,
        dropzoneElement = event.target;

    // feedback the possibility of a drop
    dropzoneElement.classList.add('drop-target');
    draggableElement.classList.add('can-drop');

  },
  ondragleave: function (event) {
    console.log("ondragleave event fired");
    event.target.classList.remove('drop-target');
    event.relatedTarget.classList.remove('can-drop');
    if(startOfMove){
	   console.log("Source:"+event.target.id);
       sourceId=event.target.id;
	   startOfMove=false;
    }
  },
  ondrop: function (event) {
	destId=event.target.id;
	console.log("destination:"+destId);
	startOfMove=true;
    var move=sourceId+"-"+destId;
    stompClient.send("/app/move/${GAME_UUID}",{priority: 9},move);
  },
  ondropdeactivate: function (event) {
    // remove active dropzone feedback
    event.target.classList.remove('drop-active');
    event.target.classList.remove('drop-target');
  }
});
    
    
	var socket=new WebSocket("ws://192.168.1.6:8080/AMCChessGame/app/chessgame/chessgame");
	var stompClient=Stomp.over(socket);
	var subid='234';
	stompClient.connect({},
	        function(frame){
	    	stompClient.subscribe("/user/queue/updates",
	    	        function(message){
	    	    			$('#gameInfoPanel').text(message)},
	    	    			{id:subid}
	    	);
	    	
	    	stompClient.subscribe("/topic/updates",
    	        function(message){
    	    			$('#gameInfoPanel').text(message);
                        createChessBoard(message.body);
                },{id:subid}
    		); 		    	
	});	    	
});
</script>
</head>
<body>
    <div class="container-fluid full-height">
    <div class="row full-height">
        <div class="sidebar-left col-sm-2 hidden-sm hidden-xs">
            <a id="chess-icon" href="https://openclipart.org/detail/18661/-by--18661">
                <img alt="Queen" src="https://openclipart.org/download/18661/portablejim-Chess-tile-Queen-3.svg" />
            </a>    
            <div class="side-menu">
            <ul>
                <li><a href="http://adrianmclaughlin.ie">Message</a></li>
                <li><a class="description" href="#">Save</a></li>
                <li><a class="description" href="#">Quit</a></li>
            </ul>
            </div>
        </div><!-- sidebar-left -->
        <div class="player-name col-sm-10 col-xs-12">
            <div class="col-sm-5"><span class="title">Player:</span><span class="name">Adrian McLaughlin</span></div>
            <div class="col-sm-5"><span class="title">Opponent:</span><span class="name">Adrian McLaughlin</span></div>
        </div>
        <div id="chessboard-surround" class="col-sm-6 col-xs-8">
            <div class="inner col-xs-offset-1 col-xs-11">
        <svg
 xmlns:dc="http://purl.org/dc/elements/1.1/"
 xmlns:cc="http://creativecommons.org/ns#"
 xmlns:rdf="http://www.w3.org/1999/02/22-rdf-syntax-ns#"
 xmlns:svg="http://www.w3.org/2000/svg"
 xmlns="http://www.w3.org/2000/svg"
 xmlns:xlink="http://www.w3.org/1999/xlink"
 xmlns:sodipodi="http://sodipodi.sourceforge.net/DTD/sodipodi-0.dtd"
 xmlns:inkscape="http://www.inkscape.org/namespaces/inkscape"
 width="500"
 height="500"
 id="svg2"
 version="1.1"
 inkscape:version="0.48.4 r9939"
 sodipodi:docname="ChessBoard3.svg">
<style
 id="style3">
.drop-target{
    fill:red!important;
    }
</style>
<defs
 id="defs4" />
<sodipodi:namedview
 id="base"
 pagecolor="#92ffff"
 bordercolor="#666666"
 borderopacity="1.0"
 inkscape:pageopacity="0.90588235"
 inkscape:pageshadow="2"
 inkscape:zoom="1.304"
 inkscape:cx="308.51048"
 inkscape:cy="156.88144"
 inkscape:document-units="px"
 inkscape:current-layer="layer1"
 showgrid="false"
 inkscape:window-width="1280"
 inkscape:window-height="908"
 inkscape:window-x="0"
 inkscape:window-y="0"
 inkscape:window-maximized="1" />
<metadata
 id="metadata7">
<rdf:RDF>
<cc:Work
 rdf:about="">
<dc:format>image/svg+xml</dc:format>
<dc:type
 rdf:resource="http://purl.org/dc/dcmitype/StillImage" />
<dc:title></dc:title>
</cc:Work>
</rdf:RDF>
</metadata>
<g
 inkscape:label="Layer 1"
 inkscape:groupmode="layer"
 id="layer1">
<rect
 style="fill:#e6e6e6;fill-opacity:1;stroke:none"
 id="A8"
 class="dropzone"
 width="62.468605"
 height="62.468605"
 x="1.1230643"
 y="1.4215219"
 inkscape:label="#rect3004" />
<rect
 style="fill:#000000;fill-opacity:1;stroke:none"
 id="B8"
 class="dropzone"
 width="62.468605"
 height="62.468605"
 x="63.610096"
 y="1.4214609"
 inkscape:label="#rect3004-4" />
<rect
 style="fill:#e6e6e6;fill-opacity:1;stroke:none"
 id="C8"
 class="dropzone"
 width="62.468605"
 height="62.468605"
 x="126.07412"
 y="1.4214609"
 inkscape:label="#rect3004-7" />
<rect
 style="fill:#000000;fill-opacity:1;stroke:none"
 id="D8"
 class="dropzone"
 width="62.468605"
 height="62.468605"
 x="188.67366"
 y="1.4214609"
 inkscape:label="#rect3004-4-7" />
<rect
 style="fill:#e6e6e6;fill-opacity:1;stroke:none"
 id="E8"
 class="dropzone"
 width="62.468605"
 height="62.468605"
 x="251.27324"
 y="1.4214609"
 inkscape:label="#rect3004-7-2" />
<rect
 style="fill:#000000;fill-opacity:1;stroke:none"
 id="F8"
 class="dropzone"
 width="62.468605"
 height="62.468605"
 x="313.73727"
 y="1.4214609"
 inkscape:label="#rect3004-4-7-9" />
<rect
 style="fill:#e6e6e6;fill-opacity:1;stroke:none"
 id="G8"
 class="dropzone"
 width="62.468605"
 height="62.468605"
 x="376.20132"
 y="1.4214609"
 inkscape:label="#rect3004-7-2-9" />
<rect
 style="fill:#000000;fill-opacity:1;stroke:none"
 id="H8"
 class="dropzone"
 width="62.468605"
 height="62.468605"
 x="438.52982"
 y="1.4214609"
 inkscape:label="#rect3004-4-7-9-7" />
<rect
 style="fill:#000000;fill-opacity:1;stroke:none"
 id="A7"
 class="dropzone"
 width="62.468605"
 height="62.468605"
 x="1.166445"
 y="63.886425"
 inkscape:label="#rect3004-4-1" />
<rect
 style="fill:#e6e6e6;fill-opacity:1;stroke:none"
 id="B7"
 class="dropzone"
 width="62.468605"
 height="62.468605"
 x="63.630512"
 y="63.886425"
 inkscape:label="#rect3004-7-6" />
<rect
 style="fill:#000000;fill-opacity:1;stroke:none"
 id="C7"
 class="dropzone"
 width="62.468605"
 height="62.468605"
 x="126.23006"
 y="63.886425"
 inkscape:label="#rect3004-4-7-6" />
<rect
 style="fill:#e6e6e6;fill-opacity:1;stroke:none"
 id="D7"
 class="dropzone"
 width="62.468605"
 height="62.468605"
 x="188.82957"
 y="63.886425"
 inkscape:label="#rect3004-7-2-0" />
<rect
 style="fill:#000000;fill-opacity:1;stroke:none"
 id="E7"
 class="dropzone"
 width="62.468605"
 height="62.468605"
 x="251.29366"
 y="63.886425"
 inkscape:label="#rect3004-4-7-9-8" />
<rect
 style="fill:#e6e6e6;fill-opacity:1;stroke:none"
 id="F7"
 class="dropzone"
 width="62.468605"
 height="62.468605"
 x="313.7576"
 y="63.886425"
 inkscape:label="#rect3004-7-2-9-3" />
<rect
 style="fill:#000000;fill-opacity:1;stroke:none"
 id="G7"
 class="dropzone"
 width="62.468605"
 height="62.468605"
 x="376.0864"
 y="63.886425"
 inkscape:label="#rect3004-4-7-9-7-2" />
<rect
 style="fill:#e6e6e6;fill-opacity:1;stroke:none"
 id="H7"
 class="dropzone"
 width="62.468605"
 height="62.468605"
 x="438.52957"
 y="63.886608"
 inkscape:label="#rect3004-7-2-9-5" />
<rect
 style="fill:#e6e6e6;fill-opacity:1;stroke:none"
 id="A6"
 class="dropzone"
 width="62.468605"
 height="62.468605"
 x="1.1345236"
 y="126.38338"
 inkscape:label="#rect3004-71" />
<rect
 style="fill:#000000;fill-opacity:1;stroke:none"
 id="B6"
 class="dropzone"
 width="62.468605"
 height="62.468605"
 x="63.621571"
 y="126.38326"
 inkscape:label="#rect3004-4-3" />
<rect
 style="fill:#e6e6e6;fill-opacity:1;stroke:none"
 id="C6"
 class="dropzone"
 width="62.468605"
 height="62.468605"
 x="126.08566"
 y="126.38326"
 inkscape:label="#rect3004-7-7" />
<rect
 style="fill:#000000;fill-opacity:1;stroke:none"
 id="D6"
 class="dropzone"
 width="62.468605"
 height="62.468605"
 x="188.68513"
 y="126.38326"
 inkscape:label="#rect3004-4-7-98" />
<rect
 style="fill:#e6e6e6;fill-opacity:1;stroke:none"
 id="E6"
 class="dropzone"
 width="62.468605"
 height="62.468605"
 x="251.28471"
 y="126.38326"
 inkscape:label="#rect3004-7-2-8" />
<rect
 style="fill:#000000;fill-opacity:1;stroke:none"
 id="F6"
 class="dropzone"
 width="62.468605"
 height="62.468605"
 x="313.74872"
 y="126.38326"
 inkscape:label="#rect3004-4-7-9-5" />
<rect
 style="fill:#e6e6e6;fill-opacity:1;stroke:none"
 id="G6"
 class="dropzone"
 width="62.468605"
 height="62.468605"
 x="376.21274"
 y="126.38326"
 inkscape:label="#rect3004-7-2-9-33" />
<rect
 style="fill:#000000;fill-opacity:1;stroke:none"
 id="H6"
 class="dropzone"
 width="62.468605"
 height="62.468605"
 x="438.54135"
 y="126.38326"
 inkscape:label="#rect3004-4-7-9-7-9" />
<rect
 style="fill:#000000;fill-opacity:1;stroke:none"
 id="A5"
 class="dropzone"
 width="62.468605"
 height="62.468605"
 x="1.1779044"
 y="188.84828"
 inkscape:label="#rect3004-4-1-9" />
<rect
 style="fill:#e6e6e6;fill-opacity:1;stroke:none"
 id="B5"
 class="dropzone"
 width="62.468605"
 height="62.468605"
 x="63.641956"
 y="188.84828"
 inkscape:label="#rect3004-7-6-5" />
<rect
 style="fill:#000000;fill-opacity:1;stroke:none"
 id="C5"
 class="dropzone"
 width="62.468605"
 height="62.468605"
 x="126.24154"
 y="188.84828"
 inkscape:label="#rect3004-4-7-6-4" />
<rect
 style="fill:#e6e6e6;fill-opacity:1;stroke:none"
 id="D5"
 class="dropzone"
 width="62.468605"
 height="62.468605"
 x="188.84099"
 y="188.84828"
 inkscape:label="#rect3004-7-2-0-3" />
<rect
 style="fill:#000000;fill-opacity:1;stroke:none"
 id="E5"
 class="dropzone"
 width="62.468605"
 height="62.468605"
 x="251.30513"
 y="188.84828"
 inkscape:label="#rect3004-4-7-9-8-7" />
<rect
 style="fill:#e6e6e6;fill-opacity:1;stroke:none"
 id="F5"
 class="dropzone"
 width="62.468605"
 height="62.468605"
 x="313.76913"
 y="188.84828"
 inkscape:label="#rect3004-7-2-9-3-8" />
<rect
 style="fill:#000000;fill-opacity:1;stroke:none"
 id="G5"
 class="dropzone"
 width="62.468605"
 height="62.468605"
 x="376.09781"
 y="188.84828"
 inkscape:label="#rect3004-4-7-9-7-2-2" />
<rect
 style="fill:#e6e6e6;fill-opacity:1;stroke:none"
 id="H5"
 class="dropzone"
 width="62.468605"
 height="62.468605"
 x="438.54105"
 y="188.8484"
 inkscape:label="#rect3004-7-2-9-5-6" />
<rect
 style="fill:#e6e6e6;fill-opacity:1;stroke:none"
 id="A4"
 class="dropzone"
 width="62.468605"
 height="62.468605"
 x="1.2359946"
 y="251.19258"
 inkscape:label="#rect3004-1" />
<rect
 style="fill:#000000;fill-opacity:1;stroke:none"
 id="B4"
 class="dropzone"
 width="62.468605"
 height="62.468605"
 x="63.723072"
 y="251.19246"
 inkscape:label="#rect3004-4-17" />
<rect
 style="fill:#e6e6e6;fill-opacity:1;stroke:none"
 id="C4"
 class="dropzone"
 width="62.468605"
 height="62.468605"
 x="126.18709"
 y="251.19246"
 inkscape:label="#rect3004-7-0" />
<rect
 style="fill:#000000;fill-opacity:1;stroke:none"
 id="D4"
 class="dropzone"
 width="62.468605"
 height="62.468605"
 x="188.7867"
 y="251.19246"
 inkscape:label="#rect3004-4-7-2" />
<rect
 style="fill:#e6e6e6;fill-opacity:1;stroke:none"
 id="E4"
 class="dropzone"
 width="62.468605"
 height="62.468605"
 x="251.38618"
 y="251.19246"
 inkscape:label="#rect3004-7-2-4" />
<rect
 style="fill:#000000;fill-opacity:1;stroke:none"
 id="F4"
 class="dropzone"
 width="62.468605"
 height="62.468605"
 x="313.85025"
 y="251.19246"
 inkscape:label="#rect3004-4-7-9-75" />
<rect
 style="fill:#e6e6e6;fill-opacity:1;stroke:none"
 id="G4"
 class="dropzone"
 width="62.468605"
 height="62.468605"
 x="376.31424"
 y="251.19246"
 inkscape:label="#rect3004-7-2-9-4" />
<rect
 style="fill:#000000;fill-opacity:1;stroke:none"
 id="H4"
 class="dropzone"
 width="62.468605"
 height="62.468605"
 x="438.64291"
 y="251.19246"
 inkscape:label="#rect3004-4-7-9-7-0" />
<rect
 style="fill:#000000;fill-opacity:1;stroke:none"
 id="A3"
 class="dropzone"
 width="62.468605"
 height="62.468605"
 x="1.2793753"
 y="313.65729"
 inkscape:label="#rect3004-4-1-7" />
<rect
 style="fill:#e6e6e6;fill-opacity:1;stroke:none"
 id="B3"
 class="dropzone"
 width="62.468605"
 height="62.468605"
 x="63.743458"
 y="313.65729"
 inkscape:label="#rect3004-7-6-1" />
<rect
 style="fill:#000000;fill-opacity:1;stroke:none"
 id="C3"
 class="dropzone"
 width="62.468605"
 height="62.468605"
 x="126.34298"
 y="313.65729"
 inkscape:label="#rect3004-4-7-6-9" />
<rect
 style="fill:#e6e6e6;fill-opacity:1;stroke:none"
 id="D3"
 class="dropzone"
 width="62.468605"
 height="62.468605"
 x="188.94255"
 y="313.65729"
 inkscape:label="#rect3004-7-2-0-8" />
<rect
 style="fill:#000000;fill-opacity:1;stroke:none"
 id="E3"
 class="dropzone"
 width="62.468605"
 height="62.468605"
 x="251.40657"
 y="313.65729"
 inkscape:label="#rect3004-4-7-9-8-5" />
<rect
 style="fill:#e6e6e6;fill-opacity:1;stroke:none"
 id="F3"
 class="dropzone"
 width="62.468605"
 height="62.468605"
 x="315.40439"
 y="312.89044"
 inkscape:label="#rect3004-7-2-9-3-89" />
<rect
 style="fill:#000000;fill-opacity:1;stroke:none"
 id="G3"
 class="dropzone"
 width="62.468605"
 height="62.468605"
 x="376.19925"
 y="313.65729"
 inkscape:label="#rect3004-4-7-9-7-2-5" />
<rect
 style="fill:#e6e6e6;fill-opacity:1;stroke:none"
 id="H3"
 class="dropzone"
 width="62.468605"
 height="62.468605"
 x="438.64267"
 y="313.65753"
 inkscape:label="#rect3004-7-2-9-5-66" />
<rect
 style="fill:#e6e6e6;fill-opacity:1;stroke:none"
 id="A2"
 class="dropzone"
 width="62.468605"
 height="62.468605"
 x="1.2474997"
 y="376.1543"
 inkscape:label="#rect3004-71-9" />
<rect
 style="fill:#000000;fill-opacity:1;stroke:none"
 id="B2"
 class="dropzone"
 width="62.468605"
 height="62.468605"
 x="63.734547"
 y="376.15424"
 inkscape:label="#rect3004-4-3-2" />
<rect
 style="fill:#e6e6e6;fill-opacity:1;stroke:none"
 id="C2"
 class="dropzone"
 width="62.468605"
 height="62.468605"
 x="126.1986"
 y="376.15424"
 inkscape:label="#rect3004-7-7-7" />
<rect
 style="fill:#000000;fill-opacity:1;stroke:none"
 id="D2"
 class="dropzone"
 width="62.468605"
 height="62.468605"
 x="188.79811"
 y="376.15424"
 inkscape:label="#rect3004-4-7-98-0" />
<rect
 style="fill:#e6e6e6;fill-opacity:1;stroke:none"
 id="E2"
 class="dropzone"
 width="62.468605"
 height="62.468605"
 x="251.39763"
 y="376.15424"
 inkscape:label="#rect3004-7-2-8-7" />
<rect
 style="fill:#000000;fill-opacity:1;stroke:none"
 id="F2"
 class="dropzone"
 width="62.468605"
 height="62.468605"
 x="313.86172"
 y="376.15424"
 inkscape:label="#rect3004-4-7-9-5-2" />
<rect
 style="fill:#e6e6e6;fill-opacity:1;stroke:none"
 id="G2"
 class="dropzone"
 width="62.468605"
 height="62.468605"
 x="376.32565"
 y="376.15424"
 inkscape:label="#rect3004-7-2-9-33-5" />
<rect
 style="fill:#000000;fill-opacity:1;stroke:none"
 id="H2"
 class="dropzone"
 width="62.468605"
 height="62.468605"
 x="438.65445"
 y="376.15424"
 inkscape:label="#rect3004-4-7-9-7-9-6" />
<rect
 style="fill:#000000;fill-opacity:1;stroke:none"
 id="A1"
 class="dropzone"
 width="62.468605"
 height="62.468605"
 x="1.2908347"
 y="438.61914"
 inkscape:label="#rect3004-4-1-9-2" />
<rect
 style="fill:#e6e6e6;fill-opacity:1;stroke:none"
 id="B1"
 class="dropzone"
 width="62.468605"
 height="62.468605"
 x="63.754902"
 y="438.61914"
 inkscape:label="#rect3004-7-6-5-8" />
<rect
 style="fill:#000000;fill-opacity:1;stroke:none"
 id="C1"
 class="dropzone"
 width="62.468605"
 height="62.468605"
 x="126.35448"
 y="438.61914"
 inkscape:label="#rect3004-4-7-6-4-2" />
<rect
 style="fill:#e6e6e6;fill-opacity:1;stroke:none"
 id="D1"
 class="dropzone"
 width="62.468605"
 height="62.468605"
 x="188.95396"
 y="438.61914"
 inkscape:label="#rect3004-7-2-0-3-5" />
<rect
 style="fill:#000000;fill-opacity:1;stroke:none"
 id="E1"
 class="dropzone"
 width="62.468605"
 height="62.468605"
 x="251.41808"
 y="438.61914"
 inkscape:label="#rect3004-4-7-9-8-7-9" />
<rect
 style="fill:#e6e6e6;fill-opacity:1;stroke:none"
 id="F1"
 class="dropzone"
 width="62.468605"
 height="62.468605"
 x="313.88211"
 y="438.61914"
 inkscape:label="#rect3004-7-2-9-3-8-0" />
<rect
 style="fill:#000000;fill-opacity:1;stroke:none"
 id="G1"
 class="dropzone"
 width="62.468605"
 height="62.468605"
 x="376.21066"
 y="438.61914"
 inkscape:label="#rect3004-4-7-9-7-2-2-6" />
<rect
 style="fill:#e6e6e6;fill-opacity:1;stroke:none"
 id="H1"
 class="dropzone"
 width="62.468605"
 height="62.468605"
 x="438.65414"
 y="438.61932"
 inkscape:label="#rect3004-7-2-9-5-6-4" />
<g
 id="whitePawn8"
 class="chesspiece draggable"
 inkscape:label="Layer 1"
 transform="translate(440.95092,-2721.6258)">
<path
 sodipodi:nodetypes="ccccc"
 inkscape:connector-curvature="0"
 id="path5379-3"
 d="m 18.950592,3139.9627 c 8.405541,0.058 16.811086,0.1154 25.216627,0.1731 -3.301343,-7.2511 -11.055111,-13.6126 -9.904034,-21.7535 -2.170121,0 -4.340243,0 -6.510359,0 2.074895,8.0832 -5.868158,14.3869 -8.802234,21.5804 z"
 style="fill:#ffd5d5;fill-opacity:1;stroke:#000000;stroke-width:0.40499824;stroke-linecap:butt;stroke-linejoin:round;stroke-miterlimit:3.5;stroke-opacity:0.98453603;stroke-dasharray:none;stroke-dashoffset:50" />
<path
 inkscape:connector-curvature="0"
 id="rect2994-4"
 d="m 41.239663,3110.1612 c 0,4.7665 -4.360599,8.6304 -9.739672,8.6304 -5.37908,0 -9.739679,-3.8639 -9.739679,-8.6304 0,-4.7664 4.360599,-8.6303 9.739679,-8.6303 5.379073,0 9.739672,3.8639 9.739672,8.6303 z"
 style="fill:#ffd5d5;fill-opacity:1;stroke:#000000;stroke-width:0.40499824;stroke-linecap:butt;stroke-linejoin:round;stroke-miterlimit:3.5;stroke-opacity:0.98453603;stroke-dasharray:none;stroke-dashoffset:50" />
<path
 sodipodi:nodetypes="ccccc"
 inkscape:connector-curvature="0"
 id="rect3002-1"
 d="m 12.11958,3152.6434 c 10.92761,0 27.794506,0.3568 38.722116,0.3568 0,-4.2696 0.971879,-9.5206 -3.131624,-12.8983 -10.92761,0 -21.639245,0 -32.566855,0 -3.779546,2.3072 -3.023637,8.2718 -3.023637,12.5415 z"
 style="fill:#ffd5d5;fill-opacity:1;stroke:#000000;stroke-width:0.31832072;stroke-linecap:butt;stroke-linejoin:round;stroke-miterlimit:3.5;stroke-opacity:0.98453603;stroke-dasharray:none;stroke-dashoffset:50" />
</g>
<g
 transform="translate(373.40799,-2718.2866)"
 id="whitePawn7"
 class="chesspiece draggable"
 inkscape:label="Layer 1">
<path
 sodipodi:nodetypes="ccccc"
 inkscape:connector-curvature="0"
 id="path5379-3-3"
 d="M 18.950592,3139.9627 C 27.356133,3140.0207 35.761678,3140.0781 44.167219,3140.1358 40.865876,3132.8847 33.112108,3126.5232 34.263185,3118.3823 32.093064,3118.3823 29.922942,3118.3823 27.752826,3118.3823 29.827721,3126.4655 21.884668,3132.7692 18.950592,3139.9627 z"
 style="fill:#ffd5d5;fill-opacity:1;stroke:#000000;stroke-width:0.40499824;stroke-linecap:butt;stroke-linejoin:round;stroke-miterlimit:3.5;stroke-opacity:0.98453603;stroke-dasharray:none;stroke-dashoffset:50" />
<path
 inkscape:connector-curvature="0"
 id="rect2994-4-6"
 d="M 41.239663,3110.1612 C 41.239663,3114.9277 36.879064,3118.7916 31.499991,3118.7916 26.120911,3118.7916 21.760312,3114.9277 21.760312,3110.1612 21.760312,3105.3948 26.120911,3101.5309 31.499991,3101.5309 36.879064,3101.5309 41.239663,3105.3948 41.239663,3110.1612 z"
 style="fill:#ffd5d5;fill-opacity:1;stroke:#000000;stroke-width:0.40499824;stroke-linecap:butt;stroke-linejoin:round;stroke-miterlimit:3.5;stroke-opacity:0.98453603;stroke-dasharray:none;stroke-dashoffset:50" />
<path
 sodipodi:nodetypes="ccccc"
 inkscape:connector-curvature="0"
 id="rect3002-1-4"
 d="M 12.11958,3152.6434 C 23.04719,3152.6434 39.914086,3153.0002 50.841696,3153.0002 50.841696,3148.7306 51.813575,3143.4796 47.710072,3140.1019 36.782462,3140.1019 26.070827,3140.1019 15.143217,3140.1019 11.363671,3142.4091 12.11958,3148.3737 12.11958,3152.6434 z"
 style="fill:#ffd5d5;fill-opacity:1;stroke:#000000;stroke-width:0.31832072;stroke-linecap:butt;stroke-linejoin:round;stroke-miterlimit:3.5;stroke-opacity:0.98453603;stroke-dasharray:none;stroke-dashoffset:50" />
</g>
<g
 transform="translate(314.3589,-2719.0535)"
 id="whitePawn6"
 class="chesspiece draggable"
 inkscape:label="Layer 1">
<path
 sodipodi:nodetypes="ccccc"
 inkscape:connector-curvature="0"
 id="path5379-3-1"
 d="M 18.950592,3139.9627 C 27.356133,3140.0207 35.761678,3140.0781 44.167219,3140.1358 40.865876,3132.8847 33.112108,3126.5232 34.263185,3118.3823 32.093064,3118.3823 29.922942,3118.3823 27.752826,3118.3823 29.827721,3126.4655 21.884668,3132.7692 18.950592,3139.9627 z"
 style="fill:#ffd5d5;fill-opacity:1;stroke:#000000;stroke-width:0.40499824;stroke-linecap:butt;stroke-linejoin:round;stroke-miterlimit:3.5;stroke-opacity:0.98453603;stroke-dasharray:none;stroke-dashoffset:50" />
<path
 inkscape:connector-curvature="0"
 id="rect2994-4-7"
 d="M 41.239663,3110.1612 C 41.239663,3114.9277 36.879064,3118.7916 31.499991,3118.7916 26.120911,3118.7916 21.760312,3114.9277 21.760312,3110.1612 21.760312,3105.3948 26.120911,3101.5309 31.499991,3101.5309 36.879064,3101.5309 41.239663,3105.3948 41.239663,3110.1612 z"
 style="fill:#ffd5d5;fill-opacity:1;stroke:#000000;stroke-width:0.40499824;stroke-linecap:butt;stroke-linejoin:round;stroke-miterlimit:3.5;stroke-opacity:0.98453603;stroke-dasharray:none;stroke-dashoffset:50" />
<path
 sodipodi:nodetypes="ccccc"
 inkscape:connector-curvature="0"
 id="rect3002-1-43"
 d="M 12.11958,3152.6434 C 23.04719,3152.6434 39.914086,3153.0002 50.841696,3153.0002 50.841696,3148.7306 51.813575,3143.4796 47.710072,3140.1019 36.782462,3140.1019 26.070827,3140.1019 15.143217,3140.1019 11.363671,3142.4091 12.11958,3148.3737 12.11958,3152.6434 z"
 style="fill:#ffd5d5;fill-opacity:1;stroke:#000000;stroke-width:0.31832072;stroke-linecap:butt;stroke-linejoin:round;stroke-miterlimit:3.5;stroke-opacity:0.98453603;stroke-dasharray:none;stroke-dashoffset:50" />
</g>
<g
 transform="translate(253.00922,-2720.5872)"
 id="whitePawn5"
 class="chesspiece draggable"
 inkscape:label="Layer 1">
<path
 sodipodi:nodetypes="ccccc"
 inkscape:connector-curvature="0"
 id="path5379-3-32"
 d="M 18.950592,3139.9627 C 27.356133,3140.0207 35.761678,3140.0781 44.167219,3140.1358 40.865876,3132.8847 33.112108,3126.5232 34.263185,3118.3823 32.093064,3118.3823 29.922942,3118.3823 27.752826,3118.3823 29.827721,3126.4655 21.884668,3132.7692 18.950592,3139.9627 z"
 style="fill:#ffd5d5;fill-opacity:1;stroke:#000000;stroke-width:0.40499824;stroke-linecap:butt;stroke-linejoin:round;stroke-miterlimit:3.5;stroke-opacity:0.98453603;stroke-dasharray:none;stroke-dashoffset:50" />
<path
 inkscape:connector-curvature="0"
 id="rect2994-4-2"
 d="M 41.239663,3110.1612 C 41.239663,3114.9277 36.879064,3118.7916 31.499991,3118.7916 26.120911,3118.7916 21.760312,3114.9277 21.760312,3110.1612 21.760312,3105.3948 26.120911,3101.5309 31.499991,3101.5309 36.879064,3101.5309 41.239663,3105.3948 41.239663,3110.1612 z"
 style="fill:#ffd5d5;fill-opacity:1;stroke:#000000;stroke-width:0.40499824;stroke-linecap:butt;stroke-linejoin:round;stroke-miterlimit:3.5;stroke-opacity:0.98453603;stroke-dasharray:none;stroke-dashoffset:50" />
<path
 sodipodi:nodetypes="ccccc"
 inkscape:connector-curvature="0"
 id="rect3002-1-7"
 d="M 12.11958,3152.6434 C 23.04719,3152.6434 39.914086,3153.0002 50.841696,3153.0002 50.841696,3148.7306 51.813575,3143.4796 47.710072,3140.1019 36.782462,3140.1019 26.070827,3140.1019 15.143217,3140.1019 11.363671,3142.4091 12.11958,3148.3737 12.11958,3152.6434 z"
 style="fill:#ffd5d5;fill-opacity:1;stroke:#000000;stroke-width:0.31832072;stroke-linecap:butt;stroke-linejoin:round;stroke-miterlimit:3.5;stroke-opacity:0.98453603;stroke-dasharray:none;stroke-dashoffset:50" />
</g>
<g
 transform="translate(188.59204,-2719.0534)"
 id="whitePawn4"
 class="chesspiece draggable"
 inkscape:label="Layer 1">
<path
 sodipodi:nodetypes="ccccc"
 inkscape:connector-curvature="0"
 id="path5379-3-8"
 d="M 18.950592,3139.9627 C 27.356133,3140.0207 35.761678,3140.0781 44.167219,3140.1358 40.865876,3132.8847 33.112108,3126.5232 34.263185,3118.3823 32.093064,3118.3823 29.922942,3118.3823 27.752826,3118.3823 29.827721,3126.4655 21.884668,3132.7692 18.950592,3139.9627 z"
 style="fill:#ffd5d5;fill-opacity:1;stroke:#000000;stroke-width:0.40499824;stroke-linecap:butt;stroke-linejoin:round;stroke-miterlimit:3.5;stroke-opacity:0.98453603;stroke-dasharray:none;stroke-dashoffset:50" />
<path
 inkscape:connector-curvature="0"
 id="rect2994-4-4"
 d="M 41.239663,3110.1612 C 41.239663,3114.9277 36.879064,3118.7916 31.499991,3118.7916 26.120911,3118.7916 21.760312,3114.9277 21.760312,3110.1612 21.760312,3105.3948 26.120911,3101.5309 31.499991,3101.5309 36.879064,3101.5309 41.239663,3105.3948 41.239663,3110.1612 z"
 style="fill:#ffd5d5;fill-opacity:1;stroke:#000000;stroke-width:0.40499824;stroke-linecap:butt;stroke-linejoin:round;stroke-miterlimit:3.5;stroke-opacity:0.98453603;stroke-dasharray:none;stroke-dashoffset:50" />
<path
 sodipodi:nodetypes="ccccc"
 inkscape:connector-curvature="0"
 id="rect3002-1-1"
 d="M 12.11958,3152.6434 C 23.04719,3152.6434 39.914086,3153.0002 50.841696,3153.0002 50.841696,3148.7306 51.813575,3143.4796 47.710072,3140.1019 36.782462,3140.1019 26.070827,3140.1019 15.143217,3140.1019 11.363671,3142.4091 12.11958,3148.3737 12.11958,3152.6434 z"
 style="fill:#ffd5d5;fill-opacity:1;stroke:#000000;stroke-width:0.31832072;stroke-linecap:butt;stroke-linejoin:round;stroke-miterlimit:3.5;stroke-opacity:0.98453603;stroke-dasharray:none;stroke-dashoffset:50" />
</g>
<g
 transform="translate(129.54296,-2720.5872)"
 id="whitePawn3"
 class="chesspiece draggable"
 inkscape:label="Layer 1">
<path
 sodipodi:nodetypes="ccccc"
 inkscape:connector-curvature="0"
 id="path5379-3-4"
 d="M 18.950592,3139.9627 C 27.356133,3140.0207 35.761678,3140.0781 44.167219,3140.1358 40.865876,3132.8847 33.112108,3126.5232 34.263185,3118.3823 32.093064,3118.3823 29.922942,3118.3823 27.752826,3118.3823 29.827721,3126.4655 21.884668,3132.7692 18.950592,3139.9627 z"
 style="fill:#ffd5d5;fill-opacity:1;stroke:#000000;stroke-width:0.40499824;stroke-linecap:butt;stroke-linejoin:round;stroke-miterlimit:3.5;stroke-opacity:0.98453603;stroke-dasharray:none;stroke-dashoffset:50" />
<path
 inkscape:connector-curvature="0"
 id="rect2994-4-8"
 d="M 41.239663,3110.1612 C 41.239663,3114.9277 36.879064,3118.7916 31.499991,3118.7916 26.120911,3118.7916 21.760312,3114.9277 21.760312,3110.1612 21.760312,3105.3948 26.120911,3101.5309 31.499991,3101.5309 36.879064,3101.5309 41.239663,3105.3948 41.239663,3110.1612 z"
 style="fill:#ffd5d5;fill-opacity:1;stroke:#000000;stroke-width:0.40499824;stroke-linecap:butt;stroke-linejoin:round;stroke-miterlimit:3.5;stroke-opacity:0.98453603;stroke-dasharray:none;stroke-dashoffset:50" />
<path
 sodipodi:nodetypes="ccccc"
 inkscape:connector-curvature="0"
 id="rect3002-1-2"
 d="M 12.11958,3152.6434 C 23.04719,3152.6434 39.914086,3153.0002 50.841696,3153.0002 50.841696,3148.7306 51.813575,3143.4796 47.710072,3140.1019 36.782462,3140.1019 26.070827,3140.1019 15.143217,3140.1019 11.363671,3142.4091 12.11958,3148.3737 12.11958,3152.6434 z"
 style="fill:#ffd5d5;fill-opacity:1;stroke:#000000;stroke-width:0.31832072;stroke-linecap:butt;stroke-linejoin:round;stroke-miterlimit:3.5;stroke-opacity:0.98453603;stroke-dasharray:none;stroke-dashoffset:50" />
</g>
<g
 transform="translate(63.592033,-2718.2866)"
 id="whitePawn2"
 class="chesspiece draggable"
 inkscape:label="Layer 1">
<path
 sodipodi:nodetypes="ccccc"
 inkscape:connector-curvature="0"
 id="path5379-3-9"
 d="M 18.950592,3139.9627 C 27.356133,3140.0207 35.761678,3140.0781 44.167219,3140.1358 40.865876,3132.8847 33.112108,3126.5232 34.263185,3118.3823 32.093064,3118.3823 29.922942,3118.3823 27.752826,3118.3823 29.827721,3126.4655 21.884668,3132.7692 18.950592,3139.9627 z"
 style="fill:#ffd5d5;fill-opacity:1;stroke:#000000;stroke-width:0.40499824;stroke-linecap:butt;stroke-linejoin:round;stroke-miterlimit:3.5;stroke-opacity:0.98453603;stroke-dasharray:none;stroke-dashoffset:50" />
<path
 inkscape:connector-curvature="0"
 id="rect2994-4-5"
 d="M 41.239663,3110.1612 C 41.239663,3114.9277 36.879064,3118.7916 31.499991,3118.7916 26.120911,3118.7916 21.760312,3114.9277 21.760312,3110.1612 21.760312,3105.3948 26.120911,3101.5309 31.499991,3101.5309 36.879064,3101.5309 41.239663,3105.3948 41.239663,3110.1612 z"
 style="fill:#ffd5d5;fill-opacity:1;stroke:#000000;stroke-width:0.40499824;stroke-linecap:butt;stroke-linejoin:round;stroke-miterlimit:3.5;stroke-opacity:0.98453603;stroke-dasharray:none;stroke-dashoffset:50" />
<path
 sodipodi:nodetypes="ccccc"
 inkscape:connector-curvature="0"
 id="rect3002-1-76"
 d="M 12.11958,3152.6434 C 23.04719,3152.6434 39.914086,3153.0002 50.841696,3153.0002 50.841696,3148.7306 51.813575,3143.4796 47.710072,3140.1019 36.782462,3140.1019 26.070827,3140.1019 15.143217,3140.1019 11.363671,3142.4091 12.11958,3148.3737 12.11958,3152.6434 z"
 style="fill:#ffd5d5;fill-opacity:1;stroke:#000000;stroke-width:0.31832072;stroke-linecap:butt;stroke-linejoin:round;stroke-miterlimit:3.5;stroke-opacity:0.98453603;stroke-dasharray:none;stroke-dashoffset:50" />
</g>
<g
 transform="translate(2.2423417,-2722.8878)"
 id="whitePawn1"
 class="chesspiece draggable"
 inkscape:label="Layer 1">
<path
 sodipodi:nodetypes="ccccc"
 inkscape:connector-curvature="0"
 id="path5379-3-38"
 d="M 18.950592,3139.9627 C 27.356133,3140.0207 35.761678,3140.0781 44.167219,3140.1358 40.865876,3132.8847 33.112108,3126.5232 34.263185,3118.3823 32.093064,3118.3823 29.922942,3118.3823 27.752826,3118.3823 29.827721,3126.4655 21.884668,3132.7692 18.950592,3139.9627 z"
 style="fill:#ffd5d5;fill-opacity:1;stroke:#000000;stroke-width:0.40499824;stroke-linecap:butt;stroke-linejoin:round;stroke-miterlimit:3.5;stroke-opacity:0.98453603;stroke-dasharray:none;stroke-dashoffset:50" />
<path
 inkscape:connector-curvature="0"
 id="rect2994-4-57"
 d="M 41.239663,3110.1612 C 41.239663,3114.9277 36.879064,3118.7916 31.499991,3118.7916 26.120911,3118.7916 21.760312,3114.9277 21.760312,3110.1612 21.760312,3105.3948 26.120911,3101.5309 31.499991,3101.5309 36.879064,3101.5309 41.239663,3105.3948 41.239663,3110.1612 z"
 style="fill:#ffd5d5;fill-opacity:1;stroke:#000000;stroke-width:0.40499824;stroke-linecap:butt;stroke-linejoin:round;stroke-miterlimit:3.5;stroke-opacity:0.98453603;stroke-dasharray:none;stroke-dashoffset:50" />
<path
 sodipodi:nodetypes="ccccc"
 inkscape:connector-curvature="0"
 id="rect3002-1-6"
 d="M 12.11958,3152.6434 C 23.04719,3152.6434 39.914086,3153.0002 50.841696,3153.0002 50.841696,3148.7306 51.813575,3143.4796 47.710072,3140.1019 36.782462,3140.1019 26.070827,3140.1019 15.143217,3140.1019 11.363671,3142.4091 12.11958,3148.3737 12.11958,3152.6434 z"
 style="fill:#ffd5d5;fill-opacity:1;stroke:#000000;stroke-width:0.31832072;stroke-linecap:butt;stroke-linejoin:round;stroke-miterlimit:3.5;stroke-opacity:0.98453603;stroke-dasharray:none;stroke-dashoffset:50" />
</g>
<g
 transform="translate(439.76262,0.34005957)"
 id="whiteRook2"
 class="chesspiece draggable"
 inkscape:label="Layer 1">
<path
 sodipodi:nodetypes="ccccc"
 inkscape:connector-curvature="0"
 id="path5379"
 d="M 19.164173,457.97417 43.527644,457.78706 46.735299,482.6102 16.598051,482.5167 z"
 style="fill:#ffd5d5;fill-opacity:1;stroke:#000000;stroke-width:0.44588459;stroke-linecap:butt;stroke-linejoin:round;stroke-miterlimit:3.5;stroke-opacity:0.98453603;stroke-dasharray:none;stroke-dashoffset:50" />
<path
 sodipodi:nodetypes="ccccc"
 inkscape:connector-curvature="0"
 id="rect5449"
 d="M 13.083803,446.84895 49.62272,446.84895 48.339658,457.9659 14.687629,457.8724 z"
 style="fill:#ffd5d5;fill-opacity:1;stroke:#000000;stroke-width:0.4675346;stroke-linecap:butt;stroke-linejoin:round;stroke-miterlimit:3.5;stroke-opacity:0.98453603;stroke-dashoffset:50" />
<rect
 y="442.34985"
 x="13.069632"
 height="4.3972631"
 width="7.2172222"
 id="rect5454"
 style="fill:#ffd5d5;fill-opacity:1;stroke:#000000;stroke-width:0.44588459;stroke-linecap:butt;stroke-linejoin:round;stroke-miterlimit:3.5;stroke-opacity:0.98453603;stroke-dasharray:none;stroke-dashoffset:50" />
<rect
 y="442.30307"
 x="23.253931"
 height="4.3972631"
 width="7.2172222"
 id="rect5454-1"
 style="fill:#ffd5d5;fill-opacity:1;stroke:#000000;stroke-width:0.44588459;stroke-linecap:butt;stroke-linejoin:round;stroke-miterlimit:3.5;stroke-opacity:0.98453603;stroke-dasharray:none;stroke-dashoffset:50" />
<rect
 y="442.30307"
 x="42.018715"
 height="4.3972631"
 width="7.2172222"
 id="rect5454-7"
 style="fill:#ffd5d5;fill-opacity:1;stroke:#000000;stroke-width:0.44588459;stroke-linecap:butt;stroke-linejoin:round;stroke-miterlimit:3.5;stroke-opacity:0.98453603;stroke-dasharray:none;stroke-dashoffset:50" />
<rect
 y="442.30307"
 x="33.197662"
 height="4.3972631"
 width="7.2172222"
 id="rect5454-17"
 style="fill:#ffd5d5;fill-opacity:1;stroke:#000000;stroke-width:0.44588459;stroke-linecap:butt;stroke-linejoin:round;stroke-miterlimit:3.5;stroke-opacity:0.98453603;stroke-dasharray:none;stroke-dashoffset:50" />
<path
 sodipodi:nodetypes="ccccc"
 inkscape:connector-curvature="0"
 id="rect3002"
 d="M 9.5236162,495.76948 C 22.119724,495.76948 41.561965,496.1447 54.158072,496.1447 54.158072,491.6549 55.278348,486.13323 50.548294,482.58151 37.952186,482.58151 25.605029,482.58151 13.008921,482.58151 8.6522912,485.00757 9.5236162,491.27968 9.5236162,495.76948 z"
 style="fill:#ffd5d5;fill-opacity:1;stroke:#000000;stroke-width:0.3504566;stroke-linecap:butt;stroke-linejoin:round;stroke-miterlimit:3.5;stroke-opacity:0.98453603;stroke-dasharray:none;stroke-dashoffset:50" />
</g>
<g
 transform="translate(1.3405815,1.6415496)"
 id="whiteRook1"
 class="chesspiece draggable"
 inkscape:label="Layer 1">
<path
 sodipodi:nodetypes="ccccc"
 inkscape:connector-curvature="0"
 id="path5379-4"
 d="M 19.164173,457.97417 43.527644,457.78706 46.735299,482.6102 16.598051,482.5167 z"
 style="fill:#ffd5d5;fill-opacity:1;stroke:#000000;stroke-width:0.44588459;stroke-linecap:butt;stroke-linejoin:round;stroke-miterlimit:3.5;stroke-opacity:0.98453603;stroke-dasharray:none;stroke-dashoffset:50" />
<path
 sodipodi:nodetypes="ccccc"
 inkscape:connector-curvature="0"
 id="rect5449-9"
 d="M 13.083803,446.84895 49.62272,446.84895 48.339658,457.9659 14.687629,457.8724 z"
 style="fill:#ffd5d5;fill-opacity:1;stroke:#000000;stroke-width:0.4675346;stroke-linecap:butt;stroke-linejoin:round;stroke-miterlimit:3.5;stroke-opacity:0.98453603;stroke-dashoffset:50" />
<rect
 y="442.34985"
 x="13.069632"
 height="4.3972631"
 width="7.2172222"
 id="rect5454-2"
 style="fill:#ffd5d5;fill-opacity:1;stroke:#000000;stroke-width:0.44588459;stroke-linecap:butt;stroke-linejoin:round;stroke-miterlimit:3.5;stroke-opacity:0.98453603;stroke-dasharray:none;stroke-dashoffset:50" />
<rect
 y="442.30307"
 x="23.253931"
 height="4.3972631"
 width="7.2172222"
 id="rect5454-1-8"
 style="fill:#ffd5d5;fill-opacity:1;stroke:#000000;stroke-width:0.44588459;stroke-linecap:butt;stroke-linejoin:round;stroke-miterlimit:3.5;stroke-opacity:0.98453603;stroke-dasharray:none;stroke-dashoffset:50" />
<rect
 y="442.30307"
 x="42.018715"
 height="4.3972631"
 width="7.2172222"
 id="rect5454-7-6"
 style="fill:#ffd5d5;fill-opacity:1;stroke:#000000;stroke-width:0.44588459;stroke-linecap:butt;stroke-linejoin:round;stroke-miterlimit:3.5;stroke-opacity:0.98453603;stroke-dasharray:none;stroke-dashoffset:50" />
<rect
 y="442.30307"
 x="33.197662"
 height="4.3972631"
 width="7.2172222"
 id="rect5454-17-8"
 style="fill:#ffd5d5;fill-opacity:1;stroke:#000000;stroke-width:0.44588459;stroke-linecap:butt;stroke-linejoin:round;stroke-miterlimit:3.5;stroke-opacity:0.98453603;stroke-dasharray:none;stroke-dashoffset:50" />
<path
 sodipodi:nodetypes="ccccc"
 inkscape:connector-curvature="0"
 id="rect3002-4"
 d="M 9.5236162,495.76948 C 22.119724,495.76948 41.561965,496.1447 54.158072,496.1447 54.158072,491.6549 55.278348,486.13323 50.548294,482.58151 37.952186,482.58151 25.605029,482.58151 13.008921,482.58151 8.6522912,485.00757 9.5236162,491.27968 9.5236162,495.76948 z"
 style="fill:#ffd5d5;fill-opacity:1;stroke:#000000;stroke-width:0.3504566;stroke-linecap:butt;stroke-linejoin:round;stroke-miterlimit:3.5;stroke-opacity:0.98453603;stroke-dasharray:none;stroke-dashoffset:50" />
</g>
</g>
</svg>
        </div><!-- col-xs-11 -->
    </div>
        <div class="side-panel-right col-sm-4 col-xs-4">
            <div id="game-info" class="col-xs-12">
                <div  class="panel panel-primary">
                    <div class="panel-heading">
                        <h1 class="panel-title">Game information</h1>
                    </div>
                    <div id="gameInfoPanel" class="panel-body"></div>
                </div>
            </div>
            <div id="messages" class="col-xs-12">
                <div  class="panel panel-primary">
                    <div class="panel-heading">
                        <h1 class="panel-title">Messages</h1>
                    </div>
                <div id="messagePanel" class="panel-body"></div>
            </div>
            </div>
        </div>
        
    </div>
  </div>
</body>
</html>