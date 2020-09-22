package service;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.io.UnsupportedEncodingException;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.util.Vector;

import android.graphics.Paint;
import android.widget.TextView;
import util.ReadFileUtil;

/**
 * @author zzj 
 * @date 2014-7-29
 * read file to create pages
 */
public class BookPageService {

	private File book_file = null;
	private MappedByteBuffer m_mbBuf = null;
	private int m_mbBufLen = 0; //Buffer length
	private int m_mbBufBegin = 0;
	private int m_mbBufEnd = 0;
	private String m_strCharset = "UTF-8";

	private RandomAccessFile randomAccessFile = null;
	private Vector<String> m_lines = new Vector<String>();

	private int mLineCount;
	private float mHeight;
	private float mWidth;
	private boolean m_isfirstPage = true;
	private boolean m_islastPage = false;

	private Paint mPaint;

	public BookPageService(TextView mtv) {
		// TODO Auto-generated constructor stub
		mPaint = mtv.getPaint();
		mWidth = mtv.getWidth();
		mHeight = mtv.getHeight();
		mLineCount = (int) mHeight / mtv.getLineHeight();
	}

	public BookPageService(Paint paint,float height, float width, int lineCount) {
		// TODO Auto-generated constructor stub
		this.mPaint = paint;
		this.mHeight = height;
		this.mWidth = width;
		this.mLineCount = lineCount;
	}

