package faguan;

import java.util.ArrayList;

public class HanziTest {

	public static Hanzi Cui_4 = new Hanzi("´á", new Long[]{0x3f0000L,
			0xfc000L,
			0x3ffffff00L,
			0x3fffc3fc0L,
			0x3cff0000L,
			0xfc3c0f0000L,
			0xff3c3fc000L,
			0x3fffffff00L,
			0x3ffff0ff00L,
			0xfc3c3c000L,
			0xcffcfffc000L,
			0x3ffffffff000L,
			0x3fcffc3cfc00L,
			0x3f3f00f03c00L,
			0xc3fc0ff0000L,
			0x3fffffffc0L,
			0x3f030f0ffc0L,
			0x3c000f00000L,
			0xfc003f00000L,
			0x3f0003f00000L,
			0xff0003f00000L,
			0xfc0003f00000L,
			0x3f00000L,
			0xfc00000L
	});

	public static Hanzi Bo_1 = new Hanzi("", new Long[]{0xc00L,
			0xf000000fc0L,
			0xffc3000f00L,
			0xfff000f00L,
			0xf03f00f00L,
			0xf00c03f00L,
			0xf00fffc00L,
			0xffc03f3ffcfL,
			0xffff3c0cfffL,
			0x3cff00c0fcL,
			0xf00300c0f0L,
			0xf03ff3c0c0L,
			0xf03fffcf00L,
			0x3ccfcffff00L,
			0xfffff0c03f00L,
			0xfff3f0f0f000L,
			0xfc033f000L,
			0x3f003fc000L,
			0xf000fc0000L,
			0xfc003ff0000L,
			0xf00fc0f0000L,
			0x3ff00f0000L,
			0xc0003c000L,
			0xfc00L
			}); 

	static String[] hanziPicture = {
			"[41m    [41m    [41m    [41m    [41m    [41m    [41m  [44m    [44m  [41m    [41m    [41m    [41m    [2;37;0m",
			"[41m    [41m    [41m    [41m    [41m    [41m    [41m    [44m    [44m  [41m    [41m    [41m    [41m  [2;37;0m",
			"[41m    [41m    [41m    [41m  [44m    [44m    [44m    [44m    [44m    [44m    [44m  [41m    [41m    [2;37;0m",
			"[41m    [41m    [41m    [41m  [44m    [44m    [44m    [44m    [41m    [44m    [44m    [41m    [41m  [2;37;0m",
			"[41m    [41m    [41m    [41m    [41m  [44m    [41m  [44m    [44m    [41m    [41m    [41m    [41m    [2;37;0m",
			"[41m    [41m    [44m    [44m  [41m    [44m    [41m    [41m  [44m    [41m    [41m    [41m    [41m    [2;37;0m",
			"[41m    [41m    [44m    [44m    [41m  [44m    [41m    [44m    [44m    [41m    [41m    [41m    [41m  [2;37;0m",
			"[41m    [41m    [41m  [44m    [44m    [44m    [44m    [44m    [44m    [44m    [44m  [41m    [41m    [2;37;0m",
			"[41m    [41m    [41m  [44m    [44m    [44m    [44m    [44m  [41m    [44m    [44m    [41m    [41m    [2;37;0m",
			"[41m    [41m    [41m    [44m    [44m  [41m    [44m    [41m    [44m    [41m    [41m    [41m    [41m  [2;37;0m",
			"[41m    [44m  [41m  [44m    [44m    [44m  [41m  [44m    [44m    [44m    [44m  [41m    [41m    [41m    [41m  [2;37;0m",
			"[41m  [44m    [44m    [44m    [44m    [44m    [44m    [44m    [44m    [44m  [41m    [41m    [41m    [2;37;0m",
			"[41m  [44m    [44m    [41m  [44m    [44m    [44m  [41m    [44m    [41m  [44m    [44m  [41m    [41m    [41m  [2;37;0m",
			"[41m  [44m    [44m  [41m  [44m    [44m  [41m    [41m    [44m    [41m    [41m  [44m    [41m    [41m    [41m  [2;37;0m",
			"[41m    [44m  [41m    [44m    [44m    [41m    [41m  [44m    [44m    [41m    [41m    [41m    [41m    [2;37;0m",
			"[41m    [41m    [41m  [44m    [44m    [44m    [44m    [44m    [44m    [44m    [44m    [41m    [41m  [2;37;0m",
			"[41m    [41m  [44m    [44m  [41m    [41m  [44m  [41m    [44m    [41m    [44m    [44m    [44m  [41m    [41m  [2;37;0m",
			"[41m    [41m  [44m    [41m    [41m    [41m    [41m  [44m    [41m    [41m    [41m    [41m    [41m    [2;37;0m",
			"[41m    [44m    [44m  [41m    [41m    [41m    [44m    [44m  [41m    [41m    [41m    [41m    [41m    [2;37;0m",
			"[41m  [44m    [44m  [41m    [41m    [41m    [41m  [44m    [44m  [41m    [41m    [41m    [41m    [41m    [2;37;0m",
			"[44m    [44m    [41m    [41m    [41m    [41m  [44m    [44m  [41m    [41m    [41m    [41m    [41m    [2;37;0m",
			"[44m    [44m  [41m    [41m    [41m    [41m    [44m    [44m  [41m    [41m    [41m    [41m    [41m    [2;37;0m",
			"[41m    [41m    [41m    [41m    [41m    [41m  [44m    [44m  [41m    [41m    [41m    [41m    [41m    [2;37;0m",
			"[41m    [41m    [41m    [41m    [41m    [44m    [44m  [41m    [41m    [41m    [41m    [41m    [41m  [2;37;0m" }; // ´á

