import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Random;


public class Cache 
{
	public int cacheSizeBits;
	public int blockSizeBits;
	public int numBlockBits;
	public int cacheSize;
	public int blockSize;
	public int numBlocks;
	public final int ADDRESS_SIZE = 32;
	public boolean trace;
	public int numHits;
	public int numMisses;
	public int numAccesses;
	public ArrayList<Block> blocks;
	
	public Cache(int cacheSizeBits, int blockSizeBits, boolean trace)
	{
		this.cacheSizeBits = cacheSizeBits;
		this.blockSizeBits = blockSizeBits;
		this.cacheSize = (int) Math.pow(2.0, cacheSizeBits);
		this.blockSize = (int) Math.pow(2.0, blockSizeBits);
		this.numBlockBits = cacheSizeBits - blockSizeBits;
		this.numBlocks = (int) Math.pow(2.0, numBlockBits);
		this.trace = trace;
		numHits = 0;
		numMisses = 0;
		numAccesses = 0;
		blocks = new ArrayList<Block>();
		initializeBlocks();
	}
	
	public void initializeBlocks()
	{
		for(int i = 0; i < numBlocks; i++)
		{
			Block block = new Block();
			this.blocks.add(block);
		}
	}
	
	public int generateBlockTag()
	{
		Random rand = new Random();
		//tag size = addressSize -indexSize - offsetSize
		int tagSize = ADDRESS_SIZE - (cacheSizeBits - numBlockBits) - numBlockBits;
		int randTag = (int)Math.pow(2.0, tagSize);
		int randAddressAgain = rand.nextInt(randTag) + 1;
		return randAddressAgain;
	}
	
	public int convertToIntegerDecimal(String address)
	{
		int intAddress;
		
		if(address.startsWith("0x"))
		{
			String subStr = address.substring(2);
			intAddress = Integer.parseInt(subStr, 16);
			
		}
		else
		{
			intAddress = Integer.parseInt(address);
		}
		
		return intAddress;
	}
	
	public static String convertToHex(int addr)
	{
		String address = Integer.toHexString(addr);
		return address;
	}
	
	public int getOffset()
	{
		return 0;
	}
	
	//returns the block address.  Rename to getBlockAddress?
	public int getBlockAddress(int fullAddress)
	{
		int divVal = (int) Math.pow(2.0, blockSizeBits);
		int blockAddress = fullAddress / divVal;
		return blockAddress;
	}
	
	//returns the index
	public int getIndex(int blockAddress)
	{
		int modVal = (int) Math.pow(2.0, numBlockBits);
		int index = blockAddress % modVal;
		return index;
	}
	
	public int removeIndex()
	{
		return 0;
	}
	
	//returns the tag 
	public int getTag(int blockAddress)
	{
		int divideVal = (int)Math.pow(2.0, numBlockBits);
		int tag = blockAddress / divideVal;
		return tag;
	}
	
	public void checkCache(String address)
	{
		numAccesses++;
		String hitOrMiss = "";
		
		//breakdown address into usable parts
		int intAddress = convertToIntegerDecimal(address);
		int blockAddress = getBlockAddress(intAddress);
		int index = getIndex(blockAddress);
		int tag = getTag(blockAddress);
				
		//actual work starts here
		if(blocks.get(index).getValidBit() == 0)
		{
			hitOrMiss = "miss";
			blocks.get(index).setValidBit(1);
			blocks.get(index).setTag(tag);
			numMisses++;
		}
		else if(blocks.get(index).getTag() != tag)
		{
			hitOrMiss = "miss";
			blocks.get(index).setTag(tag);
			numMisses++;
		}
		else if(blocks.get(index).getTag() == tag)
		{
			hitOrMiss = "hit";
			numHits++;
		}
		
		if(trace)
		{
			String hexAddress = convertToHex(intAddress);
			String hextag = convertToHex(tag);
			String hexBlockNum = convertToHex(index);
			String cacheTag = "";
			if(blocks.get(index).getTag() != -1)
			{
				cacheTag = convertToHex(blocks.get(index).getTag());
			}
			
			System.out.println("Add"  + "\t" + 	"Tag"  + "\t" + "Blk"  + "\t" + "CTg"  + "\t" + "H/M"  + "\t" + "#H"  + "\t" + "#M"  + "\t" + 	"#A"  + "\t" + "MR");
			System.out.println(hexAddress + "\t" + hextag + "\t" + hexBlockNum + "\t" + cacheTag + "\t" + hitOrMiss + "\t" + numHits + "\t" + numMisses + "\t" + numAccesses + "\t" + getMissRatio());
		}
	}
	
	public String getMissRatio()
	{
		DecimalFormat format = new DecimalFormat("###,###,###,##0.00000000");
		String ratio = format.format( (numMisses / (double)numAccesses) );
		return ratio;
	}
	
	public int getNumAccesses() 
	{
		return numAccesses;
	}

	public void setNumAccesses(int numAccesses) 
	{
		this.numAccesses = numAccesses;
	}

	public int getNumHits() 
	{
		return numHits;
	}

	public void setNumHits(int numHits) 
	{
		this.numHits = numHits;
	}

	public int getNumMisses() 
	{
		return numMisses;
	}

	public void setNumMisses(int numMisses) 
	{
		this.numMisses = numMisses;
	}
	
}
