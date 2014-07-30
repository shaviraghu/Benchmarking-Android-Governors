package com.uia.apphandlers;

public class AppDetails {
	public enum APPS_ON_PLAY{
		ANGRY_BIRDS_RIO("angry birds","Angry Birds Rio by Rovio Mobile Ltd","GAMES"),
		PLANTS_VS_ZOMBIES_2("plants vs zombies 2","Plants Vs Zombies 2 by Nancy Yuen","APPS"),
		STUPID_ZOMBIES("stupid zombies","Stupid Zombies 2 by GameResort","APPS");
		
		private String searchString;
		private String description;
		private String category;
		
		APPS_ON_PLAY(String searchStr, String desc, String cat){
			this.searchString=searchStr;
			this.description=desc;
			this.category=cat;
		}
		public String getSearchString(){
			return searchString;
		}
		public String getDescription(){
			return description;	
		}
		public String getCategory(){
			return category;
		}
	};
	public enum APPS_ON_DEVICE{
		
		HOME("home screen","com.android.launcher"),
		ALL_APPS("all apps screen","com.android.launcher"),
		SETTINGS("settings","com.android.settings"),
		CLOCK("Clock","com.google.android.deskclock"),
		GOOGLE_PLAY_STORE("Play Store","com.android.vending"),
		AMAZON_KINDLE("Amazon Kindle","com.amazon.kindle"),
		FACEBOOK("Facebook","com.facebook.katana");
		
	
		private String searchString;
		private String packageName;
		
		APPS_ON_DEVICE(String searchStr,String packageName){
			this.searchString=searchStr;
			this.packageName=packageName;
		}
		public String getSearchString(){
			return searchString;
		}
		public String getPackageName(){
			return packageName;
		}
	};
}
