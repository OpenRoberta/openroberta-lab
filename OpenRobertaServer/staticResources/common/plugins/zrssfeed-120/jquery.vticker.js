/*
* Tadas Juozapaitis ( kasp3rito@gmail.com )
*
* Modifed by Zazar:
* 24.06.2011 - Corrected pausing issue with multiple instances
*
*/

(function($){

$.fn.vTicker = function(options) {
	var defaults = {
		speed: 700,
		pause: 4000,
		showItems: 3,
		animation: '',
		mousePause: true,
		isPaused: false
	};

	var options = $.extend(defaults, options);

	moveUp = function(obj2, height, paused){
		if(paused) return;
		
		var obj = obj2.children('ul');
		
	    	first = obj.children('li:first').clone(true);
		
    		obj.animate({top: '-=' + height + 'px'}, options.speed, function() {
        		$(this).children('li:first').remove();
	        	$(this).css('top', '0px');
        	});
		
		if(options.animation == 'fade') {
			obj.children('li:first').fadeOut(options.speed);
			obj.children('li:last').hide().fadeIn(options.speed);
		}

	    	first.appendTo(obj);
	};
	
	return this.each(function() {
		var obj = $(this);
		var maxHeight = 0;
		var itempause = options.isPaused;

		obj.css({overflow: 'hidden', position: 'relative'})
			.children('ul').css({position: 'absolute', margin: 0, padding: 0})
			.children('li').css({margin: 0, padding: 0});

		obj.children('ul').children('li').each(function(){

			if($(this).height() > maxHeight) {
				maxHeight = $(this).height();
			}
		});

		obj.children('ul').children('li').each(function() {
			$(this).height(maxHeight);
		});

		obj.height(maxHeight * options.showItems);
		
    		var interval = setInterval(function(){ moveUp(obj, maxHeight, itempause); }, options.pause);
		
		if (options.mousePause)
		{
			obj.bind("mouseenter",function() {
				itempause = true;
			}).bind("mouseleave",function() {
				itempause = false;
			});
		}
	});
};
})(jQuery);