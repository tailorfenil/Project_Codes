/************
 * Fenil Tailor
 * code first asks the filename and then ask which algo do you want to run(PFF/VSWS)
 * Output shows the whole trace of the algorithm
 * Pgm dynamically decides the Value of F for PFF and M,L,Q for the VSWS by looking through the whole file once before algo start to produce result and find out the most popular element in the file which is repeating consecutively most number of time
 * Used Hashmap to refer page frames.If bit is set to 1 then page is currently in Resident Set and if it is 0 then it is not present in the Resident Set
 * At each page reference,pgm showed the resident set and the logic behind the page reference  
 */


import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;


public class mem_management2 {
	static File file;
	static ArrayList<Integer> wholefile=new ArrayList<Integer>();  //To store the file
	static HashMap<Integer, Integer> m1=new HashMap<Integer,Integer>(); //To keep track of the resident set
	//static HashMap<Integer, Integer> m2=new HashMap<Integer,Integer>();
    //static File file;
	//static ArrayList<Integer> wholefile=new ArrayList<Integer>();
	static ArrayList<Integer> wholefile1=new ArrayList<Integer>();
	//static HashMap<Integer, Integer> m1=new HashMap<Integer,Integer>();
	static HashMap<Integer, Integer> m2=new HashMap<Integer,Integer>(); //To Reset the resident set to the pages that used during previous interval
	int pagefaultcnt=0;
	static int cnt=0;
	static int totalpagefault=0;  //Total pagefault in the algorithm
	
