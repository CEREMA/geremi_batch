
package com.geremi.bdrepbatch.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.extensions.excel.ExcelFileParseException;
import org.springframework.batch.extensions.excel.RowCallbackHandler;
import org.springframework.batch.extensions.excel.RowMapper;
import org.springframework.batch.extensions.excel.Sheet;
import org.springframework.batch.extensions.excel.support.rowset.DefaultRowSetFactory;
import org.springframework.batch.extensions.excel.support.rowset.RowSet;
import org.springframework.batch.extensions.excel.support.rowset.RowSetFactory;
import org.springframework.batch.item.file.ResourceAwareItemReaderItemStream;
import org.springframework.batch.item.support.AbstractItemCountingItemStreamItemReader;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.core.io.Resource;
import org.springframework.util.Assert;
import org.springframework.util.ClassUtils;

import com.geremi.bdrepbatch.ImportJobConfig;

public abstract class ExcelItemReader<T> extends AbstractItemCountingItemStreamItemReader<T>
		implements ResourceAwareItemReaderItemStream<T>, InitializingBean {

	private static final Logger log = LoggerFactory.getLogger(ImportJobConfig.class);

	private Resource resource;

	private int linesToSkip = 0;

	private int endAfterBlankLines = 1;

	private RowMapper<T> rowMapper;

	private RowCallbackHandler skippedRowsCallback;

	private boolean noInput = false;

	private boolean strict = true;

	private RowSetFactory rowSetFactory = new DefaultRowSetFactory();

	private RowSet rs;

	private String password;

	private int sheetNumber;

	private boolean hasRead = false;

	public ExcelItemReader() {
		super();
		this.setName(ClassUtils.getShortName(this.getClass()));
	}

	@Override
	public T read() throws Exception {

		T item = super.read();
		int blankLines = 0;
		while (item == null) {
			blankLines++;
			if (blankLines >= this.endAfterBlankLines) {
				return null;
			}
			item = super.read();
			if (item != null) {
				return item;
			}
		}

		return item;
	}

	@Override
	protected T doRead() {
		if (this.noInput) {
			return null;
		}

		if (this.rs == null || !this.rs.next()) {
			if (!nextSheet()) {

				return null;
			}
		}

		while (null != this.rs.getCurrentRow() && isInvalidValidRow(this.rs)) {
			this.rs.next();
		}
		try {
			return (this.rs.getCurrentRow() != null) ? this.rowMapper.mapRow(this.rs) : doRead();
		} catch (Exception ex) {
			throw new ExcelFileParseException("Exception parsing Excel file.", ex, this.resource.getDescription(),
					this.rs.getMetaData().getSheetName(), this.rs.getCurrentRowIndex(), this.rs.getCurrentRow());
		}
	}

	@Override
	protected void jumpToItem(final int itemIndex) {
		RowMapper<T> current = this.rowMapper;
		this.rowMapper = (rs) -> null;
		try {
			for (int i = 0; i < itemIndex; i++) {
				doRead();
			}
		} finally {
			this.rowMapper = current;
		}
	}

	private boolean isInvalidValidRow(RowSet rs) {
		for (String str : rs.getCurrentRow()) {
			if (str.length() > 0) {
				return false;
			}
		}
		return true;
	}

	@Override
	protected void doOpen() throws Exception {
		Assert.notNull(this.resource, "Input resource must be set");
		this.noInput = true;
		if (!this.resource.exists()) {
			if (this.strict) {
				throw new IllegalStateException(
						"Input resource must exist (reader is in 'strict' mode): " + this.resource);
			}
			log.warn("Input resource does not exist '" + this.resource.getDescription() + "'.");
			return;
		}

		if (!this.resource.isReadable()) {
			if (this.strict) {
				throw new IllegalStateException(
						"Input resource must be readable (reader is in 'strict' mode): " + this.resource);
			}
			log.warn("Input resource is not readable '" + this.resource.getDescription() + "'.");
			return;
		}

		this.openExcelFile(this.resource, this.password);
		this.noInput = false;

		log.debug(
				"Opened workbook [" + this.resource.getFilename() + "] with " + this.getNumberOfSheets() + " sheets.");

	}

	private boolean nextSheet() {
		if (hasRead) {
			log.debug("Close sheet.");
			return false;
		}
		hasRead = true;

		final Sheet sheet = this.getSheet(this.sheetNumber);
		this.rs = this.rowSetFactory.create(sheet);

		for (int i = 0; i < this.linesToSkip; i++) {
			if (this.rs.next() && this.skippedRowsCallback != null) {
				this.skippedRowsCallback.handleRow(this.rs);
			}
		}

		log.debug("Opened sheet " + sheet.getName() + ", with " + sheet.getNumberOfRows() + " rows.");

		if (this.rs.next()) {
			return true;
		}

		return false;
	}

	protected void doClose() throws Exception {
		this.rs = null;
	}

	public void setResource(final Resource resource) {
		this.resource = resource;
	}

	public void afterPropertiesSet() throws Exception {
		Assert.notNull(this.rowMapper, "RowMapper must be set");
	}

	public void setLinesToSkip(final int linesToSkip) {
		this.linesToSkip = linesToSkip;
	}

	protected abstract Sheet getSheet(int sheet);

	protected abstract int getNumberOfSheets();

	protected abstract void openExcelFile(Resource resource, String password) throws Exception;

	public void setStrict(final boolean strict) {
		this.strict = strict;
	}

	public void setRowMapper(final RowMapper<T> rowMapper) {
		this.rowMapper = rowMapper;
	}

	public void setRowSetFactory(RowSetFactory rowSetFactory) {
		this.rowSetFactory = rowSetFactory;
	}

	public void setSkippedRowsCallback(final RowCallbackHandler skippedRowsCallback) {
		this.skippedRowsCallback = skippedRowsCallback;
	}

	public void setEndAfterBlankLines(final int endAfterBlankLines) {
		this.endAfterBlankLines = endAfterBlankLines;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public int getSheetNumber() {
		return sheetNumber;
	}

	public void setSheetNumber(int sheetNumber) {
		this.sheetNumber = sheetNumber;
	}

}
