package faguan;

import static org.junit.Assert.*;

import org.junit.Test;

public class ZhzbHanziTest {

	@Test
	public void testGetWenzi() {
		ZhzbHanzi hz = new ZhzbHanzi("µ¦", "13,13,16,16,16,15,13,14,17,16,13,15,15,12,7,14,18,17,12,8,10,10,7,3,2,3,7,13,14,12,17,21,23,19,13,11,15,16,9,8,15,15,12,9,11,20,18,7");
		assertEquals("µ¦", hz.getWenzi());
	}

	@Test
	public void testGetBitCount() {
		int[] bits = new int[] {3,4,5,8,12,12,14,6,6,7,7,8,8,8,12,11,9,12,15,15,10,5,3,0,2,3,5,5,6,15,13,5,7,7,6,6,7,18,19,15,3,3,7,14,17,16,1,0};
		ZhzbHanzi hz = new ZhzbHanzi("¹¦","3,4,5,8,12,12,14,6,6,7,7,8,8,8,12,11,9,12,15,15,10,5,3,0,2,3,5,5,6,15,13,5,7,7,6,6,7,18,19,15,3,3,7,14,17,16,1,0,");
		assertArrayEquals(bits, hz.getBitCount());
	}

	@Test
	public void testGetTotalBits() {
		ZhzbHanzi hz = new ZhzbHanzi("µ¦", "13,13,16,16,16,15,13,14,17,16,13,15,15,12,7,14,18,17,12,8,10,10,7,3,2,3,7,13,14,12,17,21,23,19,13,11,15,16,9,8,15,15,12,9,11,20,18,7");
		assertEquals(310, hz.getTotalBits());
		ZhzbHanzi hz2 = new ZhzbHanzi("¹¦", "3,4,5,8,12,12,14,6,6,7,7,8,8,8,12,11,9,12,15,15,10,5,3,0,2,3,5,5,6,15,13,5,7,7,6,6,7,18,19,15,3,3,7,14,17,16,1,0,");
		assertEquals(200, hz2.getTotalBits());	}

}
