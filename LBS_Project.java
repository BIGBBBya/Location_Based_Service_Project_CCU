import java.io.*;
import java.text.DecimalFormat;
import java.text.NumberFormat;

public class LBS_Project {
	public static void main(String[] args) throws IOException
	{
		String strin="null";
		BufferedReader buf;
		int Number_of_Data=50000,Page_capacity=0;
		int i,h;
		double temp1,temp2;
		int Page_of_Data[]=new int[10005];
		DecimalFormat df=new DecimalFormat("##.000");
		
		
		//Data Generator
		System.out.println("請輸入資料數量:");
		buf=new BufferedReader(new InputStreamReader(System.in));
		strin=buf.readLine();
		Number_of_Data=Integer.parseInt(strin);
		double Data[][]=new double[Number_of_Data+1][2];
		
		FileWriter fw1 = new FileWriter("Data_Generator.txt");
		BufferedWriter bfw1 = new BufferedWriter(fw1);
		bfw1.write("size of data: "+Number_of_Data);
		bfw1.newLine();
		
		for(i=1;i<=Number_of_Data;i++){
			temp1=Math.random()*100;
			temp2=Math.random()*100;
			Data[i][0]=Math.floor(temp1*1000)/1000.0;
			Data[i][0]=Double.parseDouble(df.format(Data[i][0]));
			Data[i][1]=Math.floor(temp2*1000)/1000.0;
			Data[i][1]=Double.parseDouble(df.format(Data[i][1]));
			System.out.println(i+":\tX值:"+Data[i][0]+"\tY值:"+Data[i][1]);		
			if(i<10)
			{
				bfw1.write("#0000"+i+"#:");
			}
			if(i<100 && i>=10)
			{
				bfw1.write("#000"+i+"#:");
			}
			if(i<1000 && i>=100)
			{
				bfw1.write("#00"+i+"#:");
			}
			if(i<10000 && i>=1000)
			{
				bfw1.write("#0"+i+"#:");
			}
			if(i>=10000)
			{
				bfw1.write("#"+i+"#:");
			}
			
			if(Data[i][0]<10){
				if(String.valueOf(Data[i][0]).length()==4)
				{
					bfw1.write("0"+Data[i][0]+"0,");
				}else if(String.valueOf(Data[i][0]).length()==3)
				{
					bfw1.write("0"+Data[i][0]+"00,");
				}
				else
				{
					bfw1.write("0"+Data[i][0]+",");
				}
				
			}else{
				if(String.valueOf(Data[i][0]).length()==5)
				{
					bfw1.write(Data[i][0]+"0,");
				}else if(String.valueOf(Data[i][0]).length()==4){
					bfw1.write(Data[i][0]+"00,");
				}
				else
				{
					bfw1.write(Data[i][0]+",");
				}
				
			}
			
			if(Data[i][1]<10){
				if(String.valueOf(Data[i][1]).length()==4)
				{
					bfw1.write("0"+Data[i][1]+"0");
				}else if(String.valueOf(Data[i][1]).length()==3)
				{
					bfw1.write("0"+Data[i][1]+"00");
				}
				else
				{
					bfw1.write("0"+Data[i][1]);
				}
			}else{
				if(String.valueOf(Data[i][1]).length()==5)
				{
					bfw1.write(Data[i][1]+"0");
				}else if(String.valueOf(Data[i][1]).length()==4)
				{
					bfw1.write(Data[i][1]+"00");
				}
				else
				{
					bfw1.write(String.valueOf(Data[i][1]));
				}
			}
			bfw1.newLine();
		}
		
		System.out.println("==============================");
		bfw1.flush();
		fw1.close();	
		
		//產生gri.conf
		System.out.println("請輸入page capacity:");
		strin=buf.readLine();
		System.out.println("==============================");
		Page_capacity=Integer.parseInt(strin);
		
		double grid=0;
		int grid_size=0,cell=0;
		cell=(int)Math.ceil((double)Number_of_Data/Page_capacity);	
		grid=Math.sqrt(cell);	
		grid_size=(int)Math.ceil(grid);
		
		double cell_x=100/grid_size;
		double cell_y=100/grid_size;
		
		FileWriter fw2 = new FileWriter("gri.conf");
		BufferedWriter bfw2 = new BufferedWriter(fw2);
		bfw2.write("size of data set: "+Number_of_Data);
		bfw2.newLine();
		bfw2.write("Page capacity: "+Page_capacity);
		bfw2.newLine();
		bfw2.write("grid size: "+grid_size+" x "+grid_size);
		bfw2.newLine();
		bfw2.write("Disk page size \tX: "+cell_x+" \tY: "+cell_y);
		bfw2.newLine();
		bfw2.flush();
		fw2.close();	
		
		//產生gri.idx,gri.iof
		
		String Number="#";
		
		for(int x=1;x<=Number_of_Data;x++)
		{
			double x_axis=Math.floor(Data[x][0]/cell_x);
			double y_axis=Math.floor(Data[x][1]/cell_y);
			double page_number=y_axis*grid_size+x_axis;
			Page_of_Data[x]=(int)page_number;
			System.out.println(Number+x+":\t"+x_axis+"\t"+y_axis+"\tpage number is : "+page_number);
		}
		
		System.out.println("==============================");
		
		int Page[][]=new int[grid_size*grid_size+1][100];
		int count=0;
		
		for(int b=0;b<=grid_size*grid_size;b++){
			count=0;
			for(int a=0;a<=Number_of_Data;a++){
				if(Page_of_Data[a]==b){
					Page[b][count]=a;
					count++;
				}
			}
		}
		
		//show page 內容
		for(int k=0;k<=grid_size*grid_size-1;k++){
			System.out.println();
			System.out.println("Page"+k);
			FileWriter idx = new FileWriter("gri"+k+".idx");
			BufferedWriter bw = new BufferedWriter(idx);
			
			FileWriter iof = new FileWriter("gri"+k+".iof");
			BufferedWriter bw2 = new BufferedWriter(iof);
			
			for(int j=0;j<=99;j++){
				if(j<=Page_capacity-1){
					if(Page[k][j]!=0)
					{
						System.out.print("\tPoint:#"+Page[k][j]);
						h=Page[k][j];
						System.out.print("\tX值:"+Data[h][0]+"\tY值:"+Data[h][1]);
						System.out.println();
						if(Page[k][j]<10)
						{
							bw.write("#0000"+Page[k][j]+":");
						}
						if(Page[k][j]<100 && Page[k][j]>=10)
						{
							bw.write("#000"+Page[k][j]+":");
						}
						if(Page[k][j]<1000 && Page[k][j]>=100)
						{
							bw.write("#00"+Page[k][j]+":");
						}
						if(Page[k][j]<10000 && Page[k][j]>=1000)
						{
							bw.write("#0"+Page[k][j]+":");
						}
						if(Page[k][j]>=10000)
						{
							bw.write("#"+Page[k][j]+":");
						}
												
						if(Data[h][0]<10){
							if(String.valueOf(Data[h][0]).length()==4)
							{
								bw.write("0"+Data[h][0]+"0,");
							}else if(String.valueOf(Data[h][0]).length()==3)
							{
								bw.write("0"+Data[h][0]+"00,");
							}
							else
							{
								bw.write("0"+Data[h][0]+",");
							}
							
						}else{
							if(String.valueOf(Data[h][0]).length()==5)
							{
								bw.write(Data[h][0]+"0,");
							}else if(String.valueOf(Data[h][0]).length()==4){
								bw.write(Data[h][0]+"00,");
							}
							else
							{
								bw.write(Data[h][0]+",");
							}
							
						}
						
						if(Data[h][1]<10){
							if(String.valueOf(Data[h][1]).length()==4)
							{
								bw.write("0"+Data[h][1]+"0");
							}else if(String.valueOf(Data[h][1]).length()==3)
							{
								bw.write("0"+Data[h][1]+"00");
							}
							else
							{
								bw.write("0"+Data[h][1]);
							}
						}else{
							if(String.valueOf(Data[h][1]).length()==5)
							{
								bw.write(Data[h][1]+"0");
							}else if(String.valueOf(Data[h][1]).length()==4)
							{
								bw.write(Data[h][1]+"00");
							}
							else
							{
								bw.write(String.valueOf(Data[h][1]));
							}
						}
						bw.newLine();
					}
					
				}else{
					if(Page[k][j]!=0)
					{
						System.out.println("Overflow:");
						System.out.print("\tPoint:#"+Page[k][j]);
						h=Page[k][j];
						System.out.print("\tX值:"+Data[h][0]+"\tY值:"+Data[h][1]);
						System.out.println();
						if(Page[k][j]<10)
						{
							bw2.write("#0000"+Page[k][j]+":");
						}
						if(Page[k][j]<100 && Page[k][j]>=10)
						{
							bw2.write("#000"+Page[k][j]+":");
						}
						if(Page[k][j]<1000 && Page[k][j]>=100)
						{
							bw2.write("#00"+Page[k][j]+":");
						}
						if(Page[k][j]<10000 && Page[k][j]>=1000)
						{
							bw2.write("#0"+Page[k][j]+":");
						}
						if(Page[k][j]>=10000)
						{
							bw2.write("#"+Page[k][j]+":");
						}
						if(Data[h][0]<10){
							if(String.valueOf(Data[h][0]).length()==4)
							{
								bw2.write("0"+Data[h][0]+"0,");
							}else if(String.valueOf(Data[h][0]).length()==3)
							{
								bw2.write("0"+Data[h][0]+"00,");
							}
							else
							{
								bw2.write("0"+Data[h][0]+",");
							}
							
						}else{
							if(String.valueOf(Data[h][0]).length()==5)
							{
								bw2.write(Data[h][0]+"0,");
							}else if(String.valueOf(Data[h][0]).length()==4){
								bw2.write(Data[h][0]+"00,");
							}
							else
							{
								bw2.write(Data[h][0]+",");
							}
							
						}
						
						if(Data[h][1]<10){
							if(String.valueOf(Data[h][1]).length()==4)
							{
								bw2.write("0"+Data[h][1]+"0");
							}else if(String.valueOf(Data[h][1]).length()==3)
							{
								bw2.write("0"+Data[h][1]+"00");
							}
							else
							{
								bw2.write("0"+Data[h][1]);
							}
						}else{
							if(String.valueOf(Data[h][1]).length()==5)
							{
								bw2.write(Data[h][1]+"0");
							}else if(String.valueOf(Data[h][1]).length()==4)
							{
								bw2.write(Data[h][1]+"00");
							}
							else
							{
								bw2.write(String.valueOf(Data[h][1]));
							}
						}
						bw2.newLine();
					}
				}
			}
			bw.flush();
			idx.close();
			bw2.flush();
			iof.close();
		}
			
		
		functions(cell_x,cell_y,grid_size,Page_capacity);
		
	}
	
