####The project is a personalized service that is recommended for recipe. We recommend gives a personalized recipe uses a combination of methods recommended and items based recommended. We used the predictionIO templates.
#####ML Server : https://github.com/soma-6th/recipe-api-ml

####You can modify AppSetting.java for your setup. The project is connected with this server. You can easily set up the project.
#####Main Server : https://github.com/soma-6th/recipe-api-hub

	//recipe/util/AppSetting.java

	public class AppSetting {

	 /**
     * Server URL Setting
     */
	    final public static String serverUrl = "your server domain";
	    final public static String tokenUrl = serverUrl + "/auth/me";
	    final public static String predictionUrl = serverUrl + "/predictions";
	    final public static String recipeUrl = serverUrl + "/recipes";
	    final public static String likeUrl = serverUrl + "/likes";
	    final public static String viewUrl = serverUrl + "/views";
	    final public static String reviewUrl = serverUrl + "/reviews";

	    /**
	     * Font Setting
	     * You can modify the font to put the assets folder
	     */

	    final public static String logoFont = "Nanumbut.ttf";
	    final public static String appFont = "NanumBarunGothic.ttf";
	    final public static String appFontBold = "NanumBarunGothicBold.ttf";
	}

###SignIn Page
![Sign in](https://cloud.githubusercontent.com/assets/8899510/9449502/e2eb020c-4add-11e5-8c71-13de3afdd1c5.png)

###Recipe View Page
![Recipe View Page](https://cloud.githubusercontent.com/assets/8899510/9449506/ea7978f0-4add-11e5-9748-ab9ad2ca88de.png)


#License and Copyright Notices
##This application is licensed under Apache License, Version 2.0


	Copyright 2015 Software Maestro 6th Recipe Team

	Licensed under the Apache License, Version 2.0 (the "License");
	you may not use this file except in compliance with the License.
	You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

	Unless required by applicable law or agreed to in writing, software
	distributed under the License is distributed on an "AS IS" BASIS,
	WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
	See the License for the specific language governing permissions and
	limitations under the License.
	

##This application inclues following components:

	
###Android Volley

	Copyright (C) 2014 Xiaoke Zhang
	Copyright (C) 2011 The Android Open Source Project

	Licensed under the Apache License, Version 2.0 (the "License");
	you may not use this file except in compliance with the License.
	You may obtain a copy of the License at
	
		http://www.apache.org/licenses/LICENSE-2.0

	Unless required by applicable law or agreed to in writing, software
	distributed under the License is distributed on an "AS IS" BASIS,
	WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
	See the License for the specific language governing permissions and
	limitations under the License.
