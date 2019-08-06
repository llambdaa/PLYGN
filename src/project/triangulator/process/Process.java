package project.triangulator.process;

public enum Process {

	FILTER( "Filtering" , "Filter" ) ,
	OUTLINING( "Outlining" , "Outliner" ) ,
	TRIANGULATION( "Triangulation" , "Triangulator" ) ,
	COLORIZATION( "Colorization" , "Colorizer" ) ,
	LOAD( "Loading" , "Path" ) ,
	EXPORT( "Exporting" , "Path" );
	
	Process( String name , String tag ) {
		
		this.name = name;
		this.tag = tag;
		
	}
	
	private String name;
	private String tag;
	
	public String getName() {
		
		return this.name;
		
	}
	
	public String getTag() {
		
		return this.tag;
		
	}
	
}
