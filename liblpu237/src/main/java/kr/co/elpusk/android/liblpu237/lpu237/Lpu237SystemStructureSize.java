package kr.co.elpusk.android.liblpu237.lpu237;

public interface Lpu237SystemStructureSize {
    int cBlank = 4;
    int dwSize = 4;
    int sStructVer = 4;
    int sName = 16;
    int sSysVer = 4;
    int ModeBL = 1;
    int ModeAP = 1;
    int sSN = 8;
    int Interface = 1;
    int nBuzzerFrequency = 4;
    int nNormalWDT = 4;
    int nBootRunTime = 4;

    int Uart_nCom = 4;
    int Uart_nBaud = 4;

    int ContainerInfoMsrObj_pInfoMsrObj0 = 4;
    int ContainerInfoMsrObj_pInfoMsrObj1 = 4;
    int ContainerInfoMsrObj_pInfoMsrObj2 = 4;
    int ContainerInfoMsrObj_nCpdSystickMin = 4;
    int ContainerInfoMsrObj_nCpdSystickMax = 4;
    int ContainerInfoMsrObj_nGlobalTagCondition = 4;
    int ContainerInfoMsrObj_nNumItem = 4;
    int ContainerInfoMsrObj_nOrderObject0 = 4;
    int ContainerInfoMsrObj_nOrderObject1 = 4;
    int ContainerInfoMsrObj_nOrderObject2 = 4;
    int ContainerInfoMsrObj_Keymap_nMappingIndex = 4;
    int ContainerInfoMsrObj_Keymap_nNumMapTableItem = 4;
    int ContainerInfoMsrObj_TagPre_cSize = 1;
    int ContainerInfoMsrObj_TagPre_sTag = 14;
    int ContainerInfoMsrObj_TagPost_cSize = 1;
    int ContainerInfoMsrObj_TagPost_sTag = 14;
    int ContainerInfoMsrObj_GlobalPrefix_cSize = 1;
    int ContainerInfoMsrObj_GlobalPrefix_sTag = 14;
    int ContainerInfoMsrObj_GlobalPostfix_cSize = 1;
    int ContainerInfoMsrObj_GlobalPostfix_sTag = 14;

    int InfoMsr0_cEnableTack = 1;
    int InfoMsr0_cSupportNum = 1;
    int InfoMsr0_cActiveCombination = 1;
    int InfoMsr0_cMaxSize = 3;
    int InfoMsr0_cBitSize = 3;
    int InfoMsr0_cDataMask = 3;
    int InfoMsr0_bUseParity = 3;
    int InfoMsr0_cParityType = 3;
    int InfoMsr0_cSTX = 3;
    int InfoMsr0_cETX = 3;
    int InfoMsr0_bUseErrorCorrect = 3;
    int InfoMsr0_cECMType = 3;
    int InfoMsr0_cRDirect = 3;
    int InfoMsr0_nBufSize = 4;
    int InfoMsr0_cAddValue = 3;
    int InfoMsr0_bEnableEncryption = 1;
    int InfoMsr0_sMasterKey = 16;
    int InfoMsr0_sChangeKey = 16;
    int InfoMsr0_PrivatePrefix0_cSize = 1;
    int InfoMsr0_PrivatePrefix0_sTag = 14;
    int InfoMsr0_PrivatePrefix1_cSize = 1;
    int InfoMsr0_PrivatePrefix1_sTag = 14;
    int InfoMsr0_PrivatePrefix2_cSize = 1;
    int InfoMsr0_PrivatePrefix2_sTag = 14;
    int InfoMsr0_PrivatePostfix0_cSize = 1;
    int InfoMsr0_PrivatePostfix0_sTag = 14;
    int InfoMsr0_PrivatePostfix1_cSize = 1;
    int InfoMsr0_PrivatePostfix1_sTag = 14;
    int InfoMsr0_PrivatePostfix2_cSize = 1;
    int InfoMsr0_PrivatePostfix2_sTag = 14;
    int InfoMsr0_Keymap0_nMappingIndex = 4;
    int InfoMsr0_Keymap0_nNumMapTableItem = 4;
    int InfoMsr0_Keymap1_nMappingIndex = 4;
    int InfoMsr0_Keymap1_nNumMapTableItem = 4;
    int InfoMsr0_Keymap2_nMappingIndex = 4;
    int InfoMsr0_Keymap2_nNumMapTableItem = 4;

