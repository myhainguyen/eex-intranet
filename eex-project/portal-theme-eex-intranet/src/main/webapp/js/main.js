$(function() {
  $('.back-top').click(function () {
  	$('body,html').animate({
  		scrollTop: 0
  	}, 800);
  	return false;
  });
  
  var params;

  params = [];

  $(".event-selector select").select2({
	    minimumResultsForSearch: '10'
	 });
  
  $.each($.browser, function(k, v) {
    var pat;
    pat = /^[a-z].*/i;
    if (pat.test(k)) {
      return params.push(k);
    }
  });

  params = params.join(" ");

  $("html").addClass(params);
 
})

