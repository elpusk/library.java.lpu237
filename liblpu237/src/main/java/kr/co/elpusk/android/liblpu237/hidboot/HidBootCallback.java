package kr.co.elpusk.android.liblpu237.hidboot;

public interface HidBootCallback {
    boolean cbEraseSectorAfterDone(Object user,boolean bResult,int nCurSector,int nTotalSector);
    boolean cbWriteSectorBeforeDo(Object user,short wChain, int nCurSector,int nTotalSector);
    boolean cbWriteSectorAfterDone(Object user,boolean bResult,short wChain, int nCurSector,int nTotalSector);

}