    int InfoMsr1_cEnableTack = 1;
    int InfoMsr1_cSupportNum = 1;
    int InfoMsr1_cActiveCombination = 1;
    int InfoMsr1_cMaxSize = 3;
    int InfoMsr1_cBitSize = 3;
    int InfoMsr1_cDataMask = 3;
    int InfoMsr1_bUseParity = 3;
    int InfoMsr1_cParityType = 3;
    int InfoMsr1_cSTX = 3;
    int InfoMsr1_cETX = 3;
    int InfoMsr1_bUseErrorCorrect = 3;
    int InfoMsr1_cECMType = 3;
    int InfoMsr1_cRDirect = 3;
    int InfoMsr1_nBufSize = 4;
    int InfoMsr1_cAddValue = 3;
    int InfoMsr1_bEnableEncryption = 1;
    int InfoMsr1_sMasterKey = 16;
    int InfoMsr1_sChangeKey = 16;
    int InfoMsr1_PrivatePrefix0_cSize = 1;
    int InfoMsr1_PrivatePrefix0_sTag = 14;
    int InfoMsr1_PrivatePrefix1_Size = 1;
    int InfoMsr1_PrivatePrefix1_sTag = 14;
    int InfoMsr1_PrivatePrefix2_cSize = 1;
    int InfoMsr1_PrivatePrefix2_sTag = 14;
    int InfoMsr1_PrivatePostfix0_cSize = 1;
    int InfoMsr1_PrivatePostfix0_sTag = 14;
    int InfoMsr1_PrivatePostfix1_cSize = 1;
    int InfoMsr1_PrivatePostfix1_sTag = 14;
    int InfoMsr1_PrivatePostfix2_cSize = 1;
    int InfoMsr1_PrivatePostfix2_sTag = 14;
    int InfoMsr1_Keymap0_nMappingIndex = 4;
    int InfoMsr1_Keymap0_nNumMapTableItem = 4;
    int InfoMsr1_Keymap1_nMappingIndex = 4;
    int InfoMsr1_Keymap1_nNumMapTableItem = 4;
    int InfoMsr1_Keymap2_nMappingIndex = 4;
    int InfoMsr1_Keymap2_nNumMapTableItem = 4;

    int InfoMsr2_cEnableTack = 1;
    int InfoMsr2_cSupportNum = 1;
    int InfoMsr2_cActiveCombination = 1;
    int InfoMsr2_cMaxSize = 3;
    int InfoMsr2_cBitSize = 3;
    int InfoMsr2_cDataMask = 3;
    int InfoMsr2_bUseParity = 3;
    int InfoMsr2_cParityType = 3;
    int InfoMsr2_cSTX = 3;
    int InfoMsr2_cETX = 3;
    int InfoMsr2_bUseErrorCorrect = 3;
    int InfoMsr2_cECMType = 3;
    int InfoMsr2_cRDirect = 3;
    int InfoMsr2_nBufSize = 4;
    int InfoMsr2_cAddValue = 3;
    int InfoMsr2_bEnableEncryption = 1;
    int InfoMsr2_sMasterKey = 16;
    int InfoMsr2_sChangeKey = 16;
    int InfoMsr2_PrivatePrefix0_cSize = 1;
    int InfoMsr2_PrivatePrefix0_sTag = 14;
    int InfoMsr2_PrivatePrefix1_cSize = 1;
    int InfoMsr2_PrivatePrefix1_sTag = 14;
    int InfoMsr2_PrivatePrefix2_cSize = 1;
    int InfoMsr2_PrivatePrefix2_sTag = 14;
    int InfoMsr2_PrivatePostfix0_cSize = 1;
    int InfoMsr2_PrivatePostfix0_sTag = 14;
    int InfoMsr2_PrivatePostfix1_cSize = 1;
    int InfoMsr2_PrivatePostfix1_sTag = 14;
    int InfoMsr2_PrivatePostfix2_cSize = 1;
    int InfoMsr2_PrivatePostfix2_sTag = 14;
    int InfoMsr2_Keymap0_nMappingIndex = 4;
    int InfoMsr2_Keymap0_nNumMapTableItem = 4;
    int InfoMsr2_Keymap1_nMappingIndex = 4;
    int InfoMsr2_Keymap1_nNumMapTableItem = 4;
    int InfoMsr2_Keymap2_nMappingIndex = 4;
    int InfoMsr2_Keymap2_nNumMapTableItem = 4;
    // additional item from version 3.0
    int InfoiButton_TagPre_cSize = 1;
    int InfoiButton_TagPre_sTag = 14;
    int InfoiButton_TagPost_cSize = 1;
    int InfoiButton_TagPost_sTag = 14;
    int InfoiButton_GlobalPrefix_cSize = 1;
    int InfoiButton_GlobalPrefix_sTag = 14;
    int InfoiButton_GlobalPostfix_cSize = 1;
    int InfoiButton_GlobalPostfix_sTag = 14;

    int InfoUart_TagPre_cSize = 1;
    int InfoUart_TagPre_sTag = 14;
    int InfoUart_TagPost_cSize = 1;
    int InfoUart_TagPost_sTag = 14;
    int InfoUart_GlobalPrefix_cSize = 1;
    int InfoUart_GlobalPrefix_sTag = 14;
    int InfoUart_GlobalPostfix_cSize = 1;
    int InfoUart_GlobalPostfix_sTag = 14;

    // additional item from struture version 4.0
    int RemoveItemTag_cSize = 1;
    int RemoveItemTag_cTag = 40;//40

    int InfoiButtonRemove_TagPre_cSize = 1;
    int InfoiButtonRemove_TagPre_sTag = 14;
    int InfoiButtonRemove_TagPost_cSize = 1;
    int InfoiButtonRemove_TagPost_sTag = 14;
    int InfoiButtonRemove_GlobalPrefix_cSize = 1;
    int InfoiButtonRemove_GlobalPrefix_sTag = 14;
    int InfoiButtonRemove_GlobalPostfix_cSize = 1;
    int InfoiButtonRemove_GlobalPostfix_sTag = 14;

}
