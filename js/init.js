
var addField = function() {
  if(i<10) {
    var form = $('#content-form');
    i++;
    $('<div class="row"><div class="input-field col s9"><input type="text" class="grey lighten-5" name="con-field-'+i+'" id="con-field-'+i+'" size=255/></div><div class="input-field col s3"><p class="range-field"><input type="range" style="padding-top: 5px" id="rating-'+i+'" name="rating-'+i+'" min="0" max="5" /></p></div></div>').prependTo(form);
    return false;
  }
  else {
    Materialize.toast("Max 10 titles!!",4000);
    $('#add-btn').addClass('disabled').removeClass("waves-effect waves-light");
  }
}

$('#add-field').click(function() {
  addField();
});

function open_popup(form) {
    window.open('', 'formpopup', 'width=500,height=600,resizeable,scrollbars,status=0,titlebar=0');
    form.target = 'formpopup';
}

$(document).ready(function() {
      $('.modal-trigger').leanModal({
       dismissible: true,
       opacity: .5,
       in_duration: 200,
       out_duration: 200,
       starting_top: '10%'
    });
  $('.parallax').parallax();
  Materialize.toast("Welcome !",1000);
});