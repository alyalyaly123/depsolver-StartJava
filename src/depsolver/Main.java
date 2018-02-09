package dependency;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.json.*;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;



 class Package implements  Comparable<Package>
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

public String stringify(){
	String returned= "jsaddj" + getName()+ "="+ getVersion()+ "Size" + getSize() + "Dep : "+ getDepends().toString() + "Conflicts: " + getConflicts();
	return returned;
}

@Override
public int compareTo(Package o) {
	 return (this.getSize() < o.getSize() ? -1 : 
         (this.getSize() == o.getSize() ? 0 : 1));    
}


}


public class Main {
	public static String nameCons;
	
  public static void main(String[] args) throws IOException {
    TypeReference<List<Package>> repoType = new TypeReference<List<Package>>() {};
    List<Package> repo = JSON.parseObject(readFile("seen-0/repository.json"), repoType);
    TypeReference<List<String>> strListType = new TypeReference<List<String>>() {};
    List<String> initial = JSON.parseObject(readFile("seen-0/initial.json"), strListType);
    List<String> constraints = JSON.parseObject(readFile("seen-0/constraints.json"), strListType);
    
    //CHANGE CODE BELOW:
    // using repo, initial and constraints, compute a solution and print the answer
 /*   for (Package p : repo) {
      System.out.printf("package %s version %s\n", p.getName(), p.getVersion());
      for (List<String> clause : p.getDepends()) {
        System.out.printf("  dep:");
        for (String q : clause) {
          System.out.printf(" %s", q);
        }
        System.out.printf("\n");
      }
    }*/
    
    List<String> commands = new ArrayList<String>();
    
    System.out.println(initial);
    System.out.println(constraints);
    //Construct command list
    commands.addAll(initial);

   
      for(String s : constraints){
    	
 
    	String symbol=""+ s.charAt(0);
    	String tempConstraint= s.substring(1);
    	System.out.println(tempConstraint);
    	
    	
    	
       	ArrayList<String> cons= splitString(tempConstraint);
    	String nameCons= cons.get(0);
    	String versionCons= cons.get(1);

    	System.out.println(nameCons);
    	System.out.println(versionCons);
    	commands.addAll(install(repo , cons, constraints));


   	
    		
    	
    
    	

    }
	System.out.println(commands.toString());
	
    
  }
  
  public static ArrayList<String> install(List<Package> repo, List<String> cons, List<String> constraints){
	  String nameCons= cons.get(0);
	  ArrayList<String> result= new ArrayList<String>();
	  for (Package a : repo) {
		  
		  if(a.getName().equals(nameCons)){
		  System.out.println("Name Cons is" + nameCons +"Package is " + a.getName());
	      System.out.printf("package %s version %s\n", a.getName(), a.getVersion());
	      
	      for (List<String> clause : a.getDepends()) {
	    	   if(clause.size()<=1) {
	    	  result.add("+"+ clause);
	    	  }
	    	   else{
	    		   //implement compare size function
	    		   for(String dependant: clause){
	    		   Package added= compare(repo, clause);
	    		   System.out.println(added.getName()+"=" +added.getVersion() +"Size=" + added.getSize());
	    		   result.add("+" +added.getName() +"=" +added.getVersion());
	    	   }
	        System.out.printf("  dep:");
	        for (String q : clause) {
	          System.out.printf(" %s", q);
	        }
	        System.out.printf("\n");
	      }
	      
	      result.add("+"+ a.getName() + "=" + a.getVersion());
	      
		  
		  }
	     
	      
		
	  }
	  }
	  return result;
  }
  
   
  public static Package compare(List<Package> repo, List<String> dependancies){
	  
	  List<Package> packList= new ArrayList<Package>();
	  System.out.println(dependancies);
	  for(String dependant: dependancies){
		  		
		       	ArrayList<String> cons= splitString(dependant);
		
		       	
		    	String nameCons= cons.get(0);
		    	String versionCons= cons.get(1);
		    	
		    	
		       	
		    	System.out.println("Splitted compare: " + nameCons+ "  " + versionCons);
		  
		  System.out.println(nameCons);
		  
	  for(Package a: repo){
		  
	  
		      if(a.getName().equals(nameCons)){
			  System.out.println("done that");
			  packList.add(a);
			  break;
		  }
	  }
	  }
	  
	  Collections.sort(packList);
	  for(Package testP: packList){
		  testP.stringify();
	  }
	  return packList.get(0); 
	  
  }
      
	  
	  
	
  

  static String readFile(String filename) throws IOException {
    BufferedReader br = new BufferedReader(new FileReader(filename));
    StringBuilder sb = new StringBuilder();
    br.lines().forEach(line -> sb.append(line));
    return sb.toString();
  }
  
  //converts string into the name and the symbol required
  public static ArrayList<String> splitString(String input){
	  ArrayList returned= new ArrayList();
	  System.out.println("THE STRING BEING INPUT HERE IS " +input);
	  
	  if(input.contains(">=")){
		  String [] splitted=input.split(">=");
			 returned.add(splitted[0]);
			 returned.add(splitted[1]);
			 returned.add(">=");
			 return returned;
	  }
	  else if(input.contains("<=")){
		  String [] splitted=input.split("<=");
			 returned.add(splitted[0]);  
			 returned.add(splitted[1]);
			 returned.add("<=");
			 return returned;
	  }
	  else if(input.contains("=")){
			 String [] splitted=input.split("=");
			 
			 returned.add(splitted[0]);
			 returned.add(splitted[1]);
			 returned.add("=");
			  return returned;
		  }
	  else if(input.contains(">")){
		  String [] splitted=input.split(">");
			 returned.add(splitted[0]);
			 returned.add(splitted[1]);
			 returned.add(">");
			 return returned;
	  }
	  else if(input.contains("<")){
		  String [] splitted=input.split("<");
			 returned.add(splitted[0]);
			 returned.add(splitted[1]);
			 returned.add("<");
			 return returned;
	  }
	  else{
		  returned.add(input);
		  returned.add("None");
		  return returned;
	  }
	  
	  
  }

		
		
		

		// TODO Auto-generated method stub
		
		
    
    
	
	}
