# Read Code




## Code file 

* Login home page
    * logo
        * src/main/webapp/static/themes/openolat/images/Openolat_logo_claim_RGB.svg
    * top navigation menu (locale selection, help)
        * src/java/org/olat/gui/control/content/dmztopnav.html
        * src/java/org/olat/gui/control/OlatDmzTopNavController.java
        ```
        // shining modified to comment out the help options
		/*
		if (helpModule.isHelpEnabled()) {
			List<String> helpPlugins = new ArrayList<>();
			for (HelpLinkSPI helpLinkSPI : helpModule.getDMZHelpPlugins()) {
				helpPlugins.add(helpLinkSPI.getHelpUserTool(getWindowControl()).getMenuComponent(ureq, vc).getComponentName());
			}
			vc.contextPut("helpPlugins", helpPlugins);
		}*/
        ```
    * about 
        * TODO src/java/org/olat/login/_content/about.html
    * translate file
        * src/main/java/org/olat/login/_i18n/LocalStrings_zh_CN.properties
    * footer
        * src/java/org/olat/gui/control/content/olatFooter.html
        ```
        #if($footerInfos.isOlat)
        #end
        ```
        * src/java/org/olat/admin/layout/FooterInformations.java
        ```
        //shining added to control the footer display layout
	    public boolean isIsOlat() {
		    return false;
	    }
        ```
    * configure 
        * disable self registration
        ```
        registration.enableSelfRegistration=false
        ```
* Main page
    * footer 
        * src/main/java/org/olat/gui/control/_content/olatFooter.html

* Translation
    * default language, fallback policy 
        * src/main/resources/serviceconfig/olat.properties
        ```
        defaultlang=zh_CN
        fallbacklang=en
        fallbacklang.values=en,de
        ```
        * src/main/java/org/olat/core/util/i18n/i18nModule.java
        ```
        @Value("${defaultlang:zh_CN}")
	    private String defaultLanguage;
        ```
    * Core
        * src/main/java/org/olat/_i18n/LocalStrings_zh_CN.properties
    * Login
        * src/main/java/org/olat/login/_i18n/LocalStrings_zh_CN.properties
* Help
    * src/main/java/org/olat/core/commons/services/help/HelpModule.java

* Thems
    * src/main/webapp/static/themes/openolat/_openolat_theme.scss
    * src/main/webapp/static/themes/openolat/theme.js
    ```
    images: ['infinite.jpg','weg.jpg'], 
    ```

* Application configure
    * configure user tools menu
        * login as administrator
        * go to system management
        * go to "core configuration" -> "user tool"
    * configure license
        * login as administrator
        * go to system management
        * go to "core configuration" -> "Licenses"
    * configure Curriculum 
        * login as administrator
        * go to system management
        * go to "Module" -> "Curriculum"
    * configure Theme 
        * login as administrator
        * go to system management
        * go to "Customization" -> "Layout"
        * change layout from "OpenOlat" to "Light"

* System configuration
    * configure http port
        * src/main/resources/serviceconfig/olat.properties
         ```
        # the port on which the container is listening
        server.port=8088
         ```
