package depsolver;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;



 class Package  
 {
private String name;
private String version; 
private Integer size;
private List<List<String>> depends = new ArrayList<>();
private List<String> conflicts = new ArrayList<>();


public String getName(){return name;}
public String getVersion(){return version;}
public Integer getSize() {	return size; }
public List<List<String>> getDepends() {	return depends; 	}
public List<String> getConflicts() { return conflicts; }
public void setName(String name) { this.name = name; }
public void setVersion(String version) {this.version = version;}
public void setSize(Integer size) {this.size = size; }
public void setDepends(List<List<String>> depends) {this.depends = depends; }
public void setConflicts(List<String> conflicts) { this.conflicts = conflicts; }

public String stringify(){
	String returned= "jsaddj" + getName()+ "="+ getVersion()+ "Size" + getSize() + "Dep : "+ getDepends().toString() + "Conflicts: " + getConflicts();
	return returned;
}
/*
public float versionTotalInt(){
	float i = Main.versionTotalInt(version);
	return i;
}
*/


}


public class Main 

{
	public static String nameCons;
	public ArrayList<String> conflicts;
	
   public static void main(String[] args) throws IOException {
    TypeReference<List<Package>> repoType = new TypeReference<List<Package>>() {};
    List<Package> repo = JSON.parseObject(readFile(args[0]), repoType);
    TypeReference<List<String>> strListType = new TypeReference<List<String>>() {};
    List<String> initial = JSON.parseObject(readFile(args[1]), strListType);
    List<String> constraints = JSON.parseObject(readFile(args[2]), strListType);
    
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
  	  ArrayList<String> conflicts= new ArrayList<String>();
  	  ArrayList<String> state= new ArrayList<String>();
    	if(symbol.equals("+")){
    	commands.addAll(dependancyBuilder(repo , cons, constraints,conflicts, state));
    	}
    	
    }
	System.out.println(commands.toString());
	System.out.println("_____________________TEST ENDED_____________________________");
	
    
  }
  
  
  
  /* public static ArrayList<String> install(List<Package> repo, List<String> cons, List<String> constraints){
	  ArrayList<String> result= new ArrayList<String>();

	  
	  String nameCons= cons.get(0);
	  String versCons= cons.get(1);
	  
	  ArrayList<String> compareList= new ArrayList<String>();
	  compareList.add(nameCons);
	  
	  Package inserted = compare(repo, compareList);
	  System.out.println("CHEAPEST VERSION OF " +nameCons +"IS" + inserted.getVersion() + "of size" + inserted.getSize());
	  if(versCons.equals("Any")){
		  versCons= inserted.getVersion();
	  }
	  
	  System.out.println("BEFORE CONS IS "+ cons.toString());
	
	  ArrayList<String> conflicts= new ArrayList<String>();
	  ArrayList<String> tests= dependancyBuilder(repo,cons,constraints,conflicts);
	  System.out.println("the thing before dupe removal is" + tests.toString());
	 
	  ArrayList<String> list = new ArrayList<String>(new LinkedHashSet<String>(tests));
	  System.out.println("______________\nTHE FINAL QUESTION IS" + list +"\n ___________________");
	  Collections.reverse(list);
	/*  for (final ListIterator<String> i = list.listIterator(); i.hasNext();) {
		  final String element = i.next();
		  i.set("+["+ element + "]");
		}
	  System.out.println(list.toString());
	 
	  for (Package a : repo) {
		  
		  if(a.getName().equals(nameCons)&& a.getVersion().equals(versCons)){
		  System.out.println("Name Cons is" + nameCons +"Package is " + a.getName());
	      System.out.printf("package %s version %s\n", a.getName(), a.getVersion());
		  System.out.println(versCons);

	      for (List<String> clause : a.getDepends()) {
	    	   if(clause.size()<=1) {
	    	  result.add("+"+ clause);
	    	  }
	    	   else{
	    		   //implement compare size function
	    		   for(String dependant: clause){
	    		   Package added= compare(repo, clause);
	    		   System.out.println("THE CLAUSE ADDED IS"+ added.getName()+"=" +added.getVersion() +"Size=" + added.getSize());
	    		   result.add("+" +added.getName() +"=" +added.getVersion());
	    	   }
	        System.out.printf("  dep:");
	        for (String q : clause) {
	          System.out.printf(" %s", q);
	        }
	        System.out.printf("\n");
	      }
	      
	 
	      }
	      result.add("+"+ a.getName() + "=" + a.getVersion());
	      System.out.println(result.toString()); 
	     
	      
		
	  }
		
	  }
	  
	  System.out.println(result.toString());
	  return result;
  }
  
  */
  
   //***********************//
  
   /* Compares necessary dependancies by size- Completely unnecessary
  public static Package compare(List<Package> repo, List<String> dependancies){
	  
	  List<Package> packList= new ArrayList<Package>();
	  System.out.println(dependancies);
      Map<ArrayList<Package>, Integer> hmap = new HashMap<ArrayList<Package>, Integer>();
      
	  for(String dependant: dependancies){
		  		
		       	ArrayList<String> cons= splitString(dependant);
		
		       	
		    	String nameCons= cons.get(0);
		    	String versionCons= cons.get(1);
		    	
		    	
		       	
		    	System.out.println("Splitted compare: " + nameCons+ "  " + versionCons);
		  
		  System.out.println(nameCons);
		  
		  
	  for(Package a: repo){
		      if(a.getName().equals(nameCons)){
			  packList.add(a);
		  }
	  }
	  }
	 

	  
	// Collections.sort(packList);
	
	// ArrayList<Package> s= new ArrayList<Package>();
	 
	
	  return packList.get(0); 
	  
  }
  */
  
  public static boolean versionCompare(String vers1, String vers2, String symbol){
	  System.out.println("Package is" + vers1 +"Other is"+ vers2 +"Symbol is "+ symbol);
	  
	  
	  
	  if(symbol.equals("=")){
		  if(vers1.equals(vers2)){
			  return true;
		  }
	  }
	  
	  else if(symbol.equals("<=")){	
		  if(vers1.equals(vers2)){
			  System.out.println(vers1+""+symbol+""+vers2);
			  return true;
		  	}
		  else{ 			  
				 if(vers1.compareTo(vers2)<0){
				return true;
				  }
			  }
		  }
	  
	  
	  else if(symbol.equals("<")){

		  if(vers1.compareTo(vers2)<0){
			  return true;
		  }
		  
	  }
	  else if(symbol.equals(">=")){
		  if(vers1.equals(vers2)){ return true;}
		  else{
		  if (vers1.compareTo(vers2)>0){
			  System.out.println(vers1+""+symbol+""+vers2);

			  return true;
		  }
	  }}
	  else if(symbol.equals(">")){
		  if(vers1.compareTo(vers2)>0){
			  return true;
		  }
		  
	  }
	  System.out.println("Doesn't match any of them so false");
	  return false;
	  
  
  }
	  
	  /*
	   * DOESN'T WORK FOR ITEMS LIKE 3.0 AND 3.2 FOR SOME REASON
	  if (version.equals("Any")){return -100;}
	  int total=0;
	  String[] val= version.split("\\..");
	  if(version.equals("")){
	  return -100;
	  }
	  for(int x=0; x<val.length; x++){
		 total= 10* total+ Integer.valueOf(val[x]);
	  }
	  return total;
	  } */
  
  
  
  
  
  public static ArrayList<String> dependancyBuilder (List<Package> repo, List<String> item, List<String> constraints,List<String> conflicts, List<String> states){
	  
	  System.out.println("BUILDING ITEM" +item.toString());
	  System.out.println("THE STATE IS "+states.toString());
	  ArrayList<String> packageList = new ArrayList<String>();
	  		
	  		ArrayList<String> cons= splitString(item.toString());
			
	    	String nameCons= cons.get(0);
	    	String versionCons= cons.get(1); 
	    	String symbol;
	    	
	    	
	       	
	    	System.out.println("Splitted compare: " + nameCons+ " = " + versionCons);
	    	if(versionCons.equals("Any")){
	    		symbol= "Any";
	    		String[] c= nameCons.split("");
	    		nameCons= c[1];
	    	}
	    	else{
	    		
	    		symbol= cons.get(2);
	    	}
	    
	    	for(Package p : repo){
	    		System.out.println("P name is"+p.getName()+"="+p.getVersion()+"Intended name is" +nameCons);

	    		if((p.getName().equals(nameCons)&&versionCons.equals("Any"))
	    		    ||
	    		    (p.getName().equals(nameCons)&&versionCompare(p.getVersion(),versionCons,symbol))){
	    	
	    		
	    			System.out.println("Adding "+ p.getName());
	    			
	    			
	    			
	    			ArrayList<String> stateTest= new ArrayList<String>(states);
	    			stateTest.add("+"+p.getName()+"="+p.getVersion());
	    			
    				if(stateValid(stateTest,repo, constraints,conflicts)){

		    			packageList.add("+"+p.getName()+"="+p.getVersion());
		    			conflicts.addAll(p.getConflicts());
		    				
	    				
	    			}
	    		
	    			
	    			states.add("+"+p.getName()+"="+p.getVersion());
	    			System.out.println("Conflicts are "+ conflicts.toString());
	    			
	    			if(p.getDepends().size()==0){
	    				
	    				if(stateValid(stateTest,repo, constraints,conflicts)){

			    			packageList.add("+"+p.getName()+"="+p.getVersion());
			    			conflicts.addAll(p.getConflicts());
			    				
		    				
		    			}
	    				
	    				System.out.println("0 depends");
	    				
	    			return packageList;
	    					}
	    			
	    		
	    				
	    				for(List<String> dependancyList: p.getDepends()){
	    					
	    					System.out.println("THE DEPENDANCY LIST"+ dependancyList);
	    					
	    					if(dependancyList.size()==1){
	    						
	    						System.out.println("dep is" + dependancyList);
	    						
	    						
	    						ArrayList<String> added= new ArrayList<String>(dependancyBuilder(repo,dependancyList,constraints,conflicts,packageList));
	    						 ArrayList<String> stateTemp = new ArrayList<String>(states);
	    						 stateTemp.addAll(added);
	    						 ArrayList<String> tempConflict = conflictBuilder(repo,stateTemp);
	    						 
	    						 
	    						 
	    						if(stateValid(stateTemp,repo,constraints, tempConflict)){
	    						
	    							    							
		    						packageList.addAll(added);
		    						states.addAll(added);
	    						}

	    						
	    					}
	    					}
	    					
	    					
	    				
	    				for(List<String> dependancyList: p.getDepends()){
	    					System.out.println("THE DEPENDANCY LIST... AGAIN"+ dependancyList);

	    					if(dependancyList.size()>1){
	    					for(String z: dependancyList){
	    						
	    						//IN THIS AREA, CREATE A THING THAT COMPARES CONFLICTS AND ADDS IF THE FINAL STATE IS VALID
	    						//EASIER SAID THAN DONE BUT YOU KNOW
	    						
	    						System.out.println("Z is"+z);
	    						ArrayList<String> x= new ArrayList<String>();
	    						x.add(z);
	    						System.out.println(x.toString());
	    						
	    						
	    					    
	    						ArrayList<String> added= new ArrayList<String>(dependancyBuilder(repo,x, constraints,conflicts,states));
	    						ArrayList<String> stateForThing=new ArrayList<String>(states);
	    						stateForThing.addAll(added);
	    						System.out.println("State for thing"+stateForThing.toString());
	    						 ArrayList<String> tempConflict= conflictBuilder(repo, stateForThing);
	
	    						 System.out.println("TEMP CONFLICT IS"+tempConflict.toString()+"TEMP STATE IS " +stateForThing.toString() );
	    						 
	    						 
	    						if(!stateValid(stateForThing,repo,constraints, tempConflict)){
	    						System.out.println(added.toString()+"Is an invalid state");
	    						}
	    						else{
	    							System.out.println("wait is" + added.toString() +" added i have no idea");
	    					    packageList.addAll(added);
	    					    return packageList;
	    						}
    						

	    					}
	    					}
	
	    					

	    				
	    				return packageList;
	    			}
	    			}
	    		
	    		
	    	}
	    	
	   System.out.println("All done i guess");
	  return packageList;
	  
	    	}
  
	   public static ArrayList<String> conflictBuilder (List<Package> repo, ArrayList<String> state){
		   
				   ArrayList<String> tempConflict= new ArrayList<String>();
			 for(String s: state){
				 
				 //**********************************//
				  
				  ArrayList<String> stateAsArray= splitString(s);
					
			    	String name= stateAsArray.get(0);
			    	String version= stateAsArray.get(1); 
			    	String symb;
			    	
			    	System.out.println("TEMPTEST"+name+"="+version);
				   name= name.replace("+", "");
				 
				 
				 for (Package pack : repo){
					 
					 if((pack.getName().equals(name) && pack.getVersion().equals(version))
				    ||
				    (pack.getName().equals(name)&&versionCompare(pack.getVersion(),version,"="))){
					 
					 System.out.println("heres hoping");
				 tempConflict.addAll(pack.getConflicts());
				 }
				 }
			 }
			 System.out.println("Conflict of state :"+ state.toString()+ "="+ tempConflict.toString());
	   return tempConflict;
	   }
  
  
  
  
  
  
