package logparser.dto;

import javax.xml.crypto.Data;

public class DataTransferObject {
    private Army firstArmy;
    private Army secondArmy;

    private LogDate beginDate;
    private LogDate endDate;

    private Result result;

    private boolean isFormed = false;

    private boolean isFirstArmySet = false;

    private boolean isBeginDateSet = false;

    private DataTransferObject(){}

    public Army getFirstArmy() {
        return firstArmy;
    }

    public Army getSecondArmy() {
        return secondArmy;
    }

    public LogDate getBeginDate() {
        return beginDate;
    }

    public LogDate getEndDate() {
        return endDate;
    }

    public Result getResult() {
        return result;
    }

    public boolean isFormed() {
        return isFormed;
    }

    public boolean isFirstArmySet() {
        return isFirstArmySet;
    }

    public boolean isBeginDateSet() {
        return isBeginDateSet;
    }

    public static DTOBuilder newBuilder() {
        return new DataTransferObject().new DTOBuilder();
    }

    public class DTOBuilder {
        private DTOBuilder(){}
        public DTOBuilder setFirstArmy(final Army army) {
            DataTransferObject.this.firstArmy = army;
            return this;
        }

        public DTOBuilder setSecondArmy(final Army secondArmy) {
            DataTransferObject.this.secondArmy = secondArmy;
            return this;
        }

        public DTOBuilder setBeginDate(final LogDate beginDate) {
            DataTransferObject.this.beginDate = beginDate;
            return this;
        }

        public DTOBuilder setEndDate(final LogDate endDate) {
            DataTransferObject.this.endDate = endDate;
            return this;
        }

        public DTOBuilder setResult(final Result result) {
            DataTransferObject.this.result = result;
            return this;
        }

        public DTOBuilder setIsFormed() {
            DataTransferObject.this.isFormed = true;
            return this;
        }

        public void setIsFirstArmySet() {
            DataTransferObject.this.isFirstArmySet = true;
        }

        public void setIsBeginDateSet() {
            DataTransferObject.this.isBeginDateSet = true;
        }

        public DataTransferObject build() {
            return DataTransferObject.this;
        }

        public void abort() {
            DataTransferObject.this.isFormed = false;
            DataTransferObject.this.isFirstArmySet = false;
            DataTransferObject.this.isBeginDateSet = false;
        }
    }
}
