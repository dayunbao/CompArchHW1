import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Random;
import java.util.Scanner;


public class Main 
{
	public static boolean trace;
	public static File file;
	
	public static int cacheSizeBits;
	public static int blockSizeBits;
	public static int numBlockBits;
	public static int indexSizeBits;
	public static int tagSizeBits;
	
	public static int cacheSize;
	public static int blockSize;
	public static int tagSize;
	//size of address in bits
	public static int addressSize;
	public static int numBlocks;

	
	public static boolean verifyInput(String [] args)
	{
		if(args.length < 4)
		{
			System.out.println("This program requires 4 arguments.");
			return false;
		}
		
		//size of cache in bits
		cacheSizeBits = Integer.parseInt(args[0]);
		//size of blocks in bits
		blockSizeBits = Integer.parseInt(args[1]);
		//number of bits to calculate the number of blocks in the cache
		numBlockBits = cacheSizeBits - blockSizeBits;
		//size of index in bits
		indexSizeBits = cacheSizeBits - blockSizeBits;
		//size of tag in bits
		tagSizeBits = addressSize- indexSizeBits - blockSizeBits;
		
		String traceVal = args[2].toLowerCase();
		//System.out.println("Trace Value = " + traceVal);
		file = new File(args[3]);
		
		cacheSize = (int) Math.pow(2.0, cacheSizeBits);
		blockSize = (int) Math.pow(2.0, blockSizeBits);
		tagSize = (int) Math.pow(2.0, blockSizeBits);
		numBlocks = (int) Math.pow(2.0, numBlockBits);
		
		
		//System.out.println("Cache size " + cacheSize);
		//System.out.println("Block size: " + blockSize);
		//System.out.println("The number of blocks are " + numBlocks);
		
		if(cacheSizeBits <= 0 || blockSizeBits <= 0 || numBlocks <= 0)
		{
			System.out.println("The cache size, block size and number of blocks must be non-negative and nonzero");
			return false;
		}
		
		if(!file.isFile())
		{
			System.out.println("The file provided is not a valid file.");
			return false;
		}
		
		if((blockSize * numBlocks) != cacheSize)
		{
			System.out.println("The cache and block size are incorrect");
			return false;
		}
		
		
		if(traceVal.equals("off"))
		{
			trace = false;
		}
		else if (traceVal.equals("on"))
		{
			trace = true;
		}
		else
		{
			System.out.println("Invalid trace argument given");
			return false;
		}
		
		return true;
	}
	
	public static int convertFromHex(String addr)
	{
		int address = Integer.parseInt(addr, 16);
		return address;
	}
	
	public static String convertToHex(int addr)
	{
		String address = Integer.toHexString(addr);
		return address;
	}
	
	public static void generateAddresses(int fileNum, int numAddresses)
	{
		Random rand = new Random();
		String filePath = "/home/maotouying/workspace/CompArchHW1/testFile";
		filePath += fileNum;
		filePath += ".txt";		
		File file = new File(filePath);
		
		if (!file.exists()) 
		{
			try 
			{
				file.createNewFile();
			} 
			catch (IOException e) 
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		FileWriter fw;
		
		try 
		{
			fw = new FileWriter(file.getAbsoluteFile());
			
			try 
			{
				BufferedWriter writer = new BufferedWriter(fw);
				
				for(int i = 0; i < numAddresses; i ++)
				{
					/*
					int addressSizeBits = rand.nextInt(20) + 13;
					//System.out.println("Num bits in rand address: " + addressSizeBits);
					int randAddress = (int)Math.pow(2.0, addressSizeBits);
					int randAddressAgain = rand.nextInt(randAddress) + 1;
					//System.out.println("Random address after nextInt " + randAddressAgain);
					*/
					int randAddress = (int)Math.pow(2.0, 32);
					int randAddressAgain = rand.nextInt(randAddress) + 1;
					
					if((i % 2) == 0)
					{
						String hexAdd = "0x";
						hexAdd += convertToHex(randAddressAgain);
						//System.out.println("Rand hex address is " + hexAdd);
						
						writer.write(hexAdd);
						writer.write("\n");
					}
					else
					{
						String intAdd = "";
						intAdd += randAddressAgain;
						writer.write(intAdd);
						writer.write("\n");
					}
				}
				
				writer.close();
			}
			catch (IOException e) 
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} 
		catch (IOException e1) 
		{
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		
	}

	public static void main(String[] args) 
	{
		Scanner in = null;
		
		if(verifyInput(args))
		{
			Cache simulatedCache = new Cache(cacheSizeBits, blockSizeBits, trace);
			
			try 
			{
				in = new Scanner(new File(args[3]));
				
			}
			catch (FileNotFoundException exception)
			{
				
			}
			
			while(in.hasNextLine())
			{
				String address = in.nextLine();
				if(address.equals(""))
				{
					continue;
				}
				String strippedAddress = address.trim();
				simulatedCache.checkCache(strippedAddress);
				
				/*
				for(int i = 1; i < 5; i++)
				{
					//double percent = (i / 100.0);
					//int numAdd = (int) Math.floor(numBlocks + (numBlocks * percent));
					int numAdd = i * 500;					
					generateAddresses(i, numAdd);
				}
				*/
				
			}
			System.out.println("Andrew S. Clark");
			for(String a : args)
			{
				System.out.print(a + " ");
			}
			System.out.println();
			System.out.println("memory accesses: " + simulatedCache.getNumAccesses());
			System.out.println("hits: " + simulatedCache.getNumHits());
			System.out.println("misses: " + simulatedCache.getNumMisses());
			System.out.println("miss ratio: " + simulatedCache.getMissRatio());
		}

	}

}