	static String[] hanziPicture_Bo = {
			"[41m    [41m    [41m    [41m    [41m    [41m    [41m    [41m    [41m    [44m  [41m    [41m    [41m  [2;37;0m",
			"[41m    [41m    [44m    [41m    [41m    [41m    [41m    [41m    [41m    [44m    [44m  [41m    [41m  [2;37;0m",
			"[41m    [41m    [44m    [44m    [44m  [41m    [44m  [41m    [41m    [41m    [44m    [41m    [41m    [2;37;0m",
			"[41m    [41m    [41m    [44m    [44m    [44m    [41m    [41m    [41m    [44m    [41m    [41m    [2;37;0m",
			"[41m    [41m    [41m    [44m    [41m    [41m  [44m    [44m  [41m    [41m    [44m    [41m    [41m    [2;37;0m",
			"[41m    [41m    [41m    [44m    [41m    [41m    [44m  [41m    [41m    [44m    [44m  [41m    [41m    [2;37;0m",
			"[41m    [41m    [41m    [44m    [41m    [41m    [44m    [44m    [44m    [44m  [41m    [41m    [41m  [2;37;0m",
			"[41m    [44m    [44m    [44m  [41m    [41m    [44m    [44m  [41m  [44m    [44m    [44m    [41m  [44m    [2;37;0m",
			"[41m    [44m    [44m    [44m    [44m    [41m  [44m    [41m    [41m  [44m  [41m  [44m    [44m    [44m    [2;37;0m",
			"[41m    [41m    [41m  [44m    [41m  [44m    [44m    [41m    [41m    [44m  [41m    [41m  [44m    [44m  [41m  [2;37;0m",
			"[41m    [41m    [44m    [41m    [41m    [41m  [44m  [41m    [41m    [44m  [41m    [41m  [44m    [41m    [2;37;0m",
			"[41m    [41m    [44m    [41m    [41m  [44m    [44m    [44m  [41m  [44m    [41m    [41m  [44m  [41m    [41m  [2;37;0m",
			"[41m    [41m    [44m    [41m    [41m  [44m    [44m    [44m    [44m    [41m  [44m    [41m    [41m    [2;37;0m",
			"[41m    [41m  [44m    [41m  [44m  [41m  [44m    [44m  [41m  [44m    [44m    [44m    [44m    [41m    [41m    [2;37;0m",
			"[44m    [44m    [44m    [44m    [44m    [41m    [44m  [41m    [41m    [44m    [44m  [41m    [41m    [2;37;0m",
			"[44m    [44m    [44m    [41m  [44m    [44m  [41m    [44m    [41m    [44m    [41m    [41m    [41m    [2;37;0m",
			"[41m    [41m    [41m    [44m    [44m  [41m    [41m    [44m  [41m  [44m    [44m  [41m    [41m    [41m    [2;37;0m",
			"[41m    [41m    [41m  [44m    [44m  [41m    [41m    [41m  [44m    [44m    [41m    [41m    [41m    [41m  [2;37;0m",
			"[41m    [41m    [44m    [41m    [41m    [41m    [44m    [44m  [41m    [41m    [41m    [41m    [41m  [2;37;0m",
			"[41m    [44m    [44m  [41m    [41m    [41m    [44m    [44m    [44m  [41m    [41m    [41m    [41m    [2;37;0m",
			"[41m    [44m    [41m    [41m    [44m    [44m  [41m    [41m  [44m    [41m    [41m    [41m    [41m    [2;37;0m",
			"[41m    [41m    [41m  [44m    [44m    [44m  [41m    [41m    [44m    [41m    [41m    [41m    [41m    [2;37;0m",
			"[41m    [41m    [41m    [44m  [41m    [41m    [41m    [41m    [44m    [41m    [41m    [41m    [41m  [2;37;0m",
			"[41m    [41m    [41m    [41m    [41m    [41m    [41m    [41m    [44m    [44m  [41m    [41m    [41m  [2;37;0m" }; // ²£

