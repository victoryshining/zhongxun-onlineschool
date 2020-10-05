/**
 *  OpenOLAT theme JS extensions as jQuery module
 *  
 *  @dependencies jQuery
 */
+(function($) {

		var ThemeJS = function() {
			// nothing to do
		}
		/**
		 * Adds a link to the logo and the copyright footer
		 * 
		 * @method
		 */
		ThemeJS.prototype.addClientLinks = function(){
			var logoElement = $(".o_navbar-brand");
			if (logoElement && logoElement.length > 0 && !logoElement.hasClass('o_clickable')) {
				// add marker css to remember this link is already ok, add link reference
				// shining modified for zhongxun logo
				logoElement.addClass('o_clickable');					
				logoElement.prop('href', "http://www.zhongxunedu.com");
				logoElement.prop('target', "_blank");
				//logoElement.prop('title', 'OpenOLAT - infinite learning');
				logoElement.prop('title', 'ZhongXun - infinite learning');
			}
		},
		
		/**
		 * Method to install the theme add-ons. Will be re-executed after each DOM replacement. 
		 * 
		 * @method
		 */
		ThemeJS.prototype.execThemeJS = function() {
			OPOL.themejs.addClientLinks()
			// execute after each dom replacement (navbar might have been changed)
			$(document).on("oo.dom.replacement.after", OPOL.themejs.addClientLinks);
		}
		
		/**
		 * Use the carrousel effect for background images on the login screen based 
		 * on the ooBgCarrousel OpenOLAT jQuery plugin
		 */
		ThemeJS.prototype.initDmzCarrousel = function() {
			this.dmzCarrousel = jQuery().ooBgCarrousel();
			this.dmzCarrousel.initCarrousel({
				query: "#o_body.o_dmz #o_bg", 
				// shining modified to remove openlat images
				// images: ['infinite.jpg', 'holger.jpg', 'marco.jpg', 'openolat_award.jpg', 'weg.jpg', 'christian.jpg'], 
				images: ['infinite.jpg','weg.jpg'], 
				shuffle: true,
				shuffleFirst: false,
				durationshow: 5000,
				durationout: 500,
				durationin: 500,
				easeout : 'ease',
				easein : 'ease'
			});
		}
		
		//Execute when loading of page has been finished
		$(document).ready(function() {
			OPOL.themejs = new ThemeJS();
			OPOL.themejs.execThemeJS();			
			OPOL.themejs.initDmzCarrousel();
		});
		
})(jQuery);
