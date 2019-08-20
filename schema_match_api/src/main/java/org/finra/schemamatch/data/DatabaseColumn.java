package org.finra.schemamatch.data;

import org.finra.schemamatch.pattern.DataPattern;
import org.finra.schemamatch.pattern.LabelLogic;

public class DatabaseColumn extends DatabaseEntity{
	DataPattern dp;
	LabelLogic ll;
	
	public DatabaseColumn(String label) {
		super(label, DatabaseEntityType.COLUMN);
	}

	public DataPattern getDp() {
		return dp;
	}

	public void setDp(DataPattern dp) {
		this.dp = dp;
	}

	public LabelLogic getLl() {
		return ll;
	}

	public void setLl(LabelLogic ll) {
		this.ll = ll;
	}
}