	static String[] hanziPicture_Bo_2 = {
			"[43m    [43m    [43m    [43m    [43m    [43m    [43m    [43m    [43m    [43m    [43m    [43m    [2;37;0m",
			"[43m    [43m    [43m    [43m    [43m    [43m    [43m    [47;1m    [43m    [43m    [43m    [43m    [2;37;0m",
			"[43m    [43m  [47;1m    [47;1m    [47;1m  [43m    [43m    [43m    [47;1m    [43m    [43m    [43m    [43m    [2;37;0m",
			"[43m  [47;1m    [47;1m    [47;1m    [47;1m  [43m  [47;1m    [47;1m    [47;1m    [47;1m    [47;1m    [47;1m    [47;1m    [43m  [2;37;0m",
			"[43m  [47;1m    [47;1m    [47;1m    [47;1m    [47;1m    [47;1m    [47;1m    [47;1m    [47;1m    [47;1m    [47;1m    [47;1m  [2;37;0m",
			"[43m  [47;1m    [43m  [47;1m    [43m    [43m  [47;1m    [47;1m    [47;1m    [47;1m    [47;1m    [47;1m    [47;1m    [47;1m  [2;37;0m",
			"[43m    [43m    [47;1m    [43m    [43m  [47;1m    [47;1m  [43m    [43m  [47;1m    [43m    [43m    [47;1m    [43m  [2;37;0m",
			"[43m    [43m    [47;1m    [43m    [43m  [47;1m    [47;1m  [43m    [43m  [47;1m    [43m    [43m  [47;1m    [47;1m  [43m  [2;37;0m",
			"[43m    [43m    [47;1m    [43m    [43m  [47;1m    [47;1m  [43m    [43m  [47;1m    [43m    [43m  [47;1m    [43m    [2;37;0m",
			"[43m    [43m    [47;1m    [43m    [43m  [47;1m    [47;1m  [43m    [43m  [47;1m    [43m    [43m  [47;1m    [47;1m  [43m  [2;37;0m",
			"[43m    [43m  [47;1m    [47;1m    [47;1m    [47;1m    [47;1m    [47;1m    [47;1m    [47;1m    [47;1m    [47;1m    [43m  [2;37;0m",
			"[43m  [47;1m    [47;1m    [47;1m    [47;1m    [47;1m    [47;1m    [47;1m    [47;1m    [47;1m    [47;1m    [47;1m    [43m  [2;37;0m",
			"[43m  [47;1m    [43m  [47;1m    [43m    [43m  [47;1m    [47;1m    [47;1m    [47;1m    [47;1m    [47;1m    [47;1m    [43m  [2;37;0m",
			"[43m    [43m    [47;1m    [43m    [43m  [47;1m    [47;1m    [47;1m  [43m    [43m    [43m    [47;1m    [47;1m  [43m  [2;37;0m",
			"[43m    [43m    [47;1m    [43m    [43m  [47;1m    [43m  [47;1m    [43m    [43m    [43m    [47;1m    [43m    [2;37;0m",
			"[43m    [43m    [47;1m    [43m    [43m  [47;1m    [43m  [47;1m    [47;1m  [43m    [43m    [47;1m    [47;1m  [43m    [2;37;0m",
			"[43m    [43m    [47;1m    [47;1m    [47;1m    [47;1m  [43m    [47;1m    [47;1m  [43m    [47;1m    [47;1m  [43m    [43m  [2;37;0m",
			"[43m    [43m    [47;1m    [47;1m    [47;1m    [47;1m  [43m    [43m  [47;1m    [47;1m    [47;1m    [47;1m  [43m    [43m  [2;37;0m",
			"[43m    [43m  [47;1m    [47;1m    [47;1m    [47;1m    [43m    [43m  [47;1m    [47;1m    [47;1m    [43m    [43m    [2;37;0m",
			"[43m    [43m  [47;1m    [47;1m  [43m  [47;1m    [47;1m  [43m    [43m    [43m  [47;1m    [47;1m    [43m    [43m    [43m  [2;37;0m",
			"[43m    [47;1m    [47;1m  [43m  [47;1m    [47;1m    [43m    [43m  [47;1m    [47;1m    [47;1m    [47;1m    [43m    [43m  [2;37;0m",
			"[47;1m    [47;1m  [43m    [43m  [47;1m    [47;1m  [43m    [47;1m    [47;1m    [47;1m  [43m  [47;1m    [47;1m    [47;1m    [47;1m  [2;37;0m",
			"[43m  [47;1m    [43m    [43m  [47;1m    [47;1m  [43m  [47;1m    [47;1m    [47;1m  [43m    [43m    [47;1m    [47;1m    [47;1m  [2;37;0m",
			"[43m    [43m    [43m    [43m  [47;1m    [43m  [47;1m    [47;1m    [43m    [43m    [43m    [43m  [47;1m  [43m  [47;1m  [2;37;0m" }; // ²£

