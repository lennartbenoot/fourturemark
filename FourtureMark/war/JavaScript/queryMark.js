 var lang;
function getJSON(parameters) {
    var url = "/cron/queryMark";
    $('result').update('<img src="images/loader_splash.gif" />');
    new Ajax.Request(url, {
        method: 'post',
        parameters: parameters,
        onSuccess: function(transport) {
          if(transport.responseText.indexOf("no_result_found")==-1){
        		  $('querySplash').style.width = '960px'
        		  $('result').update(transport.responseText)
          }else{
        	  $('querySplash').style.width = '400px'
        	  if (lang == "nl"){
  						$('result').update('Geen resultaat gevonden')
  				    }
  				    else if (lang == "fr"){
  				    	$('result').update('Aucun résultat trouvé')
  				    }else{
  				    	$('result').update('No result found')
  				    }
          }
        },
        onFailure: function(transport) {
        	$('result').update('error')
        }
    });
}

function gup(name) {
    name = name.replace(/[\[]/, "\\\[").replace(/[\]]/, "\\\]");
    var regexS = "[\\?&]" + name + "=([^&#]*)";
    var regex = new RegExp(regexS);
    var results = regex.exec(window.location.href);
    if (results == null)
        return "";
    else
        return results[1];
}

function queryButtonClicked(){
	//var xhr = getJSON();
	if(!$('p1').value){
		$('p1_row').addClassName('error')
		return;
	}else
		$('p1_row').removeClassName('error')
	
	var parameters = 'lang=' +lang +'&p1=' + $('p1').value + '&v1=' +  $('v1').value
	if($('p2').value)
		parameters += '&p2=' + $('p2').value + '&v2=' +  $('v2').value
	if($('p3').value)
		parameters += '&p3=' + $('p3').value + '&v3=' +  $('v3').value
	getJSON(parameters)
}

function paramOnchange(obj){
	var rowId = obj.id + "_row";
	if(obj.value)
		$(rowId).removeClassName('error')
}

function build() {

	var selectItems = ['param1','param2','param3','platform','browserName','browserVersion']
	
	lang = gup('lang');
	if (lang == "nl"){
		$('queryButton').update('Zoeken')
    }
    else if (lang == "fr"){
		$('queryButton').update('Rechercher')
    }else{
    	$('queryButton').update('Search')
    }
	
	//building opion to select param
	var selectStr = "<option></option>";
	for(var i=0;i<selectItems.length;i++)
		selectStr += "<option id='"+ selectItems[i] +"'>"+ selectItems[i] +"</option>"
	$('p1').update(selectStr)
	$('p2').update(selectStr)
	$('p3').update(selectStr)

    
}
