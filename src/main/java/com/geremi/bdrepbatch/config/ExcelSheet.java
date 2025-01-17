package com.geremi.bdrepbatch.config;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

import org.apache.poi.ss.formula.ConditionalFormattingEvaluator;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.util.LocaleUtil;
import org.springframework.batch.extensions.excel.Sheet;
import org.springframework.lang.Nullable;

class ExcelSheet implements Sheet {

	private final DataFormatter dataFormatter;

	private final org.apache.poi.ss.usermodel.Sheet delegate;
	private final boolean datesAsIso;

	private final int numberOfRows;

	private final String name;

	private FormulaEvaluator evaluator;

	ExcelSheet(final org.apache.poi.ss.usermodel.Sheet delegate, boolean datesAsIso) {
		super();
		this.delegate = delegate;
		this.datesAsIso = datesAsIso;
		this.numberOfRows = this.delegate.getLastRowNum() + 1;
		this.name = this.delegate.getSheetName();
		this.dataFormatter = this.datesAsIso ? new IsoFormattingDateDataFormatter() : new DataFormatter(Locale.FRANCE);
		this.dataFormatter.setUse4DigitYearsInAllDateFormats(true);
	}

	@Override
	public int getNumberOfRows() {
		return this.numberOfRows;
	}

	@Override
	public String getName() {
		return this.name;
	}

	@Override
	@Nullable
	public String[] getRow(final int rowNumber) {
		final Row row = this.delegate.getRow(rowNumber);
		return map(row);
	}

	@Nullable
	private String[] map(Row row) {
		if (row == null) {
			return null;
		}
		final List<String> cells = new LinkedList<>();
		final int numberOfColumns = row.getLastCellNum();

		for (int i = 0; i < numberOfColumns; i++) {
			Cell cell = row.getCell(i);
			CellType cellType = cell.getCellType();
			if (cellType == CellType.FORMULA) {
				cells.add(this.dataFormatter.formatCellValue(cell, getFormulaEvaluator()));
			} else {
				if(cellType == CellType.NUMERIC) {
					String formattedCellValue = this.dataFormatter.formatCellValue(cell);	
					if (formattedCellValue.matches("\\d{1,2}[\\/.-]\\d{1,2}[\\/.-]\\d{4}")) {
						String[] splitDate = formattedCellValue.split("/");
						String dateWithoutPermutation = String.format("%2s/%2s/%4s", splitDate[1], splitDate[0], splitDate[2]).replace(' ', '0');
						cells.add(dateWithoutPermutation);
					} else {
						cells.add(this.dataFormatter.formatCellValue(cell));
					}
				}else{
					cells.add(this.dataFormatter.formatCellValue(cell));
				}
			}
		}
		return cells.toArray(new String[0]);
	}

	private FormulaEvaluator getFormulaEvaluator() {
		if (this.evaluator == null) {
			this.evaluator = this.delegate.getWorkbook().getCreationHelper().createFormulaEvaluator();
		}
		return this.evaluator;
	}

	@Override
	public Iterator<String[]> iterator() {
		return new Iterator<String[]>() {
			private final Iterator<Row> delegateIter = ExcelSheet.this.delegate.iterator();

			@Override
			public boolean hasNext() {
				return this.delegateIter.hasNext();
			}

			@Override
			public String[] next() {
				return map(this.delegateIter.next());
			}
		};
	}

	private static class IsoFormattingDateDataFormatter extends DataFormatter {

		@Override
		public String formatCellValue(Cell cell, FormulaEvaluator evaluator,
				ConditionalFormattingEvaluator cfEvaluator) {
			if (cell == null) {
				return "";
			}

			CellType cellType = cell.getCellType();
			if (cellType == CellType.FORMULA) {
				if (evaluator == null) {
					return cell.getCellFormula();
				}
				cellType = evaluator.evaluateFormulaCell(cell);
			}

			if (cellType == CellType.NUMERIC && DateUtil.isCellDateFormatted(cell, cfEvaluator)) {
				LocalDateTime value = cell.getLocalDateTimeCellValue();
				return (value != null) ? value.format(DateTimeFormatter.ISO_OFFSET_DATE_TIME) : "";
			}
			return super.formatCellValue(cell, evaluator, cfEvaluator);
		}
	}
}