	static String[] hanziPicture_poya = {
			"[43m    [43m    [43m    [43m    [43m    [43m    [43m    [43m    [43m    [43m    [43m    [43m    [2;37;0m[41m    [41m    [41m    [41m    [41m    [41m    [41m    [41m    [41m    [41m    [41m    [41m    [2;37;0m",
			"[43m    [43m    [43m    [43m    [43m    [43m    [43m    [47;1m    [43m    [43m    [43m    [43m    [2;37;0m[41m    [41m    [41m  [47;1m  [41m    [41m    [41m    [41m    [41m    [41m    [41m    [47;1m    [41m    [2;37;0m",
			"[43m    [43m  [47;1m    [47;1m    [47;1m  [43m    [43m    [43m    [47;1m    [43m    [43m    [43m    [43m    [2;37;0m[41m    [41m    [41m  [47;1m    [47;1m    [41m  [47;1m    [41m    [41m    [41m    [47;1m    [47;1m    [41m    [2;37;0m",
			"[43m  [47;1m    [47;1m    [47;1m    [47;1m  [43m  [47;1m    [47;1m    [47;1m    [47;1m    [47;1m    [47;1m    [47;1m    [43m  [2;37;0m[41m    [41m  [47;1m    [47;1m    [47;1m    [47;1m    [47;1m    [47;1m  [41m    [41m  [47;1m    [47;1m    [41m    [41m  [2;37;0m",
			"[43m  [47;1m    [47;1m    [47;1m    [47;1m    [47;1m    [47;1m    [47;1m    [47;1m    [47;1m    [47;1m    [47;1m    [47;1m  [2;37;0m[41m    [41m  [47;1m    [47;1m  [41m    [47;1m    [41m    [47;1m    [47;1m  [41m  [47;1m    [47;1m    [47;1m  [41m    [41m  [2;37;0m",
			"[43m  [47;1m    [43m  [47;1m    [43m    [43m  [47;1m    [47;1m    [47;1m    [47;1m    [47;1m    [47;1m    [47;1m    [47;1m  [2;37;0m[41m    [41m  [47;1m    [47;1m    [47;1m    [47;1m  [41m  [47;1m    [47;1m    [47;1m    [47;1m    [47;1m    [47;1m  [41m    [2;37;0m",
			"[43m    [43m    [47;1m    [43m    [43m  [47;1m    [47;1m  [43m    [43m  [47;1m    [43m    [43m    [47;1m    [43m  [2;37;0m[41m    [41m  [47;1m    [47;1m    [47;1m    [47;1m    [47;1m    [47;1m    [47;1m  [41m    [47;1m    [47;1m    [41m    [2;37;0m",
			"[43m    [43m    [47;1m    [43m    [43m  [47;1m    [47;1m  [43m    [43m  [47;1m    [43m    [43m  [47;1m    [47;1m  [43m  [2;37;0m[41m    [41m  [47;1m    [47;1m    [47;1m    [47;1m    [47;1m    [47;1m  [41m  [47;1m    [47;1m  [41m  [47;1m    [47;1m  [41m    [2;37;0m",
			"[43m    [43m    [47;1m    [43m    [43m  [47;1m    [47;1m  [43m    [43m  [47;1m    [43m    [43m  [47;1m    [43m    [2;37;0m[41m    [41m  [47;1m    [47;1m  [41m  [47;1m    [47;1m  [41m  [47;1m    [41m  [47;1m    [47;1m    [47;1m    [47;1m    [41m    [2;37;0m",
			"[43m    [43m    [47;1m    [43m    [43m  [47;1m    [47;1m  [43m    [43m  [47;1m    [43m    [43m  [47;1m    [47;1m  [43m  [2;37;0m[41m    [41m  [47;1m    [47;1m    [47;1m    [47;1m    [47;1m    [41m  [47;1m    [47;1m    [47;1m    [41m    [41m    [2;37;0m",
			"[43m    [43m  [47;1m    [47;1m    [47;1m    [47;1m    [47;1m    [47;1m    [47;1m    [47;1m    [47;1m    [47;1m    [43m  [2;37;0m[41m    [41m    [41m  [47;1m    [47;1m    [47;1m    [47;1m  [41m    [47;1m    [47;1m    [47;1m    [41m    [41m    [2;37;0m",
			"[43m  [47;1m    [47;1m    [47;1m    [47;1m    [47;1m    [47;1m    [47;1m    [47;1m    [47;1m    [47;1m    [47;1m    [43m  [2;37;0m[41m    [41m    [41m    [47;1m    [47;1m    [41m    [41m  [47;1m    [41m    [41m  [47;1m    [41m    [41m    [2;37;0m",
			"[43m  [47;1m    [43m  [47;1m    [43m    [43m  [47;1m    [47;1m    [47;1m    [47;1m    [47;1m    [47;1m    [47;1m    [43m  [2;37;0m[41m    [41m    [41m    [47;1m    [47;1m  [41m    [41m    [47;1m    [47;1m    [47;1m    [47;1m    [41m    [41m  [2;37;0m",
			"[43m    [43m    [47;1m    [43m    [43m  [47;1m    [47;1m    [47;1m  [43m    [43m    [43m    [47;1m    [47;1m  [43m  [2;37;0m[41m    [41m    [41m  [47;1m    [41m    [41m    [41m    [47;1m    [47;1m    [47;1m    [47;1m    [41m    [41m  [2;37;0m",
			"[43m    [43m    [47;1m    [43m    [43m  [47;1m    [43m  [47;1m    [43m    [43m    [43m    [47;1m    [43m    [2;37;0m[41m    [41m    [41m  [47;1m    [41m    [41m    [41m  [47;1m    [41m    [47;1m    [47;1m  [41m  [47;1m  [41m    [41m  [2;37;0m",
			"[43m    [43m    [47;1m    [43m    [43m  [47;1m    [43m  [47;1m    [47;1m  [43m    [43m    [47;1m    [47;1m  [43m    [2;37;0m[41m    [41m    [47;1m    [47;1m  [41m    [41m    [47;1m    [47;1m    [47;1m    [47;1m    [47;1m  [41m    [41m    [2;37;0m",
			"[43m    [43m    [47;1m    [47;1m    [47;1m    [47;1m  [43m    [47;1m    [47;1m  [43m    [47;1m    [47;1m  [43m    [43m  [2;37;0m[41m    [41m  [47;1m    [47;1m    [41m    [47;1m  [41m    [41m  [47;1m    [41m    [47;1m    [47;1m  [41m    [41m    [2;37;0m",
			"[43m    [43m    [47;1m    [47;1m    [47;1m    [47;1m  [43m    [43m  [47;1m    [47;1m    [47;1m    [47;1m  [43m    [43m  [2;37;0m[41m    [41m  [47;1m    [47;1m    [47;1m    [41m  [47;1m    [47;1m  [41m  [47;1m    [47;1m  [41m    [41m  [47;1m    [41m    [2;37;0m",
			"[43m    [43m  [47;1m    [47;1m    [47;1m    [47;1m    [43m    [43m  [47;1m    [47;1m    [47;1m    [43m    [43m    [2;37;0m[41m    [41m  [47;1m    [47;1m  [41m  [47;1m    [41m  [47;1m    [47;1m    [47;1m    [47;1m    [41m  [47;1m    [47;1m    [41m  [2;37;0m",
			"[43m    [43m  [47;1m    [47;1m  [43m  [47;1m    [47;1m  [43m    [43m    [43m  [47;1m    [47;1m    [43m    [43m    [43m  [2;37;0m[41m    [47;1m    [41m    [41m    [41m    [41m    [41m  [47;1m  [41m    [47;1m    [41m  [47;1m    [47;1m  [41m    [2;37;0m",
			"[43m    [47;1m    [47;1m  [43m  [47;1m    [47;1m    [43m    [43m  [47;1m    [47;1m    [47;1m    [47;1m    [43m    [43m  [2;37;0m[41m    [41m  [47;1m  [41m    [41m    [41m    [41m    [41m    [41m    [41m    [41m  [47;1m    [47;1m  [41m    [2;37;0m",
			"[47;1m    [47;1m  [43m    [43m  [47;1m    [47;1m  [43m    [47;1m    [47;1m    [47;1m  [43m  [47;1m    [47;1m    [47;1m    [47;1m  [2;37;0m[41m    [41m  [47;1m  [41m    [41m    [41m    [41m  [47;1m    [41m    [41m    [47;1m    [47;1m    [41m    [41m  [2;37;0m",
			"[43m  [47;1m    [43m    [43m  [47;1m    [47;1m  [43m  [47;1m    [47;1m    [47;1m  [43m    [43m    [47;1m    [47;1m    [47;1m  [2;37;0m[41m    [41m    [41m    [41m    [41m    [41m    [47;1m    [41m    [41m  [47;1m    [47;1m  [41m    [41m    [2;37;0m",
			"[43m    [43m    [43m    [43m  [47;1m    [43m  [47;1m    [47;1m    [43m    [43m    [43m    [43m  [47;1m  [43m  [47;1m  [2;37;0m[41m    [41m    [41m    [41m    [41m    [41m    [47;1m    [47;1m    [47;1m    [47;1m  [41m    [41m    [41m  [2;37;0m" }; // ²£Ñ¼

