
/*Fenil Tailor
 * Please provide exact path to pgm of the fusedata files
 * pgm gives output in the console
 * If filesize is >0 & <4096 and indirect 1,pgm will simply say that the data can itself fit into location block and it'll not go to the blocks that are pointed in the location block.These block should be there in free block list otherwise free block list is incorrect
 * If filesize is >4096 then it checks that indirect is 1 and read the the file from first going on to the block no pointed by the loc block.
 * pgm checks linkcount,parent directory,current directory,atime,ctime,mtime.
 * pgm checks the deviceid which is hardcoded as 20
 * pgm checks the constrains regarding to the free block list and use block list.
 */




import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.ObjectInputStream.GetField;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.StringTokenizer;
import java.util.concurrent.CountDownLatch;
import java.io.*;

import javax.print.attribute.IntegerSyntax;

import org.omg.CORBA.FREE_MEM;


public class csefsck6 {
	
	
	static List<String> list = new ArrayList<String>();
	static List<String> listfree=new ArrayList<String>();
	static List<String> data=new ArrayList<String>();
	static File file;
	static BufferedReader reader = null;
	static String text,arr[],arr1[],arr2[];
	static StringTokenizer st1,st2,st3,stoken;
	static boolean divid=false,linkc=false;//currd=false,rootd=false;
	static int i=-1,j,a,b;
	//static List<Integer> usedblock=new ArrayList<Integer>();
	static HashSet<Integer> usedblock=new HashSet<Integer>();
	static HashSet<Integer> fullblocklist=new HashSet<Integer>();
	static HashSet<Integer> listfree1=new HashSet<Integer>();
	static HashSet<Integer> shoulfbeinlf1=new HashSet<Integer>();

	
	
	public static void recurfindirect(int loc)
	{
		try
		{
		i=-1;
		file=new File("C:\\Assignments\\OS\\FS\\fusedata."+loc);
		reader=new BufferedReader(new FileReader(file));
		text=reader.readLine();
		st1=new StringTokenizer(text,",");
    	while(st1.hasMoreTokens())
    	{
    		shoulfbeinlf1.add(Integer.pa rseInt(st1.nextToken()));
    	}
		}
		catch(Exception e)
		{
			System.out.println(e.getMessage());
		}
		
		
	}
	
	public static void recurf1(int blockno,int size)
	{
	
		try
		{
		i=-1;
		int i1=1;
		file=new File("C:\\Assignments\\OS\\FS\\fusedata."+blockno);
		reader=new BufferedReader(new FileReader(file));
		text=reader.readLine();
		st1=new StringTokenizer(text,",");
    	String arr1[]=new String[st1.countTokens()];
    	while(st1.hasMoreTokens())
    	{
    		arr1[++i]=st1.nextToken();
    	}
    	while(size>(4096*i1))
    	{
    		i1++;
    	}
    	if(i1==arr1.length)
    	{
    		for(int k=0;k<arr1.length;k++)
    			usedblock.add(Integer.parseInt(arr1[k]));
    		
    	System.out.println("File has been read suceesfully from block no's");
    	for(int k1=0;k1<arr1.length;k1++)
    	System.out.print(arr1[k1]+" ");
    	System.out.println();
    	}
    	else if(i1>arr1.length)
    	{
    		System.out.println("Indirect array of blocks are not sufficient for this size");
    	}
    	else if(i1<arr1.length)
    	{
    		System.out.println("No need of these many indirect array of blocks.!!! Only "+i1+" no of blocks are needed but there are "+arr1.length+" no of blocks allocated");
    	}
    	
		}
		catch(Exception e)
		{
			System.out.println(e.getMessage());
		}
		
	}
	