//Work on removing conflicting details
  public static boolean stateValid(ArrayList<String> state , List<Package> repo , List<String> constraints, List<String> conflicts){
	
	  
	  System.out.println("HEY WOW IT WORKED HOLY HECK"+ conflicts.toString()+"State is"+state.toString());
	  ArrayList<Package> alreadyAdded=new ArrayList<Package>();
	  ArrayList<Package> statePackList = new ArrayList<Package>();
	  
	  
	  
	  
	  
	 
		  
	  for(String s : state){	
		  System.out.print(s);
		  
		  
		  
		  ArrayList<String> stateAsArray= splitString(s);
			
	    	String nameCons= stateAsArray.get(0);
	    	String versionCons= stateAsArray.get(1); 
	    	String symbol;
	    	
	    	System.out.println("Splitted compare: " + nameCons+ " = " + versionCons);
	    	if(versionCons.equals("Any")){
	    		symbol= "Any";
	    		String[] c= nameCons.split("");
	    		nameCons= c[1];
	    	}
	    	else{
	    		
	    		symbol= stateAsArray.get(2);
	    	}
		   nameCons= nameCons.replace("+", "");
	  for (Package p : repo){
	  		
		if((p.getName().equals(nameCons)&&versionCons.equals("Any"))
				
    		    ||
    		    (p.getName().equals(nameCons)&&versionCompare(p.getVersion(),versionCons,symbol))){
	    		statePackList.add(p);
	    		} 
		 }
	  	}
	  
	  
	  
	  
	  for(Package pack: statePackList){
		  System.out.println(pack.getName()+ "=" + pack.getVersion());  
	  for(String confl: conflicts){
		  System.out.println("already added" +alreadyAdded.toString());
		  System.out.println("THE CONFL is" + confl);
		  ArrayList<String> stateAsArray= splitString(confl);
		 	System.out.println("Cons should be" +stateAsArray.toString());
	    	String nameCons= stateAsArray.get(0);
	    	String versionCons= stateAsArray.get(1); 
	    	String symbol;
	    	
	    	System.out.println("Splitted compare: " + nameCons+ " = " + versionCons);
	    	if(versionCons.equals("Any")){
	    		symbol= "Any";
	    	
	    	}
	    	else{
	    		
	    		symbol= stateAsArray.get(2);
	    	}
	  
	    	if( (pack.getName().equals(nameCons) && versionCons.equals("Any"))||
	    	   (pack.getName().equals(nameCons) && versionCompare(pack.getVersion(),versionCons,symbol)))
	    	{
		    		System.out.println("CONFLICT DETECTED------------------------------------");
			    	return false;
		    		}
			  else if(alreadyAdded.contains(pack)){
				 System.out.println("DEPENDANCY ALREADY CONTAINED, NOT VALID-----------------");
				  return false;
					  }
			  else{
				  alreadyAdded.add(pack);
				  
			  }
		  
		  
	  	
	  }
	  
	  }
	  
	  
	  
	return true;
	  
	  
	 
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
	  
	  if(input.contains(">=")){
		  input= input.replace("[", "");
		  input= input.replace("]", "");
	
		  String [] splitted=input.split(">=");
			 returned.add(splitted[0]);
			 returned.add(splitted[1]);
			 returned.add(">=");
			
			 return returned;
	  }
	  else if(input.contains("<=")){
		  input= input.replace("[", "");
		  input= input.replace("]", "");
	
		  String [] splitted=input.split("<=");
			 returned.add(splitted[0]);  
			 returned.add(splitted[1]);
			 returned.add("<=");
			 return returned;
	  }
	  else if(input.contains("=")){
		  
		  input= input.replace("[", "");
		  input= input.replace("]", "");
	
		  
		  
			 String [] splitted=input.split("=");
			 
			 returned.add(splitted[0]);
			 returned.add(splitted[1]);
			 returned.add("=");
			
			 
			  return returned;
		  }
	  else if(input.contains(">")){
		  input= input.replace("[", "");
		  input= input.replace("]", "");
	
		  String [] splitted=input.split(">");
			 returned.add(splitted[0]);
			 returned.add(splitted[1]);
			 returned.add(">");
			 return returned;
	  }
	  else if(input.contains("<")){
		  input= input.replace("[", "");
		  input= input.replace("]", "");
	
		  String [] splitted=input.split("<");
			 returned.add(splitted[0]);
			 returned.add(splitted[1]);
			 returned.add("<");
			 return returned;
	  }
	  else{
		  returned.add(input);
		  returned.add("Any");
		  return returned;
	  }
	  
	  
  }

		
		
		

		// TODO Auto-generated method stub
		

  //Checks full size of dependancy for item to compare.