	// hanzi ÎÞ, ±³¾°É«¡¢Ç°¾°É«·´Ïò
	private static String hanziString_wu_reverse = "f;c0000000ffff;c0000fffffff;ffff0fffffff;ffff0fffffff;ffff0fffffff;fffc0fff0003;fffc00000003;3;c0000000ffff;c0000f03ffff;ffff0f03ffff;ffff0f03ffff;fffc0f03ffff;fffc3f03ffff;fff03f03ffff;fff03f00fffc;fff03fc0fffc;fff03fc0fff0;ffc0ffc0fff0;ff03ffc0c000;fc03ffc00000;f00fffc03fff;c03fffffffff;";
	
	// hanzi ÐÝ, ´øµ¥µã
	private static String hanziString_xiu_with_single_point = "f000;c00f000;ff00f000;303fc00c000;ffc00c000;f3ff000c000;fff0000c000;fff3fff3c00;3cf03ffff00;3f000f0300;fc003f0000;fc00fc0000;f00ffc0000;3f03fff0000;3f3fcfff000;3ffc0fffc00;3cf00f03fc0;f0000f00ff0;f0003c000fc;fc003c000fc;30003c00030;3c00000;3f000000;f000000;";
	
	// hanzi ¼Ï, 24*24 simple
	private static String hanziString_jia_simple = "60c0;c0c0;c1c0;c180;1f8180;bf300;1fb00;79340;7dff0;1ff7f8;1ee6ff;7659ff;c619e1;8c19e3;c33e3;c76c2;18e6c4;198ccc;19cd9c;364df8;14719c;1c7100;3100;0;";
	
