let showButton = document.getElementById("showPython");
let hideButton = document.getElementById("hidePython");
let submitButton = document.getElementById("submitPython");
let fileName = "0";

function showPythonCode() {
    document.getElementById("python").style.zIndex = 1001;
    document.getElementById("python").style.visibility='visible';
  }
function hidePythonCode() {
    document.getElementById("python").style.zIndex = 1;
    document.getElementById("python").style.visibility='hidden';
}

function submitCode(){
    pythonCodeString = document.getElementById("pythonCode").value;
    if(fileName == "0")
        fileName = (new Date()).getTime();
    hidePythonCode();
    $.ajax({
           type: 'POST',
           url: "/canvas/savePythonFile",
           data: { pythonCode: pythonCodeString, pythonName: fileName}
        });
}

let codeBlock = document.getElementById("pythonCode");
codeBlock.onkeydown = function(e){
    if(e.keyCode==9 || e.which==9){
        e.preventDefault();
        let s = this.selectionStart;
        this.value = this.value.substring(0,this.selectionStart) + "\t" + this.value.substring(this.selectionEnd);
        this.selectionEnd = s+1;
    }
};

showButton.addEventListener("click", showPythonCode);
hideButton.addEventListener("click", hidePythonCode);
submitButton.addEventListener("click", submitCode);

