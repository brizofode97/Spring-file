package com.fixdecode.sbfileuploaddownload.common.utils;

import java.time.Instant;

public class IdentifiantUnique {

    public static Long setIdByTime(){
        return Instant.now().toEpochMilli();
    }


}