/*
* 
*
public static HashMap<ArrayList<Package>, Integer> dependancySize(List<Package> repo, List<String> dependancies){
	  System.out.println("DEPENDANCY START");
	  System.out.println(dependancies.toString());
	  int size= 0;
	   ArrayList<Integer> sizeList = new ArrayList<Integer>();
	   
	   HashMap<ArrayList<Package>, Integer> h= new HashMap<ArrayList<Package>,Integer>();
	  for(String dependant: dependancies){
		  
		 	ArrayList<String> cons= splitString(dependant);
			
	       	
	    	String nameCons= cons.get(0);
	    	String versionCons= cons.get(1);
	    	
		  for(Package p : repo){
			  ArrayList<Package> packList= new ArrayList<Package>();
			  if(p.getName().equals(nameCons)){
				    
				  if(p.getDepends().size()==0){
					  
					  size=size+p.getSize();
					  packList.add(p);
					  sizeList.add(size);
				  }
				  else{
					  for(List<String> clause: p.getDepends()){
						  packList.add(p);
						  size= p.getSize();
						  for(int i : dependancySize(repo,clause).values()){
							  System.out.println(i);
							  size=size+i;
						  }
						  
					  }
					  
				  }
				  h.put(packList, size);
				  }
			  }
		  }
	  
	  System.out.println("PackList"+ h.entrySet());
	  System.out.println("SizeList" + sizeList.toString());
	  System.out.println("The full size of this item is "+ size);
	  return h;
}
	  */
	  
	

    
    
	
	}