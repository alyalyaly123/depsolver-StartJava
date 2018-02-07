package dependency;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.json.*;
import com.google.gson.*;
import com.google.gson.reflect.TypeToken;


 class Package
 {
private String name;
private String version; 
private Integer size;
private List<List<String>> depends = new ArrayList<>();
private List<String> conflicts = new ArrayList<>();


public String getName(){
	return name;}

public String getVersion(){
	return version;
}

public Integer getSize() {
	return size; 
}

public List<List<String>> getDepends() {
	return depends; 
	}

public List<String> getConflicts() { 
	return conflicts; 
	}

public String stringify(){
	Gson testGson= new Gson();
	String stringed=  getName()+"\n" + getVersion()+ "/n"+ getSize() +"/n" + getDepends().toString() + "/n" + getConflicts().toString();
	String stringedJson= testGson.toJson(stringed);
	System.out.println(stringedJson);
	return testGson.toJson(stringedJson);
}


public void setName(String name) { 
	this.name = name; 
	}

public void setVersion(String version) {
	this.version = version;
	}

public void setSize(Integer size) {
	this.size = size; 
	}

public void setDepends(List<List<String>> depends) {
	this.depends = depends; 
	}

public void setConflicts(List<String> conflicts) { 
	this.conflicts = conflicts; 
	}


}


public class Main {
	public static void main(String[] args) throws IOException {
		Gson g = new Gson();
		TypeToken<List<Package>> repositType= new TypeToken<List<Package>>(){};
		
		Package pack= createFile();
	    String jsonPack= pack.stringify();
	   
	    List<Package> Ptype= new ArrayList<Package>();
	    List<String> type= new ArrayList<String>();
	    List<Package> repo = g.fromJson( readFile(args[0]), Ptype.getClass());
	    
	    List<String> initial = g.fromJson(readFile(args[1]), type.getClass());
	    List<String> constraints = g.fromJson(readFile(args[2]), type.getClass());
		
	    for (Package p : repo) {
	        System.out.printf("package %s version %s\n", p.getName(), p.getVersion());
	        for (List<String> clause : p.getDepends()) {
	          System.out.printf("  dep:");
	          for (String q : clause) {
	            System.out.printf(" %s", q);
	          }
	          System.out.printf("\n");
	        }
	      }
		
		
		

		// TODO Auto-generated method stub
		
		

	}
	//testing for now
    public static Package createFile(){
    Package newFile= new Package();
    newFile.setName("JDK");
    newFile.setVersion("8");
    newFile.setSize(55);
    List conflictList= new ArrayList<String>();
    conflictList.add("C");
    
    List dependList= new ArrayList<String>();
    dependList.add("A");
    
    newFile.setConflicts(conflictList);
    newFile.setDepends(dependList);
    	return newFile;
    }
    
    
	
	
	 static String readFile(String filename) throws IOException {
		    BufferedReader br = new BufferedReader(new FileReader(filename));
		    StringBuilder sb = new StringBuilder();
		    br.lines().forEach(line -> sb.append(line));
		    return sb.toString();
		  }
	}
