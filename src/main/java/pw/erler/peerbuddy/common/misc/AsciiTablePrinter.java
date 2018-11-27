package pw.erler.peerbuddy.common.misc;

import java.io.PrintStream;
import java.util.Arrays;
import java.util.List;

public class AsciiTablePrinter {

	private int checkTable(final List<List<String>> table) {
		if (table.isEmpty()) {
			return 0;
		}
		final int expectedColumnCount = table.get(0).size();
		for (final List<String> row : table) {
			if (row.size() != expectedColumnCount) {
				throw new IllegalArgumentException("Table must have consistent column count");
			}
		}
		return expectedColumnCount;
	}

	private int[] getColumnWidths(final List<List<String>> table, final int columnCount) {
		final int[] result = new int[columnCount];
		for (int columnIndex = 0; columnIndex < columnCount; ++columnIndex) {
			for (final List<String> row : table) {
				result[columnIndex] = Math.max(result[columnIndex], row.get(columnIndex).length());
			}
		}
		return result;
	}

	private void printEmptyLine(final PrintStream ps, final int width) {
		ps.print('+');
		for (int i = 0; i < width - 2; ++i) {
			ps.print('-');
		}
		ps.println('+');
	}

	private void printColumn(final PrintStream ps, final String column, final int columnWidth) {
		ps.print("| ");
		ps.print(column);
		for (int k = 2 + column.length(); k < columnWidth + 3; ++k) {
			ps.print(' ');
		}
	}

	private void printRowLine(final PrintStream ps, final int[] columnWidths, final List<String> row) {
		for (int i = 0; i < row.size(); i++) {
			final String column = row.get(i);
			printColumn(ps, column, columnWidths[i]);
		}
		ps.println('|');
	}

	public void print(final PrintStream ps, final List<List<String>> table) {
		final int columnCount = checkTable(table);
		final int[] columnWidths = getColumnWidths(table, columnCount);
		final int totalWidth = Arrays.stream(columnWidths).map(width -> width + 3).sum() + 1;

		printEmptyLine(ps, totalWidth);
		for (final List<String> row : table) {
			printRowLine(ps, columnWidths, row);
			printEmptyLine(ps, totalWidth);
		}
	}

}
