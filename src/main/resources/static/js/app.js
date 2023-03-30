/**
 * 
 */
 const hostSummary = $('#hostsSummary');
 const hostDetails = $('#hostDetails');
 const configDetails = $('#configDetails');
 
 
 $(document).ready(function () {
    //$("#search-host").click(function (event) {

        //stop submit the form, we will post it manually.
        //event.preventDefault();
        getHostSummary();
        getConfigData('reboot');
    //});
  });
 
 function getQueryParam(key){
   	const params = new URLSearchParams(window.location.search);
	for (const [key, value] of params) {
  		alert(key);
	}
 }
 
 
 function getHostSummary() {
    var host = location.hostname;
    var port = location.port;
    var protocol = location.protocol;
    
    var hostName = host + (port=='' ? '':':'+port)
    $.ajax({
    	headers: {
            'Access-Control-Allow-Origin': '*',
            'Access-Control-Allow-Methods': 'GET',
            'Content-Type':'application/json'
        },
        type: "GET",
        url: protocol + "//" + hostName + "/service/host/summary",
        //data: JSON.stringify(search),
        //dataType: 'jsonp',
        cache: false,
        timeout: 600000,
        success: function (data) {

            var objArr = jQuery.parseJSON(JSON.stringify(data));

            hostSummary.children().remove();


            $.each(objArr, function(key,value) {
            	rebootDT = (Intl.DateTimeFormat('en-US', { year: 'numeric', month: 'numeric', day: 'numeric', hour: "numeric", minute: "numeric", second: "numeric", hour12: false }).format(new Date(value.lastRebootTime))).replaceAll("/", "-");
				hostSummary.append('<tr align=center onclick="getHostDetails(\'' + value.host + '\')"><td style="width:250px">' + value.host + '</td><td  style="width:150px">' + rebootDT + '</td></tr>');
  			});
        },
        error: function (e) {
            var json = "<h4>Ajax Response</h4>&lt;pre&gt;"
                + e.responseText + "&lt;/pre&gt;";
            $('#feedback').html(json);
            console.log("ERROR : ", e);

        }
    });
}

 function getHostDetails(hostNameR) {
    var host = location.hostname;
    var port = location.port;
    var protocol = location.protocol;
    
    var hostName = host + (port=='' ? '':':'+port)
    $.ajax({
    	headers: {
            'Access-Control-Allow-Origin': '*',
            'Access-Control-Allow-Methods': 'GET',
            'Content-Type':'application/json'
        },
        type: "GET",
        url: protocol + "//" + hostName + "/service/host/details/" + hostNameR,
        //data: JSON.stringify(search),
        //dataType: 'jsonp',
        cache: false,
        timeout: 600000,
        success: function (data) {
			var i = 0;
            var objArr = jQuery.parseJSON(JSON.stringify(data));
            hostDetails.children().remove();
            $.each(objArr, function(key,value) {
            reqDT = (Intl.DateTimeFormat('en-US', { year: 'numeric', month: 'numeric', day: 'numeric', hour: "numeric", minute: "numeric", second: "numeric", hour12: false }).format(new Date(value.requestTime))).replaceAll("/", "-");
			hostDetails.append('<tr align=center><td>' + value.requestCount + '</td><td style="width:300px">' + value.hostID + '</td><td style="width:150px">' + reqDT + '</td><td>' + value.maxRestartCount + '</td><td>' + value.restart + '</td><td>' + value.alertOps + '</td></tr>');				
  			});
        },
        error: function (e) {
            var json = "<h4>Ajax Response</h4>&lt;pre&gt;"
                + e.responseText + "&lt;/pre&gt;";
            $('#feedback').html(json);
            console.log("ERROR : ", e);

        }
    });
    }
    
    
    function getConfigData(rulename) {
    var host = location.hostname;
    var port = location.port;
    var protocol = location.protocol;
    
    var hostName = host + (port=='' ? '':':'+port);
    var surl = '?rulename=' + rulename;
	//alert(protocol + "//" + hostName + "/service/host/config/" + rulename);
    $.ajax({
    	headers: {
            'Access-Control-Allow-Origin': '*',
            'Access-Control-Allow-Methods': 'GET',
            'Content-Type':'application/json'
        },
        type: 'GET',
        url: protocol + "//" + hostName + "/service/host/config/" + rulename,
        cache: false,
        timeout: 600000,
        success: function (data) {
            var objArr = jQuery.parseJSON(JSON.stringify(data));
            //configDetails.append('<tr  align=center><td><input type=hidden id=rid value=' + data.id + ' /><input type=text id=ruleId readonly value=' + rulename + ' /></td><td><input type=text id=rMinutes value=' + data.templateMinutes + ' /></td><td><input type=text id=rCount value=' + data.templateCount + ' /></td><td><!-- <button onClick=setConfigData() id=' + rulename + ' >Update</button> --></td><tr>');
            configDetails.append('<tr  align=center><td><input READONLY type=text id=rMinutes value=' + objArr[0]["value"] + ' /></td><td><input READONLY type=text id=rCount value=' + objArr[1]["value"] + ' /></td><td></td><tr>');    	  
        },
        error: function (e) {
            var json = "<h4>Ajax Response</h4>&lt;pre&gt;" + e.responseText + "&lt;/pre&gt;";
            console.log("ERROR : ", e);
        }
    });
}


    function setConfigData() {
    var host = location.hostname;
    var port = location.port;
    var protocol = location.protocol;
    
    var hostName = host + (port=='' ? '':':'+port);
    var data;
    data = { 'id': $("#rid").val(), 'ruleId': $("#ruleId").val(), 'rMinutes': $("#rMinutes").val(), 'rCount': $("#rCount").val() };

    $.ajax({
    	headers: {
            'Access-Control-Allow-Origin': '*',
            'Access-Control-Allow-Methods': 'POST',
            'Content-Type':'application/json'
        },
        type: 'POST',
        url: protocol + "//" + hostName + "/service/host/reboot/conf",
        data: JSON.stringify(data),
        //dataType: 'jsonp',
        cache: false,
        timeout: 600000,
        success: function (data) {
			alert(data);
        },
        error: function (e) {
            var json = "<h4>Ajax Response</h4>&lt;pre&gt;" + e.responseText + "&lt;/pre&gt;";
            console.log("ERROR : ", e);
        }
    });
}
