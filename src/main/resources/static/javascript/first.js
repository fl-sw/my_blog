function searchType(){
    var searchtype = document.getElementById("searchtype").innerHTML;
    if(searchtype == "Accurate")
    {
        var searchform = document.getElementById("searchform");
        searchform.action="/findEntryByNoun";
    }
}

function spread(obj) {
    window.alert("onclick!");
    var dddspread = document.getElementById(obj).innerHTML;
    if(dddspread.style.display == "none")
        dddspread.style.display="block";
    else
        dddspread.style.display="none";
}
