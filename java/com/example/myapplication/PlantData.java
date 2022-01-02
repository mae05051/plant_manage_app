package com.example.myapplication;

//데이터 클래스
public class PlantData {

    String mName, mType, mDes, mWhere, mAlertStart, mAlertDelay, mTime, mWhen;
    int baseData;

    public PlantData(String mName, String mType, String mDes, String mWhere, String mAlertStart,
                     String mAlertDelay, String mTime, String mWhen, int baseData) {
        this.mName = mName;
        this.mType = mType;
        this.mDes = mDes;
        this.mWhere = mWhere;
        this.mAlertStart = mAlertStart;
        this.mAlertDelay = mAlertDelay;
        this.mTime = mTime;
        this.mWhen = mWhen;
        this.baseData = baseData;
    }

    public String getmName() {
        return mName;
    }

    public void setmName(String mName) {
        this.mName = mName;
    }

    public String getmType() {
        return mType;
    }

    public void setmType(String mType) {
        this.mType = mType;
    }

    public String getmDes() {
        return mDes;
    }

    public void setmDes(String mDes) {
        this.mDes = mDes;
    }

    public String getmWhere() {
        return mWhere;
    }

    public void setmWhere(String mWhere) {
        this.mWhere = mWhere;
    }

    public String getmAlertStart() {
        return mAlertStart;
    }

    public void setmAlertStart(String mAlertStart) {
        this.mAlertStart = mAlertStart;
    }

    public String getmAlertDelay() {
        return mAlertDelay;
    }

    public void setmAlertDelay(String mAlertDelay) {
        this.mAlertDelay = mAlertDelay;
    }

    public String getmTime() {
        return mTime;
    }

    public void setmTime(String mTime) {
        this.mTime = mTime;
    }

    public String getmWhen() {
        return mWhen;
    }

    public void setmWhen(String mWhen) {
        this.mWhen = mWhen;
    }

    public int getBaseData() {
        return baseData;
    }

    public void setBaseData(int baseData) {
        this.baseData = baseData;
    }
}
