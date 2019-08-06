package project.triangulator.process;

public class ProcessLogger {

	public static void log( Process process , Object data ) {
		
		System.out.println( "Process: " + process.getName() + "[" + process.getTag() + ": " + data.toString() + "]" );
		
	}
	
}