	private static void testLong() {
		// BigInteger aLong = new BigInteger(Long.MAX_VALUE);
		// Long aLong = Long.MAX_VALUE;
		Long aLong = 1032192L;
		System.out.println(aLong);
		for (int i = 63; i >= 0; i--) {
			if ((aLong & (1L << i)) != 0) {
				System.out.print("*");
			} else {
				System.out.print(" ");
			}
		}
		System.out.println();
	}

	private static void testEsc() {
		String str = "\033"; // ÌØÊâ×Ö·û ESC
		System.out.println(str.length());
		String line = "[43m    [43m    [43m    [43m    [43m    [43m    [43m    [43m    [43m    [43m    [43m    [43m    [2;37;0m[41m    [41m    [41m    [41m    [41m    [41m    [41m    [41m    [41m    [41m    [41m    [41m    [2;37;0m";
		String regex = "\033\\[2;37;0m";
		System.out.println(regex.length());
		String[] parts = line.split(regex);
		System.out.println(parts.length);
		System.out.println(parts[0]);
		System.out.println(parts[1]);

	}

	private static void testHanzi() {
		HanziReader reader = new HanziReader();
		Hanzi hanzi = reader.parseOneHanzi(hanziPicture_Bo);
		System.out.println(hanzi.getDisplayString());
		System.out.println(hanzi.getWenzi());
		System.out.println(hanzi.getBitMapString());
		System.out.println(hanzi.getBitCountString());

		hanzi = reader.parseOneHanzi(hanziPicture_Bo_2);
		System.out.println(hanzi.getDisplayString());
		System.out.println(hanzi.getWenzi());
		System.out.println(hanzi.getBitMapString());
		System.out.println(hanzi.getBitCountString());
	}
	