	public static void pff(String fname)
	{
		int maxmem=0,max=0;
		int pagefaultcnt=0;
		file=new File("C:\\Assignments\\OS\\hw-3\\"+fname);
		Scanner sc=new Scanner(System.in);
		//System.out.println("Enter the value of F");
		//int F=sc.nextInt();
		int F;
		System.out.println("********************");
		System.out.println();
		try (BufferedReader br = new BufferedReader(new FileReader(file))) 
		{
		    String line;
		    int j=0,k=0,temp=0,tempcount,count=1,popular;
		    while ((line = br.readLine()) != null) 
		    {
		    	//System.out.println(line);
		    	wholefile.add(Integer.parseInt(line));
		    }
		    popular=wholefile.get(0);
		    for (int i6 = 0; i6 < wholefile.size()-1; i6++)   //These loop is to find out the maximum count of similar element which are consecutive
		    {
		      temp = wholefile.get(i6);
		      tempcount = 1;
		      //for (int j6 = 1; j6 < wholefile.size(); j6++)
		      //{
		      int j6=i6+1;
		      while(temp==wholefile.get(j6))
		    		  {
		          tempcount++;
		          j6++;
		    		  }
		      //}
		      if (tempcount > count)
		      {
		        popular = temp;
		        count = tempcount;
		      }
		    }
		    //System.out.println(popular);
		   //System.out.println(count);
		    int sum=0;
            for(int i7=1;i7<=wholefile.get(0);i7++)
            {
            	sum =sum+ wholefile.get(i7);
            }
            //System.out.println(sum+"df");
            if(sum==(wholefile.get(0)*((wholefile.get(0)+1)))/2)
            F=1;  // It can be case that page references came on any sequence till the maximum pages without repitition and because of this we pagefault for each and every page reference(example: page count-5 and 
            //references came in any combination of (1,2,3,4,5) then it has no dependency on F.
            else
		    F=count-1;//choosing the value of F based upon the count of the maximum repeating consecutive element from the file minus 1 
		    //That way we can reduce the pagefault and eventually will decrease I/O time by keeping the resident set large enough.
            
            System.out.println("PGM chooses the value of F is "+F);
		   
		    
		 System.out.println("Maximum number of pages the process occupies "+wholefile.get(0));
		 
		 for(int i=1;i<=wholefile.get(0);i++)
			 m1.put(i, 0);
		 
		 System.out.println("Initially the page and  reference bit is 0");
		 System.out.println(m1);
		 System.out.println();
       		 
		 for(int i=1;i<wholefile.size();i++)
		 {
			 System.out.println("The page reference came is "+wholefile.get(i));
			 if(m1.get(wholefile.get(i))==0)
			 {
		     pagefaultcnt+=1;
			 k=i;
			 m1.put(wholefile.get(i), 1);
			 
			 
			 if((k-j)>F)
			 {
				 for(int i1=1;i1<=wholefile.get(0);i1++)
					 m1.put(i1, 0);
				 
				 System.out.println("Pagefault occurs and time difference between page fault is "+(k-j)+" which is  greater than F which is "+F+" where current page fault time is "+k+" and previous page fault time is "+j);
				 
				 
				 for(int i2=j;i2<=k;i2++)
					 m1.put(wholefile.get(i2),1);
				 
				 System.out.println("Current resident set is "+ m1+" Time diffrence between page fault is grater than F which is "+F+" so need to reset bits except for page references between two page faults");
				 
				
				 System.out.println();
			 }
			 //System.out.println("Time diffrence between page fault is not grater than F so adding the page to the resident set");
			 else
			 {
			 System.out.println("Current resident set is "+ m1+" Pagefault occurs and time difference between page fault is "+(k-j)+" which is not greater than F which is "+F+" where current page faulf time is "+k+" and last page fault time is "+j);
			 System.out.println();
			 }
			 j=k;
			 
			 }
			 else
			 {
				 
				 System.out.println("No page Fault as page is already there in resident set");
				 System.out.println();
			 }
			 
			 
		 }
		 System.out.println("Total number of page fault cnt is "+pagefaultcnt);
		 System.out.println("Conslusion:PGM selecting the value of F based on maximum repeating consective element logic but If we increase the value of F then upto"
		  		+ "a certain limit the page fault count is decreases and then after Page fault count reamin constant with respect to the increasing F values");
		 
		 
		    
		       
		}
		catch(Exception e)
		{
			System.out.println(e.getMessage());
		}
		
	
		
	}
	
	
	public static void vsws(String fname)
	{
		int pagefaultcnt=0;
		file=new File("C:\\Assignments\\OS\\hw-3\\"+fname);
		Scanner sc=new Scanner(System.in);
		//System.out.println("Enter the value of M");
		//int M=sc.nextInt();
		//System.out.println("Enter the value of L");
		//int L=sc.nextInt();
		//System.out.println("Enter the value of Q");
		//int Q=sc.nextInt();
		int M,L,Q;
		
		System.out.println("********************");
		System.out.println();
		try (BufferedReader br = new BufferedReader(new FileReader(file))) 
		{
		    String line;
		    int j=0,k=0;
		    while ((line = br.readLine()) != null) 
		    {
		    	//System.out.println(line);
		    	wholefile.add(Integer.parseInt(line));
		    }
		    
		    int k10=0,temp=0,tempcount,count=1,popular;
		    while ((line = br.readLine()) != null) 
		    {
		    	//System.out.println(line);
		    	wholefile.add(Integer.parseInt(line));
		    }
		    popular=wholefile.get(0);
		    for (int i6 = 0; i6 < wholefile.size()-1; i6++)   //These loop is to find out the maximum count of similar element which are consecutive and according to this value,code will set the value of F 
		    {
		      temp = wholefile.get(i6);
		      tempcount = 1;
		      //for (int j6 = 1; j6 < wholefile.size(); j6++)
		      //{
		      int j10=i6+1;
		      while(temp==wholefile.get(j10))
		    		  {
		          tempcount++;
		          j10++;
		    		  }
		      //}
		      if (tempcount > count)
		      {
		        popular = temp;
		        count = tempcount;
		      }
		    }   //We have the popular element which is repeating consecutively maximum number of time
		    //System.out.println(popular);
		    //System.out.println(count);
		    if(count!=1)   //If the max consecutive similar element count is >1 then we are setting the value of Q to that count
		    	           //We are setting M be the Q+1(As by doing that we ensure that M>Q) and
		    	           //We are setting L be the Q+3(As by doing that We ensure that our resident set is large enough to process onto the next interval with minimum page fault 
		    {
            Q=count;
            M=count+1;
            L=count+3;
		    }
		    else   //If popular element count is 1 then code set the value  Q,M,L as follows
		    {
		    Q=2; 
		    M=4;
		    L=5;
		    }
		    System.out.println("PGM chooses tha value of M is "+M);
		    System.out.println("PGM chosses the value of L is "+L);
		    System.out.println("PGM chosses the value of Q is "+Q);
		    for(int i2=1;i2<wholefile.size();i2++)
		    	wholefile1.add(wholefile.get(i2));
		    	
		 System.out.println("Maximum number of pages the process occupies "+wholefile.get(0));
		 for(int i=1;i<=wholefile.get(0);i++)
			 m1.put(i, 0);
		 
		 for(int i=1;i<=wholefile.get(0);i++)
			 m2.put(i, 0);
		 
		 System.out.println("Initially the page and  reference bit is 0");
		 System.out.println(m1);
		 //System.out.println(m2);
		 
		 
		 //System.out.println(wholefile);
		 //System.out.println(wholefile1);
		 int flag=1;
		 for(int i=0;i<wholefile1.size();i++)
		 {
			//System.out.println("Cnt is "+cnt); 	 
			 
				 if((cnt%L!=0 || cnt==0) && flag==1)   //If the page reference count limit not reached to L 
				 {
					 
					 //if(M-1==cnt && pagefaultcnt>=Q)
					//	{
					//	 flag=0;
					//	}
					 
	              cnt+=1;
	              
	              
				 if(m1.get(wholefile1.get(i))==0) //If page is not already in the resident set
				 {
					 pagefaultcnt+=1;
					 
					 if(M==cnt && pagefaultcnt>=Q)  // If Q pagefaults done within M timelimit then We have to reset our resident set
								{
								 flag=0;
								}
			         totalpagefault+=1;
					 m1.put(wholefile1.get(i), 1); //current resident set
					 System.out.println("Page fault is there,Ref page is "+wholefile1.get(i) +" resident set at "+(i+1)+" page reference from file is "+ m1);
					 m2.put(wholefile1.get(i), 1);  //For resetting the resident set to the pages that refer to the last interval,m2 hashmap hash been used.
					 //System.out.println("Ref page is "+wholefile1.get(i) +" m2 at "+i+" is "+ m2);
					 
				 }
				 else   //If page is already in the resident set
				 {
					 if(M==cnt && pagefaultcnt>=Q) //If Q pagefaults done within M timelimit then We have to reset our resident set
						{
						 flag=0;
						}
					 System.out.println("No page fault,Ref page is "+wholefile1.get(i) +" resident set at "+(i+1)+" page reference from file is "+ m1);
					 //System.out.println("Ref page is "+wholefile1.get(i) +" m2 at "+i+" is "+ m2);
					 m2.put(wholefile1.get(i), 1);  //For resetting the resident set to the pages that refer to the last interval,m2 hashmap hash been used.
					 
				 }
				 
			 }
			else
			{
				//System.out.println("modulo is "+ cnt%L);
				if(cnt%L==0)
				System.out.println("resident set bit reset because of L timeout is");
				else if(pagefaultcnt>=Q)
				System.out.println("resident set bit reset because Q pagefault done within M timelimit is");
				pagefaultcnt=0;
				flag=1;
				cnt=1;
               for(int i3=1;i3<=wholefile.get(0);i3++)
            	   	m1.put(i3, 0);             //Resetting the whole m1 map
               
               
               for(int i3=1;i3<=wholefile.get(0);i3++)
               {
            	   if(m2.get(i3)==1)
            		   m1.put(i3, 1);   //Putting the value 1 for the pages that referenced during the last interval which we have saved in to hashmap m2
               }
               
               System.out.print(m2);
               System.out.println();
               
               for(int i3=1;i3<=wholefile.get(0);i3++)
           	   	m2.put(i3,0); //Resetting the m2 hashmap for storing the pages that will be referenced during the next interval 
               
               
               
               if(m1.get(wholefile1.get(i))==0)  //This iteration is for the first page reference of the next interval
				 {
					 pagefaultcnt+=1;
			         totalpagefault+=1;
					 m1.put(wholefile1.get(i), 1);
					 System.out.println("Page fault is there, Ref page is "+wholefile1.get(i) +" current resident set at "+(i+1)+" page reference from file is "+ m1);
					 m2.put(wholefile1.get(i), 1);
					 //System.out.println("Ref page is "+wholefile1.get(i) +" m2 at "+i+" is "+ m2);
					 
				 }
               else
               {
            	   System.out.println("No page fault,Ref page is "+wholefile1.get(i) +" current resident set at "+(i+1)+" page reference from file is "+ m1);
            	   m2.put(wholefile1.get(i),1);
					//System.out.println("Ref page is "+wholefile1.get(i) +" m2 at "+i+" is "+ m2);
               }
               
               //System.out.println("After resetting");
               //System.out.println(m1);
               //System.out.println(m2);
				
			}
			 
			 
				 
			 
			 
		 }
		 System.out.println("Total no of page fault is "+totalpagefault);
		 System.out.println("Conslusion:PGM selecting the value of M,L,Q based on maximum repeating consective element logic but If we increase the value of Q and increases the difference between L and M then upto"
		  		+ "a certain limit the page fault count is decreases and then after Page fault count reamin constant with respect to the increasing  values of Q and increasing difference betwwen L and M");
		}
		catch(Exception e)
		{
			System.out.println(e.getMessage());
		}
		

		
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		Scanner sc1=new Scanner(System.in);
		System.out.println("Enter the file name with extension");
	    String fname=sc1.nextLine();
		System.out.println("Do you want to do PFF/VSWS[0/1] (Choose O(PFF) or 1(VSWS))");
		int ch=sc1.nextInt();
		
		
		
		if(ch==0)
		{
			pff(fname);
		}
		else if(ch==1)
		{
            vsws(fname);			
		}

	}

	

}