	public static void recurf(int f)
	{
	
		try
		{
			i=-1;
		file=new File("C:\\Assignments\\OS\\FS\\fusedata."+f);
		reader=new BufferedReader(new FileReader(file));
		text=reader.readLine();
		text=text.substring(1,text.length()-1);
		//System.out.println(text);
		st1=new StringTokenizer(text,",");
    	String arr1[]=new String[st1.countTokens()];
    	while(st1.hasMoreTokens())
    	{
    		arr1[++i]=st1.nextToken();
    	}
    	st2=new StringTokenizer(arr1[0],":");
		st2.nextToken();
		int size=Integer.parseInt(st2.nextToken());
		
		
		st2=new StringTokenizer(arr1[5],":");
		st2.nextToken();
		int atime=Integer.parseInt(st2.nextToken());
		if(atime>(System.currentTimeMillis()/1000))
    	System.out.println("atime is in future...need to replace it with "+(System.currentTimeMillis()/1000)+" this time for file "+f);
    	else
    	System.out.println("atime is good for file"+f);
		
		st2=new StringTokenizer(arr1[6],":");
    	st2.nextToken();
    	int ctime=Integer.parseInt(st2.nextToken());
    	if(ctime>(System.currentTimeMillis()/1000))
        System.out.println("ctime is in future...need to replace it with "+(System.currentTimeMillis()/1000)+" this time for file "+f);
    	else
        System.out.println("ctime is good for file "+f);
    	
    	st2=new StringTokenizer(arr1[7],":");
    	st2.nextToken();
    	int mtime=Integer.parseInt(st2.nextToken());
    	if(mtime>(System.currentTimeMillis()/1000))
        System.out.println("mtime is in future...need to replace it with "+(System.currentTimeMillis()/1000)+" this time for file"+f);
    	else
        System.out.println("mtime is good for file "+f);
    
		
		
		
    	
    	st2=new StringTokenizer(arr1[8]," ");
    	st3=new StringTokenizer(st2.nextToken(),":");
    	st3.nextToken();
    	String indblcno=st3.nextToken();
    	if(st2.hasMoreTokens())
    	{
    	st3=new StringTokenizer(st2.nextToken(),":");
    	st3.nextToken();
    	String loc=st3.nextToken();
    	usedblock.add(Integer.parseInt(loc));
    	if(Integer.parseInt(indblcno)!=0)
    		{
    		if(size>4096 && size<=1638400)
    		{
    		System.out.println("There should be Indirect block..Good to go");
    		recurf1(Integer.parseInt(loc),size);
    		}
    		else if(size>0 && size<=4096)
    		{
    		System.out.println("Indirect index should be 0 not 1 and data can fit in block "+loc);
    		recurfindirect(Integer.parseInt(loc));
    		}
    		}
    	else
    	{
    		if(size>0 && size<=4096)
    		{
    		System.out.println("Index block is 0..and size is within limit...Good to go and data is in block "+loc);
    		System.out.println();
    		}
    		//else
    		//{
    		//System.out.println("There should be indirect block to manage these much data");
    		//System.out.println();
    		//}
    	}
    	}

    	
    		
		
		
		
		}
		catch(Exception e)
		{
			System.out.println(e.getMessage());
		}
		
	}
	
	
	public static void recur(int a,int b)
	{
	if(!(usedblock.contains(new Integer(a))))
	{
		//System.out.println("1var");
		usedblock.add(a);
		i=-1;
    	try
    	{
    		//System.out.println(a+"hi "+b);
    	boolean currd=false;boolean rootd=false;
    	file=new File("C:\\Assignments\\OS\\FS\\fusedata."+a);
    	//System.out.println(file+"hi");
    	reader=new BufferedReader(new FileReader(file));
    	text=reader.readLine();
    	text=text.substring(1,text.length()-1);
    	data.add(text);
    	st1=new StringTokenizer(text,",");
    	String arr1[]=new String[st1.countTokens()];
    	int cnttokens=st1.countTokens();
    	//System.out.println(st1.countTokens());
    	while(st1.hasMoreTokens())
    	{
    		arr1[++i]=st1.nextToken();
    	}
    		st2=new StringTokenizer(arr1[4],":");
    		st2.nextToken();
    		int atime=Integer.parseInt(st2.nextToken());
    		if(atime>(System.currentTimeMillis()/1000))
        	System.out.println("atime is in future...need to replace it with "+(System.currentTimeMillis()/1000)+" this time for directory "+a);
        	else
        	System.out.println("atime is good for directory "+a);
    		
    		st2=new StringTokenizer(arr1[5],":");
        	st2.nextToken();
        	int ctime=Integer.parseInt(st2.nextToken());
        	if(ctime>(System.currentTimeMillis()/1000))
            System.out.println("ctime is in future...need to replace it with "+(System.currentTimeMillis()/1000)+" this time for directory "+a);
        	else
            System.out.println("ctime is good for directory "+a);
        	
        	st2=new StringTokenizer(arr1[6],":");
        	st2.nextToken();
        	int mtime=Integer.parseInt(st2.nextToken());
        	if(mtime>(System.currentTimeMillis()/1000))
            System.out.println("mtime is in future...need to replace it with "+(System.currentTimeMillis()/1000)+" this time for directory "+a);
        	else
            System.out.println("mtime is good for directory "+a);
        	
        	
    	
    	
    	
    	st2=new StringTokenizer(arr1[7],":");
    	st2.nextToken();
    	int linkcount=Integer.parseInt(st2.nextToken());
    	//System.out.println(linkcount);
    	if(linkcount==cnttokens-8)
    	{
    		linkc=true;
    		System.out.println("Link count is matched for directory "+ a);
    		System.out.println();
    	}
    	else
    	{
    		System.out.println("Link count is not matched for directory "+ a +" it should be "+(cnttokens-8));
    		System.out.println();
    	}
    	
    	st2=new StringTokenizer(arr1[8],":");
    	st2.nextToken();
    	String s31=st2.nextToken();
    	if(s31.charAt(2)=='f')
    	{
    		st2.nextToken();
    		//System.out.println(st2.nextToken());//no of file to open
    		String s32=st2.nextToken();
    		if(s32.contains(String.valueOf('}')))
            	s32=s32.substring(0,s32.length()-1);
    		System.out.println("**********");
    		System.out.println("Block no of file to open is "+s32);
    		System.out.println();
    		usedblock.add(Integer.parseInt(s32));
    		recurf(Integer.parseInt(s32));
    	}
    	else if(s31.charAt(2)=='d')
    	{
    		String s41=st2.nextToken();
    		System.out.println();
    		System.out.println("Directory name is "+s41);
			String s51=st2.nextToken();
			if(s51.contains(String.valueOf('}')))
            	s51=s51.substring(0,s51.length()-1);
			if(s41.matches("."))
			{
			  currd=true;
			  System.out.println("Current direcory is present for "+ a);
			  System.out.println();
			  if(s51.matches(String.valueOf(a)))
			  {
				System.out.println("Value matches and current directory is "+ a);
				System.out.println();
			  }
			  else
			  {
				  System.out.println("Value is not matching as value is "+ s51+ "and value should be "+ a);
				  System.out.println();
			  }
			  
			}
			else if(s41.matches(".."))
			{
				 rootd=true;
				 System.out.println("Root direcory is present for "+ a);
				 System.out.println();
				  if(s51.matches(String.valueOf(b)))
				  {
					System.out.println("Value matches and root directory is "+ b);
				  }
				  else
				  {
					  System.out.println("Value is not matching as value is "+ s51+ "and value should be "+ b);
				  }
				
			}
			else
            {
            //if(s5.contains(String.valueOf('}')))
            	//s5=s5.substring(0,s5.length()-1);
            
           // System.out.println(Integer.parseInt(s5));
			b=a;
            a=Integer.parseInt(s51);
            System.out.println("*********");
            System.out.println("Directory now open up is "+a+" which has name "+s41);
            System.out.println();
            //usedblock.add(a);
            
            recur(a,b);
            }
			
    	}
    	for(int k=9;k<cnttokens;k++)
    	{
    		st2=new StringTokenizer(arr1[k],":");
    		String sdf=st2.nextToken();
    		if(sdf.matches(" d"))
    		{
    			String s4=st2.nextToken();
    			System.out.println();
    			System.out.println("Directory name is "+s4);
    			String s5=st2.nextToken();
    			if(s5.contains(String.valueOf('}')))
                	s5=s5.substring(0,s5.length()-1);
    			if(s4.matches("."))
    			{
    				System.out.println("Current direcory is present for "+ a);
    				System.out.println();
    				currd=true;
    			  if(s5.matches(String.valueOf(a)))
    			  {
    				
    				System.out.println("Value matches and cur dir is "+ a);
    				System.out.println();
    			  }
    			  else
    			  {
    				  System.out.println("Value is not matching as value is "+ s5+ "and value should be "+ a);
    				  System.out.println();
    			  }
    			}
    				
                if(s4.matches(".."))
                {
                	System.out.println("Root direcory is present for "+ a);
                	System.out.println();
                	rootd=true;
                	if(s5.contains(String.valueOf('}')))
                    	s5=s5.substring(0,s5.length()-1);
                    
                	if(s5.matches(String.valueOf(b)))
                	{
                	System.out.println("Value matches and root dir is "+b);
                	System.out.println();
                	}
                	else
                	{
                		 System.out.println("Value is not matching as value is "+ s5 +" and value should be "+ b);
                		 System.out.println();
                	}
                }
                else
                {
                /*if(s5.contains(String.valueOf('}')))
                	s5=s5.substring(0,s5.length()-1);
                System.out.println(s5);
                recur(Integer.parseInt(s5), a);
                usedblock.add(Integer.parseInt(s5));*/
                	
                	//if(s5.contains(String.valueOf('}')))
                    	//s5=s5.substring(0,s5.length()-1);
                    
                   // System.out.println(Integer.parseInt(s5));
                	b=a;
                    a=Integer.parseInt(s5);
                    System.out.println("**************");
                    System.out.println("Directory now open up is "+a+" whose name is "+s4);
                    System.out.println();
                    //usedblock.add(a);
                    recur(a,b);
                }
    			
    		}
    		else if(sdf.matches(" f"))
    		{
    			String s4=st2.nextToken();
    			System.out.println(s4);
    			String s5=st2.nextToken();
    			if(s5.contains(String.valueOf('}')))
                	s5=s5.substring(0,s5.length()-1);
    			System.out.println("************");
    			System.out.println("Block no of file to open is "+s5);
    			System.out.println();
    			usedblock.add(Integer.parseInt(s5));
    			recurf(Integer.parseInt(s5));
    		}
    		//file hoy to else no cse function banavanu vicharyu
    	}
    	
    	
    	
    	if(rootd==false)
    	{
    		System.out.println("Root dir is not present for dir "+a);
    		System.out.println();
    	}
    	if(currd==false)
    	{
    		System.out.println("Current dir is not present for dir "+a);
    		System.out.println();
    	}
        
    	
    	
    	

    	
    	
    	
    	}
    	catch(Exception e)
    	{
    		System.out.println(e.getMessage());
    	}
	 }
	}
    
    

		

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		//List<String> list = new ArrayList<String>();
		//List<String> listfree=new ArrayList<String>();
		//List<String> data=new ArrayList<String>();
		file = new File("C:\\Assignments\\OS\\FS\\fusedata.0");
		//BufferedReader reader = null;
		//String text,arr[],arr1[],arr2[];
		//StringTokenizer st1,st2,st3;
		//boolean divid=false,linkc=false,currd=false,rootd=false;
		//int i=-1,j;

