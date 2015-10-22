
public class Block 
{
	public int blockSize;
	public int validBit;
	public int tag;
	
	public Block()
	{
		validBit = 0;
		tag = -1;
	}
	
	public int getValidBit() {
		return validBit;
	}

	public void setValidBit(int validBit) {
		this.validBit = validBit;
	}

	public int getTag() {
		return tag;
	}

	public void setTag(int tag) {
		this.tag = tag;
	}

}