	private static void testHanziReverse(){
		Hanzi hanzi = new Hanzi("ÎÞ", hanziString_wu_reverse);
		System.out.println(hanzi.getDisplayString());
		System.out.println(hanzi.getWenzi());
		System.out.println(hanzi.getBitMapString());
		System.out.println(hanzi.getFormalDisplayString());
		System.out.println(hanzi.getBitCountString());
	}
	
	private static void testHanziClearSinglePoint(){
		Hanzi hanzi = new Hanzi("ÐÝ", hanziString_xiu_with_single_point);
		System.out.println(hanzi.getDisplayString());
		System.out.println(hanzi.getWenzi());
		System.out.println(hanzi.getBitMapString());
		System.out.println(hanzi.getFormalDisplayString());
		System.out.println(hanzi.getBitCountString());
	}
	
	private static void testHanziSimple(){
		Hanzi hanzi = new Hanzi("¼Ï", hanziString_jia_simple);
		System.out.println(hanzi.getDisplayString());
		System.out.println(hanzi.getWenzi());
		System.out.println(hanzi.getBitMapString());
		System.out.println(hanzi.getFormalDisplayString());
		System.out.println("4,4,5,4,8,9,8,8,14,17,17,17,11,11,11,10,10,10,12,13,10,7,3,0,2,2,2,10,11,13,9,8,13,16,11,13,8,8,9,17,18,15,8,7,7,6,5,5,");
		System.out.println(hanzi.getBitCountString());
	}
	
	private static void testHanziSimple2(){
		Hanzi hanzi = new Hanzi("ÈÍ", "70000;60000;6000e;641fe;1fe7fc;3f80cc;600cc;6c6cc;3fe6cc;1f06cc;626cc;3ffd8c;fe2d8c;62588;66318;76318;7e318;6c618;64fd8;60cf0;61870;63030;0;0;");
		System.out.println(hanzi.getDisplayString());
		System.out.println(hanzi.getWenzi());
		System.out.println(hanzi.getBitMapString());
		System.out.println(hanzi.getFormalDisplayString());
		System.out.println("3,2,5,11,17,11,6,10,15,11,9,16,14,7,8,9,10,8,11,8,7,6,0,0,1,1,4,6,6,22,22,8,7,10,10,3,5,11,10,9,13,11,5,10,17,11,2,0,");
		System.out.println(hanzi.getBitCountString());
	}
	