	public static void functions(double x,double y,int size,int cap) throws IOException{
		BufferedReader buf;
		String strin="null";
		String readfile="";
		String readfile2="";
		String search="";
		String filename;
		int count=0;
		
		buf=new BufferedReader(new InputStreamReader(System.in));
		System.out.println("==========================");
		System.out.println("Please enter what you want to do.");
		System.out.println();
		System.out.println("I)nsert ");
		System.out.println("D)elete ");
		System.out.println("Q)uery");
		System.out.println("U)pdate");
		System.out.println("E)xit ");
		System.out.print("=>");
		strin=buf.readLine();
		strin=strin.toUpperCase();
		
		switch(strin)
		{
			//###############INSERT################
			case "I":
			{	
				double x_temp,y_temp;
				int number_temp;
				System.out.println("請輸入要加入的點的編號:(Ex:00026)");
				strin=buf.readLine();
				
				 FileReader fr = new FileReader("Data_Generator.txt");
				 BufferedReader br = new BufferedReader(fr);
				 while (br.ready()) {
				    readfile+=br.readLine();
				   }
				 search="#"+strin+"#";
				 if (readfile.indexOf(search)<0)
			     {
					 System.out.println("可以使用此編號!");  
			     }
			     else
			     {
			    	 System.out.println("已經擁有這個編號!請換個編號再試一次!");   
					 functions(x,y,size,cap);
					 break;
			     }
				fr.close();
				     
				readfile=null;
				number_temp=Integer.parseInt(strin);
				System.out.println("請輸入要加入的點的X值(0 to 100):");
				strin=buf.readLine();
				x_temp=Double.parseDouble(strin);
				System.out.println("請輸入要加入的點的y值(0 to 100):");
				strin=buf.readLine();
				y_temp=Double.parseDouble(strin);
				if(x_temp>100 || y_temp>100){
					System.out.println("輸入的值不符合規定!!\t請重新輸入!");
					functions(x,y,size,cap);
					break;
				}
				FileWriter fw=new FileWriter("Data_Generator.txt",true);
				//fw.write("#"+number_temp+"#:"+String.valueOf(x_temp)+","+String.valueOf(y_temp));
				
				if(number_temp<10)
				{
					fw.write("#0000"+number_temp+"#:");
				}
				if(number_temp<100 && number_temp>=10)
				{
					fw.write("#000"+number_temp+"#:");
				}
				if(number_temp<1000 && number_temp>=100)
				{
					fw.write("#00"+number_temp+"#:");
				}
				if(number_temp<10000 && number_temp>=1000)
				{
					fw.write("#0"+number_temp+"#:");
				}
				if(number_temp>=10000)
				{
					fw.write("#"+number_temp+"#:");
				}
				
				if(x_temp<10){
					if(String.valueOf(x_temp).length()==4)
					{
						fw.write("0"+x_temp+"0,");
					}else if(String.valueOf(x_temp).length()==3)
					{
						fw.write("0"+x_temp+"00,");
					}
					else
					{
						fw.write("0"+x_temp+",");
					}
					
				}else{
					if(String.valueOf(x_temp).length()==5)
					{
						fw.write(x_temp+"0,");
					}else if(String.valueOf(x_temp).length()==4){
						fw.write(x_temp+"00,");
					}
					else
					{
						fw.write(x_temp+",");
					}
					
				}
				
				if(y_temp<10){
					if(String.valueOf(y_temp).length()==4)
					{
						fw.write("0"+y_temp+"0");
					}else if(String.valueOf(y_temp).length()==3)
					{
						fw.write("0"+y_temp+"00");
					}
					else
					{
						fw.write("0"+y_temp);
					}
				}else{
					if(String.valueOf(y_temp).length()==5)
					{
						fw.write(y_temp+"0");
					}else if(String.valueOf(y_temp).length()==4)
					{
						fw.write(y_temp+"00");
					}
					else
					{
						fw.write(String.valueOf(y_temp));
					}
					
				}
				
				fw.close();
				
				double x_axis=Math.floor(x_temp/x);
				double y_axis=Math.floor(y_temp/y);
				int page_number=(int)y_axis*size+(int)x_axis;
				System.out.println("The page number is "+page_number);
				
				filename="gri"+page_number+".iof";
				FileReader Read = new FileReader(filename);
				 BufferedReader aaaa = new BufferedReader(Read);
				 while (aaaa.ready()) {
				    readfile+=aaaa.readLine();
				   }
				
				
				 if (readfile==null)
				 {
					filename="gri"+page_number+".idx";
					FileReader Reada = new FileReader(filename);
					BufferedReader aaa = new BufferedReader(Reada);
					
					while (aaa.ready()) {
					    readfile2+=aaa.readLine();
					   }
					if(readfile2.length()>19*cap){
						
						FileWriter fwB=new FileWriter("gri"+page_number+".iof",true);
						
						if(number_temp<10)
						{
							fwB.write("#0000"+number_temp+":");
						}
						if(number_temp<100 && number_temp>=10)
						{
							fwB.write("#000"+number_temp+":");
						}
						if(number_temp<1000 && number_temp>=100)
						{
							fwB.write("#00"+number_temp+":");
						}
						if(number_temp<10000 && number_temp>=1000)
						{
							fwB.write("#0"+number_temp+":");
						}
						if(number_temp>=10000)
						{
							fwB.write("#"+number_temp+":");
						}
						
						if(x_temp<10){
							if(String.valueOf(x_temp).length()==4)
							{
								fwB.write("0"+x_temp+"0,");
							}else if(String.valueOf(x_temp).length()==3)
							{
								fwB.write("0"+x_temp+"00,");
							}
							else
							{
								fwB.write("0"+x_temp+",");
							}
							
						}else{
							if(String.valueOf(x_temp).length()==5)
							{
								fwB.write(x_temp+"0,");
							}else if(String.valueOf(x_temp).length()==4){
								fwB.write(x_temp+"00,");
							}
							else
							{
								fwB.write(x_temp+",");
							}
							
						}
						
						if(y_temp<10){
							if(String.valueOf(y_temp).length()==4)
							{
								fwB.write("0"+y_temp+"0");
							}else if(String.valueOf(y_temp).length()==3)
							{
								fwB.write("0"+y_temp+"00");
							}
							else
							{
								fwB.write("0"+y_temp);
							}
						}else{
							if(String.valueOf(y_temp).length()==5)
							{
								fwB.write(y_temp+"0");
							}else if(String.valueOf(y_temp).length()==4)
							{
								fwB.write(y_temp+"00");
							}
							else
							{
								fwB.write(String.valueOf(y_temp));
							}
							
						}
						fwB.write("\r\n");
						System.out.println("原Page已滿，將存入overflow檔");
						System.out.println("已存入gri"+page_number+".iof");
						fwB.close();
						aaa.close();
						
					}else{
						FileWriter fwB=new FileWriter("gri"+page_number+".idx",true);
						if(number_temp<10)
						{
							fwB.write("#0000"+number_temp+":");
						}
						if(number_temp<100 && number_temp>=10)
						{
							fwB.write("#000"+number_temp+":");
						}
						if(number_temp<1000 && number_temp>=100)
						{
							fwB.write("#00"+number_temp+":");
						}
						if(number_temp<10000 && number_temp>=1000)
						{
							fwB.write("#0"+number_temp+":");
						}
						if(number_temp>=10000)
						{
							fwB.write("#"+number_temp+":");
						}
						
						if(x_temp<10){
							if(String.valueOf(x_temp).length()==4)
							{
								fwB.write("0"+x_temp+"0,");
							}else if(String.valueOf(x_temp).length()==3)
							{
								fwB.write("0"+x_temp+"00,");
							}
							else
							{
								fwB.write("0"+x_temp+",");
							}
							
						}else{
							if(String.valueOf(x_temp).length()==5)
							{
								fwB.write(x_temp+"0,");
							}else if(String.valueOf(x_temp).length()==4){
								fwB.write(x_temp+"00,");
							}
							else
							{
								fwB.write(x_temp+",");
							}
							
						}
						
						if(y_temp<10){
							if(String.valueOf(y_temp).length()==4)
							{
								fwB.write("0"+y_temp+"0");
							}else if(String.valueOf(y_temp).length()==3)
							{
								fwB.write("0"+y_temp+"00");
							}
							else
							{
								fwB.write("0"+y_temp);
							}
						}else{
							if(String.valueOf(y_temp).length()==5)
							{
								fwB.write(y_temp+"0");
							}else if(String.valueOf(y_temp).length()==4)
							{
								fwB.write(y_temp+"00");
							}
							else
							{
								fwB.write(String.valueOf(y_temp));
							}
						System.out.println("已存入gri"+page_number+".idx");
						fwB.write("\n");
						fwB.close();
						aaa.close();
						}
						}
			     }else{
			    	 System.out.println("此page已經過載!");  
			    	 FileWriter fwA=new FileWriter("gri"+page_number+".iof",true);
			    	 
			    	 if(number_temp<10)
						{
							fwA.write("#0000"+number_temp+":");
						}
						if(number_temp<100 && number_temp>=10)
						{
							fwA.write("#000"+number_temp+":");
						}
						if(number_temp<1000 && number_temp>=100)
						{
							fwA.write("#00"+number_temp+":");
						}
						if(number_temp<10000 && number_temp>=1000)
						{
							fwA.write("#0"+number_temp+":");
						}
						if(number_temp>=10000)
						{
							fwA.write("#"+number_temp+":");
						}
						
						if(x_temp<10){
							if(String.valueOf(x_temp).length()==4)
							{
								fwA.write("0"+x_temp+"0,");
							}else if(String.valueOf(x_temp).length()==3)
							{
								fwA.write("0"+x_temp+"00,");
							}
							else
							{
								fwA.write("0"+x_temp+",");
							}
							
						}else{
							if(String.valueOf(x_temp).length()==5)
							{
								fwA.write(x_temp+"0,");
							}else if(String.valueOf(x_temp).length()==4){
								fwA.write(x_temp+"00,");
							}
							else
							{
								fwA.write(x_temp+",");
							}
							
						}
						
						if(y_temp<10){
							if(String.valueOf(y_temp).length()==4)
							{
								fwA.write("0"+y_temp+"0");
							}else if(String.valueOf(y_temp).length()==3)
							{
								fwA.write("0"+y_temp+"00");
							}
							else
							{
								fwA.write("0"+y_temp);
							}
						}else{
							if(String.valueOf(y_temp).length()==5)
							{
								fwA.write(y_temp+"0");
							}else if(String.valueOf(y_temp).length()==4)
							{
								fwA.write(y_temp+"00");
							}
							else
							{
								fwA.write(String.valueOf(y_temp));
							}
			    	 
							fwA.write("\n");
							fwA.close();
							System.out.println("已存入gri"+page_number+".iof");
			     }
				 	Read.close();
				   
			     }
			
				  functions(x,y,size,cap);
				     break;
				     }
			
			//##################DELETE####################
			case "D":
			{
				double x_temp,y_temp;
				int number_temp;
				String a,b,c;
				System.out.println("請輸入要刪除的點的編號:(Ex:00026)");
				strin=buf.readLine();
				a=strin;
				
				 FileReader fr = new FileReader("Data_Generator.txt");
				 BufferedReader br = new BufferedReader(fr);
				 while (br.ready()) {
				    readfile+=br.readLine();
				   }
				 search="#"+strin+"#";
				 if (readfile.indexOf(search)<0)
			     {
					 System.out.println("找不到此編號!請確認");  
					 functions(x,y,size,cap);
					 break;
			     }
			     else
			     {
			    	 System.out.println("已找到此編號!");  
			     }
				fr.close();
				readfile="";
				number_temp=Integer.parseInt(strin);
				System.out.println("請輸入要刪除的點的X值(0 to 100):");
				strin=buf.readLine();
				x_temp=Double.parseDouble(strin);
				b=strin;
				System.out.println("請輸入要刪除的點的y值(0 to 100):");
				strin=buf.readLine();
				y_temp=Double.parseDouble(strin);
				c=strin;
				if(x_temp>100 || y_temp>100){
					System.out.println("輸入的值不符合規定!!\t請重新輸入!");
					functions(x,y,size,cap);
					break;
				}
				double x_axis=Math.floor(x_temp/x);
				double y_axis=Math.floor(y_temp/y);
				int page_number=(int)y_axis*size+(int)x_axis;
				System.out.println("The page number is "+page_number);
				
				 if(a.length()==1){a="0000"+a;}
				 if(a.length()==2){a="000"+a;}
				 if(a.length()==3){a="00"+a;}
				 if(a.length()==4){a="0"+a;}
					
					
				if(x_temp<10){
						if(b.length()==4)
						{
							b="0"+b+"0";
						}else if(b.length()==3)
						{
							b="0"+b+"00";
						}
						else
						{
							if(b.length()==5)
							{
								b="0"+b;	
							}
						}
						
					}else{
						if(b.length()==5)
						{
							b=b+"0";
						}else if(b.length()==4){
							b=b+"00";
						}
					}
				if(y_temp<10){
					if(c.length()==4)
					{
						c="0"+c+"0";
					}else if(c.length()==3)
					{
						c="0"+c+"00";
					}
					else
					{
						if(c.length()==5)
						{
							c="0"+c;	
						}
					}
					
				}else{
					if(c.length()==5)
					{
						c=c+"0";
					}else if(c.length()==4){
						c=c+"00";
					}
				}
		    	 
		     System.out.println("#"+a+"#:"+b+","+c);
				
				
				filename="gri"+page_number+".iof";
				FileReader Read = new FileReader(filename);
				BufferedReader aaaa = new BufferedReader(Read);
				 while (aaaa.ready()) {
					    readfile+=aaaa.readLine()+"\r\n";		
					   }
				 
				 if(readfile!=null){
					 if (readfile.indexOf("#"+a+":"+b+","+c)>=0)
					 {
						readfile=readfile.replace("#"+a+":"+b+","+c, "");
						System.out.println("已在"+page_number+".iof檔中刪除!");
						BufferedWriter W = new BufferedWriter(new FileWriter(filename)); 
						W.write(readfile); 
						W.close(); 
					 }
				 }
					 
				filename="gri"+page_number+".idx";
				FileReader ReadD = new FileReader(filename);
				BufferedReader read = new BufferedReader(ReadD);
				while (read.ready()) {
					 readfile2+=read.readLine()+"\r\n";					 
						 }
				
				if(readfile2!=null)
				{
				if(readfile2.indexOf("#"+a+":"+b+","+c)>=0)
				{
					String iof = "";
					readfile2=readfile2.replace("#"+a+":"+b+","+c, "");
					System.out.println("已在"+page_number+".idx檔中刪除!");
					if(readfile.indexOf("#")>=0)
					{
						iof=readfile.substring(0,20);
						System.out.println("已將iof檔中一點加入idx!");
						readfile=readfile.replace(iof, "");
						BufferedWriter W = new BufferedWriter(new FileWriter("gri"+page_number+".iof")); 
						W.write(readfile); 
						W.close(); 
					}
					else
					{
						System.out.println("iof檔中沒有點可以加入idx!");
					}
					
					readfile2+=iof+"\r\n";
					BufferedWriter W = new BufferedWriter(new FileWriter(filename)); 
					W.write(readfile2); 
					W.close(); 
				}
				}
				
				String readfile3=""; 
				read.close();
				aaaa.close();
				
				
				
				 FileReader delete = new FileReader("Data_Generator.txt");
				 BufferedReader delete_generator = new BufferedReader(delete);
				 
				 
				 while (delete_generator.ready()) 
				 {
					 readfile3+=delete_generator.readLine()+"\r\n";
				 }
				 
					 if (readfile3.indexOf("#"+a+"#:"+b+","+c)>=0)
					 {
						 readfile3=readfile3.replace("#"+a+"#:"+b+","+c, "");
						System.out.println("已在Generator中刪除!");
						BufferedWriter Waa = new BufferedWriter(new FileWriter("Data_Generator.txt")); 
						Waa.write(readfile3); 
						Waa.close(); 
					 }
				 
				 delete_generator.close();
				 //需加入刪除點後將iof點存入idx
				
				functions(x,y,size,cap);
				break;
			}
			
			//################QUERY##################
			case "Q":
			{
				double x_temp,y_temp;
				int number_temp;
				System.out.println("請輸入要搜尋的點的編號:(Ex:00026)");
				strin=buf.readLine();
				
				 FileReader fr = new FileReader("Data_Generator.txt");
				 BufferedReader br = new BufferedReader(fr);
				 while (br.ready()) {
				    readfile+=br.readLine();
				   }
				 search="#"+strin+"#";
				 if (readfile.indexOf(search)<0)
			     {
					 System.out.println("找不到這個編號!請重新操作");   
					 functions(x,y,size,cap);
					 break;
			     }
			     else
			     {
			    	 System.out.println("已經找到這個編號!");   
					
			     }
				 
				
				
				readfile="";
				number_temp=Integer.parseInt(strin);
				System.out.println("請輸入要搜尋的點的X值(0 to 100):");
				strin=buf.readLine();
				x_temp=Double.parseDouble(strin);
				
				System.out.println("請輸入要搜尋的點的y值(0 to 100):");
				strin=buf.readLine();
				y_temp=Double.parseDouble(strin);
				
				if(x_temp>100 || y_temp>100){
					System.out.println("輸入的值不符合規定!!\t請重新輸入!");
					functions(x,y,size,cap);
					break;
				}
				
				
				
				fr.close();
				double x_axis=Math.floor(x_temp/x);
				double y_axis=Math.floor(y_temp/y);
				int page_number=(int)y_axis*size+(int)x_axis;
				System.out.println("Point# "+number_temp+" is stored at cell("+x_axis+","+y_axis+"), Page "+page_number);
				
				filename="gri"+page_number+".iof";
				FileReader Read = new FileReader(filename);
				BufferedReader aaaa = new BufferedReader(Read);
				
				filename="gri"+page_number+".idx";
				FileReader ReadD = new FileReader(filename);
				BufferedReader read = new BufferedReader(ReadD);
				readfile="";
				 while (aaaa.ready()) {
					    readfile+=aaaa.readLine();						    
					   }
				 System.out.println("gri"+page_number+".iof");
				 System.out.println("OverFlow:");
				 if(readfile.indexOf("#")<0)
				 {
					 System.out.println("No Overflow");
				 }
				 for(int ai=0;ai<=cap;ai++)
				 {
					 if(readfile.indexOf("#",ai*15)>=0)
					 {
						System.out.println(readfile.substring(0+ai*20,20*(ai+1)));
					 }
				 }
				 
				 while (read.ready()) {
					    readfile2+=read.readLine();					   
					   }
				 System.out.println("gri"+page_number+".idx");
				 if(readfile2.indexOf("#")<0)
				 {
					 System.out.println("No Data in the Page");
				 }
				 for(int ao=0;ao<=cap-1;ao++)
				 {
					if(readfile2.indexOf("#",ao*15)>=0)
					{
						System.out.println(readfile2.substring(0+ao*20,20*(ao+1)));
					}
				 }
				aaaa.close();
				read.close();
				
				//##找gri-cache##
				//x_temp X值
				//y_temp Y值
				//page_number Page編號
				//x_axis , y_axis
				
				System.out.println("gri-cache:");
				double X_cache_5=Math.floor((x_temp-x)/x);
				double Y_cache_5=Math.floor((y_temp-y)/y);
				int page_number_cache5=(int)Y_cache_5*size+(int)X_cache_5;
				if(x_temp-x>0)
				{
					if(y_temp-y>0)
					{
						System.out.print("Page:"+page_number_cache5+"\t");
					}
				}
				
				double X_cache_3=Math.floor(x_temp/x);
				double Y_cache_3=Math.floor((y_temp-y)/y);
				int page_number_cache3=(int)Y_cache_3*size+(int)X_cache_3;
				if(y_temp-y>0)
				{
					System.out.print("Page:"+page_number_cache3+"\t");
				}
				
				
				double X_cache_7=Math.floor((x_temp+x)/x);
				double Y_cache_7=Math.floor((y_temp-y)/y);
				int page_number_cache7=(int)Y_cache_7*size+(int)X_cache_7;
				if(x_temp+x<100)
				{
					if(y_temp-y>0)
					{
						System.out.print("Page:"+page_number_cache7+"\t");
					}
				}								
				
				double X_cache_2=Math.floor((x_temp-x)/x);
				double Y_cache_2=Math.floor(y_temp/y);
				int page_number_cache2=(int)Y_cache_2*size+(int)X_cache_2;
				if(x_temp-x>0)
				{
					System.out.print("Page:"+page_number_cache2+"\t");
				}
				
				double X_cache_1=Math.floor((x_temp+x)/x);
				double Y_cache_1=Math.floor(y_temp/y);
				int page_number_cache1=(int)Y_cache_1*size+(int)X_cache_1;
				if(x_temp+x<100)
				{
					System.out.print("Page:"+page_number_cache1+"\t");
				}
			
				System.out.print("Page:"+page_number+"\t");
				
				double X_cache_6=Math.floor((x_temp-x)/x);
				double Y_cache_6=Math.floor((y_temp+y)/y);
				int page_number_cache6=(int)Y_cache_6*size+(int)X_cache_6;
				if(x_temp-x>0)
				{
					if(y_temp+y<100)
					{
						System.out.print("Page:"+page_number_cache6+"\t");
					}
				}
				
				double X_cache_4=Math.floor(x_temp/x);
				double Y_cache_4=Math.floor((y_temp+y)/y);
				int page_number_cache4=(int)Y_cache_4*size+(int)X_cache_4;
				if(y_temp+y<100)
				{
					System.out.print("Page:"+page_number_cache4+"\t");
				}
				
				
				double X_cache_8=Math.floor((x_temp+x)/x);
				double Y_cache_8=Math.floor((y_temp+y)/y);
				int page_number_cache8=(int)Y_cache_8*size+(int)X_cache_8;
				if(x_temp+x<100)
				{
					if(y_temp+y<100)
					{
						System.out.println("Page:"+page_number_cache8);
					}
				}
				System.out.println();
				functions(x,y,size,cap);
				break;
			}
			
			
			//##########UPDATE##################
			case "U":
			{
				//Delete部分↓↓↓↓↓↓↓↓↓↓↓↓
				double x_temp,y_temp;
				int number_temp;
				String a,b,c,d,e,f;
				System.out.println("請輸入要Update的點的編號:(Ex:00026)");
				strin=buf.readLine();
				a=strin;
				int Ori_page;
				
				 FileReader fr = new FileReader("Data_Generator.txt");
				 BufferedReader br = new BufferedReader(fr);
				 while (br.ready()) {
				    readfile+=br.readLine();
				   }
				 search="#"+strin+"#";
				 if (readfile.indexOf(search)<0)
			     {
					 System.out.println("找不到此點!請確認");  
					 functions(x,y,size,cap);
					 break;
			     }
			     else
			     {
			    	 System.out.println("已找到此點!");  
			     }
				fr.close();
				readfile="";
				number_temp=Integer.parseInt(strin);
				System.out.println("請輸入要Update的點的原始X值(0 to 100):");
				strin=buf.readLine();
				x_temp=Double.parseDouble(strin);				
				b=strin;
				System.out.println("請輸入要Update的點的原始y值(0 to 100):");
				strin=buf.readLine();
				y_temp=Double.parseDouble(strin);
				c=strin;
				if(x_temp>100 || y_temp>100){
					System.out.println("輸入的值不符合規定!!\t請重新輸入!");
					functions(x,y,size,cap);
					break;
				}
				double x_axis=Math.floor(x_temp/x);
				double y_axis=Math.floor(y_temp/y);
				int page_number=(int)y_axis*size+(int)x_axis;
				System.out.println("The page number is "+page_number);
				Ori_page=page_number;
				
				 if(a.length()==1){a="0000"+a;}
				 if(a.length()==2){a="000"+a;}
				 if(a.length()==3){a="00"+a;}
				 if(a.length()==4){a="0"+a;}
					
					
				if(x_temp<10){
						if(b.length()==4)
						{
							b="0"+b+"0";
						}else if(b.length()==3)
						{
							b="0"+b+"00";
						}
						else
						{
							if(b.length()==5)
							{
								b="0"+b;	
							}
						}
						
					}else{
						if(b.length()==5)
						{
							b=b+"0";
						}else if(b.length()==4){
							b=b+"00";
						}
					}
				if(y_temp<10){
					if(c.length()==4)
					{
						c="0"+c+"0";
					}else if(c.length()==3)
					{
						c="0"+c+"00";
					}
					else
					{
						if(c.length()==5)
						{
							c="0"+c;	
						}
					}
					
				}else{
					if(c.length()==5)
					{
						c=c+"0";
					}else if(c.length()==4){
						c=c+"00";
					}
				}
		    	 
		     System.out.println("#"+a+"#:"+b+","+c);
				
				
				filename="gri"+page_number+".iof";
				FileReader Read = new FileReader(filename);
				BufferedReader aaaa = new BufferedReader(Read);
				 while (aaaa.ready()) {
					    readfile+=aaaa.readLine()+"\r\n";		
					   }
				 
				 if(readfile!=null){
					 if (readfile.indexOf("#"+a+":"+b+","+c)>=0)
					 {
						readfile=readfile.replace("#"+a+":"+b+","+c, "");
						System.out.println("已在"+page_number+".iof檔中刪除!");
						BufferedWriter W = new BufferedWriter(new FileWriter(filename)); 
						W.write(readfile); 
						W.close(); 
					 }
				 }
				String iof="";
				filename="gri"+page_number+".idx";
				FileReader ReadD = new FileReader(filename);
				BufferedReader read = new BufferedReader(ReadD);
				while (read.ready()) {
					 readfile2+=read.readLine()+"\r\n";					 
						 }
				
				if(readfile2!=null)
				{
				if(readfile2.indexOf("#"+a+":"+b+","+c)>=0)
				{
					readfile2=readfile2.replace("#"+a+":"+b+","+c, "");
					System.out.println("已在"+page_number+".idx檔中刪除!");
					
					if(readfile.indexOf("#")>=0)
					{
						iof=readfile.substring(0,20);
						System.out.println("已將iof檔中一點加入idx!");
						readfile=readfile.replace(iof, "");
						BufferedWriter W = new BufferedWriter(new FileWriter("gri"+page_number+".iof")); 
						W.write(readfile); 
						W.close(); 
					}
					else
					{
						System.out.println("iof檔中沒有點可以加入idx!");
					}
					
					readfile2+=iof+"\r\n";
					
					BufferedWriter W = new BufferedWriter(new FileWriter(filename)); 
					W.write(readfile2); 
					W.close(); 
				}else{
					System.out.println("刪除失敗!請重新操作");
					functions(x,y,size,cap);
					break;
				}
				}
				
				String readfile3=""; 
				read.close();
				aaaa.close();
				
				 //Delete部分↑↑↑↑↑↑↑↑
				 
				 //Insert部分↓↓↓↓↓↓↓↓
				 
				/**
				 readfile="";
				 FileReader fr2 = new FileReader("Data_Generator.txt");
				 BufferedReader br2 = new BufferedReader(fr2);
				 
				 while (br2.ready()) {
				    readfile+=br2.readLine();
				   }
				 search="#"+strin+"#";				
				fr2.close();
				**/    
				    
				readfile=null;
				System.out.println("請輸入要加入的點的X值(0 to 100):");
				strin=buf.readLine();
				d=strin;
				x_temp=Double.parseDouble(strin);
				System.out.println("請輸入要加入的點的y值(0 to 100):");
				strin=buf.readLine();
				e=strin;
				y_temp=Double.parseDouble(strin);
				if(x_temp>100 || y_temp>100){
					System.out.println("輸入的值不符合規定!!\t請重新輸入!");
					functions(x,y,size,cap);
					break;
				}
				/**
				FileWriter fw=new FileWriter("Data_Generator.txt",true);
				
				fw.write("#"+number_temp+"#:"+String.valueOf(x_temp)+","+String.valueOf(y_temp));
				
				fw.close();
				**/
					
					
				if(x_temp<10){
						if(d.length()==4)
						{
							d="0"+d+"0";
						}else if(d.length()==3)
						{
							d="0"+d+"00";
						}
						else
						{
							if(d.length()==5)
							{
								d="0"+d;	
							}
						}
						
					}else{
						if(d.length()==5)
						{
							d=d+"0";
						}else if(d.length()==4){
							d=d+"00";
						}
					}
				if(y_temp<10){
					if(e.length()==4)
					{
						e="0"+e+"0";
					}else if(e.length()==3)
					{
						e="0"+e+"00";
					}
					else
					{
						if(e.length()==5)
						{
							e="0"+e;	
						}
					}
					
				}else{
					if(e.length()==5)
					{
						e=e+"0";
					}else if(e.length()==4){
						e=e+"00";
					}
				}
				
				
				x_axis=Math.floor(x_temp/x);
				y_axis=Math.floor(y_temp/y);
				page_number=(int)y_axis*size+(int)x_axis;
				System.out.println("The page number is "+page_number);
				
				filename="gri"+page_number+".iof";
				FileReader Read2 = new FileReader(filename);
				 BufferedReader aaaa2 = new BufferedReader(Read2);
				 while (aaaa2.ready()) {readfile+=aaaa2.readLine();}
				 
				 
				
				 if (readfile==null)
				 {
					filename="gri"+page_number+".idx";
					FileReader Reada = new FileReader(filename);
					BufferedReader aaa = new BufferedReader(Reada);
					
					while (aaa.ready()) {
					    readfile2+=aaa.readLine();
					   }
					if(readfile2.length()>20*cap){
						
						FileWriter fwB=new FileWriter("gri"+page_number+".iof",true);
						
						if(number_temp<10)
						{
							fwB.write("#0000"+number_temp+":");
						}
						if(number_temp<100 && number_temp>=10)
						{
							fwB.write("#000"+number_temp+":");
						}
						if(number_temp<1000 && number_temp>=100)
						{
							fwB.write("#00"+number_temp+":");
						}
						if(number_temp<10000 && number_temp>=1000)
						{
							fwB.write("#0"+number_temp+":");
						}
						if(number_temp>=10000)
						{
							fwB.write("#"+number_temp+":");
						}
						
						if(x_temp<10){
							if(String.valueOf(x_temp).length()==4)
							{
								fwB.write("0"+x_temp+"0,");
							}else if(String.valueOf(x_temp).length()==3)
							{
								fwB.write("0"+x_temp+"00,");
							}
							else
							{
								fwB.write("0"+x_temp+",");
							}
							
						}
						else
						{
							if(String.valueOf(x_temp).length()==5)
							{
								fwB.write(x_temp+"0,");
							}else if(String.valueOf(x_temp).length()==4){
								fwB.write(x_temp+"00,");
							}
							else
							{
								fwB.write(x_temp+",");
							}
							
						}
						
						if(y_temp<10){
							if(String.valueOf(y_temp).length()==4)
							{
								fwB.write("0"+y_temp+"0");
							}else if(String.valueOf(y_temp).length()==3)
							{
								fwB.write("0"+y_temp+"00");
							}
							else
							{
								fwB.write("0"+y_temp);
							}
						}
						else
						{
							if(String.valueOf(y_temp).length()==5)
							{
								fwB.write(y_temp+"0");
							}else if(String.valueOf(y_temp).length()==4)
							{
								fwB.write(y_temp+"00");
							}
							else
							{
								fwB.write(String.valueOf(y_temp));
							}
							fwB.write("\n");
						}
						
						System.out.println("原Page已滿，將存入overflow檔");
						System.out.println("已存入gri"+page_number+".iof");
						fwB.close();
						aaa.close();
						
					}
					else
					{
						FileWriter fwB=new FileWriter("gri"+page_number+".idx",true);
						if(number_temp<10)
						{
							fwB.write("#0000"+number_temp+":");
						}
						if(number_temp<100 && number_temp>=10)
						{
							fwB.write("#000"+number_temp+":");
						}
						if(number_temp<1000 && number_temp>=100)
						{
							fwB.write("#00"+number_temp+":");
						}
						if(number_temp<10000 && number_temp>=1000)
						{
							fwB.write("#0"+number_temp+":");
						}
						if(number_temp>=10000)
						{
							fwB.write("#"+number_temp+":");
						}
						
						if(x_temp<10){
							if(String.valueOf(x_temp).length()==4)
							{
								fwB.write("0"+x_temp+"0,");
							}else if(String.valueOf(x_temp).length()==3)
							{
								fwB.write("0"+x_temp+"00,");
							}
							else
							{
								fwB.write("0"+x_temp+",");
							}
							
						}else{
							if(String.valueOf(x_temp).length()==5)
							{
								fwB.write(x_temp+"0,");
							}else if(String.valueOf(x_temp).length()==4){
								fwB.write(x_temp+"00,");
							}
							else
							{
								fwB.write(x_temp+",");
							}
							
						}
						
						if(y_temp<10){
							if(String.valueOf(y_temp).length()==4)
							{
								fwB.write("0"+y_temp+"0");
							}else if(String.valueOf(y_temp).length()==3)
							{
								fwB.write("0"+y_temp+"00");
							}
							else
							{
								fwB.write("0"+y_temp);
							}
						}else
						{
							if(String.valueOf(y_temp).length()==5)
							{
								fwB.write(y_temp+"0");
							}else if(String.valueOf(y_temp).length()==4)
							{
								fwB.write(y_temp+"00");
							}
							else
							{
								fwB.write(String.valueOf(y_temp));
							}
						System.out.println("已存入gri"+page_number+".idx");
						fwB.write("\n");
						fwB.close();
						aaa.close();
						}
					}
			     }
				 else
				 {
			    	 System.out.println("此page已經過載!");  
			    	 FileWriter fwA=new FileWriter("gri"+page_number+".iof",true);
			    	 
			    	 	if(number_temp<10)
						{
			    		 	fwA.write("#0000"+number_temp+":");
						}
						if(number_temp<100 && number_temp>=10)
						{
							fwA.write("#000"+number_temp+":");
						}
						if(number_temp<1000 && number_temp>=100)
						{
							fwA.write("#00"+number_temp+":");
						}
						if(number_temp<10000 && number_temp>=1000)
						{
							fwA.write("#0"+number_temp+":");
						}
						if(number_temp>=10000)
						{
							fwA.write("#"+number_temp+":");
						}
						
						if(x_temp<10){
							if(String.valueOf(x_temp).length()==4)
							{
								fwA.write("0"+x_temp+"0,");
							}else if(String.valueOf(x_temp).length()==3)
							{
								fwA.write("0"+x_temp+"00,");
							}
							else
							{
								fwA.write("0"+x_temp+",");
							}
							
						}
						else
						{
							if(String.valueOf(x_temp).length()==5)
							{
								fwA.write(x_temp+"0,");
							}else if(String.valueOf(x_temp).length()==4){
								fwA.write(x_temp+"00,");
							}
							else
							{
								fwA.write(x_temp+",");
							}
							
						}
						
						if(y_temp<10){
							if(String.valueOf(y_temp).length()==4)
							{
								fwA.write("0"+y_temp+"0");
							}else if(String.valueOf(y_temp).length()==3)
							{
								fwA.write("0"+y_temp+"00");
							}
							else
							{
								fwA.write("0"+y_temp);
							}
						}
						else
						{
							if(String.valueOf(y_temp).length()==5)
							{
								fwA.write(y_temp+"0");
							}else if(String.valueOf(y_temp).length()==4)
							{
								fwA.write(y_temp+"00");
							}
							else
							{
								fwA.write(String.valueOf(y_temp));
							}
			    	 
								fwA.write("\n");
								fwA.close();
								System.out.println("已存入gri"+page_number+".iof");
							}
						
				 }
				 	Read.close();
				 	
				 	 FileReader delete = new FileReader("Data_Generator.txt");
					 BufferedReader delete_generator = new BufferedReader(delete);
					 
					 
					 while (delete_generator.ready()) 
					 {
						 readfile3+=delete_generator.readLine()+"\r\n";
					 }
					 
						 if (readfile3.indexOf("#"+a+"#:"+b+","+c)>=0)
						 {
							 readfile3=readfile3.replace("#"+a+"#:"+b+","+c, "#"+a+"#:"+d+","+e);
							System.out.println("已在Generator中更新!!");
							BufferedWriter Waa = new BufferedWriter(new FileWriter("Data_Generator.txt")); 
							Waa.write(readfile3); 
							Waa.close(); 
						 }				 
					 delete_generator.close();
					 
				 	System.out.println("Point: "+number_temp+" is migrate from page "+Ori_page+" to page "+page_number);
				 
				 //Insert部分↑↑↑↑↑↑↑↑
				 	
				 //###GRI-Cache###
				 	System.out.println("gri-cache:");
					double X_cache_5=Math.floor((x_temp-x)/x);
					double Y_cache_5=Math.floor((y_temp-y)/y);
					int page_number_cache5=(int)Y_cache_5*size+(int)X_cache_5;
					if(x_temp-x>0)
					{
						if(y_temp-y>0)
						{
							System.out.print("Page:"+page_number_cache5+"\t");
						}
					}
					
					double X_cache_3=Math.floor(x_temp/x);
					double Y_cache_3=Math.floor((y_temp-y)/y);
					int page_number_cache3=(int)Y_cache_3*size+(int)X_cache_3;
					if(y_temp-y>0)
					{
						System.out.print("Page:"+page_number_cache3+"\t");
					}
					
					
					double X_cache_7=Math.floor((x_temp+x)/x);
					double Y_cache_7=Math.floor((y_temp-y)/y);
					int page_number_cache7=(int)Y_cache_7*size+(int)X_cache_7;
					if(x_temp+x<100)
					{
						if(y_temp-y>0)
						{
							System.out.print("Page:"+page_number_cache7+"\t");
						}
					}								
					
					double X_cache_2=Math.floor((x_temp-x)/x);
					double Y_cache_2=Math.floor(y_temp/y);
					int page_number_cache2=(int)Y_cache_2*size+(int)X_cache_2;
					if(x_temp-x>0)
					{
						System.out.print("Page:"+page_number_cache2+"\t");
					}
					
					System.out.print("Page:"+page_number+"\t");
					
					double X_cache_1=Math.floor((x_temp+x)/x);
					double Y_cache_1=Math.floor(y_temp/y);
					int page_number_cache1=(int)Y_cache_1*size+(int)X_cache_1;
					if(x_temp+x<100)
					{
						System.out.print("Page:"+page_number_cache1+"\t");
					}
				
					
					double X_cache_6=Math.floor((x_temp-x)/x);
					double Y_cache_6=Math.floor((y_temp+y)/y);
					int page_number_cache6=(int)Y_cache_6*size+(int)X_cache_6;
					if(x_temp-x>0)
					{
						if(y_temp+y<100)
						{
							System.out.print("Page:"+page_number_cache6+"\t");
						}
					}
					
					double X_cache_4=Math.floor(x_temp/x);
					double Y_cache_4=Math.floor((y_temp+y)/y);
					int page_number_cache4=(int)Y_cache_4*size+(int)X_cache_4;
					if(y_temp+y<100)
					{
						System.out.print("Page:"+page_number_cache4+"\t");
					}
					
					
					double X_cache_8=Math.floor((x_temp+x)/x);
					double Y_cache_8=Math.floor((y_temp+y)/y);
					int page_number_cache8=(int)Y_cache_8*size+(int)X_cache_8;
					if(x_temp+x<100)
					{
						if(y_temp+y<100)
						{
							System.out.println("Page:"+page_number_cache8);
						}
					}
					
					
					functions(x,y,size,cap);
					break;
				
			}
			case "E":
			{
				System.out.println("Thanks for using!");
				break;
			}
			default:
			{
				System.out.println("輸入錯誤!請重新輸入!!!");
				functions(x,y,size,cap);
				break;
			}
				
			
		}
	}
	
}