	/**
	 * Reads file contents into memory
	 */
	public void openbook(String strFilePath) throws IOException {
		book_file = new File(strFilePath);
		try {
			m_strCharset = ReadFileUtil.getCharset(strFilePath);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		long lLen = book_file.length();
		m_mbBufLen = (int) lLen;
		randomAccessFile = new RandomAccessFile(book_file, "r");
		m_mbBuf = randomAccessFile.getChannel().map(
				FileChannel.MapMode.READ_ONLY, 0, lLen);
	}

	/**
	 * read paragraph back
	 */
	private byte[] readParagraphBack(int nFromPos) {
		int nEnd = nFromPos;
		int i;
		byte b0, b1;
		if (m_strCharset.equals("UTF-16LE")) {
			i = nEnd - 2;
			while (i > 0) {
				b0 = m_mbBuf.get(i);
				b1 = m_mbBuf.get(i + 1);
				if (b0 == 0x0a && b1 == 0x00 && i != nEnd - 2) {
					i += 2;
					break;
				}
				i--;
			}

		} else if (m_strCharset.equals("UTF-16BE")) {
			i = nEnd - 2;
			while (i > 0) {
				b0 = m_mbBuf.get(i);
				b1 = m_mbBuf.get(i + 1);
				if (b0 == 0x00 && b1 == 0x0a && i != nEnd - 2) {
					i += 2;
					break;
				}
				i--;
			}
		} else {
			i = nEnd - 1;
			while (i > 0) {
				b0 = m_mbBuf.get(i);
				if (b0 == 0x0a && i != nEnd - 1) {
					i++;
					break;
				}
				i--;
			}
		}
		if (i < 0)
			i = 0;
		int nParaSize = nEnd - i;
		int j;
		byte[] buf = new byte[nParaSize];
		for (j = 0; j < nParaSize; j++) {
			buf[j] = m_mbBuf.get(i + j);
		}
		return buf;
	}

	/**
	 * read paragraph forward
	 */
	private byte[] readParagraphForward(int nFromPos) {
		int nStart = nFromPos;
		int i = nStart;
		byte b0, b1;
		// determine if need to create a new line
		if (m_strCharset.equals("UTF-16LE")) {
			while (i < m_mbBufLen - 1) {
				b0 = m_mbBuf.get(i++);
				b1 = m_mbBuf.get(i++);
				if (b0 == 0x0a && b1 == 0x00) {
					break;
				}
			}
		} else if (m_strCharset.equals("UTF-16BE")) {
			while (i < m_mbBufLen - 1) {
				b0 = m_mbBuf.get(i++);
				b1 = m_mbBuf.get(i++);
				if (b0 == 0x00 && b1 == 0x0a) {
					break;
				}
			}
		} else {
			while (i < m_mbBufLen) {
				b0 = m_mbBuf.get(i++);
				if (b0 == 0x0a) {
					break;
				}
			}
		}
		int nParaSize = i - nStart;
		byte[] buf = new byte[nParaSize];
		for (i = 0; i < nParaSize; i++) {
			buf[i] = m_mbBuf.get(nFromPos + i);
		}
		return buf;
	}

	/**
	 * get next paragraph list of lines
	 */
	private Vector<String> getNextPageVector() {
		String strParagraph = "";
		Vector<String> lines = new Vector<String>();
		while (lines.size() < mLineCount && m_mbBufEnd < m_mbBufLen) {
			byte[] paraBuf = readParagraphForward(m_mbBufEnd); 
			m_mbBufEnd += paraBuf.length;
			try {
				strParagraph = new String(paraBuf, m_strCharset);
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			String strReturn = "";
			if (strParagraph.indexOf("\r\n") != -1) {
				strReturn = "\r\n";
				strParagraph = strParagraph.replaceAll("\r\n", "");
			} else if (strParagraph.indexOf("\n") != -1) {
				strReturn = "\n";
				strParagraph = strParagraph.replaceAll("\n", "");
			}

			if (strParagraph.length() == 0) {
				lines.add(strParagraph);
			}
			while (strParagraph.length() > 0) {
				//divide the paragraph
				int nSize = mPaint.breakText(strParagraph, true, mWidth, null);
				lines.add(strParagraph.substring(0, nSize));
				strParagraph = strParagraph.substring(nSize);
				if (lines.size() >= mLineCount) {
					break;
				}
			}
			if (strParagraph.length() != 0) {
				//Calculates the number of bits beyond the screen
				//and newline characters
				//adjusts the position pointer
				try {
					m_mbBufEnd -= (strParagraph + strReturn)
							.getBytes(m_strCharset).length;
				} catch (UnsupportedEncodingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		return lines;
	}

	/**
	 * get forward paragraph list of lines
	 */
	private Vector<String> getPrePageVector() {
		if (m_mbBufBegin < 0) {
			m_mbBufBegin = 0;
		}
		Vector<String> lines = new Vector<String>();
		String strParagraph = "";
		while (lines.size() < mLineCount && m_mbBufBegin > 0) {
			Vector<String> paraLines = new Vector<String>();
			byte[] paraBuf = readParagraphBack(m_mbBufBegin);
			m_mbBufBegin -= paraBuf.length;
			try {
				strParagraph = new String(paraBuf, m_strCharset);
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			strParagraph = strParagraph.replaceAll("\r\n", "");
			strParagraph = strParagraph.replaceAll("\n", "");

			if (strParagraph.length() == 0) {
				paraLines.add(strParagraph);
			}
			while (strParagraph.length() > 0) {
				int nSize = mPaint.breakText(strParagraph, true, mWidth, null);
				paraLines.add(strParagraph.substring(0, nSize));
				strParagraph = strParagraph.substring(nSize);
			}
			lines.addAll(0, paraLines);
		}
		while (lines.size() > mLineCount) {
			try {
				m_mbBufBegin += lines.get(0).getBytes(m_strCharset).length;
				lines.remove(0);
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		m_mbBufEnd = m_mbBufBegin;
		return lines;
	}

	/**
	 * read last page
	 */
	public void prePage() throws IOException {
		if (m_mbBufBegin <= 0) {
			m_mbBufBegin = 0;
			m_isfirstPage = true;
			return;
		} else {
			m_isfirstPage = false;
		}
		m_lines.clear();
		getPrePageVector();
		m_lines = getNextPageVector();
	}

	/**
	 * read next page
	 */
	public void nextPage() throws IOException {
		if (m_mbBufEnd >= m_mbBufLen) {
			m_islastPage = true;
			return;
		} else {
			m_islastPage = false;
		}
		m_lines.clear();
		m_mbBufBegin = m_mbBufEnd;
		m_lines = getNextPageVector();
	}

	public float getPercent() {
		return (float) (m_mbBufBegin * 1.0 / m_mbBufLen);
	}

	/**
	 * get the paragraph to be shown
	 */
	public String getText() {
		StringBuilder sb = new StringBuilder();
		if (m_lines.size() == 0) {
			m_lines = getNextPageVector();
		}
		if (m_lines.size() > 0) {
			for (String strLine : m_lines) {
				sb.append(strLine + "\n");
			}
		}
		return sb.toString();
	}

	/**
	 * get the list of lines to be shown
	 */
	public Vector<String> getTextVector() {
		if (m_lines.size() == 0) {
			m_lines = getNextPageVector();
		}
		return m_lines;
	}

	public boolean isfirstPage() {
		return m_isfirstPage;
	}

	public boolean islastPage() {
		return m_islastPage;
	}
}