	private static void testHanzi2() {
		HanziReader2 reader = new HanziReader2();
		Hanzi hanzi = reader.parseOneHanzi(hanziPicture_Bo);
		System.out.println(hanzi.getDisplayString());
		System.out.println(hanzi.getWenzi());
		System.out.println(hanzi.getBitMapString());
		System.out.println(hanzi.getBitCountString());

		hanzi = reader.parseOneHanzi(hanziPicture_Bo_2);
		System.out.println(hanzi.getDisplayString());
		System.out.println(hanzi.getWenzi());
		System.out.println(hanzi.getBitMapString());
		System.out.println(hanzi.getBitCountString());
	}

	private static void testTwoHanzi() {
		HanziReader reader = new HanziReader();
		Hanzi[] hanziArray = reader.parseTwoHanzi(hanziPicture_poya);
		System.out.println(hanziArray[0].getDisplayString());
		System.out.println(hanziArray[0].getWenzi());
		System.out.println(hanziArray[0].getBitMapString());
		System.out.println(hanziArray[1].getDisplayString());
		System.out.println(hanziArray[1].getWenzi());
		System.out.println(hanziArray[1].getBitMapString());
	}
	
	private static void testHanziFile() {
		HanziReader reader = new HanziReader();
		ArrayList<Hanzi> hanziList = reader.parseOriginFile("resource_origin/aaa.txt");
		for(Hanzi hanzi : hanziList) {
			System.out.println(hanzi.getDisplayString());
			System.out.println(hanzi.getWenzi());
			System.out.println(hanzi.getWenzi() + " " + hanzi.getBitMapString());
			System.out.println(hanzi.getBitCountString());
		}
	}
	
	private static int computeDelta(Hanzi hanzi1, Hanzi hanzi2){
		int delta = 0;
		Long[] bitMap1 = hanzi1.getFormalBitMap();
		Long[] bitMap2 = hanzi2.getFormalBitMap();
		
		int[] rowBitCount1 = new int[24];
		int[] rowBitCount2 = new int[24];
		for (int i = 0; i < 24; i++) {
			rowBitCount1[i] = 0;
			rowBitCount2[i] = 0;
			Long row1 = bitMap1[i];
			Long row2 = bitMap2[i];
			for(int j = 0; j < 24; j++) {
				if((row1 & (1L<<(23-j))) != 0) {
					rowBitCount1[i]++;
				}
				if((row2 & (1L<<(23-j))) != 0) {
					rowBitCount2[i]++;
				}
			}
			
			delta += (rowBitCount1[i] - rowBitCount2[i]) * (rowBitCount1[i] - rowBitCount2[i]);
		}
		
		int[] colBitCount1 = new int[24];
		int[] colBitCount2 = new int[24];
		for (int i = 0; i < 24; i++) {
			colBitCount1[i] = 0;
			colBitCount2[i] = 0;
			for(Long row : bitMap1) {
				if((row & (1L<<(23-i))) != 0) {
					colBitCount1[i]++;
				}
			}
			for(Long row : bitMap2) {
				if((row & (1L<<(23-i))) != 0) {
					colBitCount2[i]++;
				}
			}
			
			delta += (colBitCount1[i] - colBitCount2[i]) * (colBitCount1[i] - colBitCount2[i]);
		}
		
		return delta;
	}
	
	public static void main(String[] args) {
//		testLong();
		
//		testEsc();
		
//		testHanzi();
		
//		testHanziReverse();
		
//		testHanziClearSinglePoint();
		
//		testTwoHanzi();
		
//		testHanzi2();
		
//		Hanzi hanzi1 = new Hanzi("ÎÞ", hanziString_wu_reverse);
//		Hanzi hanzi2 = new Hanzi("ÎÞ", hanziString_wu_reverse);
//		
//		int delta = computeDelta(hanzi1, hanzi2);
//		System.out.println(delta);
		
//		testHanziSimple();
//		testHanziSimple2();
		
		testHanziFile();
	}

}