		try 
		{
		    reader = new BufferedReader(new FileReader(file));
            //list.add(reader.readLine());		    
		    text=reader.readLine();
		    text=text.substring(1, text.length()-1);
		    list.add(text);
		    //System.out.println(text);
		    st1=new StringTokenizer(text,",");
		    //st1.nextToken();st1.nextToken();
		    //text=st1.nextToken();
		    //System.out.println(text);
		    //while(st1.hasMoreTokens())
		    	//System.out.println(st1.nextToken());
		   /* st1.nextToken();
		    st1.nextToken();
		    text=st1.nextToken();*/
		    //st2=new StringTokenizer(text,":");
		    //st2.nextToken();
		   //System.out.println(st2.nextToken());
                 		 
		    //if(st2.nextToken().matches("20"))
		    //{
		    //divid=true;
		   // }
		   
		    arr=new String[st1.countTokens()];
		    while(st1.hasMoreTokens())
		    {
		    	arr[++i]=st1.nextToken();
		    }
		    st2=new StringTokenizer(arr[0],":");
    		st2.nextToken();
    		int creationtime=Integer.parseInt(st2.nextToken().substring(1));
    		//System.out.println(creationtime);
    		if(creationtime>(System.currentTimeMillis()/1000))
        	System.out.println("creation time is in future...need to replace it with "+(System.currentTimeMillis()/1000)+" this time for superblock  0");
        	else
        	System.out.println("Creation time is good for superblock 0");
		    
		    
		    st2=new StringTokenizer(arr[2],":");
		    st2.nextToken();
		    if(st2.nextToken().matches("20"))
			  {
			    divid=true;
			  }
		
		    
            st2=new StringTokenizer(arr[3],":");
            st2.nextToken();
            int startfb=Integer.parseInt(st2.nextToken());
            j=startfb;
            
            
            st2=new StringTokenizer(arr[4],":");
            st2.nextToken();
            int endfb=Integer.parseInt(st2.nextToken());
            
            for(int i1=startfb;i1<=endfb;i1++)
            	usedblock.add(i1);
            
            st2=new StringTokenizer(arr[5],":");
            st2.nextToken();
            int root=Integer.parseInt(st2.nextToken());
            usedblock.add(root);
            
            st2=new StringTokenizer(arr[6],":");
            st2.nextToken();
            int maxb=Integer.parseInt(st2.nextToken());
            System.out.println("Maximum block size is "+maxb+" Starting of the free block number is  "+startfb+" Ending of free block number is  "+endfb);
            System.out.println();
            
            for(int k3=1;k3<=startfb+maxb-2;k3++)
            	fullblocklist.add(k3);
            
            	
            if(maxb==(endfb-startfb+1)*400)
            {
              System.out.println("Block size is OK as maximum blocksize "+maxb+" = ("+endfb+" - "+startfb+" +1) "+" *400");
              while(j!=endfb+1)
              {
            	String s1="C:\\Assignments\\OS\\FS\\fusedata."+j;
            	//System.out.println(s1);
            	 file = new File(s1);
            	 try
            	 {
            		 reader=new BufferedReader(new FileReader(file));
            		 text=reader.readLine();
            		 text=text.substring(0,text.length());
            		 listfree.add(text);
            		 j++;
            	 }
            	 catch (FileNotFoundException e) 
         		{
         		    e.printStackTrace();
         		}
         		catch (IOException e) 
         		{
         		    e.printStackTrace();
         		}
            	
              }
            	 
            }
            else
            {
            	System.out.println("Block size is not matched as it should be "+((endfb-startfb+1)*400)+" but it is given as "+ maxb);
            	System.out.println();
            }
            
            if(divid)
            {
            	boolean rootd=false,currd=false;
            	System.out.println("Device Id is correct and it's vaue is 20");
            	i=-1;
            	try
            	{
            	System.out.println("***************");
            	System.out.println("Directory now open is root");
            	file=new File("C:\\Assignments\\OS\\FS\\fusedata."+root);
            	reader=new BufferedReader(new FileReader(file));
            	text=reader.readLine();
            	text=text.substring(1,text.length()-1);
            	data.add(text);
            	st1=new StringTokenizer(text,",");
            	arr1=new String[st1.countTokens()];
            	int cnttokens=st1.countTokens();
            	//System.out.println(st1.countTokens());
            	while(st1.hasMoreTokens())
            	{
            		arr1[++i]=st1.nextToken();
            	}
            	/*if(Integer.parseInt(arr1[5])>(System.currentTimeMillis()/1000))
            	System.out.println("atime is in future...need to replace it with "+System.currentTimeMillis()+" this time for directory"+root);
            	else
            	System.out.println("atime is good for directory"+root);
            	if(Integer.parseInt(arr1[6])>(System.currentTimeMillis()/1000))
                System.out.println("ctime is in future...need to replace it with "+System.currentTimeMillis()+" this time for directory"+root);
            	else
                System.out.println("ctime is good for directory"+root);
            	if(Integer.parseInt(arr1[7])>(System.currentTimeMillis()/1000))
                System.out.println("mtime is in future...need to replace it with "+System.currentTimeMillis()+" this time for directory"+root);
            	else
                System.out.println("mtime is good for directory"+root);*/
            	
            	
            	st2=new StringTokenizer(arr1[4],":");
        		st2.nextToken();
        		int atime=Integer.parseInt(st2.nextToken());
        		if(atime>(System.currentTimeMillis()/1000))
            	System.out.println("atime is in future...need to replace it with "+(System.currentTimeMillis()/1000)+" this time for directory "+root);
            	else
            	System.out.println("atime is good for directory "+root);
        		
        		st2=new StringTokenizer(arr1[5],":");
            	st2.nextToken();
            	int ctime=Integer.parseInt(st2.nextToken());
            	if(ctime>(System.currentTimeMillis()/1000))
                System.out.println("ctime is in future...need to replace it with "+(System.currentTimeMillis()/1000)+" this time for directory "+root);
            	else
                System.out.println("ctime is good for directory "+root);
            	
            	st2=new StringTokenizer(arr1[6],":");
            	st2.nextToken();
            	int mtime=Integer.parseInt(st2.nextToken());
            	if(mtime>(System.currentTimeMillis()/1000))
                System.out.println("mtime is in future...need to replace it with "+(System.currentTimeMillis()/1000)+" this time for directory "+root);
            	else
                System.out.println("mtime is good for directory "+root);
            
            	
            	st2=new StringTokenizer(arr1[7],":");
            	st2.nextToken();
            	int linkcount=Integer.parseInt(st2.nextToken());
            	//System.out.println(linkcount);
            	if(linkcount==cnttokens-8)
            	{
            		linkc=true;
            		System.out.println("Link count matches for root and it's directory is "+root +" and below is the info of files,dir that "+root+" dir contains");
            		System.out.println();
            	}
            	else
            	{
            		System.out.println("Link count is not matched it should be "+(cnttokens-8));
            		System.out.println();
            	}
            	
            	st2=new StringTokenizer(arr1[8],":");
            	st2.nextToken();
            	String s32=st2.nextToken();
            	if(s32.charAt(2)=='f')
            	{
            		st2.nextToken();
            		//System.out.println(st2.nextToken());//no of file to open
            		String s34=st2.nextToken();
            		if(s34.contains(String.valueOf('}')))
                    	s34=s34.substring(0,s34.length()-1);
            		System.out.println("*************");
            		System.out.println("Block no of file to open "+s34);
            		System.out.println();
            		usedblock.add(Integer.parseInt(s34));
            		recurf(Integer.parseInt(s34));
            	}
            	else if(s32.charAt(2)=='d')
            	{
            		String s41=st2.nextToken();
            		System.out.println(s41);
            		System.out.println();
        			String s51=st2.nextToken();
        			if(s51.contains(String.valueOf('}')))
                    	s51=s51.substring(0,s51.length()-1);
        			   System.out.println();
        			if(s41.matches("."))
        			{
        				currd=true;
        			  if(s51.matches(String.valueOf(root)))
        			  {
        				
        				System.out.println("Value matches and current directory is "+root);
        				System.out.println();
        			  }
        			  else
        			  {
        				  System.out.println("Value is not matching as value is "+ s51+ "and value should be "+ root);
        				  System.out.println();
        			  }
        			  
        			}
        			else if(s41.matches(".."))
        			{
        				 rootd=true;
        				  if(s51.matches(String.valueOf(root)))
        				  {
        					
        					System.out.println("Value matches and root directory is "+root);
        					System.out.println();
        				  }
        				  else
        				  {
        					  System.out.println("Value is not matching as value is "+ s51+ "and value should be "+ root);
        					  System.out.println();
        				  }
        				  
        				  
        				  
        				  
        				  
        				
        			}
        			else
                    {
                    //if(s5.contains(String.valueOf('}')))
                    	//s5=s5.substring(0,s5.length()-1);
                    
                   // System.out.println(Integer.parseInt(s5));
                    a=Integer.parseInt(s51);
                    System.out.println("************");
                    System.out.println("Directory now open up is "+a+" whose value is "+s41);
                    System.out.println();
                    //usedblock.add(a);
                    b=root;
                    recur(a,b);
                    }
            	}
            	//System.out.println(arr1.length);
            	for(int k1=9;k1<cnttokens;k1++)
            	{
            		
            		st2=new StringTokenizer(arr1[k1],":");
            		
            		String sdf=st2.nextToken();
            		//stoken=new StringTokenizer(arr1[k],":");
            		if(sdf.matches(" d"))
            		{
            			String s4=st2.nextToken();
            			System.out.println();
            			System.out.println("Directory name is "+ s4);
            			System.out.println();
            			String s5=st2.nextToken();
            			if(s5.contains(String.valueOf('}')))
                        	s5=s5.substring(0,s5.length()-1);
            			
            			if(s4.matches("."))
            			{
            				currd=true;
            			  if(s5.matches(String.valueOf(root)))
            			 {
            				  System.out.println("Value matches and current directory is "+root);
              				  System.out.println();
          			     }
          			  else
          			     {
          				  System.out.println("Value is not matching as value is "+ s5+ "and value should be "+ root);
          				  System.out.println();
          		     	 }
            			}
            				
            			else if(s4.matches(".."))
                        {
            				rootd=true;
                        	if(s5.matches(String.valueOf(root)))
                        	{
                        		System.out.println("Value matches and root directory is "+root);
                				System.out.println();
                        	}
                        	else
                        	{
                        		System.out.println("Value is not matching as value is "+ s5+ "and value should be "+ root);
                        	}
                        }
                        else
                        {
                        //if(s5.contains(String.valueOf('}')))
                        	//s5=s5.substring(0,s5.length()-1);
                        
                       // System.out.println(Integer.parseInt(s5));
                        a=Integer.parseInt(s5);
                        if((!usedblock.contains(new Integer(a))))
                        {
                        System.out.println("Directory now open up is "+a+" whose name is "+s4);
                        System.out.println("***********");
                        }
                        else
                        System.out.println("Already opened that directory which has no "+a+" once...!!!");
                        System.out.println();
                        //usedblock.add(a);
                        b=root;
                        recur(a,b);
                        //System.out.println("have thay");
                        }
            			
            		}
            		//file hoy to else no cse function banavanu vicharyu
            		else if(sdf.matches(" f"))
            		{
            			String s4=st2.nextToken();
            			System.out.println(s4);
            			String s5=st2.nextToken();
            			if(s5.contains(String.valueOf('}')))
                        	s5=s5.substring(0,s5.length()-1);
            			System.out.println("**************");
            			System.out.println("Block number of file is "+ s5);
            			System.out.println();
            			usedblock.add(Integer.parseInt(s5));
            			recurf(Integer.parseInt(s5));
            		}
            	}
            	
            	
            	
                
            	
            	if(rootd==false)
            		System.out.println("Cuurent dir is not present for dir "+root);
            	if(currd==false)
            		System.out.println("Root dir is not present for dir "+root);
            	
 
            	
            	
            	
            	}
            	catch(Exception e)
            	{
            		System.out.println(e.getMessage());
            	}
            	
            	
            	
            }
            else
        	{
        		System.out.println("Device id is not correct it should be 20");
        		System.out.println();
        	}
            
        
            
            
		    
		} 
		catch (FileNotFoundException e) 
		{
		    e.printStackTrace();
		}
		catch (IOException e) 
		{
		    e.printStackTrace();
		}
		finally 
		{
		    try {
		        if (reader != null)
		        {
		            reader.close();
		        }
		        } 
		    catch (IOException e) 
		    {
		    }
		}
           
		
		//while(stoken.hasMoreTokens())
			//System.out.println(stoken.nextToken());
		System.out.println();
		System.out.println();
		//System.out.println(list);
		System.out.println("Free block list is as follows as given in the files");
		System.out.println(listfree);
		//System.out.println(data);
		//System.out.println(linkc+" "+currd+" "+rootd);
		System.out.println("Used block list is as follows");
		System.out.println(usedblock);
		System.out.println();
		//System.out.println(fullblocklist);
		//HashSet<Integer> sp7=new HashSet<Integer>(fullblocklist);
		//sp7.removeAll(usedblock);
		//sp7.removeAll(listfree);
		//sp7.removeAll(listfree1);
		StringTokenizer sp6=new StringTokenizer(listfree.toString().substring(1,listfree.toString().length()-1)," ,");
		while(sp6.hasMoreTokens())
		{
			listfree1.add(Integer.parseInt(sp6.nextToken()));
		}
		HashSet<Integer> s78=new HashSet<Integer>(fullblocklist);
		s78.removeAll(listfree1);
		s78.removeAll(usedblock);
		if(listfree1.equals(fullblocklist.removeAll(usedblock)))
		System.out.println("Free block list is accurate");
		else if(!(s78.isEmpty()))
		System.out.println("Free block list is not accurate as there should be blocks "+s78+" in the free block list");
		else
		System.out.println("Free block list is not accurate");
		//System.out.println(usedblock.retainAll(listfree));
		
		HashSet<Integer> sp4=new HashSet<Integer>(usedblock);
		sp4.retainAll(listfree1);
		//System.out.println(sp4);
		//if(usedblock.retainAll(listfree))
		if(sp4.isEmpty())
			System.out.println("There are no files/directories stored on items listed in the free block list");
		else
			System.out.println("There are some blocks "+sp4+" which are stored but also in the free blocked list");
		
			
	

	}

}





