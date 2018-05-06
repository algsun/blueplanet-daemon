/**
 * 
 */
package com.microwise.msp.util;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.FileChannel.MapMode;

/**
 * <pre>
 * 从“共享内存”读出数据
 * </pre>
 * 
 * @author heming
 * @since 2012-01-07
 */
public class ShareMemoryRead {

	/**
	 * @param args
	 * @throws IOException
	 */
	public static void main(String[] args) throws IOException {

		RandomAccessFile raf = new RandomAccessFile("swap.galaxy", "rw");

		FileChannel fc = raf.getChannel();
		MappedByteBuffer mbb = fc.map(MapMode.READ_WRITE, 0, 1024);
		int lastIndex = 0;

		for (int i = 1; i < 27; i++) {
			int flag = mbb.get(0); // 取读写数据的标志
			int index = mbb.get(1); // 读取数据的位置,2 为可读

			if (flag != 2 || index == lastIndex) { // 假如不可读，或未写入新数据时重复循环
				i--;
				continue;
			}

			lastIndex = index;
			System.out.println("程序 ShareMemoryRead："
					+ System.currentTimeMillis() + "：位置：" + index + " 读出数据："
					+ (char) mbb.get(index));

			mbb.put(0, (byte) 0); // 置第一个字节为可读标志为 0

			if (index == 27) { // 读完数据后退出
				break;
			}
		}
	}
}
