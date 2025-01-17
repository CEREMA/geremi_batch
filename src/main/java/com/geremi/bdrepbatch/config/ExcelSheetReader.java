package com.geremi.bdrepbatch.config;

import java.io.File;
import java.io.InputStream;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.springframework.batch.extensions.excel.Sheet;
import org.springframework.core.io.Resource;

/**
 * @author a875497
 *
 * @param <T>
 */
public class ExcelSheetReader<T> extends ExcelItemReader<T> {

	private Workbook workbook;

	private InputStream inputStream;

	private boolean datesAsIso = false;

	@Override
	public Sheet getSheet(final int sheet) {
		return new ExcelSheet(this.workbook.getSheetAt(sheet), this.datesAsIso);
	}

	@Override
	protected int getNumberOfSheets() {
		return this.workbook.getNumberOfSheets();
	}

	@Override
	protected void doClose() throws Exception {
		super.doClose();
		if (this.inputStream != null) {
			this.inputStream.close();
			this.inputStream = null;
		}

		if (this.workbook != null) {
			this.workbook.close();
			this.workbook = null;
		}
	}

	@Override
	protected void openExcelFile(final Resource resource, String password) throws Exception {
		if (resource.isFile()) {
			File file = resource.getFile();
			this.workbook = WorkbookFactory.create(file, password, false);

		} else {
			this.inputStream = resource.getInputStream();
			this.workbook = WorkbookFactory.create(this.inputStream, password);
		}

		this.workbook.setMissingCellPolicy(Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
	}

	public void setDatesAsIso(boolean datesAsIso) {
		this.datesAsIso = datesAsIso;
	}

}
